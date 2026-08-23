package com.example.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceCoachManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    var isMuted = false

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w("VoiceCoach", "Language US not supported or missing data")
                }
                tts?.setSpeechRate(1.05f)
                tts?.setPitch(1.0f)
                isInitialized = true
            } else {
                Log.e("VoiceCoach", "TTS Initialization failed: $status")
            }
        }
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (isMuted || !isInitialized) return
        try {
            tts?.speak(text, queueMode, null, "FitPulse_TTS_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.e("VoiceCoach", "Error speaking text: ${e.message}")
        }
    }

    fun speakCountdown(number: Int) {
        if (isMuted || !isInitialized) return
        speak("$number", TextToSpeech.QUEUE_FLUSH)
    }

    fun speakMotivation() {
        val motivationalQuotes = listOf(
            "Push through! You've got this!",
            "Stay strong, keep that perfect form!",
            "Every single rep counts! Let's go!",
            "Focus on the contraction, power through!",
            "Mind to muscle connection! Finish strong!"
        )
        speak(motivationalQuotes.random(), TextToSpeech.QUEUE_ADD)
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
