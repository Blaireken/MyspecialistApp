package com.myspecialist.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myspecialist.Interface.ItemClickListener;
import com.myspecialist.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ItemClickListener itemClickListener;
    public HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public void setTitle(String title) {
        TextView Title = itemView.findViewById(R.id.title);
        Title.setText(title);
    }
    public void setImage(String image) {
        ImageView imageView = itemView.findViewById(R.id.imageView);
        Picasso.get().load(image).into(imageView);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
