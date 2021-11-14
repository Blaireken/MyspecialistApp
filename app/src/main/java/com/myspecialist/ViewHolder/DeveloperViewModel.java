package com.myspecialist.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.myspecialist.Adapter.DeveloperAdapter;
import com.myspecialist.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeveloperViewModel extends RecyclerView.Adapter<DeveloperViewModel.ViewModel>{
    private final Context context;
    private final LayoutInflater layoutInflater;
    List<DeveloperAdapter> list;

    public DeveloperViewModel(Context c, List<DeveloperAdapter> list) {
        this.context = c;
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public DeveloperViewModel.ViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.developers_child, parent, false);
        return new ViewModel(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeveloperViewModel.ViewModel holder, int position) {
        DeveloperAdapter developerAdapter = list.get(position);
        holder.title.setText(developerAdapter.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        public TextView title;
        public ViewModel(@NonNull final View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, title.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
