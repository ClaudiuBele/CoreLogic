package dk.sidereal.corelogic.app.repo

import android.content.Context
import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.api.model.RepositoryResponse
import retrofit2.Response

interface DataRepository {
    suspend fun getSomeData(): Response<RepositoryResponse>
}

class DataRepositoryImpl(
    private val context: Context,
    private val githubService: GithubService
) : DataRepository {

    override suspend fun getSomeData(): Response<RepositoryResponse> {
        return githubService.listRepos()
    }

}