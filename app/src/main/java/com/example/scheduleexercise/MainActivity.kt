package com.example.scheduleexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleexercise.models.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    val viewModel:MainActivityViewModel by lazy{
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(MainActivityViewModel::class.java);
    };
    val recyclerView:RecyclerView by lazy{ findViewById(R.id.games)}
    val mainActivityList by lazy{ MainActivityList(this,viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = mainActivityList;
    }
}