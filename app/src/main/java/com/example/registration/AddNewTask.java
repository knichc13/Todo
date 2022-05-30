package com.example.registration;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.registration.Model.ToDoModel;
import com.example.registration.Utils.DatabaseHandler;
import com.example.registration.Utils.PrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText, newTaskTextLong;
    private RadioButton newPublicSet;
    private Button newTaskSaveButton;
    private String userDetails;
    private EditText detailsGenerated;
    private EditText detailsModified;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        //new
        newTaskTextLong = requireView().findViewById(R.id.newTaskFullText);
        newPublicSet = requireView().findViewById(R.id.radioButton2);
        detailsGenerated = requireView().findViewById(R.id.detailsGenerated);
        detailsModified = requireView().findViewById(R.id.detailsModified);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            newTaskTextLong.setText(bundle.getString("desc"));
            //newPublicSet
            detailsGenerated.setText("Erstellt von "+bundle.getInt("orgid"));

            if(bundle.getInt("pub")==0)
            {
                newPublicSet.setEnabled(false);
                newPublicSet.setChecked(false);
            }
            else
            {
                newPublicSet.setEnabled(false);
                newPublicSet.setChecked(true);
            }
            detailsModified.setText("Geändert am "+bundle.getString("mod")+" von User "+bundle.getInt("modid"));

            assert task != null;
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
        }

        //db = new DatabaseHandler(getActivity());
        db = new DatabaseHandler();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;

        int userid = new PrefManager(this.getContext()).getUserID(); //keine Ahnung ob funktioniert

        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                //new
                String textLong = newTaskTextLong.getText().toString();
                int publicset = 0;
                if(newPublicSet.isSelected())
                {
                    publicset = 1;
                }

                if(finalIsUpdate){

                    //new
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    //get current date time with Date()
                    Date date = new Date();
                    System.out.println(dateFormat.format(date));

                    //get current date time with Calendar()
                    Calendar cal = Calendar.getInstance();
                    String datime = dateFormat.format(cal.getTime());
                    int updateID = userid;

                    //TODO
                    //CONFLICT ERZEUGEN und Alert Dialog ausgeben mit JA, überschreiben oder NEIN, neuen Task anlegen
                    db.updateTask(bundle.getInt("id"), text, textLong, updateID,datime, publicset);
                }
                else {
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);

                    //new
                    task.setIsPublic(publicset);
                    task.setTaskDescription(textLong);
                    task.setOrginatorID(userid); //ID
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    //get current date time with Date()
                    Date date = new Date();
                    System.out.println(dateFormat.format(date));

                    //get current date time with Calendar()
                    Calendar cal = Calendar.getInstance();
                    String datime = dateFormat.format(cal.getTime());
                    task.setLastModified(datime);
                    int updateID = userid;
                    task.setLastModifiedID(userid);

                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof com.example.registration.DialogCloseListener) {
            try {
                ((DialogCloseListener)activity).handleDialogClose(dialog);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
