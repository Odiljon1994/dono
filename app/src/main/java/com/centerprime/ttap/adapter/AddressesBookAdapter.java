package com.centerprime.ttap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ItemAddressesBookBinding;
import com.centerprime.ttap.models.AddressesBookModel;

import java.util.List;

public class AddressesBookAdapter extends RecyclerView.Adapter<AddressesBookAdapter.ViewHolder>{

    private List<AddressesBookModel> items;
    private Context context;

    public AddressesBookAdapter(List<AddressesBookModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressesBookBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_addresses_book, parent, false);
        return new AddressesBookAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressesBookModel item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemAddressesBookBinding binding;

        public ViewHolder(@NonNull ItemAddressesBookBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(AddressesBookModel model) {
            binding.walletAddress.setText(model.getWalletAddress());
            binding.name.setText(model.getName());
        }
    }
}
