package com.berkd.spectrum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class HelpDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Help")
                .setMessage("FAQ\n\n" +
                        "What is Spectrum?\n\n" +
                        "Spectrum is a room frequency response app which is aimed at audio " +
                        "enthusiasts and people who want to step up their audio experience." +
                        "The purpose of this app is to use the built-in microphone in your smartphone " +
                        "to record the sound coming from external speakers and display the frequency response.\n\n" +

                        "What should I be looking for?\n\n" +
                        "According to audio and acoustic engineers, producers and musicians should look for a relatively flat " +
                        "frequency response when measuring the acoustics of their room. Of course this only matters if you're using " +
                        "speakers.")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });
        return builder.create();
    }
}
