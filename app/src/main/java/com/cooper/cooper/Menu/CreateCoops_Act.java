package com.cooper.cooper.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.MainActivity;
import com.cooper.cooper.MainMenu;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.PostRequests;

import org.json.JSONObject;

public class CreateCoops_Act extends AppCompatActivity {

    private TextView pool_name,
                     pool_amount,
                     start_date,
                     end_date;
    private RadioButton isCreditCard,
                        isCash,
                        isPrivate,
                        isPublic;

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

            Log.d("create_pool_json", create_pool_json.toString());

            PostRequests create_pool_request = new PostRequests(create_pool_json);
            create_pool_request.execute(Utils.URL + "/pool");

            JSONObject response = create_pool_request.get();
            int response_status_code = response.getInt("status_code");
            Log.d("status_code", response_status_code+"");
            if(response_status_code == 200) {
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
}
