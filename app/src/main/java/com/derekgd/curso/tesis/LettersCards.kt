package com.derekgd.curso.tesis

import com.derekgd.curso.R

data class LettersCards(
    val image: Int,
    val title: Int,
    val description: Int,
    val answer: String
)

data class VideoGifsCards(
    val uri: String,
    val title: Int,
    val description: Int,
    val answer: String
)

sealed class CardData{
    abstract val title: Int
    abstract val description: Int
    abstract val answer: String

    data class Letters(val data: LettersCards): CardData() {
        override val title: Int get() = data.title
        override val description: Int get() = data.description
        override val answer: String get() = data.answer
    }

    data class VideoGif(val data: VideoGifsCards): CardData() {
        override val title: Int get() = data.title
        override val description: Int get() = data.description
        override val answer: String get() = data.answer
    }

}

data class LevelCard(
    val cards: List<CardData>,
    val level: Int
)

val lettersCardsList = listOf(
    CardData.Letters(
        LettersCards(
            R.drawable.a,
            R.string.title_a,
            R.string.description_a,
            "A"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.b,
            R.string.title_b,
            R.string.description_b,
            "B"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.c,
            R.string.title_c,
            R.string.description_c,
            "C"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.d,
            R.string.title_d,
            R.string.description_d,
            "D"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.e,
            R.string.title_e,
            R.string.description_e,
            "E"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.f,
            R.string.title_f,
            R.string.description_f,
            "F"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.g,
            R.string.title_g,
            R.string.description_g,
            "G"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.h,
            R.string.title_h,
            R.string.description_h,
            "H"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.i,
            R.string.title_i,
            R.string.description_i,
            "I"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.j,
            R.string.title_j,
            R.string.description_j,
            "J"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.k,
            R.string.title_k,
            R.string.description_k,
            "K"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.l,
            R.string.title_l,
            R.string.description_l,
            "L"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.m,
            R.string.title_m,
            R.string.description_m,
            "M"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n,
            R.string.title_n,
            R.string.description_n,
            "N"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.nn,
            R.string.title_ñ,
            R.string.description_ñ,
            "Ñ"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.o,
            R.string.title_o,
            R.string.description_o,
            "O"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.o,
            R.string.title_p,
            R.string.description_p,
            "P"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.q,
            R.string.title_q,
            R.string.description_q,
            "Q"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.r,
            R.string.title_r,
            R.string.description_r,
            "R"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.s,
            R.string.title_s,
            R.string.description_s,
            "S"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.t,
            R.string.title_t,
            R.string.description_t,
            "T"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.u,
            R.string.title_u,
            R.string.description_u,
            "U"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.v,
            R.string.title_v,
            R.string.description_v,
            "V"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.w,
            R.string.title_w,
            R.string.description_w,
            "W"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.x,
            R.string.title_x,
            R.string.description_x,
            "X"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.y,
            R.string.title_y,
            R.string.description_y,
            "Y"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.z,
            R.string.title_z,
            R.string.description_z,
            "Z"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n1,
            R.string.number_1,
            R.string.description_number_1,
            "1"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n2,
            R.string.number_2,
            R.string.description_number_2,
            "2"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n3,
            R.string.number_3,
            R.string.description_number_3,
            "3"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n4,
            R.string.number_4,
            R.string.description_number_4,
            "4"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n5,
            R.string.number_5,
            R.string.description_number_5,
            "5"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n6,
            R.string.number_6,
            R.string.description_number_6,
            "6"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n7,
            R.string.number_7,
            R.string.description_number_7,
            "7"
        )
    ),
    CardData.Letters(
        LettersCards(
            R.drawable.n8,
            R.string.number_8,
            R.string.description_number_8,
            "8"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "https://firebasestorage.googleapis.com/v0/b/lsmdatabase-21d9c.appspot.com/o/gifs%2Fn9.gif?alt=media&token=716c374a-cabb-47d1-ab72-ee6feb5c8978",
            R.string.number_9,
            R.string.description_number_9,
            "9"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.number_10,
            R.string.description_number_10,
            "10"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_white,
            R.string.description_color_white,
            "Blanco"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_black,
            R.string.description_color_black,
            "Negro"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_blue,
            R.string.description_color_blue,
            "Azul"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_yellow,
            R.string.description_color_yellow,
            "Amarillo"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_gray,
            R.string.description_color_gray,
            "Gris"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_purple,
            R.string.description_color_purple,
            "Morado"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_green,
            R.string.description_color_green,
            "Verde"
        )
    ),
    CardData.VideoGif(
        VideoGifsCards(
            "",
            R.string.color_pink,
            R.string.description_color_pink,
            "Rosa"
        )
    )
)


val levelsList = listOf(
    LevelCard(
        lettersCardsList.subList(0, 3),
        1
    ), LevelCard(
        lettersCardsList.subList(4, 7),
        2
    ), LevelCard(
        lettersCardsList.subList(8, 11),
        3
    ), LevelCard(
        lettersCardsList.subList(12, 15),
        4
    ), LevelCard(
        lettersCardsList.subList(16, 19),
        5
    ), LevelCard(
        lettersCardsList.subList(20, 23),
        6
    ), LevelCard(
        lettersCardsList.subList(24, 27),
        7
    ), LevelCard(
        lettersCardsList.subList(28, 31),
        8
    ), LevelCard(
        lettersCardsList.subList(32, 35),
        9
    ), LevelCard(
        lettersCardsList.subList(36, 39),
        10
    ), LevelCard(
        lettersCardsList.subList(40, 43),
        11
    ), LevelCard(
        lettersCardsList.subList(44, 45),
        12
    )
)

val SectionTitle = listOf(
    "Seccion 1 El abecedario",
    "Seccion 2 Los numeros",
    "Seccion 3 Los colores"
)

val SectionDescription = listOf(
    "Aqui aprenderas todo sobre el abecedario en LSM y como formar cada una de las letras",
    "Se mostraran los primeros 10 numeros y como formarlos",
    "Los colores con animaciones de manera rapida y sencilla"
)

val letters = listOf(
    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "Ñ", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
)

val numbers = listOf("1","2","3","4","5","6","7","8","9","10")

val colors = listOf(
    "Blanco", "Negro", "Azul", "Amarillo", "Gris", "Morado", "Verde", "Rosa"
)
