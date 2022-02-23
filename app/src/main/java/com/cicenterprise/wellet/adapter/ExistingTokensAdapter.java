package com.cicenterprise.wellet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ItemExistingTokensBinding;
import com.cicenterprise.wellet.models.ExistingTokenModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExistingTokensAdapter extends RecyclerView.Adapter<ExistingTokensAdapter.ViewHolder> implements Filterable {

    List<ExistingTokenModel> items;
    Context context;
    ClickListener clickListener;
    //  private CustomFilter mFilter;
    List<ExistingTokenModel> allItems;


    public ExistingTokensAdapter(List<ExistingTokenModel> items, Context context, ClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
        this.allItems = new ArrayList<>(items);
        //  mFilter = new CustomFilter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExistingTokensBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_existing_tokens, parent, false);
        return new ExistingTokensAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExistingTokenModel existingTokenModel = items.get(position);
        holder.bind(existingTokenModel);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExistingTokenModel> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(allItems);
            } else {
                for (int i = 0; i < allItems.size(); i++) {
                    if (allItems.get(i).getTokenName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(allItems.get(i));
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((Collection<? extends ExistingTokenModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemExistingTokensBinding binding;

        public ViewHolder(@NonNull ItemExistingTokensBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(ExistingTokenModel model) {

            binding.logo.setImageDrawable(model.getLogo());
            binding.tokenName.setText(model.getTokenName());
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

//            binding.checkButton.setOnClickListener(v -> {
//             //   binding.checkButton.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_token));
//                clickListener.onClick();
//            });
        }
    }

    public interface ClickListener {
        void onClick(ExistingTokenModel tokenModel);
    }

}
