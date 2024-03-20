package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class EditClassActivity extends Activity {
    Button btnSaveClass, btnClearClass, btnCloseClass;
    EditText edtCode, edtName, edtNumber;
    String id_class;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);
        initWidget();
        getData();
        btnSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = getIntent();
                if(saveClass()){
                    Room r = new Room(id_class, edtCode.getText().toString(), edtName.getText().toString(),edtNumber.getText().toString());
                    bundle.putSerializable("room", r);
                    intent.putExtra("data",bundle);
                    setResult(ClassList.SAVE_CLASS, intent);
                    Toast.makeText(getApplication(), "Cập nhật lớp học thành công", Toast.LENGTH_SHORT).show();
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
                Notify.exit(EditClassActivity.this);
            }
        });
    }

    private void initWidget(){
        btnSaveClass = findViewById(R.id.btnSaveEditClass);
        btnClearClass = findViewById(R.id.btnClearEditClass);
        btnCloseClass = findViewById(R.id.btnCloseEditClass);
        edtCode = findViewById(R.id.edtEditClassCode);
        edtName = findViewById(R.id.edtEditClassName);
        edtNumber = findViewById(R.id.edtEditClassNumber);
    }
    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        Room room = (Room) bundle.getSerializable("room");
        id_class = room.getId_class();
        edtCode.setText(room.getCode_class());
        edtName.setText(room.getName_class());
        edtNumber.setText(room.getClass_number());
    }
    private boolean saveClass(){
        try{
            SQLiteDatabase db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("code_class", edtCode.getText().toString());
            values.put("name_class", edtName.getText().toString());
            values.put("number_student", Integer.parseInt(edtNumber.getText().toString()));
            if(db.update("tblclass", values, "id_class=?", new String[]{id_class})!=-1)
                return true;
        }catch (Exception ex){
            Toast.makeText(getApplication(), "Cập nhật lớp học không thành công", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private void clearClass(){
        edtCode.setText("");
        edtName.setText("");
        edtNumber.setText("");
    }
}
