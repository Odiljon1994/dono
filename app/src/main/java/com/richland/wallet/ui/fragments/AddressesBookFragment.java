package com.richland.wallet.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.richland.wallet.R;
import com.richland.wallet.adapter.AddressesBookAdapter;
import com.richland.wallet.database.AddressesBookDB;
import com.richland.wallet.databinding.FragmentAddressesBookBinding;
import com.richland.wallet.models.AddressesBookModel;
import com.richland.wallet.ui.AddWalletAddressActivity;
import com.richland.wallet.ui.MainActivity;
import com.richland.wallet.ui.dialogs.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class AddressesBookFragment extends Fragment {
    FragmentAddressesBookBinding binding;
    public static AddressesBookAdapter adapter;
    private static AddressesBookDB addressesBookDB;
    public static RecyclerView recyclerView;
    List<AddressesBookModel> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_addresses_book, container, false);
        View view = binding.getRoot();
        addressesBookDB = new AddressesBookDB(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);

        binding.backBtn.setOnClickListener(v -> {
            Fragment someFragment = new MainFragment3();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, someFragment); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            MainActivity.appBarLayout.setVisibility(View.VISIBLE);
            transaction.commit();
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        adapter = new AddressesBookAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        AddressesBookAdapter.ClickListener clickListener = new AddressesBookAdapter.ClickListener() {
            @Override
            public void onClick(AddressesBookModel model) {

            }
        };
        adapter.setClickListener(clickListener);

        binding.selectAddresses.setOnClickListener(v -> {
            if (binding.selectAddresses.getText().toString().equals("선택")) {
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
            startActivity(new Intent(getActivity(), AddWalletAddressActivity.class));
        });

        binding.deleteBtn.setOnClickListener(v -> {
            showDialog();
//            List<AddressesBookModel> modelsToDelete = adapter.getCheckedAddresses();
//            for (int i = 0; i < modelsToDelete.size(); i++) {
//                addressesBookDB.deleteRow(modelsToDelete.get(i));
//                System.out.println(modelsToDelete.get(i).getID());
//                System.out.println(modelsToDelete.get(i).getName());
//                System.out.println(modelsToDelete.get(i).getWalletAddress());
//            }
//            list = getDataFromDB();
//            adapter = new AddressesBookAdapter(list, getActivity());
//            recyclerView.setAdapter(adapter);

        });

        return view;
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
        BaseDialog baseDialog = new BaseDialog(getActivity());
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        baseDialog.changeText("정말 삭제 하시겠습니까?");

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
                adapter = new AddressesBookAdapter(list, getActivity());
                recyclerView.setAdapter(adapter);
                dialog.dismiss();


            }
        };
        baseDialog.setClickListener(clickListener);
    }
}
