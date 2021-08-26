package com.example.scheduleexercise.models

class Game {

 var Week = ""
 var Label = ""
 var TV = ""
 var Radio = ""
 var Venue = ""
 var WLT = ""
 var GameState = ""
 var Clock = ""
 var QuarterShorthand = ""
 var Quarter = ""
 var AwayScore = ""
 var HomeScore = ""
 var Down = ""
 var IsHome = false;
 var ScheduleHeader = ""
 var IsSuperStadium = false;
 var CardData:CardData? = null;

 var Id = ""
 var Type = ""
 var Date:DateObject? = null
 var Opponent:Opponent? = null
 var Tickets :Tickets? = null
 var Result = ""
 var Home = ""
 var Buttons :List<ButtonData> = ArrayList();
 //var SecondaryButtons = ""
}
class ButtonData{
  var Title = ""
  var URL = ""
}
class CardData{
    var ClickthroughURL = ""
    var IsDefault = false
}
class DateObject{
 var Numeric = ""
 var Text = ""
 var Time = ""
 var Timestamp = ""
 var IsTBA = ""
 var IsTba = ""
}
class Opponent {
 var TriCode = ""
 var FullName = ""
 var Name = ""
 var City = ""
 var Record = ""
}
class Tickets{
 var HasLink = ""
 var Label = ""
 var Link = ""
}
