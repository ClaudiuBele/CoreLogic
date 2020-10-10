package dk.sidereal.corelogic.app.repo

import android.annotation.SuppressLint
import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.api.model.RepositoryResponse
import dk.sidereal.corelogic.kotlin.BaseSchedulerProvider
import dk.sidereal.corelogic.kotlin.ManagedCoroutineScope
import io.reactivex.Observable
import retrofit2.Response

interface DataRepository {
    suspend fun getSomeData(): Response<RepositoryResponse>
     fun getSomeDataObs(): Observable<RepositoryResponse>
    fun receivedData(): Boolean
}

class DataRepositoryImpl(
    private val githubService: GithubService,
    private val scope: ManagedCoroutineScope,
    private val schedulerProvider: BaseSchedulerProvider
) : DataRepository {

    var receivedInternal: Boolean = false

    override suspend fun getSomeData(): Response<RepositoryResponse> = scope.run {
        val resp = githubService.listRepos()
        receivedInternal = true
        return resp
    }

    @SuppressLint("CheckResult")
    override  fun getSomeDataObs(): Observable<RepositoryResponse> {
        val obs = githubService.listReposObs()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
        obs.subscribe {
            if (it!= null) {
                receivedInternal = true
            }
        }
        return obs
    }

    override fun receivedData(): Boolean {
        return receivedInternal
    }


}