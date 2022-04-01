package com.example.a7minuteworkout

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minuteworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding : ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistory)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Workout History"
        }

        binding?.toolbarHistory?.setNavigationOnClickListener {
            onBackPressed()
        }
        getAllDates()
    }


    private fun getAllDates() {
        var sharedPref  = getSharedPreferences("workout_dates", MODE_PRIVATE)

        val alldates = sharedPref.getStringSet("date",setOf<String>())

        if (alldates != null) {
            if(alldates.isNotEmpty()){
                binding?.tvNoDataAvailable?.visibility = View.GONE
                binding?.tvHistory?.visibility = View.VISIBLE
                binding?.rvHistory?.visibility = View.VISIBLE

                binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                val dates = ArrayList<String>()
                for(date in alldates){
                    dates.add(date)
                }
                val historyAdapter = HistoryAdapter(dates)
                binding?.rvHistory?.adapter = historyAdapter
            }else{
                binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                binding?.tvHistory?.visibility = View.GONE
                binding?.rvHistory?.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        binding=null
        super.onDestroy()
    }
}