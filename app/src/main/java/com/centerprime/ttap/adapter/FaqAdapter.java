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

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder>{

    private Context context;
    private List<FaqModel> items;
    private ClickListener clickListener;

    public FaqAdapter(Context context, List<FaqModel> items, ClickListener clickListener) {
        this.context = context;
        this.items = items;
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
        FaqModel item = items.get(position);
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

        void bind(FaqModel model) {
            binding.faq.setText(model.getFaq());

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public interface ClickListener {
        void onClick(FaqModel model);
    }
}
