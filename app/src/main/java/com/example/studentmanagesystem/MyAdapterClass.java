package com.example.studentmanagesystem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterClass extends ArrayAdapter<Room> {
    ArrayList<Room> classList = new ArrayList<Room>();

    public MyAdapterClass(@NonNull Context context, int resource, ArrayList<Room>objects) {
        super(context, resource, objects);
        classList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_class, null);
        TextView txtcodeclass = v.findViewById(R.id.txtclasscode);
        TextView txtnameclass = v.findViewById(R.id.txtclassname);
        TextView txtnumberclass = v.findViewById(R.id.txtclassnumber);
        if(position == 0){
            txtcodeclass.setBackgroundColor(Color.BLACK);
            txtnameclass.setBackgroundColor(Color.BLACK);
            txtnumberclass.setBackgroundColor(Color.BLACK);
        }

        txtcodeclass.setText(classList.get(position).getCode_class());
        txtnameclass.setText(classList.get(position).getName_class());
        txtnumberclass.setText(classList.get(position).getClass_number());
        return v;
    }
}
