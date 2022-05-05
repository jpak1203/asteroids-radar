package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.TimeFrameProvider.getToday
import com.udacity.asteroidradar.database.TimeFrameProvider.getWeek
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.models.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidRepository(private val database: AsteroidDatabase){

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids(getToday())) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids(
        startDate: String = getToday(),
        endDate: String = getWeek()
    ) {
        var asteroidList: List<Asteroid>
        withContext(Dispatchers.IO) {
            val asteroidResponseBody = AsteroidApi.asteroids.getAsteroidsAsync(
                startDate, endDate, Constants.API_KEY
            ).await()

            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
            val networkAsteroidContainer = NetworkAsteroidContainer(asteroidList)

            database.asteroidDao.insertAll(*networkAsteroidContainer.asDatabaseModel())
        }
    }
}