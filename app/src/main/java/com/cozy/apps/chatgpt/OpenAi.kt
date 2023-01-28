package com.cozy.apps.chatgpt

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class OpenAI(private val apiKey: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openAIService = retrofit.create(OpenAIService::class.java)

    suspend fun createCompletion(
        model: String,
        prompt: String,
        temperature: Double,
        max_tokens: Int,
        top_p: Int,
        frequency_penalty: Double,
        presence_penalty: Double,
        stop: List<String>
    ): Response<CompletionResponse> {
        val request = CompletionRequest(
            model,
            prompt,
            temperature,
            max_tokens,
            top_p,
            frequency_penalty,
            presence_penalty,
            stop
        )

        return openAIService.completions(apiKey = apiKey, request = request)
    }
}
interface OpenAIService {
    @POST("completions")
    suspend fun completions(
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") apiKey: String,
        @Body request: CompletionRequest
    ): Response<CompletionResponse>
}
data class CompletionRequest(
    val model: String,
    val prompt: String,
    val temperature: Double,
    val max_tokens: Int,
    val top_p: Int,
    val frequency_penalty: Double,
    val presence_penalty: Double,
    val stop: List<String>
)
data class CompletionResponse(
    @SerializedName("id") val id: String,
    @SerializedName("choices") val choices: List<Choices>
)
data class Choices(
    @SerializedName("text") val text: String,
    @SerializedName("index") val index: Int,
    @SerializedName("logprobs") val logprobs: String,
    @SerializedName("finish_reason") val finishReason: String
)