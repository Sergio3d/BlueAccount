package com.cumn.blueaccount.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ITransacDAO {
    @Query("SELECT * FROM " + TransacEntity.TABLA)
    LiveData<List<TransacEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(TransacEntity grupo);

    @Query("DELETE FROM " + TransacEntity.TABLA)
    void deleteAll();

    @Delete
    void delete(TransacEntity grupo);
}