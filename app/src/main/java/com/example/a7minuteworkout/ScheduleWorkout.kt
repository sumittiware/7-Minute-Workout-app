package com.example.a7minuteworkout

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.a7minuteworkout.databinding.ActivityScheduleWorkoutBinding
import java.util.*

class ScheduleWorkout : AppCompatActivity() {
    var binding : ActivityScheduleWorkoutBinding? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleWorkoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarSchedule)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Schedule Workout"
        }
        binding?.toolbarSchedule?.setNavigationOnClickListener {
            onBackPressed()
        }
        createNotificationChannel()
        binding?.scheduleBtn?.setOnClickListener {
//            scheduleNotification()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification(notification : Notification, delay: Long) {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Let's do 7 minutes workout"
        intent.putExtra(titleExtra, title)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime(): Long
    {
        val minute = binding?.timePicker?.minute
        val hour = binding?.timePicker?.hour
        val c  = Calendar.getInstance()
        val dateTime = c.time

        val calendar = Calendar.getInstance()
        if (hour != null && minute != null) {
            calendar.set(dateTime.year, dateTime.month, dateTime.day, hour, minute)
        }
        return calendar.timeInMillis
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel()  {
        val name = "7 minutes workout "
        val desc = "workout time fot the 7 min workout app"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}