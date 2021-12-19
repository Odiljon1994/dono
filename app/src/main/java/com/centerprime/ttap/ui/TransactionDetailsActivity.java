package com.centerprime.ttap.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityTransactionDetailsBinding;
import com.centerprime.ttap.web3.BalanceUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionDetailsActivity extends AppCompatActivity {
    private ActivityTransactionDetailsBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_details);


        Long timestamp = getIntent().getLongExtra("timeStamp", 0);
        String hash = getIntent().getStringExtra("hash");
        String amount = getIntent().getStringExtra("value");
        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        String blockNumber = getIntent().getStringExtra("blockNumber");
        String blockHash = getIntent().getStringExtra("blockHash");
        String isError = getIntent().getStringExtra("isError");

        Timestamp ts = new Timestamp(timestamp*1000);
        DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm");
        Date dateobj =new Date(ts.getTime());
        String dateToString = df.format(dateobj);

        BigDecimal amountBigDecimal = BalanceUtils.weiToEth(new BigDecimal(amount));

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("거래 내역 상세");


        binding.date.setText(dateToString);
        binding.transactionHash.setText(hash);
        binding.value.setText(amountBigDecimal.toString());
        binding.fromAddress.setText(from);
        binding.toAddress.setText(to);
        binding.block.setText(blockNumber);
        if (TextUtils.isEmpty(isError)) {
            binding.status.setText("Unknown");
        } else if (isError.equals("0")) {
            binding.status.setText("Success");
        } else {
            binding.status.setText("Failed");
        }
        binding.copyBtn.setOnClickListener(v -> {
            copyText(binding.transactionHash.getText().toString());
        });
        binding.copyFromAddress.setOnClickListener(v -> {
            copyText(binding.fromAddress.getText().toString());
        });
        binding.copyToAddress.setOnClickListener(v -> {
            copyText(binding.toAddress.getText().toString());
        });
    }

    public void copyText(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
    }
}
