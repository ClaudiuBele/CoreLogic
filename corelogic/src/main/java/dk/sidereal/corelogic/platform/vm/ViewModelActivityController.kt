package dk.sidereal.corelogic.platform.vm

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dk.sidereal.corelogic.kotlin.ext.simpleTagName
import dk.sidereal.corelogic.platform.lifecycle.ActivityController
import dk.sidereal.corelogic.platform.lifecycle.CoreActivity
import dk.sidereal.corelogic.platform.lifecycle.CoreApplication

/** [ActivityController] tasked with saving and restoring state from viewModels (if [StatefulViewModel]).
 *
 * Get your viewModels with [get]
 *
 * Using this class restricts your viewmodel usage to
 * - [StatefulViewModel] subclasses with a [CoreApplication]/[CoreActivity]/[Context] constructor parameter; or
 * - [ViewModel] subclasses with [Application]/[AppCompatActivity]/empty constructor.
 * attempting
 *
 */
open class ViewModelActivityController(coreActivity: CoreActivity) :
    ActivityController(coreActivity),
    StatefulViewModelFactory.OnViewModelCreated {

    companion object {
        private val TAG = ViewModelActivityController::class.java.simpleTagName()
    }

    /** List of models managed through this controller. G
     *
     */
    private val viewModels = mutableListOf<ViewModel>()

    protected open val viewModelProvider = ViewModelProviders.of(activity, StatefulViewModelFactory(activity, this))

    private var lastSavedInstance: Bundle? = null

    @CallSuper
    override fun onViewModelCreated(viewModel: ViewModel) {
        if (viewModel is StatefulViewModel) {
            viewModel.restoreStateInternal(lastSavedInstance)
            Log.d(TAG, "onViewModelCreated: restoreState called for ${viewModel::class.java.simpleTagName()}")
        }
        viewModels.add(viewModel)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lastSavedInstance = savedInstanceState
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModels.forEach {
            if (it is StatefulViewModel) {
                it.saveInstanceStateInternal(outState)
                Log.d(TAG, "onSaveInstanceState: saveInstanceState called for ${it::class.java.simpleTagName()}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModels.clear()
    }


    /** Retrieves the desired view model. Will create it if neeeded. For supported viewmodel
     * classes and constructors for them, check
     *
     */
    fun <T : ViewModel> get(clazz: Class<T>): T {
        return viewModelProvider.get(clazz)
    }
}