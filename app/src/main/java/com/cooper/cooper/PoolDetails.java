package com.cooper.cooper;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cooper.cooper.http_requests.GetRequests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PoolDetails extends AppCompatActivity {

    private ListView members_listview;
    private TextView pool_name;
    private TextView pool_amount;
    private TextView payment_method;
    private TextView pool_pending;
    private TextView pool_paid;
    private String invitation_code;
    private ArrayList<JSONObject> members;
    private long pool_id;
    private double tempAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_details);

        this.members_listview = (ListView) findViewById(R.id.members);
        this.pool_name = (TextView) findViewById(R.id.name);
        this.pool_amount = (TextView) findViewById(R.id.amount);
        this.payment_method = (TextView) findViewById(R.id.payment);
        this.pool_paid = (TextView) findViewById(R.id.paid);
        this.pool_pending = (TextView) findViewById(R.id.pending);

        Intent intent = getIntent();
        this.pool_id = intent.getLongExtra("pool", 267);
        this.members = new ArrayList<>();

        try {
            GetRequests get_pool_data = new GetRequests();
            get_pool_data.execute(Utils.URL + "/pool/"+pool_id);

            JSONObject response = get_pool_data.get();
            JSONObject pool_data= new JSONObject(response.getString("response"));
            this.setPoolData(pool_data.getJSONObject("pool"));
            Log.d("pool_members", pool_data.toString());
            JSONArray pool_members = pool_data.getJSONArray("participants");

            /*
                FIXME: this method, get from GET REQUEST, pending backend
             */
            double amount_each = this.tempAmount / pool_members.length();
            for (int i = 0; i < pool_members.length() ; i++) {
                JSONObject object = pool_members.getJSONObject(i);
                JSONObject member = new JSONObject();
                member.put("name", object.getJSONObject("node").getString("name"));
                member.put("amount", amount_each);
                this.setMembers_listview(member);
            }
            //JSONObject pool_list = new JSONObject(response.getString("response"));

            Log.d("response get pool list", pool_data.toString());
        } catch (Exception e) {
            Log.d("Get Pool List Error", e.toString());
        }


         MemberListAdapter membersAdapter = new MemberListAdapter(members, this);

        this.members_listview.setAdapter(membersAdapter);
    }

    public void setPoolData(JSONObject node) throws Exception {
        String pool_name = node.getString("name");
        String payment = node.getString("paymentMethod");
        boolean isPrivate = node.getBoolean("private");
        String currency = node.getString("currency");
        String invitation_code = node.getString("invite");
        String ends = node.getString("ends");
        int id = node.getInt("_id");
        double total = node.getDouble("total");
        this.tempAmount = total;

        this.invitation_code = invitation_code;
        this.pool_name.setText(pool_name);
        this.pool_amount.setText("$"+total);
        this.payment_method.setText("Payment: " + payment.toUpperCase());
        this.pool_pending.setText("Pend: $"+total);
        this.pool_paid.setText("Paid: $"+0.00);
    }

    public void setMembers_listview(JSONObject member) throws Exception {
        this.members.add(member);
    }

    public void goToInvitePool(View view) {
        Log.d("go to invitation", this.pool_id+"");
        Intent i = new Intent(this, Invite_toPool.class);
        i.putExtra("poolid",this.pool_id);
        this.startActivity(i);
    }
}
