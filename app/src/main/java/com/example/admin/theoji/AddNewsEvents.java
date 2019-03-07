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
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.theoji.Connection.CameraUtils;
import com.example.admin.theoji.Connection.CommonUtils;
import com.example.admin.theoji.Connection.Utilities;
import com.example.admin.theoji.Connection.Utility;
import com.example.admin.theoji.Shared_prefrence.AppPreference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class AddNewsEvents extends AppCompatActivity {


    ImageView postimage;
    EditText title, description;
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
    String Title_news, Description_news;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news_events);

        choose_img = (Button) findViewById(R.id.btn_img);
        btn_post = (Button) findViewById(R.id.btn_post);

        postimage = (ImageView) findViewById(R.id.post_image);
        title = (EditText) findViewById(R.id.titleaddnews);
        description = (EditText) findViewById(R.id.addnewsdescription);

        choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Title_news = title.getText().toString();
                Description_news = description.getText().toString();

//                if (Title_news.isEmpty() && Description_news.isEmpty()  ) {
//
//                    if(Title_news.isEmpty())
//                    {
//                        title.setError("Please Enter News Title");
//                        title.requestFocus();
//                    }
//
//                    if (Description_news.isEmpty()) {
//                        description.setError("Please Enter News Description");
//                        description.requestFocus();
//                    }
//
//
//                }
                if (validate()  )  {
                    Title_news = title.getText().toString();
                    Description_news = description.getText().toString();


                    previewCapturedImage();
                }

//                else {
//                    Toast.makeText(AddNewsEvents.this, "some problem", Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    //----------------------------------------------

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewsEvents.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddNewsEvents.this);

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
/*
            if (imgFile.exists()) {
                //  imgPolicyNo.setImageURI(Uri.fromFile(imgFile));
                //  show(imgFile);
                new ImageUploadTask(imgFile).execute();
                //  Toast.makeText(ClaimActivity.this,"data:="+imgFile,Toast.LENGTH_LONG).show();
            }*/

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
                        CameraUtils.openSettings(AddNewsEvents.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;
        String result = "";
        File Image;

        public ImageUploadTask(File imgFile) {
            this.Image = imgFile;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AddNewsEvents.this);
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

                String id= AppPreference.getUserid(AddNewsEvents.this);
                entity.addPart("newsimage", new FileBody(Image));
                entity.addPart("News_title", new StringBody(Title_news));
                entity.addPart("News_description", new StringBody(Description_news));
                entity.addPart("id",new StringBody(id));

                result = Utilities.postEntityAndFindJson("http://theoji.com/index.php/Api/news_and_update",entity);
//                        AppPreference.getUserid(AddNewsEvents.this), entity);

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
                    String img = object.getString("respone");
                    if (img.equals("true")) {
                        /* *//*  username.setText("");
                        email.setText("");
                        phone_no.setText("");
                        password.setText("");*//*
                        Intent i = new Intent(AddPostActivity.this, AdminDashboard.class);
                        i.putExtra("CheckingUser", "User");
                        startActivity(i);*/
                        Toast.makeText(AddNewsEvents.this, "Success", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AddNewsEvents.this,NewsActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(AddNewsEvents.this, "Some Problem", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(AddNewsEvents.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            } else {
                dialog.dismiss();
                //  Toast.makeText(Registration.this, "No Response From Server", Toast.LENGTH_LONG).show();
            }

        }
    }

    //.............................................................................
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

    //---------------------------

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

    //-------------------------------------

    public void AlertDialogCreate() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewsEvents.this, R.style.AlertDialogTheme);
        builder.setIcon(R.drawable.ic_launcher_background)
                .setTitle("Please Upload Image!")
                .setPositiveButton("OK", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectImage();
                        dialog.dismiss();
                    }
                }).show();

    }


    public boolean validate() {
        boolean valid = false;

       String Title_news = title.getText().toString();
      String  Description_news = description.getText().toString();

        if (Title_news.isEmpty()) {
            valid = false;
            title.setError("Please enter News Title!");
        } else {
            valid = true;
            title.setError(null);
        }

        if (Description_news.isEmpty()) {
            valid = false;
            description.setError("Please enter News Description!");
        } else {
            valid = true;
            description.setError(null);
        }
        return valid;
    }
}
