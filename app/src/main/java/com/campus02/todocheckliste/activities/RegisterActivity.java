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
import com.example.registration.R;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private Button b1;
    private EditText ed1,ed2, ed3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView btn=findViewById(R.id.alreadyHaveAccount);

        db = new DatabaseHandler();
        b1 = (Button)findViewById(R.id.btnlogin);
        ed1 = (EditText)findViewById(R.id.inputEmail);
        ed2 = (EditText)findViewById(R.id.inputPassword);
        ed3 = (EditText)findViewById(R.id.inputUsername);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                NetworkUtil neut = new NetworkUtil();
                if(neut.getConnectivityStatusString(getApplicationContext()))
                {
                    String mail = ed1.getText().toString();
                    String password = ed2.getText().toString();
                    String name = ed3.getText().toString();

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
                    if (TextUtils.isEmpty(mail)) {
                        //ed2.setError(getString(R.string.error_field_required));
                        pushMessage("Keine Email vorhanden.");
                        focusView = ed1;
                        cancel = true;
                    } else if (!isEmailValid(mail)) {
                        // ed2.setError(getString(R.string.error_invalid_email));
                        pushMessage("Email fehlerhaft.");
                        focusView = ed1;
                        cancel = true;
                    }

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    }
                    else{
                        try {
                            if(db.sendRegistrationPost(mail, password,name)) {
                                Context context = getApplicationContext();
                                CharSequence text = "Registrierung erfolgreich! Sie werden weitergeleitet.";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                                setContentView(R.layout.activity_main);
                            }
                            else
                            {
                                Context context = getApplicationContext();
                                CharSequence text = "Dieser Benutzer ist bereits vorhanden!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                }
                else
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Keine Internetverbindung vorhanden!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                //TODO
                //Mail und Password prüfen
                //DONE!

            }
        });
    }
    private void pushMessage(String text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 8;
    }

}
