package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;



public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText editTextItem;
    RecyclerView recycleViewItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadItems();

        btnAdd = findViewById(R.id.btnAdd);
        editTextItem = findViewById(R.id.editTextItem);
        recycleViewItems = findViewById(R.id.recycleViewItems);

        //for removing
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the model
                items.remove(position);

                //Notify the adapter the position where we delete
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Open edit activity. Create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                //Pass relevant data being edited
                i.putExtra(KEY_ITEM_TEXT , items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                //Display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        recycleViewItems.setAdapter(itemsAdapter);
        recycleViewItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toDoItem = editTextItem.getText().toString();
                //Add item to the model
                items.add(toDoItem);

                //Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                editTextItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        });
    }

    //Handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check if request code match with request code passed in
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {

            //Retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //Extract original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model with the new item
            items.set(position, itemText);
            //notify the adapter that something has changed
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItems();

            Toast.makeText(getApplicationContext(), "Item updated sucessfully", Toast.LENGTH_SHORT);
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir() , "data.txt");
    }

    //This function will load items by reading line of data file
    private void loadItems(){
        try{
            items = new ArrayList<>(FileUtils.readLines (getDataFile() , Charset.defaultCharset()));
        }catch(IOException e){
            Log.e("MainActivity" , "Error reading items" , e);
            items = new ArrayList<>();
        }
    }

    //This function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile() , items);
        } catch (IOException e) {
            Log.e("MainActivity" , "Error writing items" , e);
        }
    }
}