package com.example.my_record

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RecordDao {
    @Query("SELECT * FROM record ORDER BY id DESC")
    suspend fun getAll(): List<RecordEntity>

    @Insert
    suspend fun insert(record: RecordEntity)

    @Query(
        """
    UPDATE record 
    SET title = :title,
        content = :content,
        category = :category,
        rating = :rating
    WHERE id = :id
"""
    )
    suspend fun update(
        id: Int,
        title: String,
        content: String,
        category: String,
        rating: String
    )

    @Query("DELETE FROM record WHERE id = :id")
    suspend fun deleteById(id: Int)
}