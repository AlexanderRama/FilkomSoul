package com.example.filkomsoul.ui.screening.questionnaire

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.filkomsoul.MainActivity
import com.example.filkomsoul.R
import com.example.filkomsoul.R.attr
import com.example.filkomsoul.data.local.ScreeningEntity
import com.example.filkomsoul.databinding.ActivityQuestionnaireBinding
import com.example.filkomsoul.ui.screening.result.ResultActivity


class QuestionnaireActivity : AppCompatActivity() {
    private lateinit var binding : ActivityQuestionnaireBinding
    private var result1 = 0
    private var result2 = 0
    private var result3 = 0
    private var mark = 0
    private var index = 0
    private var count = 0
    private var option = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Filkomsoul)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.num.text = resources.getString(R.string.screeningnum, index+1)
        val questionList = resources.getStringArray(R.array.question)
        supportActionBar?.hide()

        binding.question.text = questionList[index]
        binding.linearProgressIndicator.progress += 3
        optionColor()
        button()
        binding.back.setOnClickListener{
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.exit2))
                setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(this@QuestionnaireActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                setNegativeButton("No", null)
            }.show()
        }
    }

    private fun button() {
        val questionList = resources.getStringArray(R.array.question)
        val viewModel: QuestionnaireViewModel by viewModels()
        binding.btnSelanjut.setOnClickListener {
            if(option) {
                    if (index < 29) {
                        viewModel.setScreeningList(ScreeningEntity(index, questionList[index], mark))
                        index++
                        if(count == 0) {
                            binding.linearProgressIndicator.progress += 3
                            binding.num.text = resources.getString(R.string.screeningnum, index+1)
                            binding.question.text = questionList[index]
                            optionReset()
                            option = false
                        } else {
                            binding.linearProgressIndicator.progress += 3
                            backwardScreening(index)
                            count--
                        }
                    } else {
                        val totalFinal = ArrayList<Int>()
                        totalFinal.add(result1)
                        totalFinal.add(result2)
                        totalFinal.add(result3)
                        totalFinal.add(result1 + result2 + result3)
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(RESULT, totalFinal)
                        startActivity(intent)
                    }
            } else {
                Toast.makeText(this@QuestionnaireActivity, resources.getString(R.string.screeningerror), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun optionColor() {
        binding.apply {
            option1.setOnClickListener {
                setOption(1)
            }
            option2.setOnClickListener {
                setOption(2)
            }
            option3.setOnClickListener {
                setOption(3)
            }
            option4.setOnClickListener {
                setOption(4)
            }
            option5.setOnClickListener {
                setOption(5)
            }
        }
    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    private fun setOption(optionBtn : Int) {
        optionReset()
        when(optionBtn) {
            1 -> {
                binding.text1.setTextColor(getColorFromAttr(attr.colorOnSecondary))
                binding.option1.background = ContextCompat.getDrawable(this, R.drawable.custom_button2)
                setScore(1)
            }
            2 -> {
                binding.text2.setTextColor(getColorFromAttr(attr.colorOnSecondary))
                binding.option2.background = ContextCompat.getDrawable(this, R.drawable.custom_button2)
                setScore(2)
            }
            3 -> {
                binding.text3.setTextColor(getColorFromAttr(attr.colorOnSecondary))
                binding.option3.background = ContextCompat.getDrawable(this, R.drawable.custom_button2)
                setScore(3)
            }
            4 -> {
                binding.text4.setTextColor(getColorFromAttr(attr.colorOnSecondary))
                binding.option4.background = ContextCompat.getDrawable(this, R.drawable.custom_button2)
                setScore(4)
            }
            5 -> {
                binding.text5.setTextColor(getColorFromAttr(attr.colorOnSecondary))
                binding.option5.background = ContextCompat.getDrawable(this, R.drawable.custom_button2)
                setScore(5)
            }
            else -> { }
        }
        option = true
        binding.textButton.setTextColor(getColorFromAttr(attr.colorOnSecondary))
        binding.btnSelanjut.background = ContextCompat.getDrawable(this, R.drawable.custom_buttonscreening2)
    }

    private fun setScore(result : Int) {
        mark = result
        if (index < 9) {
            result1 += result
        } else if(index in 9..22) {
            result2 += result
        } else if(index in 22..29) {
            result3 += result
        }
    }

    private fun optionReset() {
        binding.apply {
            text1.setTextColor(getColorFromAttr(attr.colorSecondaryVariant))
            text2.setTextColor(getColorFromAttr(attr.colorSecondaryVariant))
            text3.setTextColor(getColorFromAttr(attr.colorSecondaryVariant))
            text4.setTextColor(getColorFromAttr(attr.colorSecondaryVariant))
            text5.setTextColor(getColorFromAttr(attr.colorSecondaryVariant))
            textButton.setTextColor(getColorFromAttr(attr.colorSecondaryVariant))
            applicationContext.apply {
                option1.background = ContextCompat.getDrawable(this, R.drawable.custom_button)
                option2.background = ContextCompat.getDrawable(this, R.drawable.custom_button)
                option3.background = ContextCompat.getDrawable(this, R.drawable.custom_button)
                option4.background = ContextCompat.getDrawable(this, R.drawable.custom_button)
                option5.background = ContextCompat.getDrawable(this, R.drawable.custom_button)
                btnSelanjut.background = ContextCompat.getDrawable(this, R.drawable.custom_buttonscreening)
            }
        }
    }

    private fun backwardScreening(position: Int) {
        val viewModel: QuestionnaireViewModel by viewModels()
        viewModel.screeningList.observe(this@QuestionnaireActivity) {
            binding.num.text = resources.getString(R.string.screeningnum, position+1)
            binding.question.text = it[position].question
            setOption(it[position].answer)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        this.onBackPressedDispatcher.addCallback(this) {
            optionReset()
            binding.linearProgressIndicator.progress -= 3
            backwardScreening(index-1)
            index--
            count++
        }
        supportActionBar?.hide()
        super.onBackPressed()
        button()
    }

    companion object {
        const val RESULT = "result"
    }
}