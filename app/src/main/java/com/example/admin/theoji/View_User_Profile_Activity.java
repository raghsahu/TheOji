package com.example.admin.theoji;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.theoji.Adapter.AboutUsAdapter;
import com.example.admin.theoji.Adapter.AlbumAdapter;
import com.example.admin.theoji.Adapter.PostAdapter;
import com.example.admin.theoji.Connection.CameraUtils;
import com.example.admin.theoji.Connection.CommonUtils;
import com.example.admin.theoji.Connection.HttpHandler;
import com.example.admin.theoji.Connection.Utilities;
import com.example.admin.theoji.Connection.Utility;
import com.example.admin.theoji.ModelClass.AboutListModel;
import com.example.admin.theoji.ModelClass.AlbumListModel;
import com.example.admin.theoji.ModelClass.PostListModel;
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
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.admin.theoji.Edit_User_Profile_Activity.user_type;

public class View_User_Profile_Activity extends AppCompatActivity {
    TextView tv_total_std,tv_request_std,tv_conferm_std,tv_sch_code;
    Button btn_edit_profile,btn_add_about,btn_advertise;
    LinearLayout ll_aboutus,ll_sch_album,ll_view_about,llview_title,ll_view_album;
     String school_code;
     ImageView banner_image,edit_banner,edit_profile_img;
     CircleImageView profile_img;

     EditText et_title, et_discription;

    RecyclerView about_recycler,Album_recycler;
    String server_url;
    ArrayList<AboutListModel> AboutUsList=new ArrayList<AboutListModel>();
    private AboutUsAdapter aboutUsAdapter;

    ArrayList<AlbumListModel> AlbumList=new ArrayList<AlbumListModel>();
    private AlbumAdapter albumAdapter;
     String Et_title,Et_description;

    int BannerAnInt=0 , ProfileAnInt=0;
    private String userChoosenTask;
    public  String imageStoragePath;
    private String imageStoragePath1;
    private String imageStoragePath2;
    public static final int REQUEST_CAMERA = 0;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    private Bundle savedInstanceState;
    int Gallery_view = 2;

    CardView card_total,card_request,card_conf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        tv_total_std=(TextView)findViewById(R.id.tv_total_std);
        tv_request_std=(TextView)findViewById(R.id.tv_request_std);
        tv_conferm_std=(TextView)findViewById(R.id.tv_conferm_std);
        tv_sch_code=(TextView)findViewById(R.id.sch_code);

        et_discription=findViewById(R.id.about_description);
        et_title=findViewById(R.id.title_about);

        card_conf=findViewById(R.id.card_conferm);
        card_request=findViewById(R.id.card_request);
        card_total=findViewById(R.id.card_total);

        btn_edit_profile=(Button)findViewById(R.id.edit_profile);
       // btn_add_about=(Button)findViewById(R.id.btn_add_about);
        btn_advertise=(Button)findViewById(R.id.btn_advertise);

        ll_aboutus=(LinearLayout)findViewById(R.id.about_us);
        ll_sch_album=(LinearLayout)findViewById(R.id.sch_album);
        ll_view_about=(LinearLayout)findViewById(R.id.llview_about_us);
        llview_title=(LinearLayout)findViewById(R.id.ll_title_post);
        ll_view_album=(LinearLayout)findViewById(R.id.llview_album);

        banner_image=(ImageView)findViewById(R.id.bannere_image);
        edit_banner=(ImageView)findViewById(R.id.edit_banner_foto);
        edit_profile_img=(ImageView)findViewById(R.id.change_profile_image);
        profile_img=findViewById(R.id.profile_image);

        about_recycler = (RecyclerView)findViewById(R.id.about_us_recycler);
        Album_recycler = (RecyclerView)findViewById(R.id.album_recycler);

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(View_User_Profile_Activity.this,Edit_User_Profile_Activity.class);
                startActivity(intent);
                finish();

            }
            });
        edit_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerAnInt=1;
                ProfileAnInt=0;
                selectImage();

            }
        });

        edit_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerAnInt=0;
                ProfileAnInt=1;
                selectImage();

            }
        });



        btn_advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          Et_title=et_title.getText().toString();
          Et_description=et_discription.getText().toString();

                new PostAboutExcuteTask().execute();

            }
        });

        ll_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ll_view_about.setVisibility(View.VISIBLE);
                ll_view_album.setVisibility(View.GONE);
                llview_title.setVisibility(View.VISIBLE);


            }
        });

        ll_sch_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_view_about.setVisibility(View.GONE);
                llview_title.setVisibility(View.GONE);
                ll_view_album.setVisibility(View.VISIBLE);

            }
        });

        new GetViewProfileExcuteTask().execute();
//*************************************************************

        card_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(View_User_Profile_Activity.this,StudentActivity.class);
                startActivity(intent);
                //finish();

            }
        });
        card_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(View_User_Profile_Activity.this,StudentActivity.class);
                startActivity(intent);
                //finish();

            }
        });
        card_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(View_User_Profile_Activity.this,StudentActivity.class);
                startActivity(intent);
                //finish();

            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(View_User_Profile_Activity.this);
        if(BannerAnInt ==1)
        {
            builder.setTitle("Change Banner Photo!");
        }
        if(ProfileAnInt ==1)
        {
            builder.setTitle("Change Profile Photo!");
        }

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(View_User_Profile_Activity.this);

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

            File imgFile = new File(imageStoragePath);

            if(BannerAnInt ==1)
            {
                banner_image.setImageBitmap(bitmap);
               // new ImageCompression().execute(imageStoragePath2);
            }
            if(ProfileAnInt ==1)
            {
                profile_img.setImageBitmap(bitmap);
               // new ImageCompression().execute(imageStoragePath1);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                previewCapturedImage();

                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                if(BannerAnInt ==1)
                {
                    imageStoragePath2 = imageStoragePath;
                    //tv_banner_img.setText(imageStoragePath2);
                    Toast.makeText(this, "banner"+imageStoragePath2, Toast.LENGTH_SHORT).show();

                    if (imageStoragePath2 != null) {
                        File imgFile = new File(imageStoragePath2);
                        if (imgFile.exists()) {
                            new ImageUploadTask(imgFile).execute();
                        }
                    }
                }
                if(ProfileAnInt ==1)
                {
                    imageStoragePath1 = imageStoragePath;
                   // tv_profile_img.setText(imageStoragePath1);
                    Toast.makeText(this, "profile"+imageStoragePath1, Toast.LENGTH_SHORT).show();

                    if (imageStoragePath1 != null) {
                        File imgFile = new File(imageStoragePath1);
                        if (imgFile.exists()) {
                            new ImageUploadTask(imgFile).execute();
                        }
                    }
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

            if(BannerAnInt ==1)
            {
                banner_image.setImageBitmap(BitmapFactory.decodeFile(imageStoragePath));

            }
            if(ProfileAnInt ==1)
            {
                profile_img.setImageBitmap(BitmapFactory.decodeFile(imageStoragePath));

            }

            cursor.close();

            if(BannerAnInt ==1)
            {
                imageStoragePath2 = imageStoragePath;
                Toast.makeText(this, "banner"+imageStoragePath2, Toast.LENGTH_SHORT).show();

                if (imageStoragePath2 != null) {
                    File imgFile = new File(imageStoragePath2);
                    if (imgFile.exists()) {
                         new ImageUploadTask(imgFile).execute();
                    }
                }
            }
            if(ProfileAnInt ==1)
            {
                imageStoragePath1 = imageStoragePath;
                // tv_profile_img.setText(imageStoragePath1);
                Toast.makeText(this, "profile"+imageStoragePath1, Toast.LENGTH_SHORT).show();

                if (imageStoragePath1 != null) {
                    File imgFile = new File(imageStoragePath1);
                    if (imgFile.exists()) {
                        new ImageUploadTask(imgFile).execute();
                    }
                }
            }
        }

    }


    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(View_User_Profile_Activity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }


   //*************************************************************************
    private class GetViewProfileExcuteTask extends AsyncTask<String, Void, String> {
            String output = "";
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(View_User_Profile_Activity.this);
                dialog.setMessage("Processing");
                dialog.setCancelable(true);
                dialog.show();
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/profile_view");
                   // server_url = "https://jntrcpl.com/theoji/index.php/Api/profile_view";

//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.e("sever_url>>>>>>>>>", server_url);
//                output = HttpHandler.makeServiceCall(server_url);
//                //   Log.e("getcomment_url", output);
//                System.out.println("getcomment_url" + output);
//                return output;

                    JSONObject postDataParams = new JSONObject();
                    postDataParams.put("id",AppPreference.getUserid(View_User_Profile_Activity.this));
                   // postDataParams.put("user_type",user_type);


                    Log.e("postDataParams", postDataParams.toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds*/);
                    conn.setConnectTimeout(15000  /*milliseconds*/);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            result.append(line);
                        }
                        r.close();
                        return result.toString();

                    } else {
                        return new String("false : " + responseCode);
                    }
                }
                catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
            }


            @Override
            protected void onPostExecute(String output) {
                if (output == null) {
                    dialog.dismiss();
                } else {
                    try {
                        dialog.dismiss();
                       // Toast.makeText(View_User_Profile_Activity.this, "qq"+output, Toast.LENGTH_SHORT).show();

                        JSONObject dataJsonObject = new JSONObject(output);
                        String res = dataJsonObject.getString("responce");
                        String image = dataJsonObject.getString("image");
                        Picasso.get()
                                .load("https://jntrcpl.com/theoji/uploads/"+image)
                                .into(View_User_Profile_Activity.this.profile_img);

                        AppPreference.setProfileImage(View_User_Profile_Activity.this,image);

                        String total = dataJsonObject.getString("total");
                        tv_total_std.setText(total);
                       // Toast.makeText(View_User_Profile_Activity.this, "aa"+total, Toast.LENGTH_SHORT).show();
                        String request = dataJsonObject.getString("request");
                        tv_request_std.setText(request);
                        String confirm = dataJsonObject.getString("confirm");
                        tv_conferm_std.setText(confirm);
                        String banner_image = dataJsonObject.getString("banner_image");

                        Picasso.get()
                                .load("https://jntrcpl.com/theoji/uploads/"+banner_image)
                                .into(View_User_Profile_Activity.this.banner_image);

                        if (res.equals("true")) {

                            JSONArray Data_array = dataJsonObject.getJSONArray("data");
                            for (int i = 0; i < Data_array.length(); i++) {
                                JSONObject c = Data_array.getJSONObject(i);

                                String user_id = c.getString("user_id");
                                String firstname = c.getString("firstname");
                                AppPreference.setFirstname(View_User_Profile_Activity.this,firstname);
                                String school_code = c.getString("school_code");

                                tv_sch_code.setText(school_code);
                            }

                           // tv_sch_code.setText(school_code);


                            JSONArray Data_array1 = dataJsonObject.getJSONArray("About_Us");
                            for (int i = 0; i < Data_array1.length(); i++) {
                                JSONObject d = Data_array1.getJSONObject(i);

                                String ad_id = d.getString("ad_id");
                                String ad_description = d.getString("ad_description");
                                String ad_date = d.getString("ad_date");
                                String ad_title = d.getString("ad_title");
                                String user_id = d.getString("user_id");

                                AboutUsList.add(i, new AboutListModel(ad_id,ad_description,ad_date,ad_title, user_id));
                            }
                            aboutUsAdapter = new AboutUsAdapter(View_User_Profile_Activity.this,AboutUsList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(View_User_Profile_Activity.this);
                            about_recycler.setLayoutManager(mLayoutManager);
                            about_recycler.setItemAnimator(new DefaultItemAnimator());
                            about_recycler.setAdapter(aboutUsAdapter);

                            JSONArray Data_array2 = dataJsonObject.getJSONArray("album");
                            for (int i = 0; i < Data_array2.length(); i++) {
                                JSONObject d = Data_array2.getJSONObject(i);

                                String post_id = d.getString("post_id");
                                String post_author = d.getString("post_author");
                                String post_date = d.getString("post_date");
                                String post_title = d.getString("post_title");
                                String post_content = d.getString("post_content");
                                String post_type = d.getString("post_type");
                                String ref_id = d.getString("ref_id");
                                String pmeta_id = d.getString("pmeta_id");
                                String pmeta_key= d.getString("pmeta_key");
                                String pmeta_value = d.getString("pmeta_value");

                                AlbumList.add(i, new AlbumListModel(post_id,post_author,post_date,post_title, post_content,post_type,
                                        ref_id,pmeta_id,pmeta_key,pmeta_value));
                            }
                            albumAdapter = new AlbumAdapter(View_User_Profile_Activity.this,AlbumList);
                            RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(View_User_Profile_Activity.this);
                            Album_recycler.setLayoutManager(sLayoutManager);
                            Album_recycler.setItemAnimator(new DefaultItemAnimator());
                            Album_recycler.setAdapter(albumAdapter);

                        }else {

                            Toast.makeText(View_User_Profile_Activity.this, "no post update now, please refresh after some time!", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //  dialog.dismiss();
                    }
                    super.onPostExecute(output);
                }

            }
    }

    private class PostAboutExcuteTask  extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(View_User_Profile_Activity.this);
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String res = PostData(params);

            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                   //  Toast.makeText(View_User_Profile_Activity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

//                    JSONObject data= new JSONObject(result).getJSONObject("data");
//                    user_id=data.getString("user_id");
//                    String name=data.getString("username");
//



                    if (!res.equalsIgnoreCase("true")) {

                        Toast.makeText(View_User_Profile_Activity.this, "unsuccess", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(View_User_Profile_Activity.this, "success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(View_User_Profile_Activity.this, View_User_Profile_Activity.class);
                        startActivity(intent);
                        finish();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public String PostData(String[] values) {
        try {

            URL url = new URL("https://jntrcpl.com/theoji/index.php/Api/advertise");

            String id= AppPreference.getUserid(View_User_Profile_Activity.this);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("ad_title", Et_title);
            postDataParams.put("ad_description", Et_description);
            postDataParams.put("user_id",id);



            Log.e("postDataParams", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds*/);
            conn.setConnectTimeout(15000  /*milliseconds*/);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    result.append(line);
                }
                r.close();
                return result.toString();

            } else {
                return new String("false : " + responseCode);
            }
        }
        catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

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
                   // new ImageUploadTask(imgFile).execute();
                }
            } else {
               // AlertDialogCreate();
            }

        }
    }
//***************************************************************************************
    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;
        String result = "";
        File Image;

        public ImageUploadTask(File imgFile) {
            this.Image = imgFile;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(View_User_Profile_Activity.this);
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
                String id= AppPreference.getUserid(View_User_Profile_Activity.this);



                if(ProfileAnInt ==1)
                {
                    entity.addPart("imagename", new FileBody(Image));
                    entity.addPart("user_id",new StringBody(id));

                    result = Utilities.postEntityAndFindJson("https://jntrcpl.com/theoji/index.php/Api/uploadprofilephoto", entity);
                }
                if(BannerAnInt ==1)
                {
                    entity.addPart("bannerimage", new FileBody(Image));
                    entity.addPart("user_id",new StringBody(id));

                    result = Utilities.postEntityAndFindJson("https://jntrcpl.com/theoji/index.php/Api/uploadcoverphoto", entity);
                }



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

                    if(ProfileAnInt ==1)
                    {
                        String profileimage = object.getString("profileimage");

                        if (img.equals("true")) {
                            AppPreference.setProfileImage(View_User_Profile_Activity.this,profileimage);

                            Toast.makeText(View_User_Profile_Activity.this, "Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(View_User_Profile_Activity.this, "Some Problem", Toast.LENGTH_LONG).show();
                        }
                    }
                    if(BannerAnInt ==1)
                    {
                        if (img.equals("true")) {

                            Toast.makeText(View_User_Profile_Activity.this, "Success", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(View_User_Profile_Activity.this, "Some Problem", Toast.LENGTH_LONG).show();
                        }
                    }


                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(View_User_Profile_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            } else {
                dialog.dismiss();
                 // Toast.makeText(Registration.this, "No Response From Server", Toast.LENGTH_LONG).show();
            }

        }
    }
}
