package com.berkd.freqs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * This class needs more documentation ...
 */

public class FrequencyResponseFragment extends Fragment {


    // Simply, onCreateView
    // And simply inflate the layout. Very simple. More on it here: https://www.youtube.com/watch?v=bjYstsO1PgI


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_frequency_response, container, false);
    }
}
