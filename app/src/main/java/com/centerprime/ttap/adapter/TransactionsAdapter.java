package com.centerprime.ttap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ItemEthTransactionBinding;
import com.centerprime.ttap.models.Transaction;
import com.centerprime.ttap.web3.BalanceUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder>{
    private List<Transaction> items;
    private Context context;
    private EmptyViewListener emptyViewListener;
    String walletAddress;

    public TransactionsAdapter(String walletAddress, Context context, EmptyViewListener emptyViewListener) {
        this.items = new ArrayList<>();
        this.context = context;
        this.emptyViewListener = emptyViewListener;
        this.walletAddress = walletAddress;
    }

    public void clearItems() {
        if(items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEthTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_eth_transaction, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction item = items.get(position);
        holder.bind(item);
    }
    public void setItems(List<Transaction> items) {
//        if (this.items != null && this.items.size() > 0)  {
//            this.items.clear();
//        }
        this.items = items;

        if (emptyViewListener != null) {
//            if (items.size() > 0) {
//                emptyViewListener.showItems(false);
//            } else if(items.size() == 0){
//                emptyViewListener.showItems(true);
//            }

            emptyViewListener.showItems(this.items.isEmpty());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemEthTransactionBinding binding;
        public ViewHolder(@NonNull ItemEthTransactionBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
        void bind(Transaction transaction) {
            BigDecimal amountBigDecimal = BalanceUtils.weiToEth(new BigDecimal(transaction.getValue()));

            Timestamp ts = new Timestamp(transaction.getTimeStamp()*1000);
            DateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            Date dateobj =new Date(ts.getTime());
            String dateToString = df.format(dateobj);


            String amount = amountBigDecimal.toString();
            if (amount.toString().length() > 5){
                amount = amount.substring(0, 5);
            }
            if(walletAddress.toLowerCase().equals(transaction.getFrom().toLowerCase()) && transaction.getIsError().equals("0")) {
                binding.amont.setTextColor(Color.parseColor("#43B0DD"));
            } else if (!walletAddress.toLowerCase().equals(transaction.getFrom().toLowerCase()) && transaction.getIsError().equals("0")){
                binding.amont.setTextColor(Color.parseColor("#DD4343"));
            } else {
                binding.amont.setTextColor(Color.RED);
            }

            binding.date.setText(dateToString);
            binding.amont.setText(amount.toString());
            binding.txId.setText(transaction.getHash());

            binding.getRoot().setOnClickListener(v -> emptyViewListener.onClick(transaction));

//            binding.date.setText(transaction.getDate());
//            binding.amont.setText(transaction.getAmount());
//            binding.txId.setText(transaction.getTxID());
        }
    }
    public interface EmptyViewListener {
        void showItems(boolean isEmpty);
        void onClick(Transaction transaction);
    }

}
