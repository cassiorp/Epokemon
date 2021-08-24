package com.example.epokemon.repository;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RotinaModel {

    @PrimaryKey(autoGenerate = true)
    private Long idRoom;
    private String id;
    private float rating;

    public RotinaModel(String id, float rating) {
        this.id = id;
        this.rating = rating;
    }

    public RotinaModel() {
    }

    public Long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(Long idRoom) {
        this.idRoom = idRoom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RotinaModel{" +
                "idRoom=" + idRoom +
                ", id='" + id + '\'' +
                ", rating=" + rating +
                '}';
    }
}
