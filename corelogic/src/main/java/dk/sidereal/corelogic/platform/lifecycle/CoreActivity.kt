package dk.sidereal.corelogic.platform.lifecycle

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dk.sidereal.corelogic.kotlin.ext.simpleTagName

open class CoreActivity : AppCompatActivity() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { CoreActivity::class.simpleTagName() }
    }

    private val controllers: MutableList<ActivityController> = mutableListOf()

    val coreApplication: CoreApplication
        get() = application as CoreApplication

    private val coreFragments: List<CoreFragment>
        get() = supportFragmentManager.fragments.dropWhile { it !is CoreFragment }.map { it as CoreFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateControllers(controllers)
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
            Log.w("CoreActivity", "No content view set by activity controllers")
        }
        controllers.forEach { it.onViewCreated(this) }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        // outstate is not null, otherwise there will be NPE in platform Activity code
        val realOutState = outState!!
        controllers.forEach { it.onSaveInstanceState(realOutState) }
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

        coreFragments.forEach {
            if (!handledBackPressed) {
                handledBackPressed = handledBackPressed or it.onBackPressed()
            }
        }

        if (handledBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        controllers.forEach { it.onDestroy() }
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
        controllers.forEach { it.onAttachFragment(fragment as? CoreFragment) }

    }

    /** Returns a read-only list of controllers
     *
     */
    fun getControllers(): List<ActivityController> = controllers.toList()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityController> getController(clazz: Class<T>): T? =
        controllers.firstOrNull { clazz.isAssignableFrom(it.javaClass) } as? T

    /** Where you setup your [ActivityController]. called in [onCreate]. Add your controllers to [controllers] parameter.
     * Some default controllers are added by [CoreActivity] [dk.sidereal.corelogic.nav.CoreNavActivity]

     */
    protected open fun onCreateControllers(controllers: MutableList<ActivityController>) {}


}