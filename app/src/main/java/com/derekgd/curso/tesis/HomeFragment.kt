package com.derekgd.curso.tesis

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.derekgd.curso.R
import com.derekgd.curso.tesis.clases.VideoData

class HomeFragment : Fragment() { //Definicion de la clase en este caso un fragmento

    private lateinit var player: ExoPlayer //Declaracion de la variable player de tipo ExoPlayer con lateinit para inicializarla mas tarde
    private lateinit var sharedPrefer: SharedPreferences //Declaracion de la variable sharedPrefer de tipo SharedPreferences para guardar los datos del reproductor, lateinit para usarla en los override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { //fun principal
        super.onViewCreated(view, savedInstanceState)
        player = ExoPlayer.Builder(requireContext()).build() //Inicializa el reproductor en el fragmento
        val playerView = view.findViewById<PlayerView>(R.id.homeVideoView) //Obtiene la vista del reproductor del fragmento

        val tvHomeTitle = requireView().findViewById<TextView>(R.id.tvHomeTitle)
        val tvHomeContent1 = requireView().findViewById<TextView>(R.id.tvHomeContent1)
        val learning = requireView().findViewById<TextView>(R.id.learning)       //obtener los elementos del layout
        val contentCurse = requireView().findViewById<TextView>(R.id.contentCurse)
        val videoText = requireView().findViewById<TextView>(R.id.videoText)

        sharedPrefer = requireActivity().getSharedPreferences("MyPrefers", Context.MODE_PRIVATE) //declaracion de la variable sharedPrefer para guardar los datos del reproductor
        val SizeTitleGrande = sharedPrefer.getFloat("SizeTitleGrande", resources.getDimension(R.dimen.title_size_large))
        val SizeSubtitle = sharedPrefer.getFloat("SizeSubtitle", resources.getDimension(R.dimen.subtitle_size))  //Obtener el tamaño guardado de la fuente de los elementos del layout desde SharedPreferences
        val SizeTextBase = sharedPrefer.getFloat("SizeTextBase", resources.getDimension(R.dimen.text_size_medium))
        tvHomeTitle.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            SizeTitleGrande,
            resources.displayMetrics
        )
        tvHomeContent1.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            SizeTextBase,                                   //Aplicar el tamaño  de letra guardado a los elementos del layout
            resources.displayMetrics
        )
        learning.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            SizeSubtitle,
            resources.displayMetrics
        )
        contentCurse.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            SizeTextBase,
            resources.displayMetrics
        )
        videoText.textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            SizeTextBase,
            resources.displayMetrics
        )


        playerView.player = player  //Asignar el reproductor al reproductor del fragmento
        val uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/lsmdatabase-21d9c.appspot.com/o/videos%2Falfabeto1.mp4?alt=media&token=eb95890b-b845-430d-b699-81ac470904f5") //Uri del video
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem) //Asignar el mediaItem al reproductor
        player.prepare() //Preparar el reproductor para reproducir el video

        playerView.setOnClickListener {
            // Obtiene los datos actuales del reproductor en este caso tiempo de reproduccion y Uri del video
            val currentPosition = player.currentPosition
            val currentUri = player.currentMediaItem?.localConfiguration?.uri ?: Uri.EMPTY
            //Crea un objeto VideoData  que almacena el Uri y el tiempo de reproduccion del video (el objeto es serializable y se declaro en otra clase)
            val videoData = VideoData(currentUri, currentPosition)
            //Crea el intent y agrega los datos del video por el con extras
            val intent = Intent(context, VideoFullscreenActivity::class.java)
            intent.putExtra("VIDEO_DATA", videoData)

            //Inicia la actividad VideoFullscreenActivity y pausa el video en la ventana actual
            startActivity(intent)
            player.pause()

        }
    }

    override fun onResume() {
        super.onResume() //sobreescribe la funcion onresume para obtener el tiempo de reproduccion del video de SharedPreferences
        Log.e("currentPosition", "${sharedPrefer.getLong("currentPosition", 15000)}")
        player.seekTo(sharedPrefer.getLong("currentPosition", 0)) //Coloca el video en la posicion del tiempo de reproduccion
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop called")
        player.stop() //Detiene el reproductor al salir de la aplicacion
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called")
        player.stop() //Detiene y Libera el reproductor al salir de la aplicacion
        player.release()
    }

}