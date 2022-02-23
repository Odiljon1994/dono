package com.cicenterprise.wellet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ItemAddressesBookBinding;
import com.cicenterprise.wellet.models.AddressesBookModel;

import java.util.ArrayList;
import java.util.List;

public class AddressesBookAdapter extends RecyclerView.Adapter<AddressesBookAdapter.ViewHolder>{

    private List<AddressesBookModel> items;
    private Context context;
    private ClickListener clickListener;

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

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemAddressesBookBinding binding;

        public ViewHolder(@NonNull ItemAddressesBookBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        void bind(AddressesBookModel model) {
            if (model.isChecked()) {
                binding.checkbox.setVisibility(View.VISIBLE);
            }
            binding.walletAddress.setText(model.getWalletAddress());
            binding.name.setText(model.getName());
            binding.getRoot().setOnClickListener(v -> {clickListener.onClick(model);});

            binding.checkbox.setOnClickListener(v -> {
                if (binding.checkbox.isChecked()) {
                    model.setChecked(false);
                } else {
                    model.setChecked(true);
                }
            });
        }
    }

    public List<AddressesBookModel> getCheckedAddresses() {
        List<AddressesBookModel> models = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isChecked()) {
                models.add(items.get(i));
            }
        }
        return  models;
    }

    public interface ClickListener {
        void onClick(AddressesBookModel model);
    }
}
