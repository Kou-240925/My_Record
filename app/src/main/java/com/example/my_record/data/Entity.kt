package com.example.my_record

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val category: String,
    val rating: String,
    val date: Long
)