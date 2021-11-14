package com.myspecialist.UserInterface.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myspecialist.Adapter.DeveloperAdapter;
import com.myspecialist.R;
import com.myspecialist.ViewHolder.DeveloperViewModel;

import java.util.ArrayList;

public class DevelopersFragment extends Fragment {

    RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.developers, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Array of data(developers)
        ArrayList<DeveloperAdapter> arrayList = new ArrayList<>();
        arrayList.add(new DeveloperAdapter("Rachael Kimberly Msabeni BCSIT/0115/2018"));
        arrayList.add(new DeveloperAdapter("Kuria Harun Kinyua BCSIT/0021/2017"));
        arrayList.add(new DeveloperAdapter("Kennedy Muriithi BCSIT/0112/2017"));

        DeveloperViewModel developerViewModel = new DeveloperViewModel(getContext(), arrayList);
        recyclerView.setAdapter(developerViewModel);


        return root;
    }
}