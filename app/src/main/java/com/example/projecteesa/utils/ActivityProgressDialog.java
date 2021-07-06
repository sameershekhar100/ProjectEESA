package com.example.projecteesa.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.projecteesa.R;

public class ActivityProgressDialog {

    private Context mContext;


    private String dialogTitle = "Title";
    private String dialogMessage = "Message";

    private View dialogLayout;
    private TextView titleTextView;
    private TextView messageTextView;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private boolean cancelable;

    public ActivityProgressDialog(Context mContext, String dialogTitle, String dialogMessage, boolean cancelable) {
        this.mContext = mContext;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.cancelable = cancelable;
    }

    public ActivityProgressDialog(Context mContext) {
        this.mContext = mContext;
    }

    public void setTitle(final String title) {
        this.dialogTitle = title;
    }

    public void setMessage(final String message) {
        this.dialogMessage = message;
    }

    public void setCancelable(final boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void showDialog() {

        dialogLayout = LayoutInflater.from(mContext).inflate(R.layout.progress_dialog, null);

        titleTextView = dialogLayout.findViewById(R.id.progressDialogTitleTextView);
        messageTextView = dialogLayout.findViewById(R.id.progressDialogMessageTextView);

        titleTextView.setText(dialogTitle);
        messageTextView.setText(dialogMessage);

        builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogLayout);
        builder.setCancelable(cancelable);
        dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    public void hideDialog() {
        dialog.hide();
        dialog.dismiss();
    }

}
