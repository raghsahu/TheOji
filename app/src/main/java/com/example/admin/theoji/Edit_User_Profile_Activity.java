package com.example.admin.theoji;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Connection.CameraUtils;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.Connection.Utilities;
import com.example.admin.theoji.Connection.Utility;
import com.example.admin.theoji.ModelClass.StateModel;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Edit_User_Profile_Activity extends AppCompatActivity {
    Spinner spin_state,std_board_name;
    ArrayList<String> ChooseState;
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList;
    String[] seperateData ;

    private String userChoosenTask;
    public  String imageStoragePath;
    private String imageStoragePath1;
    private String imageStoragePath2;
    int BannerAnInt=0 , ProfileAnInt=0;
    public static final int REQUEST_CAMERA = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    private Bundle savedInstanceState;
    int Gallery_view = 2;
    String Share_test;
    private Boolean upflag = false;

    Button btn_banner_img, btn_profile_img,btn_viewProfile;
    TextView tv_banner_img, tv_profile_img,sch_code;
    Button FinalSubmit;
   CheckBox check_box;

    EditText sch_name, sch_mail,sch_mobile, schtel_no,sch_add1,sch_add2,sch_city,sch_country,sch_schcode,sch_web,
              sch_pincode,sch_staff,sch_subtitle,sch_about,sch_pww;

    String Sch_name, Sch_mail,Sch_mobile, Schtel_no,Sch_add1,Sch_add2,Sch_city,Sch_country,Sch_schcode,Sch_web,
            Sch_pincode,Sch_staff,Sch_subtitle,Sch_about,Sch_pww;
    String Sch_state,Sch_board;

    private String server_url;
     public static String user_type;
    private String umeta_value;
    ImageView banner_image;
    CircleImageView progile_img;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_profile);

        sch_name = (EditText) findViewById(R.id.sch_name);
        sch_mail = (EditText) findViewById(R.id.sch_email);
        sch_mobile = (EditText) findViewById(R.id.sch_mobile);
        schtel_no = (EditText) findViewById(R.id.sch_tel_no);
        sch_add1 = (EditText) findViewById(R.id.sch_address);
        sch_add2 = (EditText) findViewById(R.id.sch_address1);
        sch_city = (EditText) findViewById(R.id.sch_city);
        sch_country = (EditText) findViewById(R.id.sch_country);
        sch_schcode = (EditText) findViewById(R.id.sch_sch_code);
        sch_web = (EditText) findViewById(R.id.sch_website);
        sch_pincode = (EditText) findViewById(R.id.sch_pincode);
        sch_staff = (EditText) findViewById(R.id.sch_nostaff);
        sch_subtitle = (EditText) findViewById(R.id.sch_sub_title);
        sch_about = (EditText) findViewById(R.id.sch_about);
        sch_pww = (EditText) findViewById(R.id.sch_pwwww);

        FinalSubmit=(Button)findViewById(R.id.submit_final);
        std_board_name=(Spinner)findViewById(R.id.sch_boardname);
        banner_image=(ImageView)findViewById(R.id.banner_image);
        progile_img=findViewById(R.id.profile_image);

        btn_banner_img=(Button)findViewById(R.id.btn_img1);
        btn_profile_img=(Button)findViewById(R.id.btn_img2);
        btn_viewProfile=(Button)findViewById(R.id.view_profile);
        tv_banner_img=(TextView)findViewById(R.id.banner_img_path);
        tv_profile_img=(TextView)findViewById(R.id.profile_img_path);
        sch_code=(TextView)findViewById(R.id.sch_code);

        check_box=findViewById(R.id.checkbox);

        btn_viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Edit_User_Profile_Activity.this,View_User_Profile_Activity.class);
                startActivity(intent);
                finish();

            }
        });

        FinalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sch_name=sch_name.getText().toString();
                Sch_mail=sch_mail.getText().toString();
                Sch_mobile=sch_mobile.getText().toString();
                Schtel_no=schtel_no.getText().toString();
                Sch_add1=sch_add1.getText().toString();
                Sch_add2=sch_add2.getText().toString();
                Sch_city=sch_city.getText().toString();
                Sch_country=sch_country.getText().toString();
                Sch_schcode=sch_schcode.getText().toString();
                Sch_web=sch_web.getText().toString();
                Sch_pincode=sch_pincode.getText().toString();
                Sch_staff=sch_staff.getText().toString();
                Sch_subtitle=sch_subtitle.getText().toString();
                Sch_about=sch_about.getText().toString();
                Sch_pww=sch_pww.getText().toString();
                Sch_state=spin_state.getSelectedItem().toString();
                Sch_board=std_board_name.getSelectedItem().toString();

                if (check_box.isChecked())
                {
                    new SchoolProfileUpdateExecuteTask().execute();
                }else {
                    Toast.makeText(Edit_User_Profile_Activity.this, "Please accept terms & conditions", Toast.LENGTH_LONG).show();
                }
               // new SchoolProfileUpdateExecuteTask().execute();
            }
        });


        btn_banner_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerAnInt=1;
                ProfileAnInt=0;
                selectImage();
            }
        });

        btn_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileAnInt =1;
                BannerAnInt =0;
                selectImage();
            }
        });

        spin_state = (Spinner) findViewById(R.id.state);
        stateList = new ArrayList<>();
        ChooseState = new ArrayList<>();
        new stateExecuteTask().execute();

        new SchoolGetUpdateExecuteTask().execute();



//*********************************************************
    }
        class stateExecuteTask extends AsyncTask<String, Integer, String> {

            String output = "";


            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

//                String sever_url = "http://saibabacollege.com/jobsjunction/Api/********";
                String sever_url = "https://jntrcpl.com/theoji/index.php/Api/get_all_state?id=" + AppPreference.getUserid(Edit_User_Profile_Activity.this);


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
                        JSONObject object = new JSONObject(output);
                        String res = object.getString("responce");

                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String sstate_id = jsonObject1.getString("sstate_id");
                            String sstate_name = jsonObject1.getString("sstate_name");


                            stateList.add(new StateModel(sstate_id, sstate_name));
                            ChooseState.add(sstate_name);

                        }

                        stateAdapter = new ArrayAdapter<String>(Edit_User_Profile_Activity.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_state.setAdapter(stateAdapter);

                        super.onPostExecute(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_User_Profile_Activity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Edit_User_Profile_Activity.this);

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

    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        //previewCapturedImage();
                    }
                }
            }
        }
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

    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, Gallery_view);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(Edit_User_Profile_Activity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                upflag = true;
                // Refreshing the gallery
//                previewCapturedImage();

                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                if(BannerAnInt ==1)
                {
                    imageStoragePath2 = imageStoragePath;
                    tv_banner_img.setText(imageStoragePath2);
                    Toast.makeText(this, "banner"+imageStoragePath2, Toast.LENGTH_SHORT).show();
                }
                if(ProfileAnInt ==1)
                {
                    imageStoragePath1 = imageStoragePath;
                    tv_profile_img.setText(imageStoragePath1);
                    Toast.makeText(this, "profile"+imageStoragePath1, Toast.LENGTH_SHORT).show();
                }

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

          //  postimage.setImageBitmap(BitmapFactory.decodeFile(imageStoragePath));
            upflag = true;
            cursor.close();

            if(BannerAnInt ==1)
            {
                imageStoragePath2 = imageStoragePath;
                tv_banner_img.setText(imageStoragePath2);
                Toast.makeText(this, "banner"+imageStoragePath2, Toast.LENGTH_SHORT).show();
            }
            if(ProfileAnInt ==1)
            {
                imageStoragePath1 = imageStoragePath;
                tv_profile_img.setText(imageStoragePath1);
                Toast.makeText(this, "profile"+imageStoragePath1, Toast.LENGTH_SHORT).show();
            }


        }

    }

    private class SchoolGetUpdateExecuteTask extends AsyncTask<String, Void, String> {

        String output = "";
        ProgressDialog dialog;
        private Object viewHolder;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Edit_User_Profile_Activity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                server_url = "https://jntrcpl.com/theoji/index.php/Api/get_school_detail?id="
                        + AppPreference.getUserid(Edit_User_Profile_Activity.this);


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            //   Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }


        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();

                    JSONObject responce = new JSONObject(output);
                    String res = responce.getString("responce");
                    JSONObject dataJsonObject = new JSONObject(output);

                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("data");
                        //String[] seperateData = new String[40];
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                            sch_about.setText(c.getString("about"));
                            sch_mail.setText(c.getString("email"));
                            sch_name.setText(c.getString("firstname"));
                            sch_mobile.setText(c.getString("mobileno"));
                            sch_add1.setText(c.getString("address"));
                            sch_city.setText(c.getString("city"));
                            sch_schcode.setText(c.getString("school_code"));
                            sch_code.setText(c.getString("school_code"));
                            sch_pww.setText(c.getString("showpass"));

                            String user_id = c.getString("user_id");
                            String lastname = c.getString("lastname");
                            user_type = c.getString("user_type");
                             umeta_value = c.getString("umeta_value");


                        }
                        seperateData = new String[umeta_value.split(",").length];
                        Log.e("length is",""+umeta_value.split(",").length);
                        seperateData = umeta_value.split(",");
                        Log.e("length is",""+seperateData[0]);
                        Log.e("length is",""+seperateData[1]);
                        Log.e("length is",""+seperateData[2]);
                        Log.e("length is",""+seperateData[3]);
                        Log.e("length is",""+seperateData[4]);
                        Log.e("length is",""+seperateData[5]);
                        Log.e("length is",""+seperateData[6]);
                        Log.e("length is",""+seperateData[7]);
                        Log.e("length is",""+seperateData[8]);
                        String address1 = seperateData[0];
                        sch_add2.setText(address1);
                        String state = seperateData[1];
                        String country = seperateData[2];
                        sch_country.setText(country);
                        String telno= seperateData[3];
                        schtel_no.setText(telno);
                        String imagename = seperateData[4];
                        Picasso.get()
                                .load("https://jntrcpl.com/theoji/uploads/"+imagename)
                                .into(Edit_User_Profile_Activity.this.progile_img);

                        // sch_country.setText(country);
                        String websiteurl = seperateData[5];
                        sch_web.setText(websiteurl);
                        String schoolcode = seperateData[6];
                        // sch_schcode.setText(schoolcode);
                        String affiliation_no = seperateData[7];
                        String pin_code = seperateData[8];
                        sch_pincode.setText(pin_code);
                        String board_name = seperateData[9];

                        String no_off_staff = seperateData[10];
                        sch_staff.setText(no_off_staff);
                        String bannerimage = seperateData[11];
                        Picasso.get()
                                .load("https://jntrcpl.com/theoji/uploads/"+bannerimage)
                                .into(Edit_User_Profile_Activity.this.banner_image);

                        String subtitle = seperateData[12];
                        sch_subtitle.setText(subtitle);
                        String established_year = seperateData[13];

                    } else {

                        Toast.makeText(Edit_User_Profile_Activity.this, "no details update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }
    }

    private class SchoolProfileUpdateExecuteTask extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;
         String result;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Edit_User_Profile_Activity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

//            String res = PostData(params);
//
//            return res;

            try {

                org.apache.http.entity.mime.MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                String id= AppPreference.getUserid(Edit_User_Profile_Activity.this);

                entity.addPart("bannerimage", new FileBody(new File(imageStoragePath2)));
                entity.addPart("profile", new FileBody(new File(imageStoragePath1)));
                entity.addPart("user_id",new StringBody(id));
                entity.addPart("user_type",new StringBody(user_type));
                entity.addPart("email",new StringBody(Sch_mail));
                entity.addPart("firstname",new StringBody(Sch_name));
                entity.addPart("mobileno",new StringBody(Sch_mobile));
                entity.addPart("telno",new StringBody(Schtel_no));
                entity.addPart("address",new StringBody(Sch_add1));
                entity.addPart("address1",new StringBody(Sch_add2));
                entity.addPart("city",new StringBody(Sch_city));
                entity.addPart("country",new StringBody(Sch_country));
                entity.addPart("school_code",new StringBody(Sch_schcode));
                entity.addPart("websiteurl",new StringBody(Sch_web));
                entity.addPart("pin_code",new StringBody(Sch_pincode));
                entity.addPart("no_off_staff",new StringBody(Sch_staff));
                entity.addPart("subtitle",new StringBody(Sch_subtitle));
                entity.addPart("about",new StringBody(Sch_about));
                entity.addPart("showpass",new StringBody(Sch_pww));
                entity.addPart("board_name",new StringBody(Sch_board));
                entity.addPart("state",new StringBody(Sch_state));

                result = Utilities.postEntityAndFindJson("https://jntrcpl.com/theoji/index.php/Api/profile_update", entity);

                return result;

            } catch (Exception e) {
                // something went wrong. connection with the server error
                e.printStackTrace();
            }
            return null;


        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                    Toast.makeText(Edit_User_Profile_Activity.this, "result is" + result, Toast.LENGTH_SHORT).show();

                    JSONObject object = new JSONObject(result);
                    String res = object.getString("response");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//                    String email=data.getString("firstname");
//                    String mobile=data.getString("lastname");
//                    String pass=data.getString("email");
//                    String alotclass=data.getString("mobileno");
//                    String address=data.getString("date");



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(Edit_User_Profile_Activity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Edit_User_Profile_Activity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Edit_User_Profile_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
//    public String PostData(String[] values) {
//        try {
//
//            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/profile_update");
//
//            JSONObject postDataParams = new JSONObject();
//            String id= AppPreference.getUserid(Edit_User_Profile_Activity.this);
//
//            postDataParams.put("user_id",id);
//            postDataParams.put("user_type", user_type);
//
//            postDataParams.put("email", Sch_mail);
//            postDataParams.put("firstname", Sch_name);
//            postDataParams.put("mobileno", Sch_mobile);
//            postDataParams.put("telno", Schtel_no);
//            postDataParams.put("address", Sch_add1);
//            postDataParams.put("address1", Sch_add2);
//            postDataParams.put("city", Sch_city);
//            postDataParams.put("country", Sch_country);
//            postDataParams.put("school_code", Sch_schcode);
//            postDataParams.put("websiteurl", Sch_web);
//            postDataParams.put("pin_code", Sch_pincode);
//            postDataParams.put("no_off_staff", Sch_staff);
//            postDataParams.put("subtitle", Sch_subtitle);
//            postDataParams.put("about", Sch_about);
//            postDataParams.put("showpass", Sch_pww);
//            postDataParams.put("board_name", Sch_board);
//            postDataParams.put("state", Sch_state);
//
//
//            Log.e("postDataParams", postDataParams.toString());
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(15000 /* milliseconds*/);
//            conn.setConnectTimeout(15000  /*milliseconds*/);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getPostDataString(postDataParams));
//
//            writer.flush();
//            writer.close();
//            os.close();
//            int responseCode = conn.getResponseCode();
//
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//
//                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                StringBuilder result = new StringBuilder();
//                String line;
//                while ((line = r.readLine()) != null) {
//                    result.append(line);
//                }
//                r.close();
//                return result.toString();
//
//            } else {
//                return new String("false : " + responseCode);
//            }
//        }
//        catch (Exception e) {
//            return new String("Exception: " + e.getMessage());
//        }
//    }
//
//    public String getPostDataString(JSONObject params) throws Exception {
//
//        StringBuilder result = new StringBuilder();
//        boolean first = true;
//
//        Iterator<String> itr = params.keys();
//
//        while (itr.hasNext()) {
//
//            String key = itr.next();
//            Object value = params.get(key);
//
//            if (first)
//                first = false;
//            else
//                result.append("&");
//
//            result.append(URLEncoder.encode(key, "UTF-8"));
//            result.append("=");
//            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
//
//        }
//        return result.toString();
//    }
}

