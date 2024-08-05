package com.derekgd.curso.tesis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.derekgd.curso.R
import com.derekgd.curso.tesis.adapter.VideosAdapter
import com.derekgd.curso.tesis.adapter.VideosViewHolder


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var seguro: Boolean = false
    private var puntos: Float = 0f
    private var result: Float = 0f
    private var intentos:Int = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /*Un recycler view se configura en varios pasos dos clase principales
            *Adapter: la clase que se encarga de tomar y meter la informacion en el recyclerview
            *viewHolder : La clase que se encarga de pintar nuestras celdas de cada uno de los items
            * Layaout: El item que vamos a pintar su forma en xml
        */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Nadamas se cargue la activity llamara a este metodo
        //initRecyclerView()

        val btncontinues = requireView().findViewById<AppCompatButton>(R.id.continues)
        val btnsubmit = requireView().findViewById<AppCompatButton>(R.id.submit)

        val aImage = requireView().findViewById<ImageView>(R.id.aImage)
        val bImage = requireView().findViewById<ImageView>(R.id.bImage)
        val CImage = requireView().findViewById<ImageView>(R.id.CImage)


        val ex1 = requireView().findViewById<ImageView>(R.id.ex1)
        val ex2 = requireView().findViewById<ImageView>(R.id.ex2)
        val ex3 = requireView().findViewById<ImageView>(R.id.ex3)
        val ex4 = requireView().findViewById<ImageView>(R.id.ex4)
        val ex5 = requireView().findViewById<ImageView>(R.id.ex5)

        val rg1 = requireView().findViewById<RadioGroup>(R.id.rg1)
        val rg2 = requireView().findViewById<RadioGroup>(R.id.rg2)
        val rg3 = requireView().findViewById<RadioGroup>(R.id.rg3)
        val rg4 = requireView().findViewById<RadioGroup>(R.id.rg4)
        val rg5 = requireView().findViewById<RadioGroup>(R.id.rg5)
        val radio11 = requireView().findViewById<RadioButton>(R.id.radio11)
        val radio21 = requireView().findViewById<RadioButton>(R.id.radio21)
        val radio33 = requireView().findViewById<RadioButton>(R.id.radio33)
        val radio42 = requireView().findViewById<RadioButton>(R.id.radio42)
        val radio51 = requireView().findViewById<RadioButton>(R.id.radio51)

        val level1Title = requireView().findViewById<TextView>(R.id.level1Title)
        val level1SubTitle = requireView().findViewById<TextView>(R.id.level1SubTitle)
        val sharedPrefer = requireActivity().getSharedPreferences("MyPrefers", Context.MODE_PRIVATE)
        val  SizeTitleGrande= sharedPrefer.getFloat("SizeTitleGrande", resources.getDimension(R.dimen.title_size_large))
        val  SizeSubtitle= sharedPrefer.getFloat("SizeSubtitle", resources.getDimension(R.dimen.subtitle_size))
        level1Title.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTitleGrande, resources.displayMetrics)
        level1SubTitle.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeSubtitle, resources.displayMetrics)

        btncontinues.setOnClickListener() {
            aImage.visibility = View.INVISIBLE
            bImage.visibility = View.INVISIBLE
            CImage.visibility = View.INVISIBLE

            ex1.visibility = View.VISIBLE
            ex2.visibility = View.VISIBLE
            ex3.visibility = View.VISIBLE
            ex4.visibility = View.VISIBLE
            ex5.visibility = View.VISIBLE

            seguro = true
        }

        fun evaluateResponses(){
            puntos = 0f

            if(radio11.isChecked) puntos++
            if(radio21.isChecked) puntos++
            if(radio33.isChecked) puntos++
            if(radio42.isChecked) puntos++
            if(radio51.isChecked) puntos++

            result = (puntos / 5) * 100

            if(result < 60){
                if (intentos <= 0) {
                    val intent = Intent(requireView().context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    Toast.makeText(context, "Fue un buen examen estudia mas y lo conseguiras", Toast.LENGTH_SHORT).show()
                }else{
                    intentos--
                    Toast.makeText(context, "Lo siento pero no paso el examen intentelo mas tarde le quedan $intentos", Toast.LENGTH_SHORT).show()
                    rg1.clearCheck()
                    rg2.clearCheck()
                    rg3.clearCheck()
                    rg4.clearCheck()
                    rg5.clearCheck()
                }
            }else{
                Toast.makeText(context, "Felicidades esta listo para avanzar a la seccion 2 ", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,Exercise2Fragment())
                    .commit()
            }
        }

        btnsubmit.setOnClickListener() {
            if (!seguro) {
                Toast.makeText(context, "Lo siento no puede mandar las respuestas antes de iniciar el examen", Toast.LENGTH_SHORT).show()

            } else {
                evaluateResponses()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    private fun initRecyclerView() {
//        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerVideos)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = VideosAdapter(videosProvider.theVideosList)
//
//    }

}