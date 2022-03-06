package com.example.notesroomtutorial.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.notesroomtutorial.Models.Notes;

import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    public void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    public List<Notes> getAll();

    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    public void upDate(int id, String title, String notes);

    @Delete
    public void delete(Notes notes);

}
