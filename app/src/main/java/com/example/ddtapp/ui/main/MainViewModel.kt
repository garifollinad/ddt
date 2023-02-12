package com.example.ddtapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ddtapp.base.BaseViewModel
import com.example.ddtapp.model.House
import com.example.ddtapp.repository.HouseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val houseRepository: HouseRepository
) : BaseViewModel() {

    private val state = MutableLiveData<Result>()
    val liveData: LiveData<Result> = state

    fun getHouses(){
        addDisposable(
            houseRepository.getHouses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        state.value = Result.Houses(housesList = result)
                    },
                    { error -> state.value = Result.Error(error = error.message) }
                )
        )
    }

    sealed class Result {
        data class Houses(val housesList: List<House>) : Result()
        data class Error(val error: String?) : Result()
    }
}