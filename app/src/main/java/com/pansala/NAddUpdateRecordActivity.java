package com.pansala;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.Range;
import com.pansala.Database.DBHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

/**
 * The pansala program implements an application that
 * simply Add and Update operation per each record to the standard output.
 *
 * @author  Dimantha H.V.N
 * @version 1.0
 * @since   2020-04-21
 */

public class NAddUpdateRecordActivity extends AppCompatActivity {

    //views
    private CircularImageView profileIv;
    private EditText nameTemple;
    private EditText historyTemple;
    private EditText dateTemple;
    private EditText monkName;
    private EditText location;
    private EditText monkTele;
    private FloatingActionButton saveBtn;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    //arrays of permissions
    private String[] cameraPermissions; //camera and storage
    private String[] storagePermissions; //only storage

    //variables (will contain data to save)
    private Uri imageUri;
    private String id,name, history,start, monk, templocation, phone, addedTime, updatedTime;
    private boolean isEditMode = false;

    //db helper
    private DBHelper dbHelper;

    //actionBar
    private ActionBar actionBar;

    //defining AwesomeValidation object
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_add_update_record);

        //initializing awesomeValidation object
        /*
         * The library provides 3 types of validation
         * BASIC
         * COLORATION
         * UNDERLABEL
         * */
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //init
        actionBar = getSupportActionBar();
        //title
        actionBar.setTitle("පන්සල ගිණුම සාදන්න");

        //back button
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //init views
        profileIv = findViewById(R.id.profileIv);
        nameTemple = findViewById(R.id.nameTemple);
        historyTemple = findViewById(R.id.historyTemple);
        dateTemple = findViewById(R.id.dateTemple);
        monkName = findViewById(R.id.monkName);
        location = findViewById(R.id.location);
        monkTele = findViewById(R.id.monkTele);
        saveBtn = findViewById(R.id.saveBtn);

        //adding validation to edit texts
        awesomeValidation.addValidation(this, R.id.nameTemple, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.historyTemple, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.historyerror);
        awesomeValidation.addValidation(this, R.id.monkName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.monkerror);
        awesomeValidation.addValidation(this, R.id.location, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.locationerror);
        //awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.monkTele, "^[0-9\\-\\+]{9,15}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.dateTemple, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.dateerror);
        //awesomeValidation.addValidation(this, R.id.editTextAge, Range.closed(13, 60), R.string.ageerror);

//        private boolean isValidMobile(String phone) {
//            if(!Pattern.matches("[a-zA-Z]+", phone)) {
//                return phone.length() > 6 && phone.length() <= 13;
//            }
//            return false;
//        }
        //get data from intent
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        if (isEditMode){
            //update data
            actionBar.setTitle("Update Date");

            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            history = intent.getStringExtra("HISTORY");
            start = intent.getStringExtra("START");
            monk = intent.getStringExtra("MONK");
            templocation = intent.getStringExtra("TEMPLE_LOCATION");
            phone = intent.getStringExtra("PHONE");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            addedTime = intent.getStringExtra("ADDED_TIME");
            updatedTime = intent.getStringExtra("UPDATED_TIME");

            //set data to views
            nameTemple.setText(name);
            historyTemple.setText(history);
            dateTemple.setText(start);
            monkName.setText(monk);
            location.setText(templocation);
            monkTele.setText(phone);

            //if no image was selected while adding data, imageUri value will be "null"
            if (imageUri.toString().equals("null")){
                //no image set defaults
                profileIv.setImageResource(R.drawable.templenew);
            }else{
                //have image, set
                profileIv.setImageURI(imageUri);
            }

        }else{
            //add data
            actionBar.setTitle("පන්සල එකතුවට");

        }


        //init db helper
        dbHelper = new DBHelper(this);

        //init permission array
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //click image view to show image pick dialog

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show image pick dialog
                imagePickDialog();
            }

        });

        //click save button to save record
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {

                    inputData();
                    //process the data further
                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void inputData() {
        //get Data
        name = ""+nameTemple.getText().toString().trim();
        history = ""+historyTemple.getText().toString().trim();
        start = ""+dateTemple.getText().toString().trim();
        monk = ""+monkName.getText().toString().trim();
        templocation = ""+location.getText().toString().trim();
        phone = ""+monkTele.getText().toString().trim();

        if (isEditMode){
            //update data
            String timestamp = ""+System.currentTimeMillis();
            dbHelper.updateRecord(
                    ""+id,
                    ""+name,
                    ""+imageUri,
                    ""+history,
                    ""+start,
                    ""+monk,
                    ""+templocation,
                    ""+phone,
                    ""+addedTime, //added time will be the same
                    ""+updatedTime); // update time will be changed

            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
        }else {
            //new data
            String timestamp = ""+System.currentTimeMillis();
            long id = dbHelper.insertRecord(
                    ""+name,
                    ""+imageUri,
                    ""+history,
                    ""+start,
                    ""+monk,
                    ""+templocation,
                    ""+phone,
                    ""+timestamp,
                    ""+timestamp
            );

            Toast.makeText(getApplicationContext(), "Record Added against ID: "+id, Toast.LENGTH_LONG).show();
        }

    }

    private void imagePickDialog() {
        //options to display dialog
        String[] options = {"Gallery", "Camera"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //title
        builder.setTitle("Pick Image From");
        //set items/options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle clicks
                if (which == 0){
                    //camera clicked
                    if (!checkCameraPermissions()){
                        requestCameraPermission();
                    }else {
                        //permission already granted
                        pickFromCamera();
                    }
                }else if (which == 1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        //permission already granted
                        pickFromGallery();
                    }
                }
            }
        });
        //create/show dialog
        builder.create().show();
    }

    private void pickFromCamera() {
        //intent to pick image from gallery, the image will be returned in onActivity result method
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");//we want only images
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromGallery() {
        //intent to pick image from camera, the image will be returned in onActivity result method
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");

        //put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to open camera for image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        //check if storage permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        //request the storage permissions
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        //check if camera permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission(){
        //request the camera permissions
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    private void copyFileOrDirectory(String srcDir, String desDir){
        //create specified directory
        try {
            File src = new File(srcDir);
            File des = new File(desDir, src.getName());
            if (src.isDirectory()){
                String[] files = src.list();
                int filesLength = files.length;
                for (String file:files){
                    String src1 = new File(src, file).getPath();
                    String dst1 = des.getPath();

                    copyFileOrDirectory(src1,dst1);
                }
            }else{
                copyFile(src, des);
            }

        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void copyFile(File srcDir, File desDir) throws IOException {
        if (!desDir.getParentFile().exists()){
            desDir.mkdirs(); // create if not exists
        }
        if (!desDir.exists()){
            desDir.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try{
            source = new FileInputStream(srcDir).getChannel();
            destination = new FileOutputStream(desDir).getChannel();
            destination.transferFrom(source, 0,source.size());

            imageUri = Uri.parse(desDir.getPath()); // uri of saved image
            Log.d("ImagePath","copyFile: "+imageUri);
        }catch (Exception e){
            //if there is an error saving image
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        finally{
            //close resources
            if (source!=null){
            source.close();
            }
            if (destination!=null){
                destination.close();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //result of permission allowed/denied
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    //if allowed returns true otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        //both permission allowed
                        pickFromCamera();
                    }else {
                        Toast.makeText(getApplicationContext(),"Camera & Storage permissions are required",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    //if allowed returns true otherwise false
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted){
                        //storage permission allowed
                        pickFromGallery();
                    }else {
                        Toast.makeText(getApplicationContext(),"Storage permission required",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //image pic from camera or gallery will be received here
        if (resultCode == RESULT_OK){
            //image is picked
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //picked from gallery

                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //picked from camera

                //crop image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                //cropped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //set image
                    profileIv.setImageURI(resultUri);

                    copyFileOrDirectory(""+imageUri.getPath(),""+getDir("SQLitePansalaImages",MODE_PRIVATE));
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    //error
                    Exception error = result.getError();
                    Toast.makeText(getApplicationContext(),""+error, Toast.LENGTH_SHORT).show();
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

