package com.example.textdemo.Adapter;

import static android.os.Build.VERSION_CODES.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.textdemo.AddNewTAsk;
import com.example.textdemo.MainActivity;
import com.example.textdemo.Model.toDoModel;
import com.example.textdemo.R;
import com.example.textdemo.Utils.DatabaseHandler;

import java.util.List;

public class toDoAdapter extends RecyclerView.Adapter<toDoAdapter.ViewHolder> {
    private List<toDoModel> toDoModelList;
    private MainActivity activity;
    private DatabaseHandler db;
    private Context context;

    public toDoAdapter(DatabaseHandler db ,MainActivity activity){
        this.db = db;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return  new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        toDoModel item = toDoModelList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }
    @Override
    public int getItemCount(){
        return toDoModelList.size();
    }

    private boolean toBoolean(int n){
        return  n!= 0;
    }


    public void setTask(List<toDoModel> toDoModelList){
        this.toDoModelList = toDoModelList;
        notifyDataSetChanged();
    }

    public void deleteItem(int  position){
        toDoModel item = toDoModelList.get(position);
        db.deleteStus(item.getId());
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position){
        toDoModel item = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTAsk fragment = new AddNewTAsk();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTAsk.TAG);

    }



    public Context getContext() {
        return activity;
    }

    public static class  ViewHolder extends  RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.checkBox);
        }
    }

}
