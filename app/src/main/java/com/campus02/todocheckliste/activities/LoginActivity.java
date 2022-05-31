package com.campus02.todocheckliste.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.campus02.todocheckliste.utilities.DatabaseHandler;
import com.campus02.todocheckliste.utilities.NetworkUtil;
import com.campus02.todocheckliste.utilities.PrefManager;
import com.example.registration.R;

import java.io.UnsupportedEncodingException;

public class
LoginActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private Button b1;
    private  EditText ed1,ed2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SplashScreen splashScreen = SplashScreen.installSplashScreen(R.layout.activity_splash);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn=findViewById(R.id.textViewSignUp);

        b1 = (Button)findViewById(R.id.btnlogin);
        ed1 = (EditText)findViewById(R.id.inputEmail);
        ed2 = (EditText)findViewById(R.id.inputPassword);

        db = new DatabaseHandler();

        if (!new PrefManager(this).isUserLogedOut()) {
            //user's email and password both are saved in preferences
            startHomeActivity();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtil neut = new NetworkUtil();
                if(neut.getConnectivityStatusString(getApplicationContext()))
                {
                    attemptLogin();
                }
                else
                {
                    pushMessage("Keine Internetverbindung und keine Anmeldung vorhanden.");
                }
                //startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
       /*             String mail = ed1.getText().toString();
                    String password = ed2.getText().toString();
                    if(password.length()>0 && mail.length() > 0)
                    {
                        //testcase
                        /*
                        if(ed1.getText().toString().equals("admin") &&
                                ed2.getText().toString().equals("admin")) {

                            Context context = getApplicationContext();
                            CharSequence text = "Login erfolgreich!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            setContentView(R.layout.activity_main);
                        } //ede


                        //Weiterleiten an DatabaseHandler zur Überprüfunng
                        try {
                            String data = db.sendLoginPost(mail, password);
                            if(data.length() > 0)
                             {
                                 //wenn CheckLogin Name zurückgibt, dann weiterleiten
                                 //Namen global speichern!
                                 Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                                 intent.putExtra("UserDetail", data);
                                 startActivity(intent);


                                 Context context = getApplicationContext();
                            CharSequence text = "Login erfolgreich!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                                 setContentView(R.layout.activity_main);
                             }
                            else
                            {
                                Context context = getApplicationContext();
                                CharSequence text = "Login fehlgeschlagen!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //setContentView(R.layout.activity_main);
                    }
                    else
                    {
                        Context context = getApplicationContext();
                        CharSequence text = "Login fehlgeschlagen!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } */
            }
        });

    }

    private void attemptLogin() {

        // Reset errors.
        ed1.setError(null);
        ed2.setError(null);

        // Store values at the time of the login attempt.
        String email = ed1.getText().toString();
        String password = ed2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            //  ed2.setError(getString(R.string.error_invalid_password));
            pushMessage("Password fehlerhaft.");
            focusView = ed2;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            //ed2.setError(getString(R.string.error_field_required));
            pushMessage("Email fehlerhaft.");
            focusView = ed1;
            cancel = true;
        } else if (!isEmailValid(email)) {
            // ed2.setError(getString(R.string.error_invalid_email));
            pushMessage("Password fehlerhaft.");
            focusView = ed1;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // save data in local shared preferences
            try {
                String data = db.sendLoginPost(email, password);
                String[] tempFelddata = data.split("-");
                saveLoginDetails(email, password, tempFelddata[0], tempFelddata[1]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            startHomeActivity();
        }
    }

    private void saveLoginDetails(String email, String password, String id, String name) {
        new PrefManager(this).saveLoginDetails(email, password, Integer.parseInt(id),name);
    }

    //keine Ahnung ob das so geht
    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 8;
    }

    private void pushMessage(String text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
