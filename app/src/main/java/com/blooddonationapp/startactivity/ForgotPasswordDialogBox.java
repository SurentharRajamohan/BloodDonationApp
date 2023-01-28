package com.blooddonationapp.startactivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ForgotPasswordDialogBox extends AppCompatDialogFragment {

    private EditText username;
    private ForgotPasswordDialogListener forgotPasswordDialogListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_forgot_password_dialog, null);
        username = view.findViewById(R.id.ForgotPassword_ET_Username);
        builder.setView(view)
                .setTitle("Forgot Password")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Send Verification Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String usernameValue = username.getText().toString();
                        forgotPasswordDialogListener.sendTacCode(usernameValue);
                    }
                });

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            forgotPasswordDialogListener = (ForgotPasswordDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "have to implement ExampleDialogListener");
        }
    }

    public interface ForgotPasswordDialogListener {
        void sendTacCode(String username);
    }


}


