package com.cicenterprise.wellet.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.DialogFingerprintBinding;

public class FingerprintDialog extends FrameLayout {

    private DialogFingerprintBinding binding;
    private ScreenSHotDialog.ClickListener clickListener;
    private Context context;

    public FingerprintDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();

    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_fingerprint, this, true);
        binding.okBtn.setOnClickListener(v -> clickListener.onClickOk());
        binding.noBtn.setOnClickListener(v -> clickListener.onClickNo());

    }

    public void setClickListener(ScreenSHotDialog.ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener {
        default void onClickOk() {

        }
        default void onClickNo() {

        }
    }
}
