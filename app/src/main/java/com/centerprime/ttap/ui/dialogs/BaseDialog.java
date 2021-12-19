package com.centerprime.ttap.ui.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.DialogBaseBinding;

public class BaseDialog extends FrameLayout {

    private DialogBaseBinding binding;
    private ClickListener clickListener;
    private Context context;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_base, this, true);
        binding.okBtn.setOnClickListener(v -> clickListener.onClick());

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void changeText(String text) {

        binding.image.setImageDrawable(context.getDrawable(R.drawable.wallet_checked));
        binding.message.setText(text);
    }



    public interface ClickListener {
        default void onClick() {

        }
    }
}
