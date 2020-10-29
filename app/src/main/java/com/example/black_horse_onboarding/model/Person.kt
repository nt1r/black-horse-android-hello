package com.example.black_horse_onboarding.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Person(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "gender") val gender: Int?,
    @ColumnInfo(name = "age") val age: Int?
) {
    companion object {
        val MALE = 0
        val FEMALE = 1
    }
}