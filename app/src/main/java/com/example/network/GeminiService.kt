package com.example.network

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>,
    @Json(name = "role") val role: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null,
    @Json(name = "inline_data") val inlineData: GeminiInlineData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiInlineData(
    @Json(name = "mime_type") val mimeType: String,
    @Json(name = "data") val data: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.4f,
    @Json(name = "topP") val topP: Float? = 0.95f,
    @Json(name = "topK") val topK: Int? = 40,
    @Json(name = "maxOutputTokens") val maxOutputTokens: Int? = 2048
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }

    suspend fun generateText(
        prompt: String,
        systemInstruction: String? = null,
        apiKeyOverride: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = apiKeyOverride?.trim()?.takeIf { it.isNotBlank() } ?: BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("Gemini API key is not configured. Please enter your Gemini API key in settings."))
        }

        val parts = listOf(GeminiPart(text = prompt))
        val sysContent = systemInstruction?.let {
            GeminiContent(parts = listOf(GeminiPart(text = it)))
        }

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = parts)),
            systemInstruction = sysContent,
            generationConfig = GeminiGenerationConfig(temperature = 0.5f)
        )

        try {
            val response = api.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (text != null) {
                Result.success(text)
            } else {
                Result.failure(Exception("Empty response from AI"))
            }
        } catch (e: Exception) {
            val msg = when {
                e.message?.contains("400") == true -> "Invalid request or API key format."
                e.message?.contains("403") == true -> "API key is unauthorized or quota exceeded. Check your Gemini API key."
                e.message?.contains("404") == true -> "Gemini model endpoint not found."
                else -> e.message ?: "Failed to connect to Gemini API"
            }
            Result.failure(Exception(msg, e))
        }
    }

    suspend fun analyzeImage(
        bitmap: Bitmap,
        prompt: String,
        apiKeyOverride: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = apiKeyOverride?.trim()?.takeIf { it.isNotBlank() } ?: BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("Gemini API key is not configured. Please enter your Gemini API key in settings."))
        }

        val base64Image = bitmap.toBase64()
        val parts = listOf(
            GeminiPart(text = prompt),
            GeminiPart(inlineData = GeminiInlineData(mimeType = "image/jpeg", data = base64Image))
        )

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = parts)),
            generationConfig = GeminiGenerationConfig(temperature = 0.2f)
        )

        try {
            val response = api.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (text != null) {
                Result.success(text)
            } else {
                Result.failure(Exception("Could not analyze food image"))
            }
        } catch (e: Exception) {
            val msg = when {
                e.message?.contains("403") == true -> "API key unauthorized or quota exceeded."
                else -> e.message ?: "Could not analyze food image"
            }
            Result.failure(Exception(msg, e))
        }
    }

    suspend fun testApiKey(apiKey: String): Result<String> = withContext(Dispatchers.IO) {
        val trimmed = apiKey.trim()
        if (trimmed.isBlank() || trimmed == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("Key cannot be empty or placeholder"))
        }

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = "Respond with: OK")))),
            generationConfig = GeminiGenerationConfig(temperature = 0.1f)
        )

        try {
            val response = api.generateContent(trimmed, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!text.isNullOrBlank()) {
                Result.success("Connection verified successfully! Gemini is active.")
            } else {
                Result.failure(Exception("Received empty response from Gemini."))
            }
        } catch (e: Exception) {
            val msg = when {
                e.message?.contains("400") == true -> "Invalid API key format."
                e.message?.contains("403") == true -> "API key unauthorized or quota exceeded. Please check your key."
                e.message?.contains("404") == true -> "Model endpoint unavailable."
                else -> e.message ?: "Connection test failed."
            }
            Result.failure(Exception(msg, e))
        }
    }

    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}
