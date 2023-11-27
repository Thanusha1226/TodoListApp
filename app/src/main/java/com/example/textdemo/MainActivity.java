package com.example.textdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.textdemo.Adapter.toDoAdapter;
import com.example.textdemo.Model.toDoModel;
import com.example.textdemo.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements DialogCloseListner {
    private RecyclerView tasksRecyclerView;
    private toDoAdapter taskAdapter;
    private FloatingActionButton fab;
    private DatabaseHandler db;

    private List<toDoModel> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        db = new DatabaseHandler(this);
        db.openDatabase();
        //taskList = new ArrayList<>();


        tasksRecyclerView = findViewById(R.id.taskRecycle);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new toDoAdapter(db,MainActivity.this);
        tasksRecyclerView.setAdapter(taskAdapter);





        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);
        taskList = db.getAllTask();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTAsk.newInstant().show(getSupportFragmentManager(), AddNewTAsk.TAG);
            }
        });

    }
    @Override
    public void handlerDialogClose(DialogInterface dialog){
        taskList = db.getAllTask();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);
        taskAdapter.notifyDataSetChanged();

    }
    

}