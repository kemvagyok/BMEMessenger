package hu.bme.aut.android.bmemessenger.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.bmemessenger.LoginActivity
import hu.bme.aut.android.bmemessenger.UserMainActivity
import hu.bme.aut.android.bmemessenger.enum.KeysIntent
import hu.bme.aut.android.bmemessenger.enum.Types
import hu.bme.aut.android.bmemessenger.model.User
import kotlinx.coroutines.launch

class SettingProfessorViewModel: ViewModel(), ToastMake {

    var actualUser = mutableStateOf(User(null,null,null,null))

    var students = mutableStateListOf<User>()


    var canWrittenIconList = mutableStateListOf<Boolean>()

    fun getActualUserDataFromActivity(intent: Intent)
    {
        actualUser.value = intent.getSerializableExtra("KEY_ACTUAL_USER") as User
    }

    fun checkActualUser(context: Context)
    {
        if(Firebase.auth.currentUser == null)
        {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    fun toUserMainActivity(context: Context)
    {

        if(Firebase.auth.currentUser != null)
        {
            val intent = Intent(context, UserMainActivity::class.java)
                .putExtra(KeysIntent.actualUser,actualUser.component1())
            context.startActivity(intent)

        }
        else
            context.startActivity(Intent(context, LoginActivity::class.java))
    }

    fun listeningStudents()
    {
        var usersReference = Firebase.database.getReference("users")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                viewModelScope.launch {
                    val user = dataSnapshot?.getValue<User>()
                    if(user != null)
                    {
                        if(user.userId != actualUser.component1().userId && user.type == Types.student)
                            students.add(user)
                            canWrittenIconList.add(user.getIsCanWritten ?: true)

                    }
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                viewModelScope.launch {
                    val user = snapshot?.getValue<User>()
                    if(user != null)
                    {
                        students.forEachIndexed { index, userTemp ->
                            if(userTemp.userId == user.userId)
                            {
                                students[index] = user
                                return@forEachIndexed
                            }
                        }
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

    fun disableStudentWriting(user: User, context: Context)
    {
        students.forEachIndexed { index, userTemp ->
            if(userTemp.userId == user.userId)
            {
                canWrittenIconList[index] = !canWrittenIconList[index]
                var actualCanWritten = !(students[index].getIsCanWritten)!!

                students[index].getIsCanWritten = actualCanWritten
                updateFirebaseData(students[index])
                var textWritten : String
                if(actualCanWritten)
                    textWritten = "tud írni"
                else
                    textWritten = "nem tud írni"
                toastShow("${user.name} hallgató ${textWritten}.", context)
                return@forEachIndexed
            }
        }

    }

    private fun updateFirebaseData(user: User)
    {
            val userValues = user.toMap()
            val childUpdate = hashMapOf<String, Any>(
                "/users/${user.userId}" to userValues
            )
            Firebase.database.getReference().updateChildren(childUpdate)
    }

}