package com.derekgd.curso.tesis

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.derekgd.curso.R
import com.google.android.material.slider.RangeSlider
import com.google.android.material.switchmaterial.SwitchMaterial

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rangeSliderTextSize = requireView().findViewById<RangeSlider>(R.id.rangeSliderTextSize)
        rangeSliderTextSize.setValues(25f)
        val tvFragmentTheme = requireView().findViewById<TextView>(R.id.tvFragmentTheme)
        val tvThemeTheme = requireView().findViewById<TextView>(R.id.tvThemeTheme)
        val tvTheme = requireView().findViewById<TextView>(R.id.tvTheme)
        val tvSize = requireView().findViewById<TextView>(R.id.tvSize)

        val sharedPrefer = requireActivity().getSharedPreferences("MyPrefers", Context.MODE_PRIVATE)
        val  RangeSliderValue= sharedPrefer.getFloat("RangeSliderValue", 25f)
        val  SizeTitleBase= sharedPrefer.getFloat("SizeTitleBase", resources.getDimension(R.dimen.title_size_medium))
        val  SizeTextGrande= sharedPrefer.getFloat("SizeTextGrande", resources.getDimension(R.dimen.text_size_large))
        rangeSliderTextSize.setValues(RangeSliderValue)
        tvFragmentTheme.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTitleBase, resources.displayMetrics)
        tvThemeTheme.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTextGrande, resources.displayMetrics)
        tvTheme.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTextGrande, resources.displayMetrics)
        tvSize.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTitleBase, resources.displayMetrics)

        val modoNocturno = AppCompatDelegate.getDefaultNightMode()

        val resources = resources
        val sizeTituloGrande = resources.getDimension(R.dimen.title_size_large)
        val sizeTituloBase = resources.getDimension(R.dimen.title_size_medium)
        val sizeSubtituloBase = resources.getDimension(R.dimen.subtitle_size)
        val sizeTextoGrande = resources.getDimension(R.dimen.text_size_large)
        val sizeTextoBase = resources.getDimension(R.dimen.text_size_medium)


        when (modoNocturno) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                tvTheme.text = "Oscuro"
            }
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                tvTheme.text = "Predeterminado"
            }
            else -> {
                tvTheme.text = "Claro"
            }
        }

        tvTheme.setOnClickListener {
            val temas = arrayOf("Claro", "Oscuro", "Predeterminado")

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Elige un tema").setItems(temas) {dialog,which ->

                when(which){
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)



                }
                super.onCreate(savedInstanceState)
                tvTheme.text = temas[which]
                sharedPrefer.edit().putString("appTheme",temas[which]).apply()
            }
            builder.show()
        }


        rangeSliderTextSize.addOnChangeListener { _, value, _ ->

            val factorEscala = value / 60

            val newSizeTitleGrande = sizeTituloGrande * factorEscala
            val newSizeTitleBase = sizeTituloBase * factorEscala
            val newSizeSubtitle = sizeSubtituloBase * factorEscala
            val newSizeTextGrande = sizeTextoGrande * factorEscala
            val newSizeTextBase = sizeTextoBase * factorEscala



            with(sharedPrefer.edit()) {
                putFloat("SizeTitleGrande", newSizeTitleGrande)
                putFloat("SizeTitleBase", newSizeTitleBase)
                putFloat("SizeSubtitle", newSizeSubtitle)
                putFloat("SizeTextGrande", newSizeTextGrande)
                putFloat("SizeTextBase", newSizeTextBase)
                putFloat("RangeSliderValue",value)
                apply()
            }


            tvFragmentTheme.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,newSizeTitleBase,resources.displayMetrics)
            tvThemeTheme.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,newSizeTextGrande,resources.displayMetrics)
            tvTheme.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,newSizeTextGrande,resources.displayMetrics)
            tvSize.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,newSizeTitleBase,resources.displayMetrics)

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}