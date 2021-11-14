package com.myspecialist.UserInterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myspecialist.Adapter.DoctorListAdapter;
import com.myspecialist.Common.Common;
import com.myspecialist.Interface.ItemClickListener;
import com.myspecialist.R;
import com.myspecialist.ViewHolder.DoctorListViewHolder;

import java.util.ArrayList;
import java.util.List;


public class DoctorListFragment extends Fragment {

    String categoryID ="";
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    LinearLayout poorConnectionLayout, empty_database_layout;
    FirebaseRecyclerAdapter<DoctorListAdapter, DoctorListViewHolder> firebaseRecyclerAdapter;
    List<DoctorListAdapter> list = new ArrayList<>();
    Button retry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.doctor_list, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctors");

        recyclerView = root.findViewById(R.id.recyclerView);
        poorConnectionLayout = root.findViewById(R.id.poor_connection_layout);
        retry = root.findViewById(R.id.retry_connection);
        empty_database_layout = root.findViewById(R.id.empty_database_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle bundle = this.getArguments();
        categoryID = bundle.getString("categoryID");

        if (Common.isConnectedToInternet(getContext())){
            loadUI();
            checkDatabase();
        }
        else {
            poorConnectionLayout.setVisibility(View.VISIBLE);
        }
        return root;
    }

    private void loadUI() {
        if (getArguments() != null){
            categoryID = getArguments().getString("categoryID");
        }
        if (!categoryID.isEmpty() && categoryID != null){
            loadDoctorList();
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           /*     if (Common.isConnectedToInternet(getContext())){
                    if (getArguments() != null){
                        categoryID = getArguments().getString("categoryID");
                    }
                    if (!categoryID.isEmpty() && categoryID != null){
                        if (list.size()>0){
                            empty_database_layout.setVisibility(View.GONE);
                        }else {
                            empty_database_layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else {
                    poorConnectionLayout.setVisibility(View.VISIBLE);
                }    */
            }
        });

    }

    private void checkDatabase() {
        databaseReference.orderByChild("DoctorID").equalTo(categoryID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.exists()){
                  empty_database_layout.setVisibility(View.GONE);
              } else {
                  empty_database_layout.setVisibility(View.VISIBLE);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDoctorList() {
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DoctorListAdapter, DoctorListViewHolder>
                (DoctorListAdapter.class, R.layout.doctor_child, DoctorListViewHolder.class, databaseReference.orderByChild("DoctorID").equalTo(categoryID)) {
            @Override
            protected void populateViewHolder(DoctorListViewHolder doctorListViewHolder, DoctorListAdapter doctorListAdapter, int i) {
                doctorListViewHolder.setImage(doctorListAdapter.getImage());
                doctorListViewHolder.setName(doctorListAdapter.getName());
                doctorListViewHolder.setDepartment(doctorListAdapter.getDepartment());
                doctorListViewHolder.setDuty(doctorListAdapter.getDuty());
                doctorListViewHolder.setLocation(doctorListAdapter.getLocation());

                ((MainActivity) getActivity()).getSupportActionBar().setTitle(doctorListAdapter.getDepartment());

                doctorListViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putString("DoctorDetail", firebaseRecyclerAdapter.getRef(position).getKey());
                        Intent intent = new Intent(getContext(), DoctorDetail.class);
                        intent.putExtra("DoctorDetail", firebaseRecyclerAdapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}