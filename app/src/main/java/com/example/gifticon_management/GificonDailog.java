package com.example.gifticon_management;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

public class GificonDailog extends Dialog {
    public GificonDailog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.gifticon_detail_dialog);
    }
}
