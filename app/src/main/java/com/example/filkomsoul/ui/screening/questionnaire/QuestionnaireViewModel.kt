package com.example.filkomsoul.ui.screening.questionnaire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filkomsoul.data.local.ScreeningEntity

class QuestionnaireViewModel : ViewModel() {
    var screeningList: MutableLiveData<MutableList<ScreeningEntity>> = MutableLiveData()
    private var list = mutableListOf<ScreeningEntity>()

    fun setScreeningList(screening: ScreeningEntity) {
        list.add(screening)
        screeningList.value = list
    }
}
