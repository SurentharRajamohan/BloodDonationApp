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

public class NewPasswordDialogBox extends AppCompatDialogFragment {

    private EditText password;
    private NewPasswordDialogBoxListener newPasswordDialogListener;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_password_dialog_box, null);
        password = view.findViewById(R.id.NewPassword_ET_Password);


        builder.setView(view)
                .setTitle("New Password")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newPassword =  password.getText().toString();
                        newPasswordDialogListener.setNewPassword(newPassword);
                    }
                });

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            newPasswordDialogListener = (NewPasswordDialogBoxListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "have to implement ExampleDialogListener");
        }
    }

    public interface NewPasswordDialogBoxListener{
        void setNewPassword(String newPassword);
    }





}


