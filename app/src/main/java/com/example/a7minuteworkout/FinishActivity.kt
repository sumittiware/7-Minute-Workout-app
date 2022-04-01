package com.example.a7minuteworkout

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.a7minuteworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity() {
     private var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarFinishActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        addDateToSharedPreferences()

    }

    @SuppressLint("MutatingSharedPrefs")
    private fun addDateToSharedPreferences() {
        val c  = Calendar.getInstance()
        val dateTime = c.time
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.getDefault())
        val date = sdf.format(dateTime)
        var sharedPref  = getSharedPreferences("workout_dates", MODE_PRIVATE)

        val savedDates = sharedPref.getStringSet("date",mutableSetOf<String>())
        savedDates?.add(date)

        with(sharedPref.edit()){
            putStringSet("date",savedDates)
            apply()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding =null
    }
}