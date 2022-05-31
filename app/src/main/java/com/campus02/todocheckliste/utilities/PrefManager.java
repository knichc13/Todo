package com.campus02.todocheckliste.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.campus02.todocheckliste.model.ToDoModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PrefManager {

    Context context;

    public PrefManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(String email, String password, int id, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Userid",id+"");
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.putString("Name", name);
        editor.commit();
    }


    public boolean isUserLogedOut() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        boolean isUseridEmpty = sharedPreferences.getString("Userid", "").isEmpty();
        boolean isEmailEmpty = sharedPreferences.getString("Email", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("Password", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    public String getUserName()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Name","");
    }

    public int getUserID()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return Integer.parseInt(sharedPreferences.getString("Userid",""));
    }

    public void saveTasks(ArrayList<ToDoModel> todoList) {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = context.getSharedPreferences("TaskList", Context.MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(todoList);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("Tasks", json);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();
    }

    public ArrayList<ToDoModel> getArrayList(){
        SharedPreferences prefs = context.getSharedPreferences("Taskslist", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("Tasks", null);
        Type type = new TypeToken<ArrayList<ToDoModel>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
