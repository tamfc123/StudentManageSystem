package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class InsertStudentActivity extends Activity {
    Button btnSave, btnClear, btnClose;
    EditText edtCode, edtName, edtAddress, edtBirthday;
    RadioGroup rdigroupGender;
    Spinner spnClassCode;
    SQLiteDatabase db;
    ArrayList<Room> classList = new ArrayList<Room>();
    ArrayAdapter<Room> adapter;
    int possSpinner = -1;
    int idChecked, gender = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_student);
        initWidget();
        getClassList();
        spnClassCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                possSpinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify.exit(InsertStudentActivity.this);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = saveStudent();
                if(id!=1){
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    Student student = new Student(classList.get(possSpinner).getId_class(),
                                                  classList.get(possSpinner).getName_class(),
                                                  id+"",edtCode.getText().toString(),
                                                  edtName.getText().toString(),gender+"",
                                                  edtBirthday.getText().toString(),
                                                  edtAddress.getText().toString());
                    bundle.putSerializable("student", student);
                    intent.putExtra("data", bundle);
                    setResult(StudentListActivity.SAVE_STUDENT, intent);
                    Toast.makeText(getApplication(), "Thêm sinh viên thành công!!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearStudent();
            }
        });
    }

    private void  initWidget(){
        btnSave = findViewById(R.id.btnSaveInsertStudent);
        btnClear = findViewById(R.id.btnClearInsertStudent);
        btnClose = findViewById(R.id.btnCloseInsertStudent);
        spnClassCode = findViewById(R.id.spnClassCode);
        edtCode = findViewById(R.id.edtStudentCode);
        edtName = findViewById(R.id.edtStudentName);
        edtAddress = findViewById(R.id.edtStudentAddress);
        edtBirthday = findViewById(R.id.edtStudentBirthday);
        rdigroupGender = findViewById(R.id.rdigroupGender);
    }
    private void getClassList(){
        try{
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE,null);
            Cursor c = db.query("tblclass", null, null, null, null, null,null);
            c.moveToFirst();
            while (!c.isAfterLast()){
                classList.add(new Room(c.getInt(0)+"", c.getString(1).toString(),c.getString(2).toString(),c.getInt(3)+""));
                c.moveToNext();
            }
            adapter = new ArrayAdapter<Room>(this, android.R.layout.select_dialog_singlechoice);
            spnClassCode.setAdapter(adapter);
        }catch (Exception ex){
            Toast.makeText(getApplication(), "Loi"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private long saveStudent(){
        db = openOrCreateDatabase(Login.DATABASE_NAME,MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idChecked = rdigroupGender.getCheckedRadioButtonId();
        if(idChecked == R.id.rdiMale)
            gender = 1;
        try{
            values.put("id_class", classList.get(possSpinner).getId_class());
            values.put("code_student", edtCode.getText().toString());
            values.put("name_student", edtName.getText().toString());
            values.put("birthday_student", edtBirthday.getText().toString());
            values.put("address_student", edtAddress.getText().toString());
            values.put("gender_student", gender);
            return (db.insert("tblstudent", null, values));
        }catch (Exception ex){
            Toast.makeText(getApplication(), "Loi"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return -1;
    }
    private void clearStudent(){
        edtName.setText("");
        edtCode.setText("");
        edtBirthday.setText("");
        edtAddress.setText("");
        spnClassCode.setSelection(0);
    }
}
