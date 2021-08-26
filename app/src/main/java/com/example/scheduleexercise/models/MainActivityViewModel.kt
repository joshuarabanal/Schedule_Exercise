package com.example.scheduleexercise.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

public class MainActivityViewModel(app: Application): AndroidViewModel(app){

    val gson: Gson by lazy{ GsonBuilder().serializeNulls().create() }
    val service:GamesService by lazy{
        Retrofit.Builder().baseUrl("http://files.yinzcam.com.s3.amazonaws.com/iOS/interviews/ScheduleExercise/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okhttp).build().create(GamesService::class.java)
    }

    val okhttp: OkHttpClient by lazy{
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10,TimeUnit.MINUTES)
            .writeTimeout(10,TimeUnit.MINUTES)
            .build()
    }



    val gameSections= MutableLiveData<TeamGames>(null);

    init{
        GlobalScope.launch{
            val call = service.gameSections.execute();
            if(call.isSuccessful){
                var retu = call.body();
                Log.i("MainVM", "title:${retu?.DefaultGameId}");
                gameSections.postValue(retu)
            }
        }
    }
}