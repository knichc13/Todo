package com.campus02.todocheckliste.utilities;

import com.campus02.todocheckliste.model.ToDoModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    public DatabaseHandler() {

    }
    private static final String NAME = "toDoListDatabase";
    private static final int VERSION = 1;
    private String urlbase = "https://localhost:8080";

   /*
    //ID, title, completed, (status), public, timestamp, organisatorUserID, LastModifiedUser; LastModi)

    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;*/

   /*public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }*/

    //TODO
    //Completed mit True/False senden und NICHT MIT 0/1!!!

    public List<ToDoModel> getAllTasks(String userid) throws MalformedURLException {
        //TODO
        //Serverseitig userid bei Tasks einfügen

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        List<ToDoModel> taskList = new ArrayList<ToDoModel>();

        Boolean prv = true;

        try {

            URL requestUrl = new URL(urlbase+"/tasks/{"+userid+"}");
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.connect(); // no connection is made
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            ToDoModel task = new ToDoModel();
            while ((line = reader.readLine()) != null) {
                //   result.append(line);
                if(!line.contains("{") && !line.contains("}"))
                {
                    String lineStrings[] = line.split(":");
                    String typ = lineStrings[0];
                    typ.substring(1,typ.length()-1);
                    String wert = lineStrings[1].substring(1,lineStrings[1].length()-2);


                    //TODO
                    //Checken wie es zurückkommt (True groß oder klein?)

                    switch (typ)
                    {
                        case "id": task.setId(new Integer(wert)); break;
                        case "title": task.setTask(wert); break;
                        case "desc": task.setTaskDescription(wert); break;
                        case "completed": if(wert == "true")
                        {
                            task.setStatus(new Integer(1));
                        }
                        else
                        {
                            task.setStatus(new Integer(0));
                        }; break;
                        case "public": if(wert == "true")
                        {
                            task.setIsPublic(new Integer(1));
                        }
                        else
                        {
                            task.setIsPublic(new Integer(0));
                        };
                        break;
                        case "originatorUseId": task.setOrginatorID(new Integer(wert)); break;
                        case "lastModifiedUseId": task.setLastModifiedID(new Integer(wert)); break;
                        case "lastModifiedTime": task.setLastModified(lineStrings[1]); break;
                    }
                    taskList.add(task);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return taskList;
    }

    public String sendLoginPost (final String mail, final String password) throws UnsupportedEncodingException {

        String name ="";
        String data = URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(mail, "UTF-8");

        data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                + URLEncoder.encode(password, "UTF-8");


        String text = "";
        BufferedReader reader=null;

        // Send data
        try
        {

            // Defined URL  where to send data
            URL url = new URL(urlbase+"/users/login");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                if(line.contains("name"))
                {
                    String lineStrings[] = line.split(":");
                    String typ = lineStrings[1];
                    typ.substring(1,typ.length()-1);
                    name=typ;
                }
                if(line.contains("id"))
                {
                    String lineStrings[] = line.split(":");
                    String typ = lineStrings[1];
                    typ.substring(1,typ.length()-1);
                    name=typ+"-"+name;
                }
                sb.append(line + "\n");
            }
            text = sb.toString();
        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        return name;
    }

    public  boolean  sendRegistrationPost(final String mail, final String password,final String name) throws UnsupportedEncodingException, UnsupportedEncodingException{
        String data = URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(mail, "UTF-8");

        data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                + URLEncoder.encode(password, "UTF-8");

        data += "&" + URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(name, "UTF-8");



        String text = "";
        BufferedReader reader=null;

        try
        {

            // Defined URL  where to send data
            URL url = new URL(urlbase+"/users");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();
        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}
        }

        if(text.contains("Conflict")) {
            return false;
        }
        // Show response on activity
        return true;
    }


    public void insertTask(ToDoModel task){
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {

            URL requestUrl = new URL(urlbase+"/tasks");
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.connect(); // no connection is made

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("id",task.getId()+"");
            urlConnection.setRequestProperty("title",task.getTask());
            urlConnection.setRequestProperty("desc",task.getTaskDescription());
            //TODO
            //Checken wie es zurückkommt (True groß oder klein?)
            if(task.getStatus()==1)
            {
                urlConnection.setRequestProperty("completed","true");
            }
            else
            {
                urlConnection.setRequestProperty("completed","false");
            }
            if(task.getIsPublic()==1)
            {
                urlConnection.setRequestProperty("public","true");
            }
            else
            {
                urlConnection.setRequestProperty("public","false");
            }

            urlConnection.setRequestProperty("originatorUseId",task.getOrginatorID()+"");
            urlConnection.setRequestProperty("lastModifiedUseId",task.getLastModifiedID()+"");
            urlConnection.setRequestProperty("lastModifiedTime",task.getLastModified());

            urlConnection.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(outputPost);
            outputPost.flush();
            outputPost.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }



    public void updateStatus(int id, int status){

      /*  ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});*/

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {

            URL requestUrl = new URL(urlbase+"/tasks/{"+id+"}");
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.connect(); // no connection is made

            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("id",id+""); //notwendig?
            if(status==1)
            {
                urlConnection.setRequestProperty("completed","true");
            }
            else
            {
                urlConnection.setRequestProperty("completed","false");
            }

            urlConnection.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(outputPost);
            outputPost.flush();
            outputPost.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }




    public void updateTask(int id, String task, String text, int modID, String datumzeit, int publicset) {

        /* ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});*/

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {

            URL requestUrl = new URL(urlbase+"/tasks/{"+id+"}");
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.connect(); // no connection is made

            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("id",id+""); //notwendig?
            urlConnection.setRequestProperty("title",task);
            urlConnection.setRequestProperty("desc",text);
            if(publicset==1)
            {
                urlConnection.setRequestProperty("public","true");
            }
            else
            {
                urlConnection.setRequestProperty("public","false");
            }
            urlConnection.setRequestProperty("lastModifiedUseId",modID+"");
            urlConnection.setRequestProperty("lastModifiedTime",datumzeit);

            urlConnection.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(outputPost);
            outputPost.flush();
            outputPost.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }

    public void deleteTask(int id){
        // db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});

        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {

            URL requestUrl = new URL(urlbase+"/tasks/{"+id+"}");
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.connect(); // no connection is made

            urlConnection.setRequestMethod("DELETE");

            urlConnection.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            //writeStream(outputPost);
            outputPost.flush();
            outputPost.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }

    //TODO
    //Update Tasklisk - ein gesamter Request
    public ArrayList<ToDoModel> updateTasklist (List<ToDoModel> list)
    {

        //TODO
        return new ArrayList<ToDoModel>();
    }

}
