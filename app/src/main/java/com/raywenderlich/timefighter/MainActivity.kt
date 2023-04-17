package com.raywenderlich.timefighter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private var timeleft = 60
    private val countInterval:Long = 1000
    private var initialCountDown: Long = 60000
    private lateinit var countDownTimer: CountDownTimer
    private var gameStarted = false
    private var score: Int = 0
    private lateinit var button: Button
    private lateinit var scoreText: TextView
    private lateinit var timeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"the score is ${score}")

        button = findViewById(R.id.tapMeButton)
        scoreText = findViewById(R.id.scoreText)
        timeText = findViewById(R.id.timeText)



        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeleft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }



        button.setOnClickListener {
            if (!gameStarted) {
                startGame()
            }

            val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.btn_animation)
            it.startAnimation(bounceAnimation)
            increment()
        }


    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY,timeleft)
        countDownTimer.cancel()
        Log.d(TAG,"your score: ${score}; timeleft : ${timeleft} ")
    }



    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG , "application was destroyed.")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.alert_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.inform){
            showInfo()
        }
        return true
    }


    private fun showInfo(){
         val dialogueTitle = getString(R.string.your_version_1_d, BuildConfig.VERSION_NAME)
        val dalogueMessage = getString(R.string.about_item)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogueTitle)
        builder.setMessage(dalogueMessage)
        builder.create().show()
    }


    fun increment(){
        score += 5
        var newScore: String = getString(R.string.your_score,score)
        scoreText.text = newScore
    }

    fun resetGame(){
        println("1")
        score = 0

        var initialscore = getString(R.string.your_score,score)
        scoreText.text = initialscore

        var initialTimeLeft = getString(R.string.time_left,timeleft)
        timeText.text = initialTimeLeft
        println("B")
         countDownTimer = object:CountDownTimer(initialCountDown,countInterval){

            override fun onTick(millisUntilFinished: Long) {
                println("2")
               timeleft = millisUntilFinished.toInt() / 1000

               var timeLeftString = getString(R.string.time_left,timeleft)
               timeText.text = timeLeftString

            }

            override fun onFinish() {
                println("3")
                endGame()
            }
        }
        gameStarted = false
        println("A")
    }


    fun startGame(){
        println("4")
        countDownTimer.start()
        println("5")
        gameStarted = true
    }

    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score, score)
        scoreText.text = restoredScore
        val restoredTime = getString(R.string.time_left, timeleft)
        timeText.text = restoredTime
        countDownTimer = object : CountDownTimer(
            (timeleft * 1000).toLong(), countInterval) {

            override fun onTick(millisUntilFinished: Long) {
                timeleft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left, timeleft)
                timeText.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted = true

    }



    fun endGame(){
        Toast.makeText(this,getString(R.string.game_over_message), Toast.LENGTH_SHORT).show()
        resetGame()
    }


companion object{
    private const val SCORE_KEY  = "SCORE_KEY"
    private const  val TIME_LEFT_KEY = "TIME_LEFT_KEY"
}

}
