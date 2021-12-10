package com.centerprime.ttap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ItemAddressesBookBinding;
import com.centerprime.ttap.databinding.ItemNotificationBinding;
import com.centerprime.ttap.models.AddressesBookModel;
import com.centerprime.ttap.models.NotificationsModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    private List<NotificationsModel> items;
    private Context context;
    private ClickListener clickListener;

    public NotificationAdapter(List<NotificationsModel> items, Context context, ClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationsModel item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemNotificationBinding binding;

        public ViewHolder(@NonNull ItemNotificationBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(NotificationsModel model) {
            binding.title.setText(model.getTitle());
            binding.date.setText(model.getDate());
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public interface ClickListener {
        void onClick(NotificationsModel model);
    }
}
