package com.example.studentmanagesystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    public static final String DATABASE_NAME = "student.db";
    SQLiteDatabase db;
    EditText edtUsername, edtPassword;
    Button btnLogin, btnCloseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCloseLogin = findViewById(R.id.btnCloseLogin);
        initDB();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtUsername.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(getApplication(), "Xin vui lòng nhập tài khoản", Toast.LENGTH_SHORT).show();
                    edtUsername.requestFocus();
                }
                else if(password.isEmpty()){
                    Toast.makeText(getApplication(), "Xin vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    edtPassword.requestFocus();
                }
                else if(isUser(username, password)){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(), "Tài khoản hoặc mật khẩu không tồn tại !!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDB(){
        db = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
        String sql;
        try{
            if(!isTableExists(db,"tbluser")){
                sql = "CREATE TABLE tbluser (id_user    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
                sql += "username   TEXT NOT NULL,";
                sql += "password   TEXT NOT NULL)";
                db.execSQL(sql);
                sql = "INSERT INTO tbluser(username, password) VALUES ('admin', 'admin')";
                db.execSQL(sql);
            }

            if (!isTableExists(db,"tblstudent")){
                sql = "CREATE TABLE tblstudent (";
                sql += "id_student   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
                sql += "id_class     INTEGER NOT NULL,";
                sql += "code_student  TEXT NOT NULL,";
                sql += "name_student  TEXT,";
                sql += "number_student INTEGER,;";
                sql += "gender_student NUMERIC,;";
                sql += "birthday_student TEXT,;";
                sql += "address_student TEXT);";
                db.execSQL(sql);
            }

        }catch (Exception ex){
            Toast.makeText(this, "Khởi tạo dữ liệu không thành công", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name" + "- '" + tableName + "'", null);
        if(cursor!= null){
            if(cursor.getCount() > 0){
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
    private boolean isUser(String username, String password){
        try {
            db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
            Cursor c = db.rawQuery("select * from tbluser where username=? and password=?",
                    new String[]{username,password});
            c.moveToFirst();
            if(c.getCount() > 0){
                return true;
            }
        }catch (Exception ex){
            Toast.makeText(this, "Lỗi hệ thống", Toast.LENGTH_LONG).show();
        }
        return false;
    }

}
