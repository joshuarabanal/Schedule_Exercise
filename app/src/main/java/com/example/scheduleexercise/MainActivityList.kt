package com.example.scheduleexercise

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleexercise.models.Game
import com.example.scheduleexercise.models.GameSection
import com.example.scheduleexercise.models.MainActivityViewModel
import com.example.scheduleexercise.models.TeamGames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivityList(context:LifecycleOwner, val vm: MainActivityViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dateParser = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
    private val DateFormatter = SimpleDateFormat("EEE, MMM dd")
    private val timeFormatter = SimpleDateFormat("hh:mm a")
    private fun convertTime(input:String):String{
        var date = dateParser.parse(input);
        var calen = Calendar.getInstance()
        calen.time = date
        calen.set(Calendar.HOUR_OF_DAY, calen.get(Calendar.HOUR_OF_DAY)-4);
        return timeFormatter.format(calen.time)
    }
    private fun convertDate(input:String):String{
        var date = dateParser.parse(input);
        var calen = Calendar.getInstance()
        calen.time = date
        calen.set(Calendar.HOUR_OF_DAY, calen.get(Calendar.HOUR_OF_DAY)-4);
        return DateFormatter.format(calen.time)
    }
    private val HeaderType = 2;
    private val ItemType = 3;
    private val BYEType = 4;

    init{
        vm.gameSections.observe(context){
            this.gameSections = it;
            this.notifyDataSetChanged();
        }
    }

    private var gameSections:TeamGames? = null
    override fun getItemCount() = if(gameSections == null) 0
        else gameSections!!.GameSection.sumOf { it.Game!!.size + 1 };
    override fun getItemViewType(index:Int):Int{
        var i  = index;
        if(index == 0) return HeaderType;
        for(section in gameSections?.GameSection ?: ArrayList<GameSection>()){
            i-= section.Game!!.size+ 1;
            if(i < 0)return if(getItem(index).ScheduleHeader.contains("BYE")) BYEType else ItemType;
            if(i == 0) return HeaderType;
        }
        if(i == 0)return HeaderType
        else return if(getItem(index).ScheduleHeader.contains("BYE")) BYEType else ItemType;
    }
    private fun getHeading(index:Int):String{
        var count = 0;
        for(section in gameSections!!.GameSection){
            if(count == index)return section.Heading!!;
            count++;
            count += section.Game!!.size;
        }
        throw IndexOutOfBoundsException("heading not available:$index");
    }
    private fun getItem(index:Int):Game{
        if(index== 0) throw IndexOutOfBoundsException("invalid index");
        var count = 0;
        for(section in gameSections!!.GameSection){
            count++;
            if(count + section.Game.size> index){
                if(section.Game!!.size <=index - count) throw IndexOutOfBoundsException("failed to find:$index of $count")

                return section.Game!![ index - count ]
            };
            count += section.Game!!.size;
        }
        throw IndexOutOfBoundsException("item not available");
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == HeaderType)
            (holder as Header).heading = getHeading(position);
        else if(getItemViewType(position) == ItemType)(holder as Holder).listItem = getItem(position);
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if(viewType == HeaderType)Header(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_listview_header,
                    parent,
                    false
                )
        )
        else if(viewType == BYEType) BYE(LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_listview_bye,
                parent,
                false
            ))
        else Holder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_listview_item,
                    parent,
                    false
                )
        );





    inner class Holder(root: View) : RecyclerView.ViewHolder(root){
        val teamAScore = root.findViewById<TextView>(R.id.teamAScore);
        val teamAIcon = root.findViewById<ImageView>(R.id.teamAIcon)
        val teamAName = root.findViewById<TextView>(R.id.teamA);
        val teamBScore = root.findViewById<TextView>(R.id.teamBScore);
        val teamBIcon = root.findViewById<ImageView>(R.id.teamBIcon)
        val temBName = root.findViewById<TextView>(R.id.teamB);
        val gameDate = root.findViewById<TextView>(R.id.gameDate)
        val gameTime = root.findViewById<TextView>(R.id.gameTime)
        val channel = root.findViewById<TextView>(R.id.broadCaster)
        val week = root.findViewById<TextView>(R.id.week)

        var listItem: Game? = null
                set(value){
                    field = value
                    teamAScore.text = value!!.HomeScore;
                    teamBScore.text = value.AwayScore;
                    week.text = value.Week
                    channel.text =
                            if(value.TV != null && value.TV != "" )value.TV
                            else if(value.Radio != null && value.Radio != "" )value.Radio
                            else value.Venue;
                    temBName.text = value.Opponent?.FullName
                    gameDate.text =
                        if(value.Date != null)convertDate(value.Date!!.Timestamp)
                        else "";

                    gameTime.text = convertTime(value.Date!!.Timestamp)
                    LoadImageToView("http://yc-app-resources.s3.amazonaws.com/nfl/logos/nfl_phi_light.png", teamAIcon)

                    teamAName.text = gameSections!!.Team?.FullName
                    LoadImageToView("https://yc-app-resources.s3.amazonaws.com/nfl/logos/nfl_${value.Opponent?.TriCode?.lowercase()}_light.png", teamBIcon)
                };

        private fun LoadImageToView(url: String, iv: ImageView) {
            GlobalScope.launch {
                try{
                    val input: InputStream = URL(url).getContent() as InputStream
                    val drawable = Drawable.createFromStream(input, "src name")
                    withContext(Dispatchers.Main){
                        iv.setImageDrawable(drawable)
                    }
                }
                catch(e:Exception){
                    Log.i("url didnt work", url);
                }

            }
        }
    }
    inner class Header(root: View) : RecyclerView.ViewHolder(root){
        var heading:String = ""
                set(value) {
                    field = value;
                    this.itemView.findViewById<TextView>(R.id.header_title).text = value;
                };
    }
    inner class BYE(root:View) : RecyclerView.ViewHolder(root) {}

}