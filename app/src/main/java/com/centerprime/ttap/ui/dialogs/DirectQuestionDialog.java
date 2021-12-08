package com.centerprime.ttap.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.DialogDirectQuestionBinding;

public class DirectQuestionDialog extends FrameLayout {
    private DialogDirectQuestionBinding binding;
    private ClickListener clickListener;
    private Context context;

    public DirectQuestionDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_direct_question, this, true);
        binding.okBtn.setOnClickListener(v -> clickListener.onClick());

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface ClickListener {
        default void onClick() {

        }
    }
}
