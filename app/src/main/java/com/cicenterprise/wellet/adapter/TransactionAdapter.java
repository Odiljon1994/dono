package com.cicenterprise.wellet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ItemTransactionsBinding;
import com.cicenterprise.wellet.models.TransactionsModel;
import com.cicenterprise.wellet.web3.BalanceUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{

    String walletAddress;
    private Context context;
    private List<TransactionsModel> items;
    private ClickListener clickListener;

    public TransactionAdapter(String walletAddress, Context context,  ClickListener clickListener) {
        this.walletAddress = walletAddress;
        this.context = context;
        this.items = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_transactions, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionsModel item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clearItems() {
        if (items != null) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public void setItems(List<TransactionsModel> items) {

        this.items = items;

        Collections.sort(items);
        Collections.sort(items, Collections.reverseOrder());
        if (clickListener != null) {

            clickListener.showItems(this.items.isEmpty());
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemTransactionsBinding binding;

        public ViewHolder(@NonNull ItemTransactionsBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

        }

        void bind(TransactionsModel model) {
            BigDecimal amountBigDecimal = BalanceUtils.weiToEth(new BigDecimal(model.getValue()));
            BigInteger tokenDecimal;
            BigInteger tokenValue;

            String amount = amountBigDecimal.toString();
            if (amount.toString().length() > 8) {
                amount = amount.substring(0, 8);
            }

            if (model.getTokenDecimal() != null) {
                tokenDecimal = new BigInteger(model.getTokenDecimal());
                tokenValue = new BigInteger(model.getValue());
                balanceByDecimal(tokenValue, tokenDecimal);
                BigDecimal bigDecimal = balanceByDecimal(tokenValue, tokenDecimal);
                binding.amount.setText(balanceByDecimal(tokenValue, tokenDecimal).toString());
                binding.symbol.setText(model.getType());
            } else if(model.getTokenDecimal() == null){
                binding.amount.setText(amount);
                binding.symbol.setText(model.getType());
            }



            Timestamp ts = new Timestamp(model.getTimeStamp() * 1000);
            DateFormat df = new SimpleDateFormat("yyyyMMdd / HH:mm");
            Date dateobj = new Date(ts.getTime());
            String dateToString = df.format(dateobj);

            //    binding.amount.setText(amount + " " + model.getType());
            binding.date.setText(dateToString);
            binding.getRoot().setOnClickListener(v -> clickListener.onClick(model));

            if(walletAddress.equalsIgnoreCase(model.getFrom())) {
                binding.type.setTextColor(Color.parseColor("#DD4343"));
                binding.type.setText("출금");
            } else if (!walletAddress.equalsIgnoreCase(model.getFrom())){
                binding.type.setTextColor(Color.parseColor("#43B0DD"));
                binding.type.setText("입금");
            }
        }
    }


    public interface ClickListener {
        default void onClick(TransactionsModel item) {

        }

        default void showItems(boolean isEmpty) {

        }
    }

    public static BigDecimal balanceByDecimal(BigInteger balance, BigInteger decimals) {
        BigDecimal tokenDecimals = new BigDecimal(Math.pow(10, decimals.intValue()));
        BigDecimal convertBalance = new BigDecimal(balance);
        return convertBalance.divide(tokenDecimals);
    }
}
