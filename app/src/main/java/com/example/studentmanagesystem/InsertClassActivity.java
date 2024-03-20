package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class InsertClassActivity extends Activity {
    Button btnSaveClass, btnClearClass, btnCloseClass;
    EditText edtClassName, editClassCode, edtClassNumber;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class);
        initWidget();
        btnSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = saveClass();
                Bundle bundle = new Bundle();
                Intent intent = getIntent();
                if(id != -1){
                    Room r = new Room(id+"",editClassCode.getText().toString(),edtClassName.getText().toString(), edtClassNumber.getText().toString());
                    bundle.putSerializable("room", r);
                    intent.putExtra("data", bundle);
                    setResult(ClassList.SAVE_CLASS, intent);
                    Toast.makeText(getApplication(), "Thêm lớp học thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplication(), "Thêm không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnClearClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearClass();
            }
        });
        btnCloseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify.exit(InsertClassActivity.this);
            }
        });
    }

    private  void initWidget(){
        btnSaveClass = findViewById(R.id.btnSaveInsertClass);
        btnClearClass = findViewById(R.id.btnClearInsertClass);
        btnCloseClass = findViewById(R.id.btnCloseInsertClass);
        editClassCode = findViewById(R.id.edtClassCode);
        edtClassName = findViewById(R.id.edtClassName);
        edtClassNumber = findViewById(R.id.edtClassNumber);
    }
    private long saveClass(){
        try {
            String tb = "tbclass";
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE,null);
            ContentValues values = new ContentValues();
            values.put("code_class", editClassCode.getText().toString());
            values.put("name_class", edtClassName.getText().toString());
            values.put("number_student", Integer.parseInt(edtClassNumber.getText().toString()));
            long id = db.insert("tblclass", null, values);
            if(id != -1){
                return id;
            }
            if(id == -1) {
                String error = db.toString();
                Log.e("Insert Error", error);
            }
        }catch (Exception ex){
            Toast.makeText(this, "Thêm lớp học bị lỗi", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }
    private void clearClass(){
        editClassCode.setText("");
        edtClassName.setText("");
        edtClassNumber.setText("");
    }
}
