package com.example.filkomsoul.ui.screening.result

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.filkomsoul.MainActivity
import com.example.filkomsoul.R
import com.example.filkomsoul.data.local.ArchiveEntity
import com.example.filkomsoul.databinding.ActivityResultBinding
import com.example.filkomsoul.databinding.FragmentCounselingBinding
import com.example.filkomsoul.ui.ViewModelFactory
import com.example.filkomsoul.ui.WebView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


class ResultActivity : AppCompatActivity() {
    private lateinit var binding : ActivityResultBinding
    private lateinit var bindingPopUp : FragmentCounselingBinding
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var result : String
    private lateinit var summary : String
    private lateinit var archiveEntity: ArchiveEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        bindingPopUp = FragmentCounselingBinding.inflate(layoutInflater)
        val total = intent.getIntegerArrayListExtra(RESULT)

        if(total != null) {
            var text = ""
            val factorA = total[0]/9f
            val factorB = total[1]/14f
            val factorC = total[2]/7f
            if(((factorA) >= (factorB) && (factorA) >= (factorC))) {
                text += "kelesuan"
            }
            if(((factorB) >= (factorA) && (factorB) >= (factorC))) {
                if(text.length > 1) {
                    text += ", "
                }
                text += "emosi kognitif"
            }
            if(((factorC) >= (factorB) && (factorC) >= (factorA))) {
                if(text.length > 1) {
                    text += ", "
                }
                text += "motivasi akademik"
            }
                when {
                    total[3] < 73 -> {
                        result = resources.getString(R.string.result1)
                        summary = resources.getString(R.string.summary1, text)
                    }

                    total[3] in 74..94 -> {
                        result = resources.getString(R.string.result2)
                        summary = resources.getString(R.string.summary2, text)
                    }

                    total[3] in 95..118 -> {
                        result = resources.getString(R.string.result3)
                        summary = resources.getString(R.string.summary3, text)
                    }

                    total[3] in 119..150 -> {
                        result = resources.getString(R.string.result4)
                        summary = resources.getString(R.string.summary4, text)
                    }
                }

            var nim: Long = 0
            var output = 0
            viewModel.getAll().observe(this) {
                nim = it[0].parent.nim
                output = it[0].children.size
            }
            Handler(Looper.getMainLooper()).postDelayed({
                binding.hasil.text = total[3].toString()
                binding.progressbar.progress = (total[3]*100/150f).roundToInt()
                binding.kesimpulan.text = result
                binding.summary.text = summary
                archiveEntity = ArchiveEntity(output, nim, total[3], result, SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault()).format(Date()))
                viewModel.setResult(archiveEntity)
                archiveFirestore(archiveEntity)
            }, 200)
        }

        binding.hubungi.setOnClickListener {
            showBottomSheetDialog()
        }

        binding.back.setOnClickListener {
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this@ResultActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        super.onBackPressed()
    }

    private fun archiveFirestore(archiveEntity: ArchiveEntity) {
        val archive = hashMapOf(
            "date" to archiveEntity.date,
            "outputId" to archiveEntity.outputId,
            "result" to archiveEntity.result,
            "studentId" to archiveEntity.studentId,
            "total" to archiveEntity.total
        )

            val db = Firebase.firestore.collection("users").document(archiveEntity.studentId.toString())
            db.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        db.update("archive", FieldValue.arrayUnion(archive))
                            .addOnSuccessListener { }
                            .addOnFailureListener {
                                Toast.makeText(this@ResultActivity, resources.getString(R.string.internet), Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Log.d("TAG", "Error: ", task.exception)
                }
            }
        }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bindingPopUp.root)
        bottomSheetDialog.show()

        bindingPopUp.linearLayout1.setOnClickListener {
            val intent = Intent(this, WebView::class.java)
            intent.putExtra(URLID, "https://docs.google.com/forms/d/e/1FAIpQLSfKhMp7CCTEYjlVZ0OvggKZ23M02dX_D9ahOV7cEgGigBkEng/viewform")
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }
        bindingPopUp.linearLayout2.setOnClickListener {
            openWhatsAppChat()
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setOnDismissListener {
            (bindingPopUp.root.parent as? ViewGroup)?.removeView(bindingPopUp.root)
        }
    }


    private fun openWhatsAppChat() {
        val toNumber = "+62$8113600584"
        val url = "https://api.whatsapp.com/send?phone=$toNumber"
        try {
            applicationContext.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent.apply { data = Uri.parse(url) })
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(this@ResultActivity, resources.getString(R.string.counselingpoperror), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val RESULT = "result"
        const val URLID = "urlID"
    }
}