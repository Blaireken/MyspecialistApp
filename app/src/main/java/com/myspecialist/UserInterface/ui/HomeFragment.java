package com.myspecialist.UserInterface.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myspecialist.Adapter.HomeAdapter;
import com.myspecialist.Common.Common;
import com.myspecialist.Interface.ItemClickListener;
import com.myspecialist.R;
import com.myspecialist.ViewHolder.HomeViewHolder;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<HomeAdapter, HomeViewHolder> firebaseRecyclerAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);

        int columns = 2; //Number of columns to show in home fragment xml
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), columns));

        if (Common.isConnectedToInternet(getContext())){
            loadRecyclerView();
        }
        else {
            Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
        }

        return root;
    }

    private void loadRecyclerView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Departments");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HomeAdapter, HomeViewHolder>
                (HomeAdapter.class, R.layout.home_child, HomeViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(HomeViewHolder homeViewHolder, HomeAdapter homeAdapter, int i) {
                homeViewHolder.setTitle(homeAdapter.getTitle());
                homeViewHolder.setImage(homeAdapter.getImage());

                homeViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryID", firebaseRecyclerAdapter.getRef(position).getKey());
                        NavHostFragment.findNavController(HomeFragment.this)
                                .navigate(R.id.to_doctor_list, bundle);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}