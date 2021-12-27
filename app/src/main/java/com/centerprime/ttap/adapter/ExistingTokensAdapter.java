package com.centerprime.ttap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ItemExistingTokensBinding;
import com.centerprime.ttap.models.ExistingTokenModel;

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

//    class CustomFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//            if (constraint != null && constraint.length() > 0) {
//                ArrayList<ExistingTokenModel> filters = new ArrayList<>();
//                constraint = constraint.toString().toUpperCase();
//                for (int i = 0; i < allItems.size(); i++) {
//                    if (allItems.get(i).getTokenName().toUpperCase().contains(constraint)) {
//                        ExistingTokenModel model = new ExistingTokenModel(allItems.get(i).getLogo(),
//                                allItems.get(i).getTokenName(),
//                                allItems.get(i).getTokenSymbol(),
//                                allItems.get(i).getContractAddress());
//
//                        filters.add(model);
//
//                    }
//                }
//                results.count = filters.size();
//                results.values = filters;
//
//            } else {
//                results.count = allItems.size();
//                results.values = allItems;
//            }
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            items = (ArrayList<ExistingTokenModel>) results.values;
//            notifyDataSetChanged();
//        }
//    }
}
