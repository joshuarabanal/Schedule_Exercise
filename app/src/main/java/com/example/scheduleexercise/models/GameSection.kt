package com.example.scheduleexercise.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TeamGames{

    var Team:Team? = null;
    var DefaultGameId:String? = null;
    var GameSection:List<GameSection> = ArrayList();
}

class GameSection {
    var Heading = "";
    var Game =  ArrayList<Game>();
}

class Team{
    var TriCode: String? = ""
    var FullName: String? = ""
    var Name: String? = ""
    var City: String? = ""
    var Record: String? = ""
    var Wins: String? = ""
    var Losses: String? = ""
    var WinPercentage: String? = ""
}