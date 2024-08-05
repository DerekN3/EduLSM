package com.derekgd.curso.tesis.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.derekgd.curso.R
import com.derekgd.curso.tesis.clases.Videos

class VideosAdapter(private val videosList: List<Videos>) : RecyclerView.Adapter<VideosViewHolder>() { //Aqui nuestra clase videosAdapter extiende a la clase recyclerView.adapter y su viewholder sera videosViewHolder, extender una clase significa usar el codigo de este como base y agregar funcionalidades sobreescribiendo sin afectar a la clase originial

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {  //Aqui le pasamos el layaout(item) que va a recibir el viewholder para que pueda pintarlo por cada objeto
        val layoutInflater = LayoutInflater.from(parent.context) //El metodo context permite acceder a varios recursos de las actividades en este caso para acceder al contexto sin una vista buscamos un elemento que lo sea en este caso es el padre que es un viewgroup
        return VideosViewHolder(layoutInflater.inflate(R.layout.item_videos,parent,false))  //Aqui le estamos pasando el layaout
    }

    override fun getItemCount(): Int = videosList.size    //El numero de elementos de la lista es poco recomendable colocar valores enteros mejor
                                                        // medir el tamaño de nuestra lista


    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) { //Este metodo pasa por cada uno de los items y llama a la funcion videos render pasandole ese item
        val item = videosList[position]    //Esta clase ya nos devulve la instancia del viewholder y la posicion en la que estamos
        holder.render(item) //item es la posicion en la lista de objetos aqui se llama a el holder osea nuestra clase videosViewHolder y le pasamos el item
    }


}