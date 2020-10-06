package dk.sidereal.corelogic.app.repo

import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.api.model.RepositoryResponse
import dk.sidereal.corelogic.kotlin.ManagedCoroutineScope
import retrofit2.Response

interface DataRepository {
    suspend fun getSomeData(): Response<RepositoryResponse>
    fun receivedData(): Boolean
}

class DataRepositoryImpl(
    private val githubService: GithubService,
    private val scope: ManagedCoroutineScope
) : DataRepository {

    var receivedInternal: Boolean = false

    override suspend fun getSomeData(): Response<RepositoryResponse> = scope.run {
        val resp = githubService.listRepos()
        receivedInternal = true
        return resp
    }

    override fun receivedData(): Boolean {
        return receivedInternal
    }


}