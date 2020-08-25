package com.example.personaldictonary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton addButton;
    Context context;
    DataBaseHelper dataBaseHelper;
    List<Notes>dataList;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;

        recyclerView=findViewById(R.id.recyclerViewId);
        dataBaseHelper=new DataBaseHelper(MainActivity.this);
        dataBaseHelper.getWritableDatabase();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        loadData();

        addButton=findViewById(R.id.addButtonId);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog();
            }
        });

    }
    private void loadData() {
        dataList  = new ArrayList<>();
        dataList = dataBaseHelper.getAllNotes();
        if (dataList.size() > 0){
            customAdapter = new CustomAdapter(context,dataList);
            recyclerView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void CustomDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.input_box,null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        Button saveButton=view.findViewById(R.id.saveButtonId);
        final EditText englishWordEditText =view.findViewById(R.id.englishEditTextId);
        final EditText banglaWordEditText =view.findViewById(R.id.banglaEditTextId);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String englishWord=englishWordEditText.getText().toString();
                String banglaWord=banglaWordEditText.getText().toString();

                if (englishWord.isEmpty()){
                    englishWordEditText.setError("Enter Value");
                    return;
                }
                else if (banglaWord.isEmpty()){
                    banglaWordEditText.setError("Enter Value");
                    return;
                }

                long id=dataBaseHelper.insertData(new Notes(englishWord,banglaWord));

                if (id != -1){
                    alertDialog.dismiss();
                    loadData();
                    Toast.makeText(context, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();
                    Toast.makeText(context, "Failed to Insert", Toast.LENGTH_SHORT).show();
                }


            }
        });

        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        //set Search View Action
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }
}