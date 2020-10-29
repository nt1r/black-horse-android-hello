package com.example.black_horse_onboarding.model

import androidx.room.*

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun getAll(): List<Person>

    @Insert
    fun create(person: Person): Long

    @Update
    fun update(person: Person): Int

    @Delete
    fun delete(person: Person): Int
}