package hu.bme.aut.android.bmemessenger.viewmodel

import androidx.compose.runtime.MutableState
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hu.bme.aut.android.bmemessenger.model.Message
import hu.bme.aut.android.bmemessenger.model.User

interface FirebaseUpdateUser {
    fun updateUserFirebaseData(user: User)
    {
        val userValues = user.toMap()
        val childUpdate = hashMapOf<String, Any>(
            "/users/${user.userId}" to userValues
        )
        Firebase.database.getReference().updateChildren(childUpdate)
    }

}