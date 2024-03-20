package com.example.studentmanagesystem;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EditStudentActivity extends Activity {
    Button btnSave, btnClear, btnClose;
    EditText edtCode, edtName, edtAddress, edtBirthday;
    RadioGroup rdigroupGender;
    RadioButton rdiMale, rdiFemale;
    Spinner spnClassCode;
    SQLiteDatabase db;
    ArrayList<Room> classList = new ArrayList<Room>();
    ArrayAdapter<Room> adapter;
    int possSpinner = -1;
    int idChecked, gender = 0;
    String id_student;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        initWidget();
        getData();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveStudent()){
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    Student student = new Student(classList.get(possSpinner).getId_class(),
                                                  classList.get(possSpinner).getName_class(), id_student,
                                                  edtCode.getText().toString(),edtName.getText().toString(),
                                                  gender+"", edtBirthday.getText().toString(),
                                                  edtAddress.getText().toString());
                    bundle.putSerializable("student", student);
                    intent.putExtra("data", bundle);
                    setResult(StudentListActivity.SAVE_STUDENT,intent);
                    Toast.makeText(getApplication(), "Cập nhật sinh viên thành công!!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notify.exit(EditStudentActivity.this);
            }
        });
        spnClassCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                possSpinner = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                possSpinner=-1;
            }
        });
    }

    private void  initWidget(){
        btnSave = findViewById(R.id.btnSaveEditStudent);
        btnClear = findViewById(R.id.btnClearEditStudent);
        btnClose = findViewById(R.id.btnCloseEditStudent);
        spnClassCode = findViewById(R.id.spnEditClassCode);
        edtCode = findViewById(R.id.edtEditStudentCode);
        edtName = findViewById(R.id.edtEditStudentName);
        edtAddress = findViewById(R.id.edtEditStudentAddress);
        edtBirthday = findViewById(R.id.edtEditStudentBirthday);
        rdigroupGender = findViewById(R.id.rdigroupEditGender);
        rdiFemale = findViewById(R.id.rdiEditFemale);
        rdiMale = findViewById(R.id.rdiEditMale);
    }
    private  void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        Student student = (Student) bundle.getSerializable("student");
        edtCode.setText(student.getCode_student());
        edtName.setText(student.getName_student());
        edtBirthday.setText(student.getBirthday_student());
        edtAddress.setText(student.getAddress_student());
        id_student = student.getId_student();
        if(student.getGender_student().contains("1")){
            rdiMale.setChecked(true);
        }
        else{
            rdiFemale.setChecked(true);
        }
        int i = 0;
        while (i<classList.size()){
            if((student.getId_class().contains(classList.get(i).getId_class())))
                break;
            i++;
        }
        spnClassCode.setSelection(i);
    }
    private boolean saveStudent(){
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idChecked = rdigroupGender.getCheckedRadioButtonId();
        if(idChecked==R.id.rdiEditMale)
            gender=1;
        try{
            values.put("id_class", classList.get(possSpinner).getId_class());
            values.put("code_student", edtCode.getText().toString());
            values.put("name_student", edtName.getText().toString());
            values.put("birthday_student", edtBirthday.getText().toString());
            values.put("address_student", edtAddress.getText().toString());
            values.put("gender_student", gender);
            if (db.update("tblstudent", values, "=id_student=?",new String[]{id_student})!=-1)
                return true;
        }catch (Exception ex){
            Toast.makeText(getApplication(), "Loi"+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
