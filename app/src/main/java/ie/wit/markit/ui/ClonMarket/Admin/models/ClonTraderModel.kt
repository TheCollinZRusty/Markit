package ie.wit.AdminFragment

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.util.*

@IgnoreExtraProperties
@Parcelize
data class ClonTraderModel(
    var uid: String? = "",
    var paymenttype: String = "N/A",
    var Title: String = "",
    var Description: String = "",
    var CurrentTime: String = "",
    var Number: String = "",
    var TraderEmail: String = "",
    var TraderStart: String = "",
    var Post_Title: String = "",
    var TraderEnd: String = "a message",
    var Name: String = "",
    var PostBody: String = "",
    var isfavourite: Boolean = false,
    var Upvotes: Int = 0,
    var profilepic: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "Title" to Title,
            "Name" to Name,
            "PostBody" to PostBody,
            "Description" to Description,
            "Number" to Number,
            "Post_Title" to Post_Title,
            "TraderEmail" to TraderEmail,
            "TraderStart" to TraderStart,
            "TraderEnd" to TraderEnd,
            "upvotes" to Upvotes,
            "profilepic" to profilepic,
            "latitude" to latitude,
            "longitude" to longitude,
            "email" to email
        )
    }
}



