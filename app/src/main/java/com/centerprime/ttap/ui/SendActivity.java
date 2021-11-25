package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivitySendBinding;

import jnr.ffi.annotations.In;

public class SendActivity extends AppCompatActivity {
    ActivitySendBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.endImage.setImageDrawable(getDrawable(R.drawable.share_icon));

        binding.nextBtn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.receiverAddress.getText().toString())
                    && !TextUtils.isEmpty(binding.amount.getText().toString())) {

                Intent intent = new Intent(SendActivity.this, VerifySendingActivity.class);
                intent.putExtra("receiverAddress", binding.receiverAddress.getText().toString());
                intent.putExtra("amount", binding.amount.getText().toString());
                startActivity(intent);

            } else if (TextUtils.isEmpty(binding.receiverAddress.getText().toString())) {
                binding.errorMessage.setText("Please enter receiver address");
            } else if (TextUtils.isEmpty(binding.amount.getText().toString())) {
                binding.errorMessage.setText("Please enter token amount");
            }
        });

    }
}
