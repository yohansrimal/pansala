package com.pansala;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.pansala.Database.DBHelper;

public class td_dahampasala_Form extends AppCompatActivity {
   DBHelper myDb;
   EditText editPansalaName, editTheroName, editDahampasala, editStudentCount, editClasses, editTextId;
   Button btnAddData;
   Button btnViewAll;
   Button btnViewUpdate;
   Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_td_dahampasala__form);
        myDb = new DBHelper(this);

        editPansalaName = (EditText) findViewById(R.id.PansalaName);
        editTheroName = (EditText) findViewById(R.id.theroName);
        editDahampasala = (EditText) findViewById(R.id.dahampasalaName);
        editStudentCount = (EditText) findViewById(R.id.student_Count);
        editClasses = (EditText) findViewById(R.id.classes_count);
        editTextId =(EditText) findViewById(R.id.idNo);
        btnAddData = (Button) findViewById(R.id.button_add);
        btnViewAll =(Button) findViewById(R.id.button_view);
        btnViewUpdate =(Button) findViewById(R.id.button_update);
        btnDelete =(Button) findViewById(R.id.button_delete);
        AddData();
        viewAll();
        updateData();
        deleteData();
    }

    public void deleteData(){
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteDahampasalaData(editTextId.getText().toString());
                        if(deletedRows > 0){
                            Toast.makeText(td_dahampasala_Form.this, "Data has been deleted successfully.", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(td_dahampasala_Form.this, "Error occurred during deletion.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }


    public void updateData(){
        btnViewUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdate = myDb.updateData(editTextId.getText().toString(), editPansalaName.getText().toString(), editTheroName.getText().toString(),
                                editDahampasala.getText().toString(), editStudentCount.getText().toString(),
                                editClasses.getText().toString());

                        if(isUpdate == true){
                            Toast.makeText(td_dahampasala_Form.this, "Data has been updated successfully.", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(td_dahampasala_Form.this, "Error occurred during updation.", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void AddData(){
       btnAddData.setOnClickListener(
               new View.OnClickListener() {

                   @Override
                   public void onClick(View v) {
                       boolean isInserted = myDb.insertData(editPansalaName.getText().toString(), editTheroName.getText().toString(),
                                editDahampasala.getText().toString(), editStudentCount.getText().toString(),
                                editClasses.getText().toString());
                       if(isInserted == true){
                           Toast.makeText(td_dahampasala_Form.this, "Data has been inserted successfully.", Toast.LENGTH_LONG).show();
                       }
                       else
                           Toast.makeText(td_dahampasala_Form.this, "Error occurred during insertion.", Toast.LENGTH_LONG).show();
                   }
               }
       );
    }
    public void viewAll(){
        btnViewAll.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       Cursor res =  myDb.getAllData();
                       if(res.getCount() == 0){

                           showMessage("Error","No data found");
                           return;
                       }
                           StringBuffer buffer = new StringBuffer();
                           while (res.moveToNext()){
                               buffer.append("අංකය :"+ res.getString(0)+"\n");
                               buffer.append("පන්සලේ නම : " +res.getString(1)+"\n");
                               buffer.append("නායක හිමි : " + res.getString(2)+"\n");
                               buffer.append("දහම්පාසලේ නම : " + res.getString(3)+"\n");
                               buffer.append("සිසුන් ගණන : " + res.getString(4)+"\n");
                               buffer.append("පන්ති ගණන : " + res.getString(5)+"\n\n");
                           }
                           //show data
                        showMessage("විස්තරය", buffer.toString());
                    }
                }
        );
    }

    public void showMessage (String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();

    }



}
