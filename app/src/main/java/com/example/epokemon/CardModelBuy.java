package com.example.epokemon;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class CardModelBuy extends RecyclerView.ViewHolder{
    public RatingBar ratingBar;
    public ImageView image;
    public TextView name;
    public TextView price;
    public TextView hp;
    public TextView attack;
    public TextView defense;
    public Button buttonComprar;

    public CardModelBuy(@NonNull @NotNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.imageViewBuy);
        name = itemView.findViewById(R.id.nameBuy);
        price = itemView.findViewById(R.id.priceBuy);
        hp = itemView.findViewById(R.id.hpBuy);
        attack = itemView.findViewById(R.id.attackBuy);
        defense = itemView.findViewById(R.id.defenseBuy);
        buttonComprar = itemView.findViewById(R.id.buttonBuy);
        ratingBar = itemView.findViewById(R.id.ratingBarToBuy);
    }


}
