package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.models.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg asteroid: Asteroid)

    @Query("SELECT * FROM asteroids_table ORDER BY closeApproachDate DESC")
    fun getAllAsteroids() : LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate >= :startDay AND closeApproachDate <= :endDay ORDER BY closeApproachDate")
    fun getAsteroidsFromThisWeek(startDay: String, endDay: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate = :today ORDER BY closeApproachDate DESC")
    fun getAsteroidToday(today: String): LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroids_table")
    suspend fun clear()
}

@Database(entities = [Asteroid::class], version=1)
abstract class AsteroidDatabase : RoomDatabase(){
    abstract val asteroidDao : AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids_table").build()
        }
    }
    return INSTANCE
}