package com.example.a7minuteworkout

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minuteworkout.databinding.ActivityExerciseBinding
import java.util.*


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
//    Countdown timer
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTime = 10


//    Exercise timer
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTime = 30

    private var exerciseList : ArrayList<ExerciseModel>?=null
    private var currentExercisePosition = -1

    private var binding:ActivityExerciseBinding? = null
    private var tts : TextToSpeech? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null
    private  var dialogBuilder  :  AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)
        setupRestView() // REST View is set in this function
        setUpExerciseStatusView()// Exercise Recycle is setup here
    }

    private fun setUpExerciseStatusView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView() {
        binding?.flProgressBar?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.GONE
        binding?.exerciseImage?.visibility = View.GONE
        binding?.tvTitle?.text = when(currentExercisePosition){
            -1 -> "GET READY"
            else ->"GET READY FOR \n${exerciseList?.get(currentExercisePosition+1)?.getName()}"
        }
        if(currentExercisePosition!=-1){
            speakOut("Take rest")
        }
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress
        restTimer = object : CountDownTimer((restTime*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text =
                    (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                    exerciseList!![currentExercisePosition].setIsSelected(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setExerciseView()

            }
        }.start()
    }

    private fun setExerciseView(){
        var exName = exerciseList?.get(currentExercisePosition)?.getName()
        binding?.flProgressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.text = exName
        //setting the background image here
        exerciseList?.get(currentExercisePosition)?.getImage()
            ?.let { binding?.exerciseImage?.setImageResource(it) }
        //view binding
        binding?.exerciseImage?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
//        speak out the next exercise now
        speakOut("30 seconds $exName")
        if(exerciseTimer!=null){
            exerciseTimer?.cancel();
            exerciseProgress=0
        }
        setExerciseProgressBar()
    }

    private  fun setExerciseProgressBar() {
        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer((exerciseTime*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition< exerciseList?.size!!-1){
                    exerciseList?.get(currentExercisePosition)?.setIsCompleted(true)
                    exerciseList?.get(currentExercisePosition)?.setIsSelected(false)
                    exerciseAdapter?.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }

            }
        }.start()
    }

    public override fun onDestroy() {
//        canceling all the timers on popping screen to avoid memory leaks
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress= 0
        }
        super.onDestroy()
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private  fun speakOut(msg:String){
        tts?.speak(msg, TextToSpeech.QUEUE_FLUSH, null, "")
    }


    override fun onBackPressed() {
        dialogBuilder = AlertDialog.Builder(this)
        //Creating dialog box
        dialogBuilder?.setTitle("Are you sure?")
        dialogBuilder?.setMessage("Do you want to stop workout ?")
        dialogBuilder?.setPositiveButton("Yes") { _, _ ->
            super.onBackPressed()
        }
        dialogBuilder?.setNegativeButton("No") { _, _ ->
            print("Fuck you")
        }

            var alert = dialogBuilder?.create()
            alert?.setCancelable(false)
            alert?.show()
        }
    }
