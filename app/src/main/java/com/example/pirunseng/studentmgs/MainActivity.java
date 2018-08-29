package com.example.pirunseng.studentmgs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAdd, btnView, btnViewAll, btnUpdate, btnDelete;
    private EditText sID, sNAME;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnView = (Button)findViewById(R.id.btnView);
        btnViewAll = (Button)findViewById(R.id.btnViewAll);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        sID = (EditText)findViewById(R.id.sID);
        sNAME = (EditText)findViewById(R.id.sNAME);

        db=openOrCreateDatabase("StudentInfoDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS students (id int primary key, name varchar)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS students (id int primary key, name varchar)");

        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        if (view == btnAdd){
            if(sID.getText().toString().equals("0")){
                showMessage("Error", "Zero is invalid for" + " input to database");
            }else{
                if(sID.getText().toString().equals("") || sNAME.getText().toString().equals("")){
                    showMessage("Error", "Please enter all values");
                    return;
//                    where name = ????
                }
                Cursor c = db.rawQuery("SELECT id FROM students " + "WHERE id='" +
                        sID.getText().toString() + "'", null);

                if (c.moveToFirst()) {
                    showMessage("Error", "Record already exists");
                    clearText();
                }else{
                    db.execSQL("INSERT INTO students VALUES(" + sID.getText() + ",'" + sNAME.getText() + "');");
                    showMessage("Successful", "Record added");
                    clearText();
                }
            }
        }

        if(view == btnView){
            if(sID.getText().toString().trim().length() == 0){
                showMessage("Error", "Please enter Student ID");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM students where id = '" + sID.getText() + "'", null);
            if(c.moveToFirst()){
                sNAME.setText(c.getString(1));
            }else{
                showMessage("Error", "Invalid Student ID");
                clearText();
            }
        }

        if(view == btnViewAll){
            Cursor c = db.rawQuery("SELECT * FROM students", null);
            if(c.getCount() == 0){
                showMessage("Error", "No Records found");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while(c.moveToNext()){

                int index;

                index = c.getColumnIndexOrThrow("id");
                long s_id = c.getLong(index);

                index = c.getColumnIndexOrThrow("name");
                String s_name = c.getString(index);


                buffer.append("ID: " + s_id + "\n");

                buffer.append("Name: " + s_name + "\n");

//                buffer.append("ID: " + c.getString(0) + "\n");
//                buffer.append("Name: " + c.getString(1) + "\n");
            }
            showMessage("Student Information", buffer.toString());
        }

        if(view == btnUpdate){
            if(sID.getText().toString().trim().length() == 0){
                showMessage("Error", "Please enter Student ID for Update");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM students WHERE id ='" + sID.getText() + "'", null);
            if(c.moveToFirst()){
                db.execSQL("UPDATE students SET name ='" + sNAME.getText() + "'" + " WHERE id ='"+ sID.getText() + "'");
                showMessage("Successfully Updated", "Record was updated");
            }else{
                showMessage("Error", "Invalid Student ID");
                clearText();
            }
        }

        if(view == btnDelete){
            if(sID.getText().toString().trim().length() == 0){
                showMessage("Error", "Please enter Student ID for Delete");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM students WHERE id ='" + sID.getText() + "'", null);
            if(c.moveToFirst()){
                db.execSQL("DELETE FROM students WHERE id ='"+ sID.getText() + "'");
                showMessage("Successfully", "Record was deleted");
                clearText();
            }else{
                showMessage("Error", "Invalid Student ID");
                clearText();
            }
        }
    }

    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText(){
        sID.setText("");
        sNAME.setText("");
    }
}
