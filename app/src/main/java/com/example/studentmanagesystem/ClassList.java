package com.example.studentmanagesystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ClassList extends Activity {
    ListView lstClass;
    Button btnopenclass;
    ArrayList<Room> classList = new ArrayList<Room>();
    MyAdapterClass adapter;
    SQLiteDatabase db;
    int posselected = -1;
    public static final int OPEN_CLASS = 113;
    public static final int EDIT_CLASS = 114;
    public static final int SAVE_CLASS = 115;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        lstClass = findViewById(R.id.lstclass);
        btnopenclass = findViewById(R.id.btnOpenClass);
        //getClassList();
        registerForContextMenu(lstClass);
        btnopenclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassList.this, InsertClassActivity.class);
                startActivityForResult(intent, ClassList.OPEN_CLASS);
            }
        });
        lstClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posselected = position;
                return false;
            }
        });
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tblclass (" +
                "id_class INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code_class TEXT, " +
                "name_class TEXT, " +
                "number_student INTEGER)");
        getClassList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuclass, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menueditclass){
            Room room = classList.get(posselected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(ClassList.this, EditClassActivity.class);
            bundle.putSerializable("room", room);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, ClassList.EDIT_CLASS);
        }
        if(item.getItemId() == R.id.menudeleteclass){
            comfirmDelete();
        }
        if(item.getItemId() == R.id.menucloseclass){
            Notify.exit(this);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ClassList.OPEN_CLASS:
                if(requestCode == ClassList.SAVE_CLASS){
                    Bundle bundle = data.getBundleExtra("data");
                    Room room = (Room) bundle.getSerializable("room");
                    classList.add(room);
                    adapter.notifyDataSetChanged();
                }
                break;
            case ClassList.EDIT_CLASS:
                if (requestCode == ClassList.SAVE_CLASS){
                    Bundle bundle = data.getBundleExtra("data");
                    Room room = (Room) bundle.getSerializable("room");
                    classList.set(posselected, room);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void getClassList(){
        try{
            classList.add(new Room("Mã lớp", "Tên lớp", "Sỉ số"));
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE,null);
            Cursor c = db.query("tblclass", null,null,null,null,null, null);
            c.moveToFirst();
            while (!c.isAfterLast()){
                classList.add(new Room(c.getInt(0)+"",c.getString(1).toString(), c.getString(2).toString(),c.getInt(3)+""));
                c.moveToNext();
            }
            adapter = new MyAdapterClass(this, android.R.layout.simple_list_item_1, classList);
            lstClass.setAdapter(adapter);
        }catch (Exception ex){
            Toast.makeText(getApplication(), "Lỗi" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void comfirmDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận để xóa lớp");
        builder.setIcon(R.drawable.question);
        builder.setMessage("Bạn có chắc xóa lớp học?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = openOrCreateDatabase(Login.DATABASE_NAME,MODE_PRIVATE, null);
                String id_class = classList.get(posselected).getId_class();
                if(db.delete("tblclass", "id_class=?", new String[]{id_class})!=-1){
                    classList.remove(posselected);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplication(), "Xóa thành công lớp này!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
