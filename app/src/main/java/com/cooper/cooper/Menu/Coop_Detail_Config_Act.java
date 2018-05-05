package com.cooper.cooper.Menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.MainMenu;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.DeleteRequest;
import com.cooper.cooper.http_requests.PostRequests;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class Coop_Detail_Config_Act extends AppCompatActivity {

    TextView poolName;
    Button deletePoolBtn, askPaymentButton;
    Long poolId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coop_detail_config_layout);

        Intent intent = getIntent();
        String poolName = intent.getStringExtra("pool_name");
        this.poolId = intent.getLongExtra("pool_id", 0);

        this.deletePoolBtn = findViewById(R.id.deletePool);
        this.askPaymentButton = findViewById(R.id.notifyAll);
        this.poolName = findViewById(R.id.poolName);
        final Context context = this;

        this.askPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostRequests req = new PostRequests(new JSONObject());
                req.execute(Utils.URL + "/pool/" + poolId.toString() + "/ask_payment");
                new SuccessToast().Show_Toast(context, view, "Users notified!");
            }
        });

        this.poolName.setText(poolName);

    }

    public void deletePool(View view) {
        DeleteRequest deletePool = new DeleteRequest();
        deletePool.execute(Utils.URL + "/pool/" + this.poolId);

        try {
            Toast.makeText(this, deletePool.get().getString("response"), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MainMenu.class);
            startActivity(i);
        } catch(Exception error) {
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }

    }
}
