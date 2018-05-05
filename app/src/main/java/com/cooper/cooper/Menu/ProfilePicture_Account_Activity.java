package com.cooper.cooper.Menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.MainMenu;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.PostRequests;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class ProfilePicture_Account_Activity extends AppCompatActivity {

    private Button updateProfile;
    private ImageView profilePic;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic_layout);

        this.profilePic = (ImageView) findViewById(R.id.profilePic);
        this.updateProfile = (Button) findViewById(R.id.updateProfileBtn);
        Intent i = getIntent();
        String url = i.getStringExtra("url");
        this.setImageProfile(url);

        this.updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    public void updateProfilePic(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void postImage(String picturebase64) {
        try {
            JSONObject updateImageProfile = new JSONObject();
            updateImageProfile.put("picture", picturebase64);

            Log.d("create_pool_json", updateImageProfile.toString());

            PostRequests imagepoolrequest = new PostRequests(updateImageProfile);
            imagepoolrequest.execute(Utils.URL + "/account/profile");

            JSONObject response = imagepoolrequest.get();
            int response_status_code = response.getInt("status_code");
            Log.d("status_code", response_status_code+"");
            /*if(response_status_code == 200) {
                //this.finish();
                /*Intent i = new Intent(this, MainMenu.class);
                this.startActivity(i);
            } else {
                new AlertToast().Show_Toast(this, v, response.getString("response"));
            }*/
        } catch (Exception e) {
            //new AlertToast().Show_Toast(this, v, "Error");
            Log.d("CreatePoolError", e.toString());
        }
    }

    public void setImageProfile(String urlImage) {
        if(urlImage != null) {
            //this.profilePic.setImageURI(imageUri);
            GetImageContent imageContent = new GetImageContent(this, 150, 200);
            //String urlImage = "";
            imageContent.execute(urlImage);


            try {
                this.profilePic.setImageDrawable(imageContent.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            this.profilePic.setImageURI(imageUri);
            Log.d("MESSAGE", "activityREsult");
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Log.wtf("IMAGE64", encodeImage(selectedImage));
                String pictureBase64 = encodeImage(selectedImage);
                this.postImage(pictureBase64);
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }
}
