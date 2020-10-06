package dk.sidereal.corelogic.app

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import com.google.gson.Gson
import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.repo.DataRepository
import dk.sidereal.corelogic.app.repo.DataRepositoryImpl
import dk.sidereal.corelogic.app.view.MoreInfoViewModel
import dk.sidereal.corelogic.kotlin.LifecycleManagedCoroutineScope
import dk.sidereal.corelogic.kotlin.ManagedCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val modules = module {
        // val lifecycleCoroutineScope: LifecycleCoroutineScope
        // override val coroutineContext: CoroutineContext
        single <CoroutineScope> { CoroutineScope(Dispatchers.IO)}
        single <ManagedCoroutineScope> { LifecycleManagedCoroutineScope(get<Lifecycle>().coroutineScope,get())}
        single { Gson() }
        single { GithubService.getRetrofit(get()) }
        single { GithubService.getService(get()) }
        single<DataRepository> { DataRepositoryImpl(get(),get()) }
        viewModel { MoreInfoViewModel(get(), get()) }
    }
}