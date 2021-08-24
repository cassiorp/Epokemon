package com.example.epokemon.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RotinaDAO {

    @Query("SELECT * FROM RotinaModel")
    List<RotinaModel> listarTodos();

    @Update
    void atualizar(RotinaModel rotinaModel);

    @Insert
    long inserir(RotinaModel rotinaModel);

    @Delete
    void excluir(RotinaModel rotinaModel);

}
