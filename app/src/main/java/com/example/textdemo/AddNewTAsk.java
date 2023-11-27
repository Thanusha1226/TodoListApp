package com.example.textdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.textdemo.Model.toDoModel;
import com.example.textdemo.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTAsk extends BottomSheetDialogFragment {
    public static final String TAG="ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTAsk newInstant(){
        return new  AddNewTAsk();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus){
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onViewCreated(@NonNull View view,@NonNull Bundle saveInstanceStatus){
        super.onViewCreated(view, saveInstanceStatus);
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.newTaskText);
        newTaskSaveButton =getView().findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate =true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), com.google.android.material.R.color.design_default_color_primary_dark));

        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), com.google.android.material.R.color.design_default_color_primary_dark));
                }else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     String text =  newTaskText.getText().toString();
                     if (finalIsUpdate){
                         db.updateTask(bundle.getInt("id"), text);
                     }else {
                         toDoModel task = new toDoModel();
                         task.setTask(text);
                         task.setStatus(0);
                         db.insertTask(task);
                     }
                     dismiss();
                 }
             });

        }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity= getActivity();
        if(activity instanceof DialogCloseListner){
            ((DialogCloseListner)activity).handlerDialogClose(dialog);

        }
    }

}
