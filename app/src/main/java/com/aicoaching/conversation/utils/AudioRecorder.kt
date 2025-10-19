package com.aicoaching.conversation.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class AudioRecorder(private val context: Context) {
    
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    
    suspend fun startRecording(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val outputDir = File(context.cacheDir, "audio_recordings")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }
            
            outputFile = File(outputDir, "recording_${System.currentTimeMillis()}.3gp")
            
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }
            
            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(outputFile!!.absolutePath)
                prepare()
                start()
            }
            
            Result.success(outputFile!!.absolutePath)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun stopRecording(): Result<String> = withContext(Dispatchers.IO) {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            
            val audioBase64 = outputFile?.let { file ->
                val inputStream = FileInputStream(file)
                val bytes = inputStream.readBytes()
                inputStream.close()
                Base64.encodeToString(bytes, Base64.DEFAULT)
            }
            
            // Clean up the file
            outputFile?.delete()
            outputFile = null
            
            if (audioBase64 != null) {
                Result.success(audioBase64)
            } else {
                Result.failure(Exception("Failed to encode audio"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun isRecording(): Boolean {
        return mediaRecorder != null
    }
}
