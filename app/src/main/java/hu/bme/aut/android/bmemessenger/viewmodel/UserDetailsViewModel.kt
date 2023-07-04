package hu.bme.aut.android.bmemessenger.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.bmemessenger.LoginActivity
import hu.bme.aut.android.bmemessenger.UserMainActivity
import hu.bme.aut.android.bmemessenger.enum.KeysIntent
import hu.bme.aut.android.bmemessenger.enum.Types
import hu.bme.aut.android.bmemessenger.model.User

class UserDetailsViewModel: ViewModel(), ToastMake, FirebaseUpdateUser {
    var actualUser = mutableStateOf(User(null,null,null, null))
    val typeOptions = listOf(Types.student,Types.professor)
    val selectedOption = mutableStateOf(actualUser.component1().type)
    var username by mutableStateOf(actualUser.component1().name ?: "")

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
            context.startActivity(Intent(context,LoginActivity::class.java))
    }

    fun setUserName(context: Context)
    {
        if(username != "")
        {
            actualUser.component1().name = username
            updateUserFirebaseData(actualUser.component1())
            toastShow("Ön ${username} lett",context);
        }
    }

    fun setType(context: Context)
    {
        actualUser.component1().type = selectedOption.component1()
        updateUserFirebaseData(actualUser.component1())
        toastShow("Ön ${selectedOption.component1().toString()} lett",context);

    }
}