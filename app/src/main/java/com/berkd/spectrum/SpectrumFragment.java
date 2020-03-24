package com.berkd.spectrum;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


/**
 * This class needs more documentation ...
 */

public class SpectrumFragment extends Fragment {


    // Simply, onCreateView
    // And simply inflate the layout. Very simple. More on it here: https://www.youtube.com/watch?v=bjYstsO1PgI


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_spectrum, container, false);


    }




}
