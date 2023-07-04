package hu.bme.aut.android.bmemessenger.viewmodel

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.bmemessenger.LoginActivity
import hu.bme.aut.android.bmemessenger.enum.KeysIntent
import hu.bme.aut.android.bmemessenger.model.Message
import hu.bme.aut.android.bmemessenger.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ConversationViewModel: ViewModel() {
    var actualUser = mutableStateOf(User(null,null,null, null))
    var otherUser = mutableStateOf(User(null,null,null, null))
    var messages = mutableStateListOf<Message>()
    var yourMessage by mutableStateOf("")

    fun getActualUserDataFromActivity(intent: Intent)
    {
        actualUser.value = intent.getSerializableExtra(KeysIntent.actualUser) as User
        otherUser.value = intent.getSerializableExtra(KeysIntent.otherUser) as User
    }

    fun checkActualUser(context: Context)
    {
        if(Firebase.auth.currentUser == null)
        {
            var intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun listeningMessage()
    {
        var messagesReference = Firebase.database.getReference("messages")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                viewModelScope.launch {
                    val message = dataSnapshot?.getValue<Message>()
                    if(message != null)
                    {
                        if(checkOwnMessage(message))
                        {
                            if(message.writer != actualUser.component1().userId)
                                messages.add(message)
                        }
                    }
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        messagesReference.addChildEventListener(childEventListener)
    }

    fun sendMessage()
    {
        viewModelScope.launch {
            messages.add(Message(actualUser.component1().userId,otherUser.component1().userId,yourMessage.toString()))
            var actualMessage = Message(actualUser.component1().userId,otherUser.component1().userId,yourMessage,
            Date().toString()
        )
        Firebase.database.getReference("messages").child(actualMessage.date?:"NOMESSAGE").setValue(actualMessage)
        yourMessage = ""
        }
    }

    private fun checkOwnMessage(message: Message): Boolean
    {
        if(message.writer == actualUser.component1().userId && message.receiver == otherUser.component1().userId)
            return true
        if(message.writer == otherUser.component1().userId && message.receiver == actualUser.component1().userId)
            return true
        return false
    }
}

