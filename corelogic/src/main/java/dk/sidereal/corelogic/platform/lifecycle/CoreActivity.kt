package dk.sidereal.corelogic.platform.lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.ControllerHolder
import dk.sidereal.corelogic.platform.HandlesBackPress
import dk.sidereal.corelogic.platform.vm.ViewModelAc

open class CoreActivity : AppCompatActivity(),
    ControllerHolder<ActivityController> {

    protected val TAG by lazy { javaClass.simpleTagName() }

    companion object {
        val INNER_TAG by lazy { CoreActivity::class.simpleTagName() }
    }

    override var mutableControllers: MutableList<ActivityController> = mutableListOf()

    val coreApplication: CoreApplication? by lazy { application as? CoreApplication }

    /** Will throw if application is not of type [CoreApplication]
     *
     */
    val requireCoreApplication: CoreApplication by lazy { application as CoreApplication }

    val coreFragments: List<CoreFragment>
        get() = supportFragmentManager.fragments.map { it as? CoreFragment }.filterNotNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        onCreateControllers()
        mutableControllers.forEach { it.onCreate(savedInstanceState) }
        var hasSetContentView = false
        mutableControllers.forEach {
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
        mutableControllers.forEach { it.onViewCreated(this) }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        mutableControllers.forEach { it.onAttachFragment(fragment as? CoreFragment) }
    }

    override fun onStart() {
        super.onStart()
        mutableControllers.forEach { it.onStart() }
    }

    override fun onResume() {
        super.onResume()
        mutableControllers.forEach { it.onResume() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mutableControllers.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // outstate is not null, otherwise there will be NPE in platform Activity code
        mutableControllers.forEach { it.onSaveInstanceState(outState) }
    }

    override fun onDestroy() {
        mutableControllers.forEach { it.onDestroy() }
        coreFragments.forEach { it.onActivityDestroyed() }
        super.onDestroy()
    }

    override fun onBackPressed() {
        Log.d("alt-nav", "CoreActivity (${this::class.java.simpleName}) onBackPressed")
        var handledBackPressed = false
        mutableControllers.forEach {
            if (!handledBackPressed) {
                handledBackPressed = handledBackPressed or it.onBackPressed()
            }
        }
        Log.d(
            "alt-nav",
            "CoreActivity (${this::class.java.simpleName}) controllers handled onBackPressed $handledBackPressed"
        )
        if (handledBackPressed) {
            return
        }

        supportFragmentManager.fragments
            .filter { it is HandlesBackPress }
            .map { it as HandlesBackPress }
            .forEach {
                if (!handledBackPressed) {
                    handledBackPressed = handledBackPressed or it.onBackPressedInternal()
                }
            }
        Log.d(
            "alt-nav",
            "CoreActivity (${this::class.java.simpleName}) fragments handled onBackPressed $handledBackPressed"
        )

        if (!handledBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var handledOptionsItem = false
        mutableControllers.forEach {
            if (!handledOptionsItem) {
                handledOptionsItem = handledOptionsItem or it.onOptionsItemSelected(item)
            }
        }
        return handledOptionsItem or super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        var handledNavigateUp = false
        mutableControllers.forEach {
            if (!handledNavigateUp) {
                handledNavigateUp = handledNavigateUp or it.onNavigateUp()
            }
        }
        return handledNavigateUp or super.onSupportNavigateUp()
    }

    @CallSuper
    override fun onCreateControllers(outControllers: MutableList<ActivityController>) {
        super.onCreateControllers(outControllers)
        outControllers.add(ViewModelAc(this))
    }

    /** Retrieves the desired view model. Will create it if neeeded. For supported viewmodel
     * classes and constructors for them, check [ViewModelAc]
     *
     */
    fun <T : ViewModel> getVm(clazz: Class<T>): T {
        val vmController = getController(ViewModelAc::class.java)
        checkNotNull(vmController)
        return vmController.get(clazz)
    }


}