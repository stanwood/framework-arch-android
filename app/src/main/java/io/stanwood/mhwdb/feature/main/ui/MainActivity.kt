package io.stanwood.mhwdb.feature.main.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.stanwood.mhwdb.R
import io.stanwood.mhwdb.feature.main.dataprovider.MainDataProvider
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var dataProvider: MainDataProvider
    private var disposable: Disposable? = null
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setContentView(R.layout.activity_main)
        disposable = dataProvider.currentUser
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                findNavController(R.id.nav_host_fragment)
                    .apply {
                        graph = navInflater.inflate(R.navigation.nav_graph_main)
                            .apply {
                                startDestination = if (it.isNewUser) R.id.nav_graph_onboarding else R.id.containerFragment
                            }
                    }
            })
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
}