package com.tbondarenko.changeablerecyclerview.data

import com.tbondarenko.changeablerecyclerview.data.model.Number
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NumberRepository @Inject constructor() {

    val numberList: List<Number>
        get() = _numberList
    private var _numberList: MutableList<Number> = mutableListOf(
        Number(1),
        Number(2),
        Number(3),
        Number(4),
        Number(5),
        Number(6),
        Number(7),
        Number(8),
        Number(9),
        Number(10),
        Number(11),
        Number(12),
        Number(13),
        Number(14),
        Number(15),
    )
    private var _remoteNumberList: MutableList<Number> = mutableListOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = Dispatchers.Default
        .limitedParallelism(1)


    /**
     *  Add new number to the list every 5 second. If the list of deleted numbers is empty,
     *  then increment the number, otherwise take it from the list of deleted elements
     */
    suspend fun addItem1(): Flow<List<Number>> = flow {
        while (true) {
            delay(5000)
            if (_remoteNumberList.isEmpty()) {
                _numberList += Number(_numberList.maxOf { it.number }.plus(1))
            } else {
                val addNumber = _remoteNumberList.minByOrNull { it.number }!!
                _numberList += addNumber
                _remoteNumberList -= addNumber
            }
            emit(_numberList.sortedBy { it.number })
        }
    }
        .flowOn(dispatcher)

    /**
     *  Remove select number from the list. Create list of the removing numbers
     */
    suspend fun removeNumber1(number: Number): Flow<List<Number>> = flow {
        _numberList -= number
        emit(_numberList.sortedBy { it.number })
        _remoteNumberList += number
    }
        .flowOn(dispatcher)


    /**
     *  Add new number to the list every 5 second
     */
    suspend fun addItem(): Flow<List<Number>> = flow<List<Number>> {
        while (true) {
            delay(5000)
            _numberList += Number(_numberList.last().number.plus(1))
            emit(_numberList)
        }
    }
        .flowOn(Dispatchers.Default)

    /**
     *  Remove select number from the list
     */
    suspend fun removeNumber(number: Number): Flow<List<Number>> = flow {
        _numberList -= number
        emit(_numberList)
    }
        .flowOn(Dispatchers.Default)
}