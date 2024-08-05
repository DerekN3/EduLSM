package com.derekgd.curso.tesis

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.derekgd.curso.databinding.ActivityVideoFullscreenBinding
import com.derekgd.curso.tesis.clases.VideoData

@Suppress("DEPRECATION")
class VideoFullscreenActivity : AppCompatActivity() { //Activity que reproduce el video en pantalla completa

    private lateinit var binding: ActivityVideoFullscreenBinding //Variable para acceder a los elementos del layout con binding
    private  lateinit var player: ExoPlayer //variable para inicializar el reproductor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE) //Oculta la barra de titulo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { //
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES //Oculta la barra de titulo solo si la version es mayor o igual a P
        }
        binding = ActivityVideoFullscreenBinding.inflate(layoutInflater) //infla el layout con binding
        setContentView(binding.root) //Asignar el layout al activity
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Mostrar la flecha de retroceso


        //Recuperar los datos del intent de homeFragment
        val videoData = intent.getParcelableExtra<VideoData>("VIDEO_DATA")
        //Inicializa el reproductor en la actividad de pantalla completa
        player = ExoPlayer.Builder(this).build() //Crea el reproductor
        binding.fullscreenVideo.player = player //Asignar el reproductor al reproductor del layout con binding
        //En caso que videoData no sea nulo
        if (videoData != null){

            //Configurar el reproductor con los datos recibidos del intent
            val mediaItem = MediaItem.fromUri(videoData.uri)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.seekTo(videoData.currentPosition)
            player.play()

        }else{
            //Codigo a ejecutar si no  se reciben datos de video
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() { //Cuando se presiona la flecha de retroceso para salir de la actividad
        val sharedPrefer = getSharedPreferences("MyPrefers", Context.MODE_PRIVATE)
        val editor = sharedPrefer.edit()
        editor.putLong("currentPosition", player.currentPosition)
        editor.apply()   //define sharedPreferences luego las edita el editor coloca el long del tiempo de reproduccion en el editor y lo aplica
        player.stop() //Detiene el reproductor
        player.release() //Libera el reproductor
        super.onBackPressed() //al finalizar el proceso comportamiento predeterminado
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }
}

