package com.example.epokemon;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class CardComprados extends RecyclerView.ViewHolder{

    public ImageView image;
    public TextView name;
    public TextView price;
    public TextView hp;
    public TextView attack;
    public TextView defense;
    public Button buttonExcluir;

    public CardComprados(@NonNull @NotNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.imageViewComprado);
        name = itemView.findViewById(R.id.nameComprado);
        price = itemView.findViewById(R.id.priceComprado);
        hp = itemView.findViewById(R.id.hpComprado);
        attack = itemView.findViewById(R.id.attackComprado);
        defense = itemView.findViewById(R.id.defenseComprado);
        buttonExcluir = itemView.findViewById(R.id.buttonDeletar);
    }


}
