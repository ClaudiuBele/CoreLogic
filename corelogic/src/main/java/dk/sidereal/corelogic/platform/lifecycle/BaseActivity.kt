package dk.sidereal.corelogic.platform.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity : AppCompatActivity() {

    protected val controllers: MutableList<ActivityController> = mutableListOf()

    val baseApplication: BaseApplication
        get() = application as BaseApplication

    val baseFragments: List<BaseFragment>
        get() = supportFragmentManager.fragments.dropWhile { it !is BaseFragment }.map { it as BaseFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onSetupControllers()
        controllers.forEach { it.onCreate(savedInstanceState) }

    }

    override fun onBackPressed() {
        baseFragments.firstOrNull() {
            it.onBackPressed()
        }?.let {
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        controllers.forEach { it.dispose() }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        controllers.forEach { it.onAttachFragment(fragment as? BaseFragment) }

    }

    /** Where you setup your [ActivityController]. called in [onCreate]

     */
    internal open fun onSetupControllers() {}

    @Suppress("UNCHECKED_CAST")
    internal fun <T : ActivityController> getController(clazz: Class<T>): T? =
        controllers.firstOrNull { it.javaClass.isAssignableFrom(clazz) } as? T


}