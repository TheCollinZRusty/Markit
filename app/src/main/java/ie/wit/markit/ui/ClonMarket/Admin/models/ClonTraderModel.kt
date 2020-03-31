package ie.wit.markit.ui.ClonMarket.Admin.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ClonTraderModel(
    var uid: String? = "",
    var paymenttype: String = "N/A",
    var Name: String = "",
    var Description: String = "",
    var Number: String = "",
    var message: String = "a message",
    var upvotes: Int = 0,
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "Name" to Name,
            "Description" to Description,
            "Number" to Number,
            "message" to message,
            "upvotes" to upvotes,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}