# Stanwood Architecture: DI

This library provides scopes, modules and factories for easy integration of dagger into your Android project.
It is based on Dagger 2.16 and provides all the necessary dependencies (except for kapt dependencies).

## Setup

_In addition to the setup instructions outlined in the main README perform the following steps:_

Set up kapt in your app module's _build.gradle_ like so:

```groovy
apply plugin: 'kotlin-kapt'

...

kapt {
    correctErrorTypes true
}

...

dependencies {
    ...
    def dagger_version = '2.16' // always use the same version as supplied by this library!
    kapt "com.google.dagger:dagger-compiler:$dagger_version"
    kapt "com.google.dagger:dagger-android-processor:$dagger_version"
}
```

## Packages

The classes described below belong into packages as shown in the following screenshot:

![Packages](https://imgur.com/zsNOTeo.png)

## For each Activity

Create a module extending `ActivityModule` and add `@FragmentScope` Android injectors.

E.g.

```kotlin
@Module(includes = [MainActivitySubModule::class])
abstract class MainActivityModule : ActivityModule<MainActivity>() {

    // one for each fragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    internal abstract fun contributeHomeFragment(): HomeFragment

}
```

As this module needs to be abstract to have dagger implement the binds defined in `ActivityModule` we need to create
another sub-module if we want to actually provide dependencies other than via the `@ContributesAndroidInjector`
annotation.

```kotlin
@Module
class MainActivitySubModule {

    @Provides
    @ActivityScope
    internal fun provideVideoPlayer(
        context: Application,
        httpDataSource: HttpDataSource.BaseFactory
    ) = VideoPlayer(context, httpDataSource)

}
```

To actually inject stuff into your Activity set it up like this:

```kotlin
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
```

## For each Fragment

Create a module extending `FragmentModule`. This one is usually empty:

```kotlin
@Module(includes = [HomeFragmentSubModule::class])
abstract class HomeFragmentModule : FragmentModule<HomeFragment>()
```

The actual provides are declared in the sub-module:

```kotlin
@Module
class HomeFragmentSubModule {

    @Provides
    @FragmentScope
    internal fun provideViewModel(
        homeInteractor: HomeInteractor,
        settingsInteractor: SettingsInteractor
    ) = HomeViewModel(homeInteractor, settingsInteractor)

}
```

To actually inject stuff into your Fragment set it up like this:

```kotlin
class HomeFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = androidInjector

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<HomeViewModel> // optional if you use Android ViewModels

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
    }
}
```

## For each ViewModel

There is not much to do for ViewModels except for adding a constructor annotated with `@Inject`:

```kotlin
class HomeViewModel @Inject constructor(/* inject properties here if you want */) : ViewModel() {}
```

## What else do I need?

Due to the nature of how libraries and dagger work we cannot provide all necessary classes to get DI up and running.

Add and adapt the following classes yourself and you should be off to a good start.

### ActivityBuilderModule

```kotlin
@Module
abstract class ActivityBuilderModule {

    // one for each activity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
```

### AppComponent

```kotlin
@Singleton
@Component(modules = [
    AppModule::class, // contains all your app scoped dependencies (usually Singletons)
    AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class,
    BroadcastReceiverBuilderModule::class, // if you need to inject things into broadcast receivers (see sample below)
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: StanwoodApp) // or whatever you app class is named
}
```

### Injecting into Broadcast receivers

```kotlin
@Module
abstract class BroadcastReceiverBuilderModule {

    // one for each broadcast receiver

    @BroadcastReceiverScope
    @ContributesAndroidInjector
    abstract fun contributeUploadStateReceiver(): UploadStateReceiver
}
```

### Initialise in your Application class

```kotlin

class StanwoodApp : Application(), HasActivityInjector, HasBroadcastReceiverInjector, HasServiceInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    @Inject
    lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> =
            broadcastReceiverInjector

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)
    }
}

```
