package com.campus02.todocheckliste.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.campus02.todocheckliste.adapters.ToDoAdapter;
import com.campus02.todocheckliste.model.ToDoModel;
import com.campus02.todocheckliste.utilities.DatabaseHandler;
import com.campus02.todocheckliste.utilities.NetworkUtil;
import com.campus02.todocheckliste.utilities.PrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.registration.R;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements com.campus02.todocheckliste.activities.DialogCloseListener {

    private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private FloatingActionButton fab2;
    private List<ToDoModel> taskList;
    private String uid;



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
        //DONE

        NetworkUtil neut = new NetworkUtil();
        if(neut.getConnectivityStatusString(getApplicationContext()))
        {
            try {
                uid = new PrefManager(this).getUserID()+"";
                taskList = db.getAllTasks(uid);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Collections.reverse(taskList);

            tasksAdapter.setTasks(taskList);
            saveTasks((ArrayList<ToDoModel>) taskList);
        }
        else
        {
            //Data von ProfManager
             taskList = new PrefManager(this).getArrayList();
            Collections.reverse(taskList);
            tasksAdapter.setTasks(taskList);
            tasksAdapter.notifyDataSetChanged();
        }


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
                //DONE
                NetworkUtil neut = new NetworkUtil();
                if(neut.getConnectivityStatusString(getApplicationContext())) {
                    List<ToDoModel> test = tasksAdapter.getList();
                    ArrayList<ToDoModel> colList = db.updateTasklist(test);

                    for (ToDoModel tdm : colList) {
                       int id = tdm.getId();
                       //TODO ALert

                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                        builder.setTitle("Konflikt bei Task "+tdm.getTask());
                        builder.setMessage(tdm.getTaskDescription()+"\n" +
                                "Möchten Sie die Daten überschreiben?");

                        builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                //Daten überschreiben
                                for (ToDoModel task : taskList) {
                                if(tdm.getId() == task.getId()){
                                    int i= taskList.indexOf(task);
                                    taskList.set(i,tdm);
                                }
                                }
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NEIN, neuen Task erstellen", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                tdm.setLastModifiedID(Integer.parseInt(uid));
                                taskList.add(tdm);
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    Collections.reverse(taskList);
                    tasksAdapter.setTasks(taskList);
                    tasksAdapter.notifyDataSetChanged();
                    saveTasks((ArrayList<ToDoModel>) taskList);
                }
                else
                {
                    pushMessage("Keine Aktualisierung möglich. Error: Internetverbindung");
                }
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

    private void pushMessage(String text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void saveTasks(ArrayList<ToDoModel> list) {
        new PrefManager(this).saveTasks(list);
    }

}
