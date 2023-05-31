package com.example.gifticon_management;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MainDao {
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    @Delete
    void delete(MainData mainData);

    @Delete
    void reset(List<MainData> mainData);

    @Update
    void update(MainData mainData);


    @Query("SELECT * FROM table_name")
    //List<MainData> getAll();
    LiveData<List<MainData>> getAll();

    @Query("SELECT * FROM table_name WHERE id = :id")
    MainData getDataById(int id);

}
