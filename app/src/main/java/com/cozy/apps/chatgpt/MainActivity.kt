package com.cozy.apps.chatgpt

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editText: EditText
    private lateinit var sendButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        editText = findViewById(R.id.edit_text)
        sendButton = findViewById(R.id.send_button)
        val arraylistMessage = ArrayList<Message>()
        arraylistMessage.add(Message("Ai","Ask Anything you want",""))
        adapter = MessageAdapter(arraylistMessage)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
//        val openAi = OpenAi.Builder("apiKey").build()

        val API_KEY = "Bearer MY_API_KEY  " // Replace MY_API_KEY with your own key and keep the word Bearer
        val openAI = OpenAI(API_KEY)
        // You can change the prompt, this is what we find in the chat example from the OpenAI page
        var prompt = "The following is a conversation with an AI assistant. The assistant is helpful, creative, clever, and very friendly."


        sendButton.setOnClickListener {
            val message = editText.text.toString()
            val calendar: Calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTime: String = simpleDateFormat.format(calendar.getTime())
            if (message.isNotEmpty()) {
                adapter.addMessage(Message("me",message,currentTime))
                editText.setText("")
                // Use the message to send a request to GPT-3 and receive the response
                // Save the message and the response in the chat history
            }

            CoroutineScope(Dispatchers.IO).launch {
                prompt += "\n\nHuman: $message \nAI:"

                try {
                    val response = openAI.createCompletion(
                        model = "text-davinci-003",
                        prompt = prompt,
                        temperature = 0.9,
                        max_tokens = 150,
                        top_p = 1,
                        frequency_penalty = 0.0,
                        presence_penalty = 0.6,
                        stop = listOf(" me:", " AI:")
                    )

                    if (response.isSuccessful) {
                        var answer = response.body()?.choices?.first()?.text
                        answer = answer?.trimStart() // Delete the first space from the answer

                        runOnUiThread {
                            adapter.addMessage(Message("AI",answer,currentTime))
                        }
                    } else {
                        Log.d("RESPONSE", "Error: ${response.code()} ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.d("RESPONSE", "Error: $e")
                }
            }

        }
    }
}
