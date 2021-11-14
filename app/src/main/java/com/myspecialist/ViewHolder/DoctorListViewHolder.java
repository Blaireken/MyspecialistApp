package com.myspecialist.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myspecialist.Interface.ItemClickListener;
import com.myspecialist.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ItemClickListener itemClickListener;
    public DoctorListViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    public void setName(String name) {
        TextView Name = itemView.findViewById(R.id.doctor_name);
        Name.setText(name);
    }
    public void setDuty(String duty) {
        TextView Duty = itemView.findViewById(R.id.doctor_duty);
        Duty.setText(duty);
    }
    public void setDepartment(String department) {
        TextView Department = itemView.findViewById(R.id.doctor_department);
        Department.setText(department);
    }
    public void setImage(String image) {
        ImageView doctor_pic = itemView.findViewById(R.id.doctor_pic);
        Picasso.get().load(image).into(doctor_pic);
    }
    public void setLocation(String location) {
        TextView Location = itemView.findViewById(R.id.doctor_location);
        Location.setText(location);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
       itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
