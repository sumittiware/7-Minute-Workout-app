package com.example.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.a7minuteworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flStart?.setOnClickListener{
            val intent = Intent(this, FinishActivity::class.java)
//            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flSchedule?.setOnClickListener {
            val intent = Intent(this, ScheduleWorkout::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

//    release the binding variable to avoid the memory leak
    override fun onDestroy() {
        binding=null
        super.onDestroy()

    }
}