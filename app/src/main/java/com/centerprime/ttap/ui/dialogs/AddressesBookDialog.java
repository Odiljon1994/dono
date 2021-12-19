package com.centerprime.ttap.ui.dialogs;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.adapter.AddressesBookAdapter;
import com.centerprime.ttap.database.AddressesBookDB;
import com.centerprime.ttap.databinding.DialogAddressesBookBinding;
import com.centerprime.ttap.models.AddressesBookModel;

import java.util.ArrayList;
import java.util.List;

public class AddressesBookDialog extends FrameLayout {
    private DialogAddressesBookBinding binding;

    private AddressesBookAdapter adapter;
    private AddressesBookDB addressesBookDB;
    private Context context;
    private ClickListener clickListener;

    public AddressesBookDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_addresses_book, this, true);
        List<AddressesBookModel> list = new ArrayList<>();
        addressesBookDB = new AddressesBookDB(context);
        Cursor cursor = addressesBookDB.getData();

        while (cursor.moveToNext()) {
            list.add(new AddressesBookModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), false));
        }

        if (list.size() == 0) {
            binding.recyclerView.setVisibility(GONE);
            binding.noTransactions.setVisibility(VISIBLE);
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        adapter = new AddressesBookAdapter(list, context);
        binding.recyclerView.setAdapter(adapter);
        AddressesBookAdapter.ClickListener onClicking = new AddressesBookAdapter.ClickListener() {
            @Override
            public void onClick(AddressesBookModel model) {
                clickListener.onClick(model);
            }
        };
        adapter.setClickListener(onClicking);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        default void onClick(AddressesBookModel model) {

        }
    }
}
