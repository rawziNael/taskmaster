package com.example.taskmaster.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.taskmaster.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.amplifyframework.datastore.generated.model.Task;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private final List<Task> dataList;
    private OnItemClickListener listener;

    public RecyclerViewAdapter(List<Task> foodItems, OnItemClickListener listener) {
        this.dataList = foodItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_layout,
                parent,
                false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.title.setText(dataList.get(position).getTitle());
        holder.body.setText(dataList.get(position).getBody());
        holder.status.setText(dataList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView body;
        private TextView status;

        ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.title_label);
            body = itemView.findViewById(R.id.body_label);
            status = itemView.findViewById(R.id.status_label);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
}
