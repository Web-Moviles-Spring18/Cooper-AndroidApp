package com.cooper.cooper.Menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.MainActivity;
import com.cooper.cooper.MainMenu;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.PostRequests;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CreateCoops_Act extends AppCompatActivity {

    private TextView pool_name,
                     pool_amount,
                     start_date,
                     end_date;
    private RadioButton isCreditCard,
                        isCash,
                        isPrivate,
                        isPublic;

    private String picutreBase64;
    private static int RESULT_LOAD_IMAGE = 1;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);

        this.initComponents();
    }

    private void initComponents() {
        this.pool_name = (TextView) findViewById(R.id.pool_name);
        this.pool_amount = (TextView) findViewById(R.id.pool_amount);
        this.start_date = (TextView) findViewById(R.id.start_date);
        this.end_date = (TextView) findViewById(R.id.end_date);
        this.isCreditCard = (RadioButton) findViewById(R.id.credit_card);
        this.isCash = (RadioButton) findViewById(R.id.cash);
        this.isPrivate = (RadioButton) findViewById(R.id.is_private);
        this.isPublic = (RadioButton) findViewById(R.id.is_public);
    }

    public void createPool(View v) {
        //PostRequests createPool = new PostRequests()
        try {
            double amount = 0;
            try {
               amount = Double.parseDouble(this.pool_amount.getText().toString());
            } catch (Exception e) {
                new AlertToast().Show_Toast(this, v, "Error, must be an amount");
            }
            String payment = "cash";
            if(this.isCreditCard.isChecked()) {
                payment = "credit";
            } else if(this.isCash.isChecked()) {
                payment = "cash";
            }
            JSONObject create_pool_json = new JSONObject();
            create_pool_json.put("name", this.pool_name.getText().toString());
            create_pool_json.put("total", amount);
            create_pool_json.put("private", this.isPrivate.isChecked());
            create_pool_json.put("paymentMethod", payment);
            create_pool_json.put("currency", "mxn");
            create_pool_json.put("picture", this.picutreBase64);

            Log.d("create_pool_json", create_pool_json.toString());

            PostRequests create_pool_request = new PostRequests(create_pool_json);
            create_pool_request.execute(Utils.URL + "/pool");

            JSONObject response = create_pool_request.get();
            int response_status_code = response.getInt("status_code");
            Log.d("status_code", response_status_code+"");
            if(response_status_code == 200 || response_status_code == 201) {
                //this.finish();
                Intent i = new Intent(this, MainMenu.class);
                this.startActivity(i);
            } else {
                new AlertToast().Show_Toast(this, v, response.getString("response"));
            }
        } catch (Exception e) {
            new AlertToast().Show_Toast(this, v, "Error");
            Log.d("CreatePoolError", e.toString());
        }

    }

    public void uploadImage(View view) {
        Toast.makeText(this, "Upload Image", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Log.d("MESSAGE", "activityREsult");
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Log.wtf("IMAGE64", encodeImage(selectedImage));
                this.picutreBase64 = encodeImage(selectedImage);
            } catch(Exception e) {
                e.printStackTrace();
            }


        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}
