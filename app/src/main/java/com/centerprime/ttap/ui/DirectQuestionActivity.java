package com.centerprime.ttap.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityDirectQuestionBinding;
import com.centerprime.ttap.ui.dialogs.DirectQuestionDialog;
import com.centerprime.ttap.ui.dialogs.ScreenSHotDialog;

public class DirectQuestionActivity extends AppCompatActivity {

    private ActivityDirectQuestionBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_question);

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("1:1 문의하기");

        binding.confirmQuestion.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.name.getText().toString()) &&
                    !TextUtils.isEmpty(binding.email.getText().toString()) &&
                    !TextUtils.isEmpty(binding.question.getText().toString())) {

                showDialog();

            } else if (TextUtils.isEmpty(binding.name.getText().toString()) &&
                    TextUtils.isEmpty(binding.email.getText().toString()) &&
                    TextUtils.isEmpty(binding.question.getText().toString())) {
                binding.error.setText("내용을 입력해주세요");
            } else if (TextUtils.isEmpty(binding.name.getText().toString())) {
                binding.error.setText("이름을 입력해주세요");
            } else if (TextUtils.isEmpty(binding.email.getText().toString())) {
                binding.error.setText("이메일을 입력해주세요");
            } else if (TextUtils.isEmpty(binding.question.getText().toString())) {
                binding.error.setText("내용을 입력해주세요");
            }
        });

    }

    public void showDialog() {
        DirectQuestionDialog directQuestionDialog = new DirectQuestionDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(directQuestionDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        DirectQuestionDialog.ClickListener clickListener = new DirectQuestionDialog.ClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(DirectQuestionActivity.this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        };
        directQuestionDialog.setClickListener(clickListener);
    }
}
