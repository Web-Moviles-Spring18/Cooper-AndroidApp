package com.cooper.cooper.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.PostRequests;

import org.json.JSONObject;

public class MemberDetail_Act extends AppCompatActivity {

    Button submit;
    TextView coopName, memberName;
    EditText pending, paid;
    Long poolId;

    String coopNameTxt, memberEmailTxt, memberNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail_layout);

        Intent i = getIntent();
        this.memberEmailTxt = i.getStringExtra("email");
        this.coopNameTxt = i.getStringExtra("coopName");
        this.memberNameTxt = i.getStringExtra("name");
        this.poolId = i.getLongExtra("poolId", 0);

        this.submit = (Button) findViewById(R.id.submit);
        this.coopName = (TextView) findViewById(R.id.coopName);
        this.coopName.setText(this.coopNameTxt);
        this.memberName = (TextView) findViewById(R.id.member_name);
        this.memberName.setText(this.memberNameTxt);
        this.pending = (EditText) findViewById(R.id.pending);
        this.paid = (EditText) findViewById(R.id.paid);

    }

    public void updateMember(View view) {
        try {
            JSONObject updateMemberData = new JSONObject();
            updateMemberData.put("userEmail", this.memberEmailTxt);
            JSONObject userInfo = new JSONObject();
            userInfo.put("paid", this.paid.getText().toString());
            userInfo.put("debt", this.pending.getText().toString());
            updateMemberData.put("userInfo", userInfo);

            PostRequests updateMember = new PostRequests(updateMemberData);
            updateMember.execute(Utils.URL+"/pool/"+this.poolId);

            JSONObject response = updateMember.get();
            int response_status_code = response.getInt("status_code");
            Log.d("status_code", response_status_code+"");
            if(response_status_code == 200) {
                this.finish();
                new SuccessToast().Show_Toast(this, view, response.getString("response"));
                /*Intent i = new Intent(this, MainMenu.class);
                this.startActivity(i);*/
            } else {
                new AlertToast().Show_Toast(this, view, response.getString("response"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
