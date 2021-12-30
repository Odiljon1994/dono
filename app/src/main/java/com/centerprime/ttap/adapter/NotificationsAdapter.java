package com.centerprime.ttap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ItemNotificationBinding;
import com.centerprime.ttap.models.NotificationModel;
import com.centerprime.ttap.models.NotificationsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{
    private ClickListener clickListener;

    private List<NotificationModel.Data> items;
    private Context context;

    public NotificationsAdapter(Context context, ClickListener clickListener) {
        this.clickListener = clickListener;
        this.items = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_notification, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationModel.Data item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setItems(List<NotificationModel.Data> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemNotificationBinding binding;

        public ViewHolder(@NonNull ItemNotificationBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(NotificationModel.Data model) {

            String[] pairs = model.getCreated_at().split("T");

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
            binding.date.setText(pairs[0] + " " + pairs[1].substring(0, 5));
           // binding.date.setText(model.getCreated_at());
            if (model.getType().equals("1")) {
                binding.title.setText("[공지] " + model.getName());
            } else if (model.getType().equals("2")) {
                binding.title.setText("[이벤트] " + model.getName());
            }

        }
    }

    public interface ClickListener {
        void onClick(NotificationModel.Data model);
    }
}
