package dk.sidereal.corelogic.platform.vm

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.sidereal.corelogic.kotlin.ext.hasConstructor
import dk.sidereal.corelogic.platform.lifecycle.BaseActivity
import dk.sidereal.corelogic.platform.lifecycle.BaseApplication

/** View model factory that supports all viewmodels created through (in this order):
 *
 * - [StatefulViewModel] subclasses with a [BaseApplication]/[BaseActivity]/[Context]/empty constructor; or
 * - [ViewModel] subclasses with [Application]/[AppCompatActivity]/empty constructor
 */
class StatefulViewModelFactory(val activity: BaseActivity, private val onViewModelCreated: OnViewModelCreated) :
    ViewModelProvider.AndroidViewModelFactory(activity.application) {

    interface OnViewModelCreated {
        fun onViewModelCreated(viewModel: ViewModel)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        val viewModel =
            (if (StatefulViewModel::class.java.isAssignableFrom(modelClass)) {
                if (modelClass.hasConstructor(BaseApplication::class.java)) {
                    modelClass
                        .getConstructor(BaseApplication::class.java)
                        .newInstance(activity.application) as ViewModel
                } else if (modelClass.hasConstructor(BaseActivity::class.java)) {
                    modelClass
                        .getConstructor(BaseActivity::class.java)
                        .newInstance(activity, activity.baseApplication) as ViewModel
                } else if (modelClass.hasConstructor(Context::class.java)) {
                    modelClass
                        .getConstructor(Context::class.java)
                        .newInstance(activity, activity) as ViewModel
                } else if (modelClass.hasConstructor()) {
                    modelClass
                        .getConstructor()
                        .newInstance() as ViewModel
                } else {
                    null
                }
            } else if (ViewModel::class.java.isAssignableFrom(modelClass)) {
                if (modelClass.hasConstructor(Application::class.java)) {
                    modelClass
                        .getConstructor(Application::class.java)
                        .newInstance(activity.application) as ViewModel
                } else if (modelClass.hasConstructor(AppCompatActivity::class.java)) {
                    modelClass
                        .getConstructor(AppCompatActivity::class.java)
                        .newInstance(activity) as ViewModel
                } else if (modelClass.hasConstructor()) {
                    modelClass
                        .getConstructor()
                        .newInstance() as ViewModel
                } else {
                    null
                }
            } else {
                null
            }) ?: throw IllegalArgumentException(
                "" +
                        "Invalid ViewModel class provided ${modelClass.canonicalName}. Must be either (instanciated in this order):\n" +
                        "- StatefulViewModel with BaseApplication/BaseActivity/Context constructor; or \n" +
                        "- ViewModel with Application/AppCompatActivity/empty constructors.\n"
            )
        onViewModelCreated.onViewModelCreated(viewModel)
        return super.create(modelClass)
    }

}