package com.cooper.cooper.Menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.GetRequests;
import com.cooper.cooper.http_requests.HTTPRequestListener;
import com.cooper.cooper.http_requests.PostRequests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class Join_Coop_Act extends AppCompatActivity implements HTTPRequestListener, AdapterView.OnItemClickListener{

    private ListView coops_toJoin;
    private ArrayList<JSONObject> coopsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_coop_layout);

        this.coops_toJoin = (ListView) findViewById(R.id.join_coops);
        this.coopsList = new ArrayList<>();

        this.getCoopsToJoin();

        JoinCoopsAdapter adapterCoops = new JoinCoopsAdapter(this.coopsList, this);
        this.coops_toJoin.setAdapter(adapterCoops);
        this.coops_toJoin.setOnItemClickListener(this);


    }

    private void getCoopsToJoin() {
        try {
            GetRequests getJoinCoops = new GetRequests(this);
            getJoinCoops.execute(Utils.URL.concat("/profile/pools/invites"));

            JSONObject response = getJoinCoops.get();
            JSONArray coopsJoin_list = new JSONArray(response.getString("response"));
            if(coopsJoin_list.length() > 0) {
                for (int i = 0; i < coopsJoin_list.length() ; i++) {
                    JSONObject object = coopsJoin_list.getJSONObject(i);
                    this.coopsList.add(object);
                    Log.d("Key", object.toString());
                }
            } else {
                JSONObject temp = new JSONObject();
                temp.put("name", "No invitations yet!");
                temp.put("total", 0.00);
                this.coopsList.add(temp);
            }


            //JSONObject pool_list = new JSONObject(response.getString("response"));
            Log.d("response get users list", coopsJoin_list.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestDone(Object object, int statusCode) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            int poolId = this.coopsList.get(i).getInt("_id");
            GetRequests acceptInvitation = new GetRequests(this);
            acceptInvitation.execute(Utils.URL.concat("/pool/accept/"+poolId));

            JSONObject response = acceptInvitation.get();
            if(response.getInt("status_code") == HttpURLConnection.HTTP_OK) {
                new SuccessToast().Show_Toast(this, view, response.getString("response"));
            } else {
                new AlertToast().Show_Toast(this, view, response.getString("response"));
            }
        } catch(Exception e) {
            new AlertToast().Show_Toast(this, view, "Cannot join to coop");
            e.printStackTrace();
        }
    }
}
