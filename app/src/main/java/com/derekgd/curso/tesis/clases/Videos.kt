package com.derekgd.curso.tesis.clases

data class Videos(
    val title: String,
    val video: String,
    val duration: Int
)

//Se usaran las data clases un tipo de clase a la que se le añade el atributo data que
// hace que sea necesario definir almenos  un parametro. Nos permite crear un modelo de datos ya que esta clase
// es lo suficientemente inteligente para devolver los set, get y este tipo de funciones basicas de java


