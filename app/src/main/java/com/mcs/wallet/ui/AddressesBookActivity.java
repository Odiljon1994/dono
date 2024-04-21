package com.mcs.wallet.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mcs.wallet.R;
import com.mcs.wallet.adapter.AddressesBookAdapter;
import com.mcs.wallet.database.AddressesBookDB;
import com.mcs.wallet.databinding.FragmentAddressesBookBinding;
import com.mcs.wallet.models.AddressesBookModel;
import com.mcs.wallet.ui.dialogs.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class AddressesBookActivity extends AppCompatActivity {
    private FragmentAddressesBookBinding binding;
    public static AddressesBookAdapter adapter;
    private static AddressesBookDB addressesBookDB;
    public static RecyclerView recyclerView;
    List<AddressesBookModel> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_addresses_book);

        addressesBookDB = new AddressesBookDB(this);
        recyclerView = binding.recyclerView;
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

        list = getDataFromDB();
        if (list.size() == 0) {
            binding.noData.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            if (list.size() == 0) {
                binding.noData.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(AddressesBookActivity.this, RecyclerView.VERTICAL, false));
        adapter = new AddressesBookAdapter(list, AddressesBookActivity.this);
        recyclerView.setAdapter(adapter);
        AddressesBookAdapter.ClickListener clickListener = new AddressesBookAdapter.ClickListener() {
            @Override
            public void onClick(AddressesBookModel model) {

            }
        };
        adapter.setClickListener(clickListener);

        binding.selectAddresses.setOnClickListener(v -> {
            if (binding.selectAddresses.getText().toString().equals(R.string.select)) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setChecked(true);
                }
                adapter.notifyDataSetChanged();

                if (list.size() > 0) {
                    binding.addBtn.setVisibility(View.GONE);
                    binding.deleteBtn.setVisibility(View.VISIBLE);
                    //  binding.selectAddresses.setText("전체선택");
                }
            }

        });

        binding.addBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, AddWalletAddressActivity.class));
        });

        binding.deleteBtn.setOnClickListener(v -> {
            showDialog();
        });
    }

    public static List<AddressesBookModel> getDataFromDB() {
        List<AddressesBookModel> list = new ArrayList<>();
        Cursor cursor = addressesBookDB.getData();

        while (cursor.moveToNext()) {
            list.add(new AddressesBookModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2), false));
        }

        return list;
    }


    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(AddressesBookActivity.this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddressesBookActivity.this);
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        baseDialog.changeText(getResources().getString(R.string.really_want_delete));

        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClick() {
                List<AddressesBookModel> modelsToDelete = adapter.getCheckedAddresses();
                for (int i = 0; i < modelsToDelete.size(); i++) {
                    addressesBookDB.deleteRow(modelsToDelete.get(i));
                    System.out.println(modelsToDelete.get(i).getID());
                    System.out.println(modelsToDelete.get(i).getName());
                    System.out.println(modelsToDelete.get(i).getWalletAddress());
                }
                list = getDataFromDB();

                if (list.size() == 0) {
                    binding.noData.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                } else {
                    if (list.size() == 0) {
                        binding.noData.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                binding.deleteBtn.setVisibility(View.GONE);
                binding.addBtn.setVisibility(View.VISIBLE);
                adapter = new AddressesBookAdapter(list, AddressesBookActivity.this);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();


            }
        };
        baseDialog.setClickListener(clickListener);
    }
}
