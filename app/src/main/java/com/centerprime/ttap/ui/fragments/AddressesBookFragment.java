package com.centerprime.ttap.ui.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.adapter.AddressesBookAdapter;
import com.centerprime.ttap.database.AddressesBookDB;
import com.centerprime.ttap.databinding.FragmentAddressesBookBinding;
import com.centerprime.ttap.models.AddressesBookModel;
import com.centerprime.ttap.ui.AddWalletAddressActivity;

import java.util.ArrayList;
import java.util.List;

public class AddressesBookFragment extends Fragment {
    FragmentAddressesBookBinding binding;
    private AddressesBookAdapter adapter;
    private AddressesBookDB addressesBookDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_addresses_book, container, false);
        View view = binding.getRoot();
        addressesBookDB = new AddressesBookDB(getActivity());

        List<AddressesBookModel> list = new ArrayList<>();
        Cursor cursor = addressesBookDB.getData();

        while (cursor.moveToNext()) {
            list.add(new AddressesBookModel(cursor.getString(1), cursor.getString(2)));
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        adapter = new AddressesBookAdapter(list, getActivity());
        binding.recyclerView.setAdapter(adapter);

        binding.addBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddWalletAddressActivity.class));
        });

        return view;
    }
}
