package dk.sidereal.corelogic.app.repo

import android.content.Context
import com.google.gson.JsonElement
import dk.sidereal.corelogic.app.api.GithubService

interface DataRepository {
    suspend fun getSomeData(): String
}

class DataRepositoryImpl(private val context: Context,
                         private val githubService: GithubService) : DataRepository{

    override suspend fun getSomeData(): String {
        return githubService.listRepos().toString()
    }

}