package fi.tiituste.elisaspaceflightnews.repository

import fi.tiituste.elisaspaceflightnews.api.SpaceFlightApi

class SpaceFlightNewsRepository {
    private val call = SpaceFlightApi.service

    suspend fun getArticles() = call.getArticles()
}