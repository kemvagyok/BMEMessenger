package hu.bme.aut.android.bmemessenger.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.bmemessenger.ConversationActivity
import hu.bme.aut.android.bmemessenger.LoginActivity
import hu.bme.aut.android.bmemessenger.SettingProfessorActivity
import hu.bme.aut.android.bmemessenger.UserDetailsActivity
import hu.bme.aut.android.bmemessenger.enum.KeysIntent
import hu.bme.aut.android.bmemessenger.model.Message
import hu.bme.aut.android.bmemessenger.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserMainViewModel: ViewModel() {
        var actualUser = mutableStateOf(User(null,null,null, null))
        var profiles = mutableStateListOf<User>()

        fun getActualUserDataFromActivity(intent: Intent)
        {
                actualUser.value = intent.getSerializableExtra("KEY_ACTUAL_USER") as User
        }
        fun signOut(context: Context)
        {
                Firebase.auth.signOut()
                context.startActivity(Intent(context,LoginActivity::class.java))
        }

        fun toConversationActivity(otherUser: User, context: Context)
        {
                if(Firebase.auth.currentUser != null)
                {
                    val intent = Intent(context,ConversationActivity::class.java)
                        .putExtra(KeysIntent.actualUser,actualUser.component1())
                        .putExtra(KeysIntent.otherUser,otherUser)
                    context.startActivity(intent)

                }
                else
                        context.startActivity(Intent(context,LoginActivity::class.java))
        }

        fun toSettingProfessorActivity(context: Context)
        {
                if(Firebase.auth.currentUser != null)
                {
                    val intent = Intent(context,SettingProfessorActivity::class.java)
                        .putExtra(KeysIntent.actualUser,actualUser.component1())
                    context.startActivity(intent)
                }
                else
                        context.startActivity(Intent(context,LoginActivity::class.java))
        }

        fun toUserDetailsActivity(context: Context)
        {
                if(Firebase.auth.currentUser != null)
                {
                    val intent = Intent(context,UserDetailsActivity::class.java)
                        .putExtra(KeysIntent.actualUser,actualUser.component1())
                    context.startActivity(intent)
                }
                else
                        context.startActivity(Intent(context,LoginActivity::class.java))
        }


        fun checkActualUser(context: Context)
        {
                if(Firebase.auth.currentUser == null)
                {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                }
        }

    fun listeningUsers()
    {
        var usersReference = Firebase.database.getReference("users")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                viewModelScope.launch {
                    val user = dataSnapshot?.getValue<User>()
                    if(user != null)
                    {
                            if(user.userId != actualUser.component1().userId)
                                profiles.add(user)

                    }
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                viewModelScope.launch {
                    val user = snapshot?.getValue<User>()
                    if(user != null)
                    {
                        var actualIndex = 0
                        profiles.forEachIndexed { index, userTemp ->
                           if(userTemp.userId == user.userId)
                           {
                               actualIndex = index
                           }
                       }
                        profiles[actualIndex] = user
                    }
                }
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
        usersReference.addChildEventListener(childEventListener)
    }


     }

