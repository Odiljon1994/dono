package com.richland.wallet.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.richland.wallet.R;
import com.richland.wallet.databinding.DialogSeedBackupBinding;

public class SeedBackupDialog extends FrameLayout {
    private ClickListener clickListener;
    private Context context;
    private DialogSeedBackupBinding binding;
    public SeedBackupDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_seed_backup, this, true);
        binding.okBtn.setOnClickListener(v -> clickListener.onClickOk());
        binding.noBtn.setOnClickListener(v -> clickListener.onClickNo());

    }
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        default void onClickOk() {

        }
        default void onClickNo() {

        }
    }
}
