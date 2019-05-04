[![Release](https://jitpack.io/v/stanwood/framework-arch-android.svg?style=flat-square)](https://jitpack.io/#stanwood/framework-arch-android)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=16)

# stanwood Architecture Utilities (Android)

A set of libraries containing homegrown architecture related utility classes as well as stanwood's general architecture guidelines.

## Import

The stanwood Architecture Utilities are hosted on JitPack. Therefore you can simply import them by adding

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

to your project's `build.gradle`.

Then add this to you app's `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.stanwood.framework-arch-android:<module>:<insert latest version here>' // aar versions available as well
}
```

## Usage

In general we recommend usage of the [stanwood Android templates](https://plugins.jetbrains.com/plugin/11954-stanwood-android-templates) IntelliJ plugin. It provides easy to use templates for all the concepts described below and more. Install the plugin and find the templates in the `New...` context menu in the `stanwood` folder.

The templates include comments and TODOs. Fix all TODOs and you should have a nicely running app in no time.

The plugin adds the libraries in this repository to your dependencies for you when needed.

For detailed usage of the various libraries in this repository please refer to the README's of the respective libraries. You can find them at the root of the library folders, e.g. _di/README.md_.

Find a sample app showing off more (also advanced concepts like loading state, error handling, paging etc.) in the _app_ folder.

## Kotlin

All our apps are written in Kotlin and that's what the IntelliJ plugin and the libraries target. We don't offer any official Java support although many classes in the libraries might work just fine when used in combination with Java as well.

## Dependencies

The libraries contain a number of dependencies on third party libraries we use in all our apps. There is nothing out of the ordinary to find here. In general we recommend to stick with the versions provided by this library and not override them in your app. We will make sure to keep those dependencies updated within a reasonable timeframe and after having tested them with the components provided in this library.

The main third party libraries we use in our architecture aside of Google's usual Android libraries are (in no special order):

- [Google/Dagger](https://github.com/google/dagger) (specifically the Android flavour) for DI
- [ReactiveX/RxJava](https://github.com/ReactiveX/RxJava) for handling data streams
- [NYTimes/Store](https://github.com/NYTimes/Store) and its Kotlin/Android specific flavours for simple network data retrieval and offline persistence
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/) for data loading, data streaming within the View layer, navigation and general persistence

## Libraries

This repository offers the following libraries:

### core
The core library contains the most important basic components of our architecture such as `ViewModel` (don't confuse it with Google's `ViewModel`!), `ViewDataProvider`, `Resource` as well as some RxJava 2 helper classes. All other libraries depend on this library so that you rarely need to pull this in manually.

### di
This library provides a set of dagger compatible factories for `ViewModel`s and `ViewDataProvider`s as well as Android specific scopes and modules.

### nav
The nav library provides helper classes for simplified navigation handling. This is mainly used by the IntelliJ plugin.

## Packaging

We package our classes as follows (WIP):

**TODO: Update screenshot**

![Packages](https://imgur.com/PVvcczQ.png)

Generic (app-wide) components belong into the root folder packages while those for specific features are to be moved into the specific feature folders.

Interactors are *always* located in the *interactor* package in the app root package. Similarly Repositories are *always* located in the *repositories* package. You never find any of those within a feature package.

## General architecture

![Architecture](https://i.imgur.com/MxrQdaP.jpg)

The overall architecture consists of three layers:

1. **Data layer**: where the data is fetched from some source (i.e. network, database, file system). This is where the *Repository* classes reside
2. **Domain layer**: where data from the data layer is brought into a form suitable for the app - this is mainly done by single-purpose *Interactors* which often fetch data from multiple Repositories
3. **View layer**: where domain data is presented to the user (after optional conversion by means of the ViewModel), here we find *ViewDataProvider*s, *ViewModel*s and our usual Android suspects like Fragments and Activities.

Most dependencies are resolved by means of the dagger DI framework (don't worry, the plugin generates most of the modules and components for you). Usually data flows by means of RxJava 2 streams between the layers.

### Data layer

The data layer is the central source for data not originating from within the app. It is our interface to the outside world.

Repositories are the main components of the data layer. They take care of fetching and sending data from/to outside data sources.

As the data layer is the bottom-most layer repositories don't know anything about the layers above. They are usually contacted directly by Interactors from the domain layers.

Instead of sending raw source data to the interactors, Repositories *map* data to *Domain Objects*. This helps at abstracting the actual sources from the rest of the app and allows for relatively simple replacements or changes of sources.

They also take care of transparently persisting data, e.g. for offline cases. In case of network sources we use the Store library for that. The network data itself is fetched via retrofit.

When a DB is needed we prefer *Room* for its reactive interface which integrates very well in our reactive way of coding.

A simple repository implementation might look like this (the IntelliJ plugin will aid you in creating similar classes within seconds):

```kotlin
class MhwRepository @Inject constructor(private val api: MhwApi, fileSystem: FileSystem) {
    companion object {
        private val allArmor = BarCode("Armor", "all")
    }

    private val sourcePersister = SourcePersisterFactory.create(fileSystem, 60, TimeUnit.MINUTES)
    private val memoryPolicy =
        MemoryPolicy.builder().setExpireAfterWrite(30).setExpireAfterTimeUnit(TimeUnit.MINUTES)
            .build()

    private val armorStore by lazy {
        SerializationParserFactory.createSourceParser(MhwArmor.serializer().list)
            .fetchFrom { api.fetchArmor() }
            .open()
    }

    fun fetchArmorById(id: Long): Single<Armor> =
        armorStore.get(allArmor).map { src ->
            src.first { it.id == id }
                .mapToArmor()
        }

    private fun <T> Parser<BufferedSource, T>.fetchFrom(fetcher: (BarCode) -> Single<BufferedSource>) =
        StoreBuilder.parsedWithKey<BarCode, BufferedSource, T>()
            .fetcher(fetcher)
            .persister(sourcePersister)
            .refreshOnStale()
            .memoryPolicy(memoryPolicy)
            .parser(this)
}
```

## Domain Layer

The domain layer is where our business rules are defined - usually in the form of Interactors.

Interactors usually fetch data from multiple Repositories, merge that data and return it as more complex Domain Models. Often they also implement business rules like: "only return data here when the user is logged in, otherwise throw an error" or "only enable the feature when the user has completed an IAP".

Even if an Interactor just fetches data straight from one Repository don't feel tempted to skip implementation of that Interactor and directly access the repository from the above View layer. You will loose the benefit of having all business rules defined in an encapsulated way.

Interactors are usually quite tightly scoped and thus very reusable. It is not uncommon for ViewDataProviders to access multiple interactors to get the data needed by its ViewModel.

An Interactor can be as simple as the following, but it can get much more complex when user handling, dynamic feature flags and merging of data from different sources (usually repositories) are involved:

```kotlin
class GetArmorInteractor @Inject constructor(private val repository: MhwRepository) {

    fun getArmor() = repository.fetchArmorSets()
}
```

## View layer

The View layer is the topmost layer in our Architecture. As such it is the user facing layer. The View layer is where most of our features are sitting.

The interface between the View layer and the Domain layer is defined by ViewDataProviders.

ViewDataProviders are regular Android ViewModels. As such they remain in place even across configuration changes. However, their sole purpose is to fetch (and possibly post) data, not to handle any UI logic.

This is done by the ViewModel (again, not the Android ViewModel!) in collaboration with the Fragment.

Every ViewModel has access to a ViewDataProvider. The ViewModel subscribes to an Observable supplied by the ViewDataProvider to receive data from the lower layers.

The ViewDataProvider is also where you would implement refresh handling and where data from multiple Interactors is merged. Thus the ViewDataProvider decides *which streams are provided to the ViewModel depending on what the ViewModel asks for*.

A simple ViewDataProvider might look like this:

```kotlin
class ArmorDataProviderImpl @Inject constructor(
    private val armorInteractor: GetArmorInteractor,
    private val exceptionMapper: ExceptionMessageMapper
) : ViewDataProvider(), ArmorDataProvider {
    private var disposable: CompositeDisposable = CompositeDisposable()
    private val retrySubject = PublishSubject.create<Unit>()

    override val data =
        retrySubject
            .startWith(Unit)
            .switchMap { Observable.concat(Observable.just(Resource.Loading()), mappedArmorSets) }
            .replay(1)
            .autoConnect(1) { disposable += it }

    private val mappedArmorSets
        get() = armorInteractor.getArmor()
            .map { res ->
                res.associateBy(
                    { ArmorItem.SetViewModel(it.id, "${it.name} (${it.rank})") },
                    { armor ->
                        armor.pieces.map {
                            ArmorItem.ArmorViewModel(it.id, it.name, it.image, it.type)
                        }
                    })
            }
            .compose(ResourceTransformer.fromSingle(exceptionMapper))
            .toObservable()

    override fun retry() {
        retrySubject.onNext(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
```

Again the plugin will aid you in defining the necessary interfaces and the class itself.

When the ViewModel receives data from the ViewDataProvider it may map it again to data objects suitable for whatever UI the data shall be presented in (e.g. by applying filters). The mapped objects can in turn be ViewModels (e.g. when presenting data in a RecyclerView, we usually suffix those with _Item_) or just data classes. In both cases data is bound to the UI by means of `android.databinding.Observable*` properties. 

For better performance and control of what happens we usually avoid having our ViewModels implement `BaseObservable`, better use the `notifyPropertyChanged()` methods to inform the UI of changes.

It is also fine to use `LiveData` to forward data to the UI, but keep in mind that this also leads to less control over what happens when.

Note, that ViewModels usually don't know anything about the UI itself (so they don't have references to Views etc.). Use data binding instead and provide only View *data* to the ViewModel (e.g. by using the generated binding in the Fragment, writing binding adapters should only rarely be necessary then).

*Contrary to the Android ViewModel, our ViewModel can easily have access to the Fragment or Activity context, just inject it and don't worry about leaks.*

```kotlin
class ArmorsViewModel @Inject constructor(private val dataProvider: ArmorDataProvider) : ViewModel {
    private val navigation = PublishSubject.create<NavigationTarget>()
    val navigator = navigation.firstElement()!!

    val items =
        dataProvider.data
            .filter { it.data != null }
            .map { it.data!! }
            .observeOn(AndroidSchedulers.mainThread())!!

    fun retry() {
        dataProvider.retry()
    }

    val status = dataProvider.data
        .compose(ResourceStatusTransformer.fromObservable())
        .observeOn(AndroidSchedulers.mainThread())!!
}
```

The last part of the chain is the Fragment that injects the ViewModel. This is likely the most boring part.

The Fragment subscribes to the data stream provided by the ViewModel (usually in `onCreate()`). If the Fragment hosts a RecyclerView it might then forward the data to the Adapter once it arrives.

```kotlin
class ArmorsFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<ArmorsViewModel>
    private var viewModel: ArmorsViewModel? = null
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var dataBindingComponent: DataBindingComponent
    private var binding: FragmentArmorBinding? = null
    private var rcvAdapter: ArmorsAdapter? = null

    override fun supportFragmentInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(ArmorsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<FragmentArmorBinding>(inflater, R.layout.fragment_armor, container, false, dataBindingComponent)
            .apply {
                binding = this
                retryCallback = View.OnClickListener { viewModel?.retry() }
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestApplyInsets()
        binding?.apply {
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
            }
            lifecycleOwner = viewLifecycleOwner
        }
        rcvAdapter = ArmorsAdapter(LayoutInflater.from(context), dataBindingComponent) { viewModel?.itemClicked(it) }
        viewModel?.apply {
            items.subscribeBy(viewLifecycleOwner, onNext = {
                binding?.rcv?.apply {
                    rcvAdapter?.apply {
                        if (adapter == null) {
                            adapter = this
                        }
                        submitList(it)
                    }
                }
            })
            status.subscribeBy(viewLifecycleOwner, onNext = {
                binding?.status = it
            })
            navigator.subscribeBy(
                viewLifecycleOwner,
                onSuccess = { findNavController().navigate(it.navDirections, it.navOptions) })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.destroy()
    }
}
```

## A word on `Resource`s

The Resource class is a sealed class wrapping objects returned by asynchronous operations (usually in streams). We use it within the View layer to ease status handling (there also is a pure `ResourceStatus` class which only propagates status without any data), so make sure to always wrap your data objects in Resources first thing when preparing stream data in a ViewDataProvider. Various transformers in the core library will help you with mapping RxJava data streams into RxJava Resource/ResourceStatus streams (check out the `ResourceTransformer`/`ResourceStatusTransformer` object classes for details).

A Resource can have three states: Success, Failed and Loading. Depending on the state the Resource will contain the data itself (**Success**), a message and an optionally a Throwable and data (**Failed**, the data might originate from other merged sources) or no data (**Loading**).

The sealed class concept will help you to easily react to state changes in your UI.

Usually your ViewModel will split the Resource data stream coming from the Interactors into two streams: a pure data stream and a ResourceStatus stream. This makes reacting on changes in your UI even simpler.

```kotlin
val items =
    dataProvider.data
        .filter { it.data != null }
        .map { it.data!! }
        .observeOn(AndroidSchedulers.mainThread())!!

val status = dataProvider.data
    .compose(ResourceStatusTransformer.fromObservable())
    .observeOn(AndroidSchedulers.mainThread())!!
```

State and Resource listening can happen in either your XML or in the Fragment. A Fragment might do the following in `onViewCreated()`:

```kotlin
viewModel.items.subscribeBy(viewLifecycleOwner, onNext = {
    binding?.rcv?.apply {
        rcvAdapter?.apply {
            if (adapter == null) {
                adapter = this
            }
            submitList(it)
        }
    }
})
viewModel.status.subscribeBy(viewLifecycleOwner, onNext = {
    binding?.status = it
})
```

## Contribute

This project follows the [Android Kotlin Code Style](https://android.github.io/kotlin-guides/style.html)
for all Kotlin classes (exception: line length = 140).

The project ships with all necessary IDE settings and checks enabled. Pre-commit we run ktlint to check for adherence. Usually running `
./gradlew ktlintFormat` will fix all errors reported by ktlint.

Our CI runs those checks as well when you create your PR.
