package dk.sidereal.corelogic.app

import com.google.gson.Gson
import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.repo.DataRepository
import dk.sidereal.corelogic.app.repo.DataRepositoryImpl
import dk.sidereal.corelogic.app.view.MoreInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {

    val modules = module {
        single { Gson() }
        single { GithubService.getRetrofit(get()) }
        single { GithubService.getService(get()) }
        single<DataRepository> { DataRepositoryImpl(get(), get()) }
        viewModel { MoreInfoViewModel(get(), get()) }
    }
}