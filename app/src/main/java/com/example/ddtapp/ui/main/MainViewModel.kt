package com.example.ddtapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ddtapp.base.BaseViewModel
import com.example.ddtapp.model.House
import com.example.ddtapp.repository.HouseDaoRepository
import com.example.ddtapp.repository.HouseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val houseRepository: HouseRepository,
    private val houseDaoRepository: HouseDaoRepository
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
                        houseDaoRepository.insertHouses(result)
                        state.value = Result.Houses(housesList = result)
                    },
                    { error -> state.value = Result.Error(error = error.message) }
                )
        )
    }

    fun getLocalHouses() {
        addDisposable(
            houseDaoRepository.getHouses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.isEmpty()) {
                            getHouses()
                        } else {
                            state.value = Result.Houses(housesList = result)
                        }
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