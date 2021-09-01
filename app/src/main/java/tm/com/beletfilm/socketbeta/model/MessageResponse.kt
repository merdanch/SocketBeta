package tm.com.beletfilm.socketbeta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class nameValuePairs(val nameValuePairs : Message):Parcelable

@Parcelize
data class Message (
    var phone : String,
    var message : String,
): Parcelable