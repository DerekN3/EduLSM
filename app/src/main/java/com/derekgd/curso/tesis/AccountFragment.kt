package com.derekgd.curso.tesis

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.derekgd.curso.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseDatabase
    private var storage = Firebase.storage
    var storageRef = storage.reference
    lateinit var imagesRef: StorageReference

    private var uploadedImageUrl:String? =null

    // Crea el launcher para capturar imágenes
    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, lanza la cámara
            takePicture()
        } else {
            // Permiso denegado, maneja la situación
            // ...
        }
    }

    // Crea el launcher para obtener la imagen capturada
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        // Procesa la imagen capturada (bitmap)
        // ...
        if (bitmap != null) {


            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            uploadImage(data, this, object : ImageUploadCallback {
                override fun onImageUploaded(imageUrl: String ,ImageView: ImageView) {
                    Glide.with(this@AccountFragment).load(imageUrl).into(ImageView)
                    uploadedImageUrl = imageUrl
                }
            })
        } else {
            // Maneja el caso en que no se capturó ninguna imagen
        }
    }

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
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etName = view.findViewById<TextView>(R.id.etName)
        val etEmail = view.findViewById<TextView>(R.id.etEmail)
        val etPassword = view.findViewById<TextView>(R.id.etPassword)
        val etAge = view.findViewById<TextView>(R.id.etAge)
        val userImage = view.findViewById<ImageView>(R.id.userImage)

        val btnSignOut = view.findViewById<Button>(R.id.btnSignOut)
        val btnSaveAccount = view.findViewById<Button>(R.id.btnSaveAccount)
        val btnCamera = view.findViewById<FloatingActionButton>(R.id.btnCamera)
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        var uID = ""


        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvPassword = view.findViewById<TextView>(R.id.tvPassword)
        val tvAge = view.findViewById<TextView>(R.id.tvAge)

        val sharedPrefer = requireActivity().getSharedPreferences("MyPrefers", Context.MODE_PRIVATE)
        val  SizeTextBase= sharedPrefer.getFloat("SizeTextBase", resources.getDimension(R.dimen.text_size_medium))
        tvName.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTextBase, resources.displayMetrics)
        tvEmail.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTextBase, resources.displayMetrics)
        tvPassword.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTextBase, resources.displayMetrics)
        tvAge.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, SizeTextBase, resources.displayMetrics)

        user?.let {
            val name = it.displayName
            val email = it.email
            val photoURl = it.photoUrl
            uID = it.uid

            etName.text = name
            etEmail.text = email
            Glide.with(this).load(photoURl).into(userImage)

            imagesRef = storageRef.child("images/$uID.jpg")

        }

        etPassword.text = "1234567"
        val storageRef: StorageReference = storage.reference
        db = Firebase.database

        val users = db.reference.child("users").child(uID)

        val nombre = users.child("Nombre")
        val correo = users.child("correo")
        val edad = users.child("edad")
        correo.setValue(etEmail.text.toString())

        edad.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val edad = dataSnapshot.value // Leer el valor como un Int

                if (edad != null) {
                    etAge.text = edad.toString()
                } else {
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        userImage.setOnClickListener{

            val imageUrl = user?.let {it.photoUrl.toString()}
            //Crea el intent y le pasa el uri de la imagen
            val intent = Intent(context, ImageFullscreenActivity::class.java)
            intent.putExtra("IMAGE_URI", imageUrl)

            //Inicia la actividad ImageFullscreenActivity
            startActivity(intent)

        }

        btnCamera.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                // Permiso ya concedido, lanza la cámara
                takePicture()
            } else {
                // Solicita el permiso de la cámara
                requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }

        }

        etPassword.setOnClickListener {

            val intent = Intent(requireView().context, passwordActivity::class.java)
            startActivity(intent)

        }

        btnSaveAccount.setOnClickListener {
            nombre.setValue(etName.text.toString())
            correo.setValue(etEmail.text.toString())
            edad.setValue(etAge.text.toString())
            val newEmail = etEmail.text.toString()

            user?.verifyBeforeUpdateEmail(newEmail)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e("correo", "$newEmail")
                        Toast.makeText(
                            requireContext(),
                            "Se a enviado un correo de verificacion a su nuevo correo confirmelo para completar la actualizacion",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), "Introduzca un correo diferente para actualizarlo", Toast.LENGTH_LONG)
                            .show()
                        val errorCode = task.exception
                        Log.e("correo", "$errorCode")
                    }
                }

            val profileUpdates = userProfileChangeRequest {
                displayName = etName.text.toString()
                try {
                    photoUri = Uri.parse(uploadedImageUrl)
                }catch(exception:Exception) {}
            }

            user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Perfil actualizado con exito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnSignOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireView().context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }




    }

    fun takePicture() {
        takePictureLauncher.launch(null)
    }

    interface ImageUploadCallback {
        fun onImageUploaded(imageUrl: String, imageView:ImageView){
        }
    }

    private fun uploadImage(data: ByteArray, fragment: AccountFragment, callback: ImageUploadCallback) {
        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            Log.e("cameraInfo", "La imagen no se cargo correctamente")
        }.addOnSuccessListener {
            Log.e("cameraInfo", "Imagen cargada con exito")
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                callback.onImageUploaded(uri.toString(), requireView().findViewById<ImageView>(R.id.userImage))
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
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}