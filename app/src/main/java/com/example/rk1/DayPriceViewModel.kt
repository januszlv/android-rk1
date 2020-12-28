package com.example.rk1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DayPriceViewModel : ViewModel() {
    data class DayPriceItemModel(
        val sym: String,
        val ts: Long,
        val value: Float,
        val min: Float,
        val max: Float,
    )

    private val dayPrice: MutableLiveData<MutableList<DayPriceItemModel>> = MutableLiveData()
    fun getPrice(): LiveData<MutableList<DayPriceItemModel>> = dayPrice


    fun refresh(
        fsym: String,
        tsym: String,
        limit: Int
    ): Job {
        return viewModelScope.launch {
            try {
                val res = CryptoCompareApi.retrofitService.getDaily(
                    from = fsym,
                    to = tsym,
                    lim = limit
                ).data.data

                storeInfoItems(tsym, res.reversed())
            } catch (e: Exception) {
                Log.i(TAG, "load failed: " + e.message)
            }
        }
    }

    private fun storeInfoItems(tsym: String, items: List<APIResponseDataItem>) {
        val list = (dayPrice.value ?: mutableListOf())
        list.clear()

        items.forEach {
            list.add(
                DayPriceItemModel(
                    tsym,
                    it.time,
                    it.close,
                    it.low,
                    it.high
                )
            )
        }

//        list.add(
//            DayPriceItemModel(
//                "USD", 1603573200, 12312.3f
//            )
//        )
//        list.add(
//            DayPriceItemModel(
//                "USD", 1603573200+800, 12312.3f
//            )
//        )
//        list.add(
//            DayPriceItemModel(
//                "USD", 1603573200+1600, 12312.3f
//            )
//        )

        Log.i(TAG, "has values")
        dayPrice.value = list
    }

    companion object {
        private const val TAG = "StartFragment"
    }

}