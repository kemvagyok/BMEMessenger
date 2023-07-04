package hu.bme.aut.android.bmemessenger.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message (
    val writer: String? = "",
    val receiver: String? = "",
    val text : String? = "",
    val date : String? = ""
    )
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "writer" to writer,
            "receiver" to receiver,
            "text" to text,
            "date" to date
        )
    }
}