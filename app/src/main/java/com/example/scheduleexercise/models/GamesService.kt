package com.example.scheduleexercise.models

import retrofit2.Call
import retrofit2.http.GET


interface GamesService {

    val gameSections: Call<TeamGames>
        @GET("schedule.json")
        get;

}