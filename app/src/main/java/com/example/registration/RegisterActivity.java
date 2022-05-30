package com.example.registration;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registration.Utils.DatabaseHandler;
import com.example.registration.Utils.NetworkUtil;

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
                //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                NetworkUtil neut = new NetworkUtil();
                if(neut.getConnectivityStatusString(getApplicationContext()))
                {
                    String mail = ed1.getText().toString();
                    String password = ed2.getText().toString();
                    String name = ed3.getText().toString();

                    if (password.length() > 0 && mail.length() > 0 && name.length() >0) {
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

                }
        });
    }
}
