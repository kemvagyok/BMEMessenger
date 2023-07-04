package hu.bme.aut.android.bmemessenger.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import hu.bme.aut.android.bmemessenger.enum.Types
import java.io.Serializable

@IgnoreExtraProperties
data class User(
   // val profilePicture : ImageBitmap?,
    var userId: String? = "",
    val email: String? = "",
    var name : String? = "",
    var type : String?= Types.student,
    var getIsCanWritten : Boolean? = true
): Serializable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "email" to email,
            "name" to name,
            "type" to type,
            "getIsCanWritten" to getIsCanWritten
        )
    }
}