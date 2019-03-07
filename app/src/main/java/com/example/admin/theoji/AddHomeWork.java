package com.example.admin.theoji;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.CheckableSpinnerAdapter;
import com.example.admin.theoji.Adapter.StudentListAdapter;
import com.example.admin.theoji.Connection.CameraUtils;
import com.example.admin.theoji.Connection.CommonUtils;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.Connection.Utilities;
import com.example.admin.theoji.Connection.Utility;

import com.example.admin.theoji.ModelClass.StudentModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.thomashaertel.widget.MultiSpinner;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.prefs.AbstractPreferences;

import static com.androidquery.util.AQUtility.getContext;
import static com.example.admin.theoji.Adapter.StudentListAdapter.present_students;

public class AddHomeWork extends AppCompatActivity {

    Spinner spin_class;
    private ArrayAdapter<String> classAdapter;
    private ArrayList<String> classList;
    String Class;

    Spinner spin_student;
    ArrayList<String>ChooseStudent;
    ArrayList<StudentModel> studentList=new ArrayList<StudentModel>();
     StudentListAdapter studentListAdapter;
   // CheckableSpinnerAdapter checkableSpinnerAdapter;

    String Student;

    ImageView postimage;
    EditText title, description, homework_date, participent;
    Button choose_img, btn_post;

    private String userChoosenTask;
    public static final int REQUEST_CAMERA = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    private static String imageStoragePath;
    private Bundle savedInstanceState;
    private Boolean upflag = false;
    int Gallery_view = 2;
    String Title_homework, Description_homework,Date_homework,Participent_homework,Spin_Class;
        String Spin_Student;

    public static HashMap<Integer , String> StudentSpinHashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home_work);

        spin_class = (Spinner)findViewById(R.id.spin_class);
        spin_student = (Spinner)findViewById(R.id.class_students);
        //spin_student.setAdapter(StudentAdapter, true, onSelectedListener);
        //MultiSelectionSpinner spinner=(MultiSelectionSpinner)findViewById(R.id.input1);

        classList = new ArrayList<>();
//        new spinnerClassExecuteTask().execute();
        classList.add("Select Class");
        classList.add("Nursery");
        classList.add("KG1");
        classList.add("KG2");
        classList.add("1");
        classList.add("2");
        classList.add("3");
        classList.add("4");
        classList.add("5");
        classList.add("6");
        classList.add("7");
        classList.add("8");
        classList.add("9");
        classList.add("10");
        classList.add("11 Mathes");
        classList.add("11 Bio");
        classList.add("11 Commerce");

        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class = classAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classAdapter = new ArrayAdapter<String>(AddHomeWork.this, R.layout.support_simple_spinner_dropdown_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_class.setAdapter(classAdapter);

        //***************student spinner******
        ChooseStudent = new ArrayList<>();
        studentList = new ArrayList<>();
        new spinnerStudentExecuteTask().execute();

//*******************************************************
        choose_img = (Button) findViewById(R.id.btn_img);
        btn_post = (Button) findViewById(R.id.btn_post);

        postimage = (ImageView) findViewById(R.id.post_image);
        title=(EditText)findViewById(R.id.title_homework);
        description=(EditText)findViewById(R.id.description);
        homework_date=(EditText)findViewById(R.id.homework_date);
        participent=(EditText)findViewById(R.id.participent);
//*********************************************************************

       homework_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (homework_date.getRight() - homework_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Calendar mcurrentDate = Calendar.getInstance();
                final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
                DatePickerDialog mDatePicker = new DatePickerDialog(AddHomeWork.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yyyy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        homework_date.setText(sdf.format(myCalendar.getTime()));

                        mDay[0] = selectedday;
                        mMonth[0] = selectedmonth;
                        mYear[0] = selectedyear;
                    }
                }, mYear[0], mMonth[0], mDay[0]);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();


                        return true;
                    }
                }
                return false;
            }
        });

       //********************************************************************
        choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });
//****************************************
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Title_homework = title.getText().toString();
                Description_homework = description.getText().toString();
                Date_homework=homework_date.getText().toString();
                Participent_homework=participent.getText().toString();
                Spin_Class=spin_class.getSelectedItem().toString();
                Spin_Student=present_students;


                if (validate()  )  {
                    Title_homework = title.getText().toString();
                    Description_homework = description.getText().toString();


                    previewCapturedImage();
                }

            }
        });
    }

    private void selectFile() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddHomeWork.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddHomeWork.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            captureImage();
                            restoreFromBundle(savedInstanceState);
                        } else {
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                        }
                    //captureImage();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //------------ private void galleryIntent()
    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, Gallery_view);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    //--------------------------------------------------------------------

    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            }
        }
    }

    private void previewCapturedImage() {

        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            new ImageCompression().execute(imageStoragePath);

            postimage.setImageBitmap(bitmap);
            File imgFile = new File(imageStoragePath);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(AddHomeWork.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }



    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                upflag = true;
                // Refreshing the gallery
                previewCapturedImage();

                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == Gallery_view && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imageStoragePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            postimage.setImageBitmap(BitmapFactory.decodeFile(imageStoragePath));
            upflag = true;
            cursor.close();
        }

    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    //********************************

    public class ImageCompression extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return CommonUtils.compressImage(strings[0]);
        }

        protected void onPostExecute(String imagePath) {
            // imagePath is path of new compressed image.
//            mivImage.setImageBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()));
            if (imagePath != null) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    new ImageUploadTask(imgFile).execute();
                }
            } else {
                AlertDialogCreate();
            }

        }
    }
    //*************************************

    public void AlertDialogCreate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddHomeWork.this, R.style.AlertDialogTheme);
        builder.setIcon(R.drawable.ic_launcher_background)
                .setTitle("Please Upload Image!")
                .setPositiveButton("OK", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectFile();
                        dialog.dismiss();
                    }
                }).show();

    }
    //*********************************************

    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;
        String result = "";
        File Image;

        public ImageUploadTask(File imgFile) {
            this.Image = imgFile;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AddHomeWork.this);
            dialog.setMessage("Processing");

            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            try {

                org.apache.http.entity.mime.MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
               Log.e("students",""+Spin_Student);
                String id= AppPreference.getUserid(AddHomeWork.this);
                entity.addPart("Homework_image", new FileBody(Image));
                entity.addPart("Student_class_type", new StringBody(Spin_Class));
                entity.addPart("Student_name", new StringBody(Spin_Student));
                entity.addPart("Homework_title", new StringBody(Title_homework));
                entity.addPart("Homework_date", new StringBody(Date_homework));
                entity.addPart("Homework_description", new StringBody(Description_homework));
                entity.addPart("Homework_participent", new StringBody(Participent_homework));
                entity.addPart("id",new StringBody(id));

                result = Utilities.postEntityAndFindJson("https://jntrcpl.com/theoji/index.php/Api/post_activity_student",entity);
//                        AppPreference.getUserid(AddHomeWork.this), entity);

                return result;

            } catch (Exception e) {
                // something went wrong. connection with the server error
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Log.v("profile",result);

            String result1 = result;
            if (result != null) {

                dialog.dismiss();
                Log.e("result_Image", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String img = object.getString("responce");
                    if (img.equals("true")) {

                        Toast.makeText(AddHomeWork.this, "Success", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AddHomeWork.this,HomeworkActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(AddHomeWork.this, "Some Problem", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(AddHomeWork.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            } else {
                dialog.dismiss();
                //  Toast.makeText(Registration.this, "No Response From Server", Toast.LENGTH_LONG).show();
            }

        }
    }


    public class spinnerStudentExecuteTask extends AsyncTask<String, Integer,String>{

            String output = "";


            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
                String sever_url = "https://jntrcpl.com/theoji/index.php/Api/student_list?id="+AppPreference.getUserid(AddHomeWork.this);


                output = HttpHandler.makeServiceCall(sever_url);
                System.out.println("getcomment_url" + output);
                return output;
            }

            @Override
            protected void onPostExecute(String output) {
                if (output == null) {
                } else {
                    try {

//                    Toast.makeText(RegistrationActivity.this, "result is" + output, Toast.LENGTH_SHORT).show();
                        JSONObject object=new JSONObject(output);

                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String user_id = jsonObject1.getString("user_id");
                            // String password = jsonObject1.getString("password");
                            // String showpass = jsonObject1.getString("showpass");
                            // String st_pass = jsonObject1.getString("stu_password");
                            String firstname = jsonObject1.getString("firstname");
                            // String lastname = jsonObject1.getString("lastname");
                            String email = jsonObject1.getString("email");
                            String mobile = jsonObject1.getString("mobileno");
                            //  String date = jsonObject1.getString("date");
                            //  String user_type = jsonObject1.getString("user_type");
                            // String gender = jsonObject1.getString("gender");
                            // String dob = jsonObject1.getString("dob");
                            String city = jsonObject1.getString("city");
                            String address = jsonObject1.getString("address");
                            // String status = jsonObject1.getString("status");
                            // String block_unblock = jsonObject1.getString("block_unblock");
                            // String ref_id = jsonObject1.getString("ref_id");
                            // String profileupdate = jsonObject1.getString("profileupdate");
                            // String about = jsonObject1.getString("about");
                            // String latest_post = jsonObject1.getString("latest_post");
                            // String latest_event = jsonObject1.getString("latest_event");
                            // String school_code = jsonObject1.getString("school_code");
                            //  String notice = jsonObject1.getString("notice");
                            //  String latest_activities = jsonObject1.getString("latest_activities");
                            //  String stutotalfees = jsonObject1.getString("stutotalfees");
                            //  String latest_news = jsonObject1.getString("latest_news");
                            // String sturemainfee = jsonObject1.getString("sturemainfee");
                            // String latest_noticboard = jsonObject1.getString("latest_noticboard");
                            // String stulastfee = jsonObject1.getString("stulastfee");
                            // String sales_lead_name = jsonObject1.getString("sales_lead_name");

                            studentList.add(new StudentModel(user_id, firstname));
                            StudentSpinHashMap.put(Integer.valueOf(user_id), firstname);
                            ChooseStudent.add(firstname);

                        }

                       studentListAdapter = new StudentListAdapter(AddHomeWork.this, android.R.layout.simple_spinner_item, studentList);
                       //StudentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                      // spin_student.setAdapter(StudentAdapter,false, onSelectedListener);
                        spin_student.setAdapter(studentListAdapter);

                        super.onPostExecute(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }



        }
//    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
//        public void onItemsSelected(boolean[] selected) {
//            // Do something here with the selected items
//
//        }
//    };


    public boolean validate() {
        boolean valid = false;

        String Title_homework = title.getText().toString();
        String  Description_homework = description.getText().toString();
        String Date_homework = homework_date.getText().toString();
        String  Participent = participent.getText().toString();
        String Spin_class = spin_class.getSelectedItem().toString();
       // String  Spin_student = spin_student.getSelected().toString();


        if (Title_homework.isEmpty()) {
            valid = false;
            title.setError("Please enter Title!");
        } else {
            valid = true;
            title.setError(null);
        }

        if (Description_homework.isEmpty()) {
            valid = false;
            description.setError("Please enter Description!");
        } else {
            valid = true;
            description.setError(null);
        }
        if (Date_homework.isEmpty()) {
            valid = false;
            homework_date.setError("Please enter date!");
        } else {
            valid = true;
            homework_date.setError(null);
        }
        if (Spin_class.equals("-select class-")) {
            valid = false;
            Toast.makeText(AddHomeWork.this,"Please select student class!",Toast.LENGTH_SHORT).show();

            return false;
        } else {
            valid = true;

        }

        return valid;
    }
    }

