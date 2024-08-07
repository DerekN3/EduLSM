package com.derekgd.curso.tesis

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.res.stringResource // Importa stringResource permite usar las direcciones de los strings  en values/strings
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext


class Exercise2Fragment : Fragment() {
    private lateinit var db: FirebaseDatabase
    private lateinit var users: DatabaseReference
    private lateinit var levelRef: DatabaseReference
    private var level =
        0 //Aqui la defino como uno pero en realizar para ahorrar lineas el nivel inicial sera 0 y asi susesivamente
    private var puntos = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        db = Firebase.database
        val uID = Firebase.auth.currentUser?.uid
        users = db.reference.child("users").child(uID ?: "")
        levelRef = users.child("Nivel")
        var transactionCompleted by mutableStateOf(false)  // State to track transaction

        levelRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                if (mutableData.value == null) {
                    mutableData.value = 0
//                    Log.e("level_mutable", mutableData.value.toString())
                }
//                Log.e("nivel_mutablefuera", mutableData.value.toString())
                level = mutableData.value.toString().toInt()
//                Log.e("level_normal", level.toString())
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    // Manejo de errores
                    Log.e("Firebase", "Error en la transacción: ${error.message}")
                } else if (committed) {
                    // La transacción fue exitosa.
                    Log.d("Firebase", "Transacción completada con éxito.")
                    transactionCompleted = true
                }
            }
        })
        //Log.e("level_fin_clase", level.toString())
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface {
                        if (transactionCompleted) {
                        LetterLearningScreen()
//                            TestSlidingPuzzle()
                        } else {
                            LoadingScreen()
                        }
                    }
                }
            }
        }
    }



    @Composable
    fun LetterIntroduction(cardData: CardData) {
        Card(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row {
                when (cardData) {
                    is CardData.Letters -> {
                        CubeImage(cardData.data.image)
                        cardData.data.title to cardData.data.description
                    }
                    is CardData.VideoGif -> {
                        CubeGif(cardData.data.uri)
                        cardData.data.title to cardData.data.description
                    }
                }.let { (title, description) ->
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = title),
                            modifier = Modifier.padding(bottom = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = stringResource(id = description))
                    }
                }
            }
        }
    }
    
    @Composable
    fun LetterQuiz(cardData: CardData, key: Int, radioOptions: List<String>, onOptionSelected: (String) -> Unit) {
        var selectedIndex by remember(key) { mutableStateOf<Int?>(null) }
        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(), shape = RoundedCornerShape(16.dp)
        ) {
            Row {
                when (cardData) {
                    is CardData.Letters -> CubeImage(cardData.data.image)
                    is CardData.VideoGif -> CubeGif(cardData.data.uri)
                }
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    radioOptions.forEachIndexed { index, text ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (index == selectedIndex),
                                onClick = {
                                    onOptionSelected(text)
                                    selectedIndex = index
                                },
                            )
                            Text(
                                text = text,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LetterLearningScreen() {
        var levelState by remember { mutableIntStateOf(level) }
        val lettersCards = remember {
            mutableStateListOf(*levelsList[levelState].cards.toTypedArray())
        }
        val shuffledCards by remember(lettersCards) {
            mutableStateOf(lettersCards.shuffled())
        }
        val titlesNumber = if (levelState < 9) 0 else if (levelState <12) 1 else 2
        val elements = listOf(letters, numbers, colors)
        val section = SectionTitle[titlesNumber]
        val sectionDescription = SectionDescription[titlesNumber]
        val selectedOptions = mutableListOf("", "", "")
        val answers: MutableList<String> = mutableListOf()
        var yesNoAnswer: String = ""
        val intentos = remember { mutableIntStateOf(3) }
        var showImages by remember { mutableStateOf(true) }
        var seguro by remember { mutableStateOf(true) }
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        var resetOptions by remember { mutableIntStateOf(0)}

        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Text(
                text = section,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = sectionDescription,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp)
            ) {
                Text("Nivel", modifier = Modifier.padding(8.dp), fontSize = 16.sp)
                val value = levelState.toFloat() / levelsList.size
                LinearProgressIndicator(
                    progress = { value },
                    modifier = Modifier.weight(1f)
                )
                Text("/${levelsList.size}", modifier = Modifier.padding(8.dp), fontSize = 16.sp)
            }
            if (showImages) {
                lettersCards.forEach { cardData ->
                    LetterIntroduction(cardData)
                }
            }
            Button(
                onClick = {
                    if (seguro) {
                        showImages = !showImages
                        seguro = false
                        resetOptions++
//                        Log.e("seguro", "$seguro")
                        coroutineScope.launch {
                            scrollState.scrollTo(0)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Hacer el ejercicio")
            }

            val shuffleLettersList by remember {
                derivedStateOf {
                    shuffledCards.map { cardData ->
                        val shuffleLetters =
                            elements[titlesNumber].shuffled().take(2).toMutableList()
                        shuffleLetters.add(cardData.answer)
                        shuffleLetters.shuffled()
                    }
                }
            }

            shuffledCards.forEachIndexed { index, lettersCards ->
                answers.add(lettersCards.answer)
                LetterQuiz(
                    cardData = lettersCards,
                    key = resetOptions,
                    radioOptions = shuffleLettersList[index],
                    onOptionSelected = { selectedAnswer ->
                        selectedOptions[index] =
                            selectedAnswer // Guardar la selección del usuario
                    })
            }


            val letRandom: CardData by remember { derivedStateOf {lettersCards.random()} }
            SlidingPuzzle(letRandom.title,letRandom)


            val currentCard: CardData by remember { derivedStateOf { shuffledCards.first() } }
            val title = remember(currentCard) { currentCard.title }
            val description = remember(currentCard) { currentCard.description }
            val randomDescription = remember(lettersCards) { lettersCards.random().description }
            val randomDecision by remember {
                derivedStateOf {
                    listOf(
                        description,
                        randomDescription
                    ).random()
                }
            }
            QuestionYesNo(
                stringResource(id = title),
                stringResource(id = randomDecision),
                key = resetOptions,
                onOptionSelected = { selectedAnswer ->
                    yesNoAnswer = selectedAnswer
                }
            )
            //shuffleLetterCards = lettersCards.shuffled()
            //shuffleLetters = lettersCards.shuffled()


            Button(
                onClick = {
                    Log.e("seguro", "$seguro")
                    if (!seguro) {
                        //Log.e("seguro_seleccionadas", "$selectedOptions")
                        //Log.e("seguro_respuestas", "$answers")


                        if (intentos.intValue > 0) {
                            puntos += answers.zip(selectedOptions) { answer, selectedOption ->
                                if (answer == selectedOption) {
                                    1
                                } else {
                                    0
                                }
                            }.sum()
                            if (description == randomDecision && yesNoAnswer == "Si") {
                                puntos++
                            } else if (description != randomDecision && yesNoAnswer == "No") {
                                puntos++
                            }
                            Log.e("seguro_puntos", "$puntos")

                            if (puntos >= 3) {

                                if (level < 10) {
                                    puntos = 0
                                    level++
                                    levelState = level
                                    levelRef.setValue(level)
                                    showImages = true
                                    seguro = true
                                    lettersCards.clear()
                                    levelsList[levelState].cards.forEach {
                                        lettersCards.add(it)
                                    }
//                                    letRandom = lettersCards.random()
                                    coroutineScope.launch {
                                        scrollState.scrollTo(0)
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Felicidades a completado todos los niveles disponibles!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Lo siento pero no paso el examen intentelo mas tarde le quedan ${intentos.intValue - 1} intentos",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intentos.intValue--
                            }

                        } else {
                            val intent = Intent(requireView().context, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                            Toast.makeText(
                                context,
                                "Fue un buen examen estudia mas y lo conseguiras",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        //Log.e("seguro_puntos", "$puntos")
                        //Log.e("seguro_intentos", "$intentos")

                    } else {
                        Toast.makeText(
                            context,
                            "Lo siento no puede mandar las respuestas antes de iniciar el examen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Comprobar resultados")
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Composable
    fun SlidingPuzzle(
        title: Int,
        cardData: CardData,
        context: Context = LocalContext.current,
        gridSize: Int = 3, // 3x3 grid
        tileSize: Dp = 100.dp,
    ) {
        var seguro by remember(title) { mutableStateOf(true) }
        var bitmap by remember(title) { mutableStateOf<Bitmap?>(null) }
        when (cardData) {
            is CardData.Letters -> {
                bitmap = BitmapFactory.decodeResource(context.resources, cardData.data.image)
            }
            is CardData.VideoGif -> {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(cardData.data.uri)
//                        .crossfade(true)
//                        .build(),
//                    contentDescription = "Image",
//                    imageLoader = imageLoader(LocalContext.current),
//                    onSuccess = { state ->
//                        bitmap = (state.result.drawable as BitmapDrawable).bitmap
//                    }
//                )

                Glide.with(context)
                    .asGif()
                    .load("https://firebasestorage.googleapis.com/v0/b/lsmdatabase-21d9c.appspot.com/o/gifs%2Fn9.gif?alt=media&token=716c374a-cabb-47d1-ab72-ee6feb5c8978")
                    .into(object : SimpleTarget<GifDrawable>() {
                        override fun onResourceReady(
                            resource: GifDrawable,
                            transition: Transition<in GifDrawable>?
                        ) {
                            val gifBitmap = resource.firstFrame
                        }
                    })
            }
        }

        val tileBitmaps = remember(title) {
            divideBitmap(bitmap!!, gridSize)
        }
        val shuffledTiles = remember(tileBitmaps, title) {
            mutableStateListOf<Bitmap?>()
//           tileBitmaps.shuffled().toMutableStateList()
       }.also { shuffledList ->
           LaunchedEffect(tileBitmaps) {
               shuffledList.clear()
               shuffledList.addAll(tileBitmaps.shuffled().toMutableList())
               seguro = false

           }
        }

        // Estado para la posición del espacio en blanco
        val emptyTilePosition = remember { mutableStateOf(Pair(gridSize - 1, gridSize - 1)) }
        val coroutineScope = rememberCoroutineScope()

        // Función para manejar el movimiento de las fichas
        val onTileMove =  { startIndex: Int, targetIndex: Int  ->
            if (isValidMove(startIndex, targetIndex, gridSize)) {
                shuffledTiles.swap(startIndex, targetIndex)
                // Actualizar la posición del espacio en blanco
                val emptyTileRow = emptyTilePosition.value.first
                val emptyTileCol = emptyTilePosition.value.second
                emptyTilePosition.value =
                    if (targetIndex == gridSize * emptyTileRow + emptyTileCol) {
                        Pair(startIndex / gridSize, startIndex % gridSize)
                    } else {
                        Pair(targetIndex / gridSize, targetIndex % gridSize)
                    }
                if (isPuzzleSolved(shuffledTiles.toList(), tileBitmaps.dropLast(1))) {
                    coroutineScope.launch {
                        Toast.makeText(context, "¡Rompecabezas resuelto!", Toast.LENGTH_SHORT)
                            .show()
                        puntos++
                    }
                }
            }
        }
        Card(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = title),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Instrucciones: Mueve las fichas para resolver el rompecabezas",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                if (!seguro) {
                    for (row in 0 until gridSize) {
                        Row {
                            for (col in 0 until gridSize) {
                                val index = row * gridSize + col
                                PuzzleTile(
                                    bitmap = shuffledTiles[index]?.asImageBitmap(),
                                    tileSize = tileSize,
                                    onMove = { shuffledTiles.swap(it, index) },
                                    index = index,
                                    emptyTilePosition = emptyTilePosition.value,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PuzzleTile(
        bitmap: ImageBitmap?,
        tileSize: Dp,
        onMove: (Int) -> Unit,
        index: Int,
        emptyTilePosition: Pair<Int, Int>
    ) {
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }
        var isDragging by remember { mutableStateOf(false) }
        // Estado para controlar la escala de la imagen
        var scale by remember { mutableFloatStateOf(1f) }
        //val boxSize = tileSize.value * LocalDensity.current.density


        val lifecycleOwner = LocalLifecycleOwner.current
        // Agrega un observador de ciclo de vida para restablecer la posición
        // de la ficha cuando la actividad se reanuda
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME && isDragging) {
                    offsetX = 0f
                    offsetY = 0f
                    isDragging = false
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        Box(
            modifier = Modifier
                .size(tileSize)
                .graphicsLayer {
                    translationX = offsetX
                    translationY = offsetY
                }
                .pointerInput(Unit) {
                    detectDragGestures(onDragStart = {
                        isDragging = true
                        offsetX = 0f
                        offsetY = 0f
                    }, onDragEnd = {
                        isDragging = false
                        // Calcula la nueva posición de la ficha
                        val targetIndex = calculateTargetIndex(
                            index, offsetX, offsetY, tileSize.value
                        )
                        onMove(targetIndex)
                        offsetX = 0f
                        offsetY = 0f
                    }, onDragCancel = {
                        isDragging = false
                        // Reinicia el desplazamiento si se cancela el arrastre
                        offsetX = 0f
                        offsetY = 0f
                    }, onDrag = { change, dragAmount ->
                        // Actualiza el desplazamiento según el movimiento de arrastre
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    })
                }
        ) {

            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            // Aplicar la escala solo durante el arrastre
                            scale = if (isDragging) {
                                0.8f  // Reducir la imagen al 80%
                            } else {
                                1f   // Volver a la escala original
                            }
                            // Aplicar la escala a la imagen
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }


            // Espacio vacío
            if (index == emptyTilePosition.first * 3 + emptyTilePosition.second) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                )
            }
        }

    }

    @Composable
    fun QuestionYesNo(title: String, description: String, key: Int, onOptionSelected: (String) -> Unit) {
        val radioOptions = listOf("Si", "No")
        var optionSelected by remember(key) {mutableStateOf<String?>(null)}

        Card(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Esta descripcion corresponde a la $title?",
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(text = description)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    radioOptions.forEach { text ->
                        RadioButton(
                            selected = (text == optionSelected),
                            onClick = {
                                optionSelected = text
                                onOptionSelected(text)
                            },
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

            }
        }
    }

    private fun calculateTargetIndex(startIndex: Int, offsetX: Float, offsetY: Float, tileSize: Float): Int {
        val row = startIndex / 3
        val col = startIndex % 3
//        val adjustedOffsetX = offsetX + tileSize / 2
//        val adjustedOffsetY = offsetY + tileSize / 2
        Log.e("funs_row", "$row")
        Log.e("funs_col", "$col")
        Log.e("funs_offsetX", "$offsetX")
        Log.e("funs_offsetY", "$offsetY")

//        val newRow = (row + (adjustedOffsetY / tileSize).toInt()).coerceIn(0..2)
//        val newCol = (col + (adjustedOffsetX / tileSize).toInt()).coerceIn(0..2)
        val newCol = when {
            offsetX > tileSize / 2 -> (col + 1).coerceAtMost(2)
            offsetX < -tileSize / 2 -> (col - 1).coerceAtLeast(0)
            else -> col
        }

        // Determina la dirección del movimiento en el eje Y
        val newRow = when {
            offsetY > tileSize / 2 -> (row + 1).coerceAtMost(2)
            offsetY < -tileSize / 2 -> (row - 1).coerceAtLeast(0)
            else -> row
        }
        Log.e("funs_newRow", "$newRow")
        Log.e("funs_newCol", "$newCol")
        return newRow * 3 + newCol
    }

    // Función para dividir un Bitmap en partes iguales
    private fun divideBitmap(bitmap: Bitmap, gridSize: Int): List<Bitmap?> {
        val tileWidth = bitmap.width / gridSize
        val tileHeight = bitmap.height / gridSize
        val result = mutableListOf<Bitmap?>()
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val x = j * tileWidth
                val y = i * tileHeight
                if (i == gridSize - 1 && j == gridSize - 1) {
                    // Última ficha vacía
                    result.add(null)
//                    result.add(Bitmap.createBitmap(bitmap, x, y, tileWidth, tileHeight))
                } else {
                    result.add(Bitmap.createBitmap(bitmap, x, y, tileWidth, tileHeight))
                }
            }
        }
        return result
    }

    // Función para intercambiar dos elementos en una MutableList
    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }

    // Función para comprobar si dos listas de Bitmaps son iguales
    private fun isPuzzleSolved(list1: List<Bitmap?>, list2: List<Bitmap?>): Boolean {
        if (list1.size != list2.size) return false
        for (i in list1.indices) {
            if (list1[i] != list2[i]) return false
        }
        return true
    }

    // Función para verificar si un movimiento es válido
    private fun isValidMove(startIndex: Int, targetIndex: Int, gridSize: Int): Boolean {
        val startRow = startIndex / gridSize
        //Log.e("funs_startrow", "$startRow")
        val startCol = startIndex % gridSize
        //Log.e("funs_startCol", "$startCol")
        val targetRow = targetIndex / gridSize
        //Log.e("funs_targetRow", "$targetRow")
        val targetCol = targetIndex % gridSize
        //Log.e("funs_targetCol", "$targetCol")


        // Comprueba si el movimiento es horizontal o vertical y si la ficha está junto al espacio vacío
        return ((startRow == targetRow && abs(startCol - targetCol) == 1) ||
                (startCol == targetCol && abs(startRow - targetRow) == 1))
    }

    // Función auxiliar para obtener el valor absoluto
    private fun abs(value: Int): Int = if (value < 0) -value else value

    @Composable
    fun LoadingScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }

    private fun imageLoader(context: Context):ImageLoader {
          val imageLoader = ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    add(ImageDecoderDecoder.Factory())
                }else{
                    add(GifDecoder.Factory())
                }
            }
            .build()
        return imageLoader
    }

    @Composable
    fun CubeImage(image: Int) {
        Image(
            modifier = Modifier
                .padding(16.dp)
                .size(150.dp),
            painter = painterResource(id = image),
            contentDescription = "A sign in LSM"
        )
    }

    @Composable
    fun CubeGif(uri: String){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            contentDescription = "GIF Image",
            imageLoader = imageLoader(LocalContext.current),
            modifier = Modifier
                .padding(16.dp)
                .size(150.dp)
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewApp() {
        MaterialTheme {
            LetterLearningScreen()
        }
    }
}