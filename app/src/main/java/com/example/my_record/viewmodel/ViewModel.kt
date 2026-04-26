package com.example.my_record

import android.R.attr.category
import android.R.attr.rating
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class RecordViewModel(private val dao: RecordDao) : ViewModel() {

    val recordList = mutableStateListOf<RecordEntity>()

    // ★ 今編集中のメモID（null = 新規）
    val editingMemoId = mutableStateOf<Int?>(null)

    fun insert(record: RecordEntity) {
        viewModelScope.launch {
            dao.insert(record)
            loadRecords()
        }
    }

    fun loadRecords() {
        viewModelScope.launch {
            recordList.clear()
            recordList.addAll(dao.getAll())
        }
    }

//    fun addRecord(title: String, content: String) {
//        viewModelScope.launch {
//            dao.insert(record)
//            loadRecords()
//        }
//    }

    fun updateRecord(record: RecordEntity) {
        viewModelScope.launch {
            dao.update(
                record.id,
                record.title,
                record.content,
                record.category,
                record.rating
            )
            loadRecords()
        }
    }

    fun deleteRecord(id: Int) {
        viewModelScope.launch {
            dao.deleteById(id)
            loadRecords()
        }
    }

    fun getRecordById(id: Int): RecordEntity? {
        return recordList.find { it.id == id }
    }
}