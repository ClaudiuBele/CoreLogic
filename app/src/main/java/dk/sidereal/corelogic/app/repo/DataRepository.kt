package dk.sidereal.corelogic.app.repo

import android.content.Context
import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.api.model.RepositoryResponse

interface DataRepository {
    suspend fun getSomeData(): RepositoryResponse
}

class DataRepositoryImpl(
    private val context: Context,
    private val githubService: GithubService
) : DataRepository {

    override suspend fun getSomeData(): RepositoryResponse {
        return githubService.listRepos()
    }

}