package com.berkd.spectrum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void aboutPopup() {
        AboutPopup aboutPopup = new AboutPopup();
        aboutPopup.show(getSupportFragmentManager(), "popup dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }


    /**
     * WHEN OPTIONS MENU IS SELECT, CURRENTLY DOES NOT MUCH BUT WIP
     * TODO -> Get these functions things implemented (CURRENTLY WIP)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            // item1
            case R.id.item1:
                aboutPopup();
                break;

            // item3
            case R.id.item2:
                aboutPopup();
                break;

            case R.id.item3:
                aboutPopup();
                break;

            case R.id.item4:
                aboutPopup();
                break;

            case R.id.item5:
                aboutPopup();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
