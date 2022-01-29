package cat.smartcoding.mendez.freedating.ui.profiles

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

//@Parcelize
data class ProfileItem(
    var userId: String? = "",
    var image: Bitmap? = null,
    var name : String? = "",
    var gender : String? = "",
    var birthdate : String? = "",
    var email: String? = "",
    var location: String? = "",
    var otherThings : String? = "",
    var description : String?= "",
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeParcelable(image, flags)
        parcel.writeString(name)
        parcel.writeString(gender)
        parcel.writeString(birthdate)
        parcel.writeString(email)
        parcel.writeString(location)
        parcel.writeString(otherThings)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileItem> {
        override fun createFromParcel(parcel: Parcel): ProfileItem {
            return ProfileItem(parcel)
        }

        override fun newArray(size: Int): Array<ProfileItem?> {
            return arrayOfNulls(size)
        }
    }

}