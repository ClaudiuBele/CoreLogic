package dk.sidereal.corelogic.app.api

import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {

    companion object {
        fun getRetrofit(gson: Gson): Retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

        fun getService(retrofit: Retrofit) =
            retrofit.create(GithubService::class.java)
    }

    @GET("search/repositories?q=stars%3A>0&s=stars&type=Repositories")
    suspend fun listRepos(): JsonElement


    @GET("/repos/{ownerData}/{repo}/pulls")
    suspend fun pullRequest(@Path("ownerData") owner: String, @Path("repo") repo: String): ResponseBody
}

