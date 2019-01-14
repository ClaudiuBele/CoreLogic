package dk.sidereal.corelogic.platform.lifecycle

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

open class BaseActivity : AppCompatActivity() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { BaseActivity::class.simpleTagName() }
    }

    val controllers: MutableList<ActivityController> = mutableListOf()

    val baseApplication: BaseApplication
        get() = application as BaseApplication

    private val baseFragments: List<BaseFragment>
        get() = supportFragmentManager.fragments.dropWhile { it !is BaseFragment }.map { it as BaseFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateControllers()
        controllers.forEach { it.onCreate(savedInstanceState) }

        var hasSetContentView = false
        controllers.forEach {
            val hasNewControllerSetContentView = it.onCreateView(this)
            if (hasSetContentView && hasNewControllerSetContentView) {
                Log.w(
                    INNER_TAG,
                    "onCreate: ${it.javaClass.simpleName} returned true in onCreateView after another Controller"
                )
            }
            hasSetContentView = hasSetContentView or hasNewControllerSetContentView
        }

        if (!hasSetContentView) {
            Log.w("BaseActivity", "No content view set by activity controllers")
        }
        controllers.forEach { it.onViewCreated(this) }
    }

    override fun onBackPressed() {
        var handledBackPressed = false
        controllers.forEach {
            if (!handledBackPressed) {
                handledBackPressed = handledBackPressed or it.onBackPressed()
            }
        }
        if (handledBackPressed) {
            return
        }

        baseFragments.forEach {
            if (!handledBackPressed) {
                handledBackPressed = handledBackPressed or it.onBackPressed()
            }
        }

        if (handledBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        controllers.forEach { it.dispose() }
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var handledOptionsItem = false
        controllers.forEach {
            if (!handledOptionsItem) {
                handledOptionsItem = handledOptionsItem or it.onOptionsItemSelected(item)
            }
        }
        return handledOptionsItem or super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        var handledNavigateUp = false
        controllers.forEach {
            if (!handledNavigateUp) {
                handledNavigateUp = handledNavigateUp or it.onNavigateUp()
            }
        }
        return handledNavigateUp or super.onSupportNavigateUp()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        controllers.forEach { it.onAttachFragment(fragment as? BaseFragment) }

    }

    /** Where you setup your [ActivityController]. called in [onCreate]. Add your controllers to [controllers]

     */
    protected open fun onCreateControllers() {}

    @Suppress("UNCHECKED_CAST")
    internal fun <T : ActivityController> getController(clazz: Class<T>): T? =
        controllers.firstOrNull { it.javaClass.isAssignableFrom(clazz) } as? T


}