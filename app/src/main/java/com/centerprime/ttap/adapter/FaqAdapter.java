package com.centerprime.ttap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ItemFaqBinding;
import com.centerprime.ttap.models.FaqModel;
import com.centerprime.ttap.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder>{

    private Context context;
    private List<FaqModel.FaqData> items;
    private ClickListener clickListener;

    public FaqAdapter(Context context, ClickListener clickListener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_faq, parent, false);
        return new FaqAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FaqModel.FaqData item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemFaqBinding binding;

        public ViewHolder(@NonNull ItemFaqBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(FaqModel.FaqData model) {
            binding.faqTitle.setText(model.getTitle());

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public void setItems(List<FaqModel.FaqData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(FaqModel.FaqData model);
    }
}
