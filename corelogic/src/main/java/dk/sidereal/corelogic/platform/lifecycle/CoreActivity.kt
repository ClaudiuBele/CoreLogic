package dk.sidereal.corelogic.platform.lifecycle

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.vm.ViewModelActivityController

open class CoreActivity : AppCompatActivity() {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { CoreActivity::class.simpleTagName() }
    }

    private val controllers: MutableList<ActivityController> = mutableListOf()

    val coreApplication: CoreApplication? by lazy { application as? CoreApplication }

    /** Will throw if application is not of type [CoreApplication]
     *
     */
    val requireCoreApplication: CoreApplication by lazy { application as CoreApplication }

     val coreFragments: List<CoreFragment>
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
                handledBackPressed = handledBackPressed or it.onBackPressedInternal()
            }
        }

        if (!handledBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        controllers.forEach { it.onDestroy() }
        coreFragments.forEach { it.onActivityDestroyed() }
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

    /** Retrieves the desired view model. Will create it if neeeded. For supported viewmodel
     * classes and constructors for them, check [ViewModelActivityController]
     *
     */
    fun <T : ViewModel> getVm(clazz: Class<T>): T {
        val vmController = getController(ViewModelActivityController::class.java)
        checkNotNull(vmController)
        return vmController.get(clazz)
    }

    /** Returns a read-only list of controllers
     *
     */
    fun getControllers(): List<ActivityController> = controllers.toList()

    @Suppress("UNCHECKED_CAST")
    fun <T : ActivityController> getController(clazz: Class<T>): T? =
        controllers.firstOrNull { clazz.isAssignableFrom(it.javaClass) } as? T

    /** Where you setup your [ActivityController]. called in [onCreate]. Add your outControllers to [outControllers] parameter.
     * Some default outControllers are added by [CoreActivity] [dk.sidereal.corelogic.nav.CoreNavActivity]

     */
    @CallSuper
    protected open fun onCreateControllers(outControllers: MutableList<ActivityController>) {
        outControllers.add(ViewModelActivityController(this))
    }


}