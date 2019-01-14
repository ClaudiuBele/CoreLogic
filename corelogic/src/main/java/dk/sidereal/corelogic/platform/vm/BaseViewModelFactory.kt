package dk.sidereal.corelogic.platform.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.sidereal.corelogic.platform.lifecycle.BaseApplication

class BaseViewModelFactory(val application: BaseApplication, val onCreated: (BaseViewModel) -> Unit) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (BaseViewModel::class.java.isAssignableFrom(modelClass)) {
            val viewModel = modelClass
                .getConstructor(BaseApplication::class.java)
                .newInstance(application)
        }

        return super.create(modelClass)
    }

}