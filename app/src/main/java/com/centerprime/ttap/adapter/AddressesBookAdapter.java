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
import com.centerprime.ttap.models.FaqModel;
import com.centerprime.ttap.ui.dialogs.ScreenSHotDialog;

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
            binding.walletAddress.setText(model.getWalletAddress());
            binding.name.setText(model.getName());
            binding.getRoot().setOnClickListener(v -> {clickListener.onClick(model);});
        }
    }

    public interface ClickListener {
        void onClick(AddressesBookModel model);
    }
}
