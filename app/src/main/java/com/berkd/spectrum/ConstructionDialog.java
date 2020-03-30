package com.berkd.spectrum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConstructionDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Welcome")
                .setMessage("Spectrum is currently under heavy development, " +
                        "so there will be a lot of bugs and half-baked features.\n\n"+
                        "So far the audio-playback has been implemented and the base UI. The next thing is to output a frequency response in realtime via the microphone.\n\n"+
                        "Stay tuned!\n\n"+
                        "Spectrum v1.0.0")

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });
        return builder.create();
    }
}
