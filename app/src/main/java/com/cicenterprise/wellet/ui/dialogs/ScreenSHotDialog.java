package com.cicenterprise.wellet.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.DialogScreenshotBinding;

public class ScreenSHotDialog extends FrameLayout {

    private DialogScreenshotBinding binding;
    private ClickListener clickListener;
    private Context context;

    public ScreenSHotDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();

    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_screenshot, this, true);
        binding.okBtn.setOnClickListener(v -> clickListener.onClickOk());
        binding.noBtn.setOnClickListener(v -> clickListener.onClickNo());

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void confirmScreenShot() {
        binding.noBtn.setVisibility(GONE);
        binding.message.setVisibility(GONE);
        binding.linearLayout.setVisibility(GONE);
        binding.result.setText("QR코드가 갤러리에\n저장되었습니다.");
    }

    public interface ClickListener {
        default void onClickOk() {

        }
        default void onClickNo() {

        }
    }

}
