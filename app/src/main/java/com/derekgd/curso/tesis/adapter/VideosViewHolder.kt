package com.derekgd.curso.tesis.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.derekgd.curso.R
import com.derekgd.curso.tesis.clases.Videos
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class VideosViewHolder(view: View) :
    RecyclerView.ViewHolder(view) { //esta clase recibe un objeto de tipo view extendiendo la clase  viewholder y asignandole esa view

    val title = view.findViewById<TextView>(R.id.tvTitle)    //creamos una variable que sera = a buscar el objeto por id en nuestro item declarar su tipo
    val duration = view.findViewById<TextView>(R.id.tvDuration)
    val player = ExoPlayer.Builder(view.context).build()
    val playerView = view.findViewById<PlayerView>(R.id.lsmVideoView)

    fun render(videosModel: Videos) {  //esta funcion colocara los valores de nuestras variables en el objeto de el item en este caso escribira el texto en el textView
        title.text = videosModel.title
        duration.text = videosModel.duration.toString()
        playerView.player = player
        try {
            val uri = Uri.parse(videosModel.video)
            val mediaItem = MediaItem.fromUri(uri)
            player.setMediaItem(mediaItem)
            player.prepare()
        } catch (e:Error) {
            Log.e("ViewHolder", "Exoplayer error ${e.toString()}" )
        }
    }
}