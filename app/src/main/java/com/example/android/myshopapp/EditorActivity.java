package com.example.android.myshopapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class EditorActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_editor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
}
