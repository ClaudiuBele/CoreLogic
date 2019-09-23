package dk.sidereal.corelogic.app.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {

    @GET("search/repositories?q=stars%3A>0&s=stars&type=Repositories")
    suspend fun listRepos(): ResponseBody


    @GET("/repos/{ownerData}/{repo}/pulls")
    suspend fun pullRequest(@Path("ownerData") owner: String, @Path("repo") repo: String): ResponseBody
}