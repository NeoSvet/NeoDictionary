package ru.neosvet.app.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import org.koin.android.ext.android.inject
import ru.neosvet.dictionary.R
import ru.neosvet.app.view.screens.DictionaryScreen

class MainActivity : AppCompatActivity() {
    private val navigator = AppNavigator(this, R.id.container)
    private val navigatorHolder: NavigatorHolder by inject()
    private val router: Router by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)

        splashScreen.setOnExitAnimationListener { provider ->
            ObjectAnimator.ofFloat(
                provider.view,
                View.ALPHA,
                1f, 0f
            ).apply {
                duration = 1500
                doOnEnd {
                    provider.remove()
                }
            }.also {
                it.start()
            }
        }

        router.newRootScreen(DictionaryScreen.create())
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        router.exit()
    }
}