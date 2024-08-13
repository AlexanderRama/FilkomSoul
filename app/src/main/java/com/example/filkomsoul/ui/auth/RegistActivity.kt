package com.example.filkomsoul.ui.auth

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.filkomsoul.MainActivity
import com.example.filkomsoul.R
import com.example.filkomsoul.data.local.AllEntity
import com.example.filkomsoul.data.local.ArchiveEntity
import com.example.filkomsoul.data.local.MahasiswaEntity
import com.example.filkomsoul.databinding.ActivityRegistBinding
import com.example.filkomsoul.ui.ViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistBinding
    private lateinit var allEntity: List<AllEntity>
    private val authViewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        allEntity = listOf()
        authViewModel.getAll().observe(this@RegistActivity) {
            allEntity = it
        }
        input1()
        input2()
        binding.apply {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    check1()
                }
            }
            checkBox2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    check2()
                }
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if(allEntity.isNotEmpty()) {
                input1()
                input2()
                binding.input1.setText(allEntity[0].parent.nim.toString())
                binding.input2.setText(allEntity[0].parent.name)
                if(allEntity[0].parent.gender == "Laki-laki") {
                    binding.checkBox.isChecked = true
                    check1()
                } else {
                    binding.checkBox2.isChecked = true
                    check2()
                }
                binding.checkBox3.isChecked = true
                binding.btnDaftar.text = resources.getString(R.string.btnDaftar)
            }
        }, 200)

        binding.apply {
            btnDaftar.setOnClickListener{
                val checkboxgenderX = checkBox.isChecked
                val checkboxgenderY = checkBox2.isChecked
                val checkboxgenderapp = checkBox3.isChecked
                val inputNIM = input1.text.toString()
                val inputname = input2.text.toString()

                when {
                    inputNIM.isEmpty() -> {
                        input1.error = resources.getString(R.string.emptyError)
                    }
                    inputname.isEmpty() -> {
                        input2.error = resources.getString(R.string.emptyError)
                    }
                    !checkboxgenderY && !checkboxgenderX -> {
                        Toast.makeText(this@RegistActivity, resources.getString(R.string.emptyGender), Toast.LENGTH_SHORT).show()
                    }
                    !checkboxgenderapp -> {
                        Toast.makeText(this@RegistActivity, resources.getString(R.string.emptyApp), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        val year = "20" + inputNIM.substring(0,2)
                        val gender = if(checkboxgenderX) {
                            "Laki-laki"
                        } else {
                            "Perempuan"
                        }
                        val mahasiswa = MahasiswaEntity(inputNIM.toLong(), inputname, gender, checkMajor(inputNIM), year)
                        if(checkForInternet(this@RegistActivity)){
                            if(allEntity.isEmpty()) {
                                val server = hashMapOf(
                                    "nim" to inputNIM,
                                    "name" to inputname,
                                    "gender" to gender,
                                    "major" to checkMajor(inputNIM),
                                    "year" to year,
                                    "archive" to listOf<ArchiveEntity>(),
                                )
                                authViewModel.setMahasiswa(mahasiswa)
                                upFirestore(server, inputNIM)
                            } else {
                                if(allEntity[0].parent.nim.toString() != inputNIM) {
                                    authViewModel.deleteAll()
                                    getFirestore(inputNIM)
                                } else {
                                    val local = hashMapOf(
                                        "nim" to allEntity[0].parent.nim,
                                        "name" to allEntity[0].parent.name,
                                        "gender" to allEntity[0].parent.gender,
                                        "major" to allEntity[0].parent.major,
                                        "year" to allEntity[0].parent.year,
                                        "archive" to allEntity[0].children,
                                    )
                                    upFirestore(local, inputNIM)
                                }
                                authViewModel.setMahasiswa(mahasiswa)
                            }
                        } else {
                            if(allEntity.isEmpty()) {
                                authViewModel.setMahasiswa(mahasiswa)
                            }
                        }
                        startActivity(Intent(this@RegistActivity, MainActivity::class.java))
                    }
                }
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun input1() {
        binding.input1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                when (p0?.length) {
                    0 -> {
                        binding.input1.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.gray))
                        binding.input1.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input)
                    }
                    else -> {
                        binding.input1.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.black))
                        binding.input1.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input2)
                    }
                }

            }
        })
    }
    private fun input2() {
        binding.input2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun afterTextChanged(p0: Editable?) {
                when (p0?.length) {
                    0 -> {
                        binding.input2.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.gray))
                        binding.input2.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input)
                    }
                    else -> {
                        binding.input2.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.black))
                        binding.input2.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input2)
                    }
                }

            }
        })
    }
    private fun check1() {
        binding.apply {
            checkBox.isEnabled = false
            checkBox2.isEnabled = true
            checkBox2.isChecked = false
            checkBox2.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.gray))
            choose2.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input)
            checkBox.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.black))
            choose1.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input2)
        }
    }
    private fun check2() {
        binding.apply {
            checkBox.isEnabled = true
            checkBox2.isEnabled = false
            checkBox.isChecked = false
            checkBox.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.gray))
            choose1.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input)
            checkBox2.setTextColor(ContextCompat.getColor(this@RegistActivity, R.color.black))
            choose2.background = ContextCompat.getDrawable(this@RegistActivity, R.drawable.custom_input2)
        }
    }

    private fun upFirestore(user: HashMap<String, Any>, inputNIM: String) {
        val db = Firebase.firestore.collection("users").document(inputNIM)
        db.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if(document != null) {
                        db.set(user)
                            .addOnSuccessListener { }
                            .addOnFailureListener {}
                }
            } else {
                Log.d("TAG", "Error: ", task.exception)
            }
        }
    }

    private fun getFirestore(inputNIM: String) {
        val db = Firebase.firestore.collection("users").document(inputNIM)
        db.get()
            .addOnSuccessListener {
                val mahasiswa = MahasiswaEntity(
                    inputNIM.toLong(), it.get("name").toString(), it.get("gender")
                    .toString(), it.get("major").toString(), it.get("year").toString())
                authViewModel.setMahasiswa(mahasiswa)
                val list = ArrayList(it.get("archive").toString().replace("[","").replace("{","").replace("]","").replace("}","").split("}, "))
                var result = " "
                var date = " "
                var total = 0
                var outputId = 0
                for (innerList in list) {
                    val dataList : List<String> = innerList.split(", ")
                    dataList.forEachIndexed { index, hasil ->
                            with(hasil) {
                                when {
                                    contains("result") -> result = dataList[index].replace("result=", "")
                                    contains("date") -> date = dataList[index].replace("date=", "")
                                    contains("total") -> total = dataList[index].replace("total=", "").toInt()
                                    contains("outputId") -> outputId = dataList[index].replace("outputId=", "").toInt()
                                }
                            }
                            if(total != 0) {
                                authViewModel.setResult(ArchiveEntity(outputId, inputNIM.toLong(), total, result, date))
                            }
                        }
                    }
                }
            .addOnFailureListener{
                it.printStackTrace()
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkMajor(inputNIM: String): String {
        var major = " "
        if(inputNIM[6] == '2') {
            major += "Teknik Informatika"
        }
        else if(inputNIM[6] == '4') {
            major += "Sistem Informasi"
        }
        else if(inputNIM[6] == '3') {
            major += "Teknik Komputer"
        }
        else if(inputNIM[6] == '7') {
            major += "Teknologi Informasi"
        }
        else if(inputNIM[6] == '6') {
            major += "Pendidikan TI"
        }
        return major
    }

}