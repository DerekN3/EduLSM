package com.derekgd.curso.tesis.clases

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.tasks.Task

data class VideoData(

    val uri: Uri,
    val currentPosition:Long

): Parcelable {

    //Constructor para crear  un videoData apartir de un parcel
    constructor(parcel: Parcel) : this(
        uri = parcel.readParcelable(Uri::class.java.classLoader)!!, //Uri implementando parcelable
        currentPosition = parcel.readLong()
    ) {
    }
    //Escribir los datos en el parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(uri, flags)
        parcel.writeLong(currentPosition)
    }
    //Describe los contenidos dentro del parcel
    override fun describeContents(): Int {
        return 0
    }
    //Crea un companion object para manejar la creacion e interpretacion del parcel
    companion object CREATOR : Parcelable.Creator<VideoData> {
        override fun createFromParcel(parcel: Parcel): VideoData {
            return VideoData(parcel)
        }

        override fun newArray(size: Int): Array<VideoData?> {
            return arrayOfNulls(size)
        }
    }

}

