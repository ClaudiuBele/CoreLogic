package dk.sidereal.corelogic.app.api

import com.google.gson.Gson
import dk.sidereal.corelogic.app.api.model.RepositoryResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

        fun getService(retrofit: Retrofit) =
            retrofit.create(GithubService::class.java)
    }

    @GET("search/repositories?q=stars%3A>0&s=stars&type=Repositories")
    suspend fun listRepos(): Response<RepositoryResponse>

    @GET("search/repositories?q=stars%3A>0&s=stars&type=Repositories")
     fun listReposObs(): Observable<RepositoryResponse>


    @GET("/repos/{ownerData}/{repo}/pulls")
    suspend fun pullRequest(@Path("ownerData") owner: String, @Path("repo") repo: String): ResponseBody
}

