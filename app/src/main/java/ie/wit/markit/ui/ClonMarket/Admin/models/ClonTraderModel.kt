package ie.wit.AdminFragment

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ClonTraderModel(
    var uid: String? = "",
    var paymenttype: String = "N/A",
    var Title: String = "",
    var Description: String = "",
    var Number: String = "",
    var TraderEmail: String = "",
    var TraderStart: String = "",
    var TraderEnd: String = "a message",
    var Name: String = "",
    var PostBody: String = "",
    var Upvotes: Int = 0,
    var profilepic: String = "",
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
            "TraderEmail" to TraderEmail,
            "TraderStart" to TraderStart,
            "TraderEnd" to TraderEnd,
//            "message" to message,
            "upvotes" to Upvotes,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}



