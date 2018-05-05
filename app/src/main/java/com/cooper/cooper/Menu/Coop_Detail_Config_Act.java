package com.cooper.cooper.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cooper.cooper.MainMenu;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.DeleteRequest;

import org.w3c.dom.Text;

public class Coop_Detail_Config_Act extends AppCompatActivity {

    TextView poolName;
    Button deletePoolBtn;
    Long poolId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coop_detail_config_layout);

        Intent intent = getIntent();
        String poolName = intent.getStringExtra("pool_name");
        this.poolId = intent.getLongExtra("pool_id", 0);

        this.deletePoolBtn = (Button) findViewById(R.id.deletePool);
        this.poolName = (TextView) findViewById(R.id.poolName);

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
