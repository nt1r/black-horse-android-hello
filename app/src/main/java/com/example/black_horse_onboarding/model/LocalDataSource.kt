package com.example.black_horse_onboarding.model

import android.content.Context
import androidx.room.Room
import io.reactivex.rxjava3.core.Single

class LocalDataSource {
    companion object {
        private var database: PersonDatabase? = null
        private var context: Context? = null

        fun setContext(newContext: Context) {
            context = newContext
        }

        private fun getDatabase(): PersonDatabase? {
            if (database == null) {
                database = Room.databaseBuilder(context!!, PersonDatabase::class.java, "person_database").build()
            }
            return database
        }

        fun createPerson(person: Person): Boolean {
            getDatabase()!!.personDao().create(person)
            return true
        }

        fun getPersons(): Single<List<Person>> {
            val persons = getDatabase()!!.personDao().getAll()
            return Single.just(persons)
        }
    }
}