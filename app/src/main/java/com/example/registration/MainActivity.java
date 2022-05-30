package com.example.registration;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registration.Adapters.ToDoAdapter;
import com.example.registration.Model.ToDoModel;
import com.example.registration.Utils.DatabaseHandler;
import com.example.registration.Utils.PrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements com.example.registration.DialogCloseListener {

   private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();


        //new UserDetail
       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            UserDetails = extras.getString("UserDetail");
        }*/


        db = new DatabaseHandler();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);

        //TODO
        //PRÜFEN OB VERBINDUNG existiert und dann entweder von SharedPreferencen List holen (PrefManager) oder REST (DatabaseHandler)



        try {
            String uid = new PrefManager(this).getUserID()+"";
            taskList = db.getAllTasks(uid);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Prüfen der Internetverbindung
                //Update der Tasks zu REST
                //Alert Dialog Box für jede Collission mit JA (Überschreiben), NEIN (Neu)


            }
        });

        //Hallo anzeigen
        Context context = getApplicationContext();
        String uName = new PrefManager(context).getUserName();
        CharSequence text = "Hallo "+uName;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        setContentView(R.layout.activity_main);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) throws MalformedURLException {
        String uid = new PrefManager(getApplicationContext()).getUserID()+"";
        taskList = db.getAllTasks(uid);
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}