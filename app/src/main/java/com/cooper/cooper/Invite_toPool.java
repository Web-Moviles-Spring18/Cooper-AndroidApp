package com.cooper.cooper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cooper.cooper.http_requests.PostRequests;

import org.json.JSONObject;

public class Invite_toPool extends AppCompatActivity {

    private TextView emailId;
    private long poolId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_to_pool);

        this.emailId = (TextView) findViewById(R.id.send_email);
        Intent intent = getIntent();
        this.poolId = intent.getLongExtra("poolid", 289);
    }

    public void sendInvitation(View v) {
        try {
            JSONObject sendInvitation = new JSONObject();
            sendInvitation.put("email", this.emailId.getText().toString());

            PostRequests send_request = new PostRequests(sendInvitation);
            send_request.execute(Utils.URL + "/pool/"+this.poolId+"/invite");

            JSONObject response = send_request.get();
            int response_status_code = response.getInt("status_code");
            Log.d("status_" +
                    "code", response_status_code+"");
            if(response_status_code == 200) {
                Intent intent = new Intent(this, MainMenu.class);
                this.startActivity(intent);
            } else {
                new CustomToast().Show_Toast(this, v, response.getString("response"));
            }
        } catch (Exception e) {
            new CustomToast().Show_Toast(this, v, "Error");
            Log.d("LoginError", e.toString());
        }
    }
}
