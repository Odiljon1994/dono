package com.centerprime.ttap.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityDirectQuestionBinding;
import com.centerprime.ttap.di.ViewModelFactory;
import com.centerprime.ttap.models.CurrentFeeModel;
import com.centerprime.ttap.models.DirectQuestionResModel;
import com.centerprime.ttap.ui.dialogs.DirectQuestionDialog;
import com.centerprime.ttap.ui.dialogs.ScreenSHotDialog;
import com.centerprime.ttap.ui.viewmodel.CurrentFeeVM;
import com.centerprime.ttap.ui.viewmodel.DirectQuestionVM;

import javax.inject.Inject;

public class DirectQuestionActivity extends AppCompatActivity {

    private ActivityDirectQuestionBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    private DirectQuestionVM directQuestionVM;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_question);
        directQuestionVM = ViewModelProviders.of(this, viewModelFactory).get(DirectQuestionVM.class);
        directQuestionVM.item().observe(this, this::onResponse);
        directQuestionVM.errorMessage().observe(this, this::onError);

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("1:1 문의하기");

        binding.confirmQuestion.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.name.getText().toString()) &&
                    !TextUtils.isEmpty(binding.email.getText().toString()) &&
                    !TextUtils.isEmpty(binding.question.getText().toString())) {

                progressDialog = ProgressDialog.show(DirectQuestionActivity.this, "Loading", "", true);
                directQuestionVM.postQuestion(binding.email.getText().toString(),
                        binding.name.getText().toString(),
                        binding.question.getText().toString());
//                showDialog();

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

    public void onResponse(DirectQuestionResModel directQuestionResModel) {

        System.out.println("*********");
        System.out.println(directQuestionResModel.getStatus());
        System.out.println("*********");
        progressDialog.dismiss();
        if (directQuestionResModel.getStatus() == 200) {
            showDialog();
        } else {
            binding.error.setText(directQuestionResModel.getMessage());
        }
    }
    public void onError(String error) {
        progressDialog.dismiss();
        binding.error.setText(error);
    }

}
