package com.boxingtimer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.os.PowerManager
import android.content.Context

class MainActivity : AppCompatActivity() {
    
    // UI Components
    private lateinit var timerDisplay: TextView
    private lateinit var phaseDisplay: TextView
    private lateinit var trainingTimePicker: NumberPicker
    private lateinit var restTimePicker: NumberPicker
    private lateinit var roundsPicker: NumberPicker
    private lateinit var bufferTimePicker: NumberPicker
    private lateinit var startButton: Button
    private lateinit var pauseResumeButton: Button
    private lateinit var resetButton: Button
    
    // Timer variables
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var isPaused = false
    private var timeLeftInMillis: Long = 0
    
    // Configuration variables (in seconds)
    private var trainingTime = 180 // 3 minutes
    private var restTime = 45
    private var numberOfRounds = 3
    private var bufferTime = 30
    
    // Current state
    private var currentPhase = TimerPhase.BUFFER
    private var currentRound = 1
    private var totalRounds = 3
    
    // Audio players
    private var singleBellPlayer: MediaPlayer? = null
    private var doubleBellPlayer: MediaPlayer? = null
    private var finalBellPlayer: MediaPlayer? = null
    
    // Wake lock for background processing
    private var wakeLock: PowerManager.WakeLock? = null
    
    enum class TimerPhase {
        BUFFER, TRAINING, REST, COMPLETE
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Hide action bar
        supportActionBar?.hide()
        
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupNumberPickers()
        setupButtons()
        initializeAudio()
        resetToDefaults()
        
        // Initialize wake lock for background processing
        initializeWakeLock()
    }
    
    private fun initializeViews() {
        timerDisplay = findViewById(R.id.timerDisplay)
        phaseDisplay = findViewById(R.id.phaseDisplay)
        trainingTimePicker = findViewById(R.id.trainingTimePicker)
        restTimePicker = findViewById(R.id.restTimePicker)
        roundsPicker = findViewById(R.id.roundsPicker)
        bufferTimePicker = findViewById(R.id.bufferTimePicker)
        startButton = findViewById(R.id.startButton)
        pauseResumeButton = findViewById(R.id.pauseResumeButton)
        resetButton = findViewById(R.id.resetButton)
    }
    
    private fun setupNumberPickers() {
        // Training time picker (1-10 minutes)
        trainingTimePicker.minValue = 1
        trainingTimePicker.maxValue = 10
        trainingTimePicker.value = 3
        
        // Rest time picker (15-120 seconds)
        restTimePicker.minValue = 15
        restTimePicker.maxValue = 120
        restTimePicker.value = 45
        
        // Rounds picker (1-12 rounds)
        roundsPicker.minValue = 1
        roundsPicker.maxValue = 12
        roundsPicker.value = 3
        
        // Buffer time picker (10-60 seconds)
        bufferTimePicker.minValue = 10
        bufferTimePicker.maxValue = 60
        bufferTimePicker.value = 30
    }
    
    private fun setupButtons() {
        startButton.setOnClickListener {
            startWorkout()
        }
        
        pauseResumeButton.setOnClickListener {
            if (isPaused) {
                resumeTimer()
            } else {
                pauseTimer()
            }
        }
        
        resetButton.setOnClickListener {
            resetWorkout()
        }
    }
    
    private fun initializeAudio() {
        try {
            singleBellPlayer = MediaPlayer.create(this, R.raw.single_bell)
            doubleBellPlayer = MediaPlayer.create(this, R.raw.double_bell)
            finalBellPlayer = MediaPlayer.create(this, R.raw.final_bell)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun initializeWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "BoxingTimer::TimerWakeLock"
        )
    }
    
    private fun acquireWakeLock() {
        wakeLock?.let {
            if (!it.isHeld) {
                it.acquire(10*60*1000L /*10 minutes*/)
            }
        }
    }
    
    private fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
    }
    
    private fun startWorkout() {
        // Get current configuration
        trainingTime = trainingTimePicker.value * 60 // Convert minutes to seconds
        restTime = restTimePicker.value
        numberOfRounds = roundsPicker.value
        totalRounds = numberOfRounds
        bufferTime = bufferTimePicker.value
        
        // Reset state
        currentPhase = TimerPhase.BUFFER
        currentRound = 1
        
        // Acquire wake lock to keep timer running in background
        acquireWakeLock()
        
        // Disable configuration controls
        setConfigurationEnabled(false)
        
        // Start buffer phase
        startBufferPhase()
        
        // Update button states
        startButton.isEnabled = false
        pauseResumeButton.isEnabled = true
        pauseResumeButton.text = "Pause"
    }
    
    private fun startBufferPhase() {
        currentPhase = TimerPhase.BUFFER
        phaseDisplay.text = "Buffer Time"
        startTimer(bufferTime * 1000L) {
            playBell(singleBellPlayer)
            startTrainingPhase()
        }
    }
    
    private fun startTrainingPhase() {
        currentPhase = TimerPhase.TRAINING
        phaseDisplay.text = "Round $currentRound - Training"
        startTimer(trainingTime * 1000L) {
            playBell(doubleBellPlayer)
            if (currentRound < totalRounds) {
                startRestPhase()
            } else {
                completeWorkout()
            }
        }
    }
    
    private fun startRestPhase() {
        currentPhase = TimerPhase.REST
        phaseDisplay.text = "Rest Time"
        startTimer(restTime * 1000L) {
            playBell(singleBellPlayer)
            currentRound++
            startTrainingPhase()
        }
    }
    
    private fun completeWorkout() {
        currentPhase = TimerPhase.COMPLETE
        phaseDisplay.text = "Workout Complete!"
        timerDisplay.text = "00:00"
        playBell(finalBellPlayer)
        
        // Release wake lock when workout is complete
        releaseWakeLock()
        
        // Reset button states
        isTimerRunning = false
        isPaused = false
        startButton.isEnabled = true
        pauseResumeButton.isEnabled = false
        setConfigurationEnabled(true)
    }
    
    private fun startTimer(millisInFuture: Long, onFinish: () -> Unit) {
        timeLeftInMillis = millisInFuture
        isTimerRunning = true
        isPaused = false
        
        countDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerDisplay(millisUntilFinished)
            }
            
            override fun onFinish() {
                onFinish()
            }
        }.start()
    }
    
    private fun pauseTimer() {
        countDownTimer?.cancel()
        isPaused = true
        isTimerRunning = false
        pauseResumeButton.text = "Resume"
    }
    
    private fun resumeTimer() {
        isPaused = false
        pauseResumeButton.text = "Pause"
        
        val onFinish = when (currentPhase) {
            TimerPhase.BUFFER -> {
                { playBell(singleBellPlayer); startTrainingPhase() }
            }
            TimerPhase.TRAINING -> {
                { 
                    playBell(doubleBellPlayer)
                    if (currentRound < totalRounds) {
                        startRestPhase()
                    } else {
                        completeWorkout()
                    }
                }
            }
            TimerPhase.REST -> {
                { 
                    playBell(singleBellPlayer)
                    currentRound++
                    startTrainingPhase()
                }
            }
            else -> { {} }
        }
        
        startTimer(timeLeftInMillis, onFinish)
    }
    
    private fun resetWorkout() {
        countDownTimer?.cancel()
        releaseWakeLock()
        resetToDefaults()
    }
    
    private fun resetToDefaults() {
        // Reset timer state
        isTimerRunning = false
        isPaused = false
        currentPhase = TimerPhase.BUFFER
        currentRound = 1
        
        // Reset display
        timerDisplay.text = "00:30"
        phaseDisplay.text = "Ready to Start"
        
        // Reset configuration to defaults
        trainingTimePicker.value = 3
        restTimePicker.value = 45
        roundsPicker.value = 3
        bufferTimePicker.value = 30
        
        // Reset button states
        startButton.isEnabled = true
        pauseResumeButton.isEnabled = false
        pauseResumeButton.text = "Pause"
        setConfigurationEnabled(true)
    }
    
    private fun updateTimerDisplay(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60
        timerDisplay.text = String.format("%02d:%02d", minutes, seconds)
    }
    
    private fun playBell(mediaPlayer: MediaPlayer?) {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                    it.prepare()
                }
                it.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setConfigurationEnabled(enabled: Boolean) {
        trainingTimePicker.isEnabled = enabled
        restTimePicker.isEnabled = enabled
        roundsPicker.isEnabled = enabled
        bufferTimePicker.isEnabled = enabled
    }
    
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        singleBellPlayer?.release()
        doubleBellPlayer?.release()
        finalBellPlayer?.release()
    }
    
    override fun onPause() {
        super.onPause()
        // Don't pause timer when screen locks - let it continue in background
        // Timer will continue running with wake lock
    }
    
    override fun onResume() {
        super.onResume()
        // Update display when returning to app
        if (isTimerRunning) {
            // Timer is still running, just refresh the display
        }
    }
}