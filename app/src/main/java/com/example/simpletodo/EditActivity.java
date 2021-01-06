package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editText;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.editText);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        //Prepopulate with current text
        editText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        btnSave.setOnClickListener(new View.OnClickListener() {

            //Save button clicked when user is done editing, then go back to the list
            @Override
            public void onClick(View v) {
                //Create an intent which will contain the result
                Intent i = new Intent(); //Empty shell to pass data

                //pass the data (results of editing)
                i.putExtra(MainActivity.KEY_ITEM_TEXT , editText.getText().toString());
                i.putExtra(MainActivity.KEY_ITEM_POSITION ,
                        getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //set the result of the intent
                setResult(RESULT_OK , i);

                //finish activity, close the screen and go back
                finish();
            }
        });


    }
}