package com.derekgd.curso.tesis

import com.derekgd.curso.tesis.clases.Videos

class videosProvider {

    //Esta clase nos dara los videos a usar que estan como una lista de objetos que creamos previamente

    companion object { //El elemento "companion object" permite acceder a un elemento de una clase sin crear antes una instancia
                       //de esa clase en este caso para acceder a esta clase simplemente usariamos videosProvider.theVideosList
        val theVideosLists = listOf<Videos>(
            Videos(
                "25 Letras en Lenguaje de señas",
                "https://firebasestorage.googleapis.com/v0/b/lsmdatabase-21d9c.appspot.com/o/videos%2Falfabeto1.mp4?alt=media&token=eb95890b-b845-430d-b699-81ac470904f5",
                1
            ),

            Videos(
                "Basicos de Lenguaje de señas",
                "https://firebasestorage.googleapis.com/v0/b/lsmdatabase-21d9c.appspot.com/o/videos%2Falfabeto2.mp4?alt=media&token=804d4ee5-8f71-4fed-b9fc-2b9bcb43c230",
                1
            )
        )
    }
}