package fi.tiituste.elisaspaceflightnews.api

import fi.tiituste.elisaspaceflightnews.data.ArticleModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object SpaceFlightApi {
    const val URL = "https://api.spaceflightnewsapi.net/v3/"

    interface Service {
        @GET("articles?_limit=7")
        suspend fun getArticles(): List<ArticleModel>
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: Service = retrofit.create(Service::class.java)
}