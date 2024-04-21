package com.mcs.wallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mcs.wallet.models.SearchSectionModel;
import com.mcs.wallet.R;
import com.mcs.wallet.databinding.ItemSearchSectionsBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchSectionsAdapter extends RecyclerView.Adapter<SearchSectionsAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<SearchSectionModel> items;
    private List<SearchSectionModel> allItems;
    private ClickListener clickListener;

    public SearchSectionsAdapter(Context context,List<SearchSectionModel> items, ClickListener clickListener ) {
        this.context = context;
        this.items = items;
        allItems = new ArrayList<>(items);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SearchSectionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchSectionsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_search_sections, parent, false);
        return new SearchSectionsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSectionsAdapter.ViewHolder holder, int position) {
        SearchSectionModel item = items.get(position);
        holder.bind(item);
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
            List<SearchSectionModel> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(allItems);
            } else {
                for (int i = 0; i < allItems.size(); i++) {
                    if (allItems.get(i).getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            items.addAll((Collection<? extends SearchSectionModel>) results.values);
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemSearchSectionsBinding binding;

        public ViewHolder(@NonNull ItemSearchSectionsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(SearchSectionModel model) {
            binding.section.setText(model.getTitle());

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));
        }
    }

    public void setItems(List<SearchSectionModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(SearchSectionModel model);
    }
}
