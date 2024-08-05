package com.derekgd.curso.tesis

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.derekgd.curso.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomBarActivity : AppCompatActivity() {
    private lateinit var bottomBarView: BottomNavigationView
    private val fragmentStack = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bottom_bar)

        bottomBarView = findViewById(R.id.bottomNavigationView)
        replaceFragment(HomeFragment())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomBarView.setOnItemSelectedListener {
            when(it.itemId){

                R.id.actionHome -> replaceFragment(HomeFragment())
                R.id.actionMultimedia -> replaceFragment(Exercise2Fragment())
                R.id.actionAccount -> replaceFragment(AccountFragment())
                R.id.actionSettings -> replaceFragment(SettingsFragment())
                else -> {}
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        fragmentStack.add(fragment::class.java.simpleName)
        Log.w("fragmentsLayaout","$fragmentStack")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(fragmentStack.isNotEmpty()){
            if (fragmentStack.first() != "HomeFragment") {
                fragmentStack.add(0,"HomeFragment")
                fragmentStack.add(1,"HomeFragment")
            }
            if(fragmentStack.first() == "HomeFragment" && fragmentStack.size == 1){
                fragmentStack.removeLast()
            }else{
                updateBottomNavBarSelection()
                fragmentStack.removeLast()
                Log.w("fragmentsLayaoutfinal", "$fragmentStack")
            }
        }
        if (fragmentStack.isEmpty()){
            //supportFragmentManager.popBackStack()
        }else{
            super.onBackPressed() // Si no es ninguno, comportamiento predeterminado
        }


    }

    private fun updateBottomNavBarSelection() {
        //Al iniciar la aplicacionl la lista estara vacia en este caso se agregara el HomeFragment por si
        //el usuario no ha seleccionado ninguna opcion
        fragmentStack.removeLast()
            when (fragmentStack.last()) {
                "HomeFragment" -> {
                    bottomBarView.selectedItemId = R.id.actionHome
                    replaceFragment(HomeFragment())
                }

                "VideosFragment" -> {
                    bottomBarView.selectedItemId = R.id.actionMultimedia
                    replaceFragment(AccountFragment())
                }

                "AccountFragment" -> {
                    bottomBarView.selectedItemId = R.id.actionAccount
                    replaceFragment(AccountFragment())
                }

                "SettingsFragment" -> {
                    bottomBarView.selectedItemId = R.id.actionSettings
                    replaceFragment(SettingsFragment())
                }
            }
            fragmentStack.removeLast()
            fragmentStack.removeLast()
            Log.w("fragmentsLayaoutFuncion", "$fragmentStack")
    }
}