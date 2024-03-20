package com.example.studentmanagesystem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterStudent extends ArrayAdapter<Student> {
    ArrayList<Student> studentList = new ArrayList<Student>();

    public MyAdapterStudent(@NonNull Context context, int resource, ArrayList<Student> objects) {
        super(context, resource, objects);
        this.studentList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_student, null);
        ImageView imgstudent = v.findViewById(R.id.imgStudent);
        TextView txtclassstudent = v.findViewById(R.id.txtStudentClass);
        TextView txtnamestudent = v.findViewById(R.id.txtStudentName);
        TextView txtbirthdaystudent = v.findViewById(R.id.txtStudentBirthday);
        TextView txtgenderstudent = v.findViewById(R.id.txtStudentGender);
        TextView txtaddressstudent = v.findViewById(R.id.txtStudentAddress);
        if(position == 0){
            txtclassstudent.setBackgroundColor(Color.WHITE);
            txtnamestudent.setBackgroundColor(Color.WHITE);
            txtbirthdaystudent.setBackgroundColor(Color.WHITE);
            txtgenderstudent.setBackgroundColor(Color.WHITE);
            txtaddressstudent.setBackgroundColor(Color.WHITE);
        }
        imgstudent.setImageResource(R.drawable.img_1);
        txtclassstudent.setText("Mã lớp: " + studentList.get(position).getName_class());
        txtnamestudent.setText("Tên sinh viên: " + studentList.get(position).getName_student());
        txtbirthdaystudent.setText("Ngày sinh: " + studentList.get(position).getBirthday_student());
        txtgenderstudent.setText("Giới tính: " + studentList.get(position).getGender_student());
        txtaddressstudent.setText("Địa chi: " + studentList.get(position).getAddress_student());
        return super.getView(position, convertView, parent);
    }
}
