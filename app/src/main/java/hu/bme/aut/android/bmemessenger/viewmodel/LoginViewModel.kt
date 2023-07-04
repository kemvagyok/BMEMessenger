package hu.bme.aut.android.bmemessenger.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import hu.bme.aut.android.bmemessenger.UserMainActivity
import hu.bme.aut.android.bmemessenger.enum.KeysIntent
import hu.bme.aut.android.bmemessenger.model.User
import hu.bme.aut.android.bmemessenger.R

class LoginViewModel: ViewModel(), ToastMake{

    var username by mutableStateOf("")
    var email by  mutableStateOf("")
    var password by  mutableStateOf("")


    fun signup(context: Context)
    {
        if(!checkTextFieldsAtSignUp()) {
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        successfulSignUp(context)
                    } else {
                        toastShow(context.getString(R.string.unsuccessfullSignUpText), context)
                    }
                }
        }
        else
        {
            toastShow(context.getString(R.string.emptyFieldsText), context)
        }
    }


    fun login(context: Context)
    {
        if(!checkTextFieldsAtLogin())
        {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        successfulLogin(context)
                    } else {
                        toastShow(context.getString(R.string.unsuccessfulLoginText), context)
                    }
                }
        }
        else{
            toastShow(context.getString(R.string.emptyFieldsText), context)
        }
    }


    private fun checkTextFieldsAtSignUp(): Boolean
    {
        return username == "" || email == "" || password == ""
    }

    private fun checkTextFieldsAtLogin(): Boolean
    {
        return email == "" || password == ""
    }

    private  fun successfulSignUp(context: Context)
    {
        val uid  = Firebase.auth.currentUser?.uid
        val actualUser = User(uid,email,username)

        setActualUser(actualUser)
        if(Firebase.auth.currentUser != null)
        {
            val intent =Intent(context,UserMainActivity::class.java).putExtra(KeysIntent.actualUser,actualUser)
            toastShow(context.getString(R.string.successfullSignUpText),context)
            context.startActivity(intent)
        }
        else
        {
            toastShow(context.getString(R.string.successfullSignUpNoDatabaseText), context)
        }
    }

    private fun successfulLogin(context: Context)
    {
        var actualUser : User?= null
        Firebase.database.getReference("users").child(Firebase.auth.currentUser?.uid.toString()).get().addOnCompleteListener {
            actualUser = it.result.getValue<User>()
            val intent =Intent(context,UserMainActivity::class.java).putExtra(KeysIntent.actualUser,actualUser)
            toastShow(context.getString(R.string.successfullLoginText),context)
            context.startActivity(intent)
        }.addOnFailureListener {
            Firebase.auth.signOut()
            toastShow(context.getString(R.string.successfullLoginNoDatabaseText), context)
        }
    }

    private fun setActualUser(actualUser: User) {

        Firebase.database.getReference("users").child(Firebase.auth.currentUser?.uid.toString()).setValue(actualUser).addOnFailureListener {
            Firebase.auth.signOut()
        }

    }
}