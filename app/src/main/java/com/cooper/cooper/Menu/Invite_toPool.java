package com.cooper.cooper.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.MainMenu;
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

public class Invite_toPool extends AppCompatActivity implements HTTPRequestListener, AdapterView.OnItemClickListener{

    private EditText userForSearch;
    private Button searchUser;
    private ListView users_listview;
    private ArrayList<JSONObject> users;

    private long poolId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_invite_to_pool);

        Intent intent = getIntent();
        this.poolId = intent.getLongExtra("poolid", 23);

        this.userForSearch = (EditText) findViewById(R.id.searchUser);
        this.searchUser = (Button) findViewById(R.id.searchBtn);
        this.users_listview = (ListView) findViewById(R.id.users_list);

        this.users = new ArrayList<>();
        UserListAdapter userListAdapter = new UserListAdapter(this.users, this);

        this.users_listview.setAdapter(userListAdapter);
        this.users_listview.setOnItemClickListener(this);

    }

    public void searchUser(View v) {
            this.users.clear();
            GetRequests searchUsers = new GetRequests(this);
            String userForSearch = this.userForSearch.getText().toString();
            Log.wtf("user", userForSearch);
            searchUsers.execute(Utils.URL + "/user/search/"+userForSearch);
            try {
                JSONObject response = searchUsers.get();
                JSONArray users_list = new JSONArray(response.getString("response"));
                for (int i = 0; i < users_list.length() ; i++) {
                    JSONObject object = users_list.getJSONObject(i);
                    this.makeUser(object);
                    Log.d("Key", object.toString());
                }

                //JSONObject pool_list = new JSONObject(response.getString("response"));
                Log.d("users list arr", this.users.toString());
                Log.d("response get users list", users_list.toString());
                this.getIntent();
            } catch (Exception e) {
                Log.wtf("search user error", e.toString());
            }

    }

    public void makeUser(JSONObject node) throws Exception {
        String name = "";
        String email = "";
        int id = 0;

        if(node.has("name")) {
            name = node.getString("name");
        }
        if(node.has("email")) {
            email = node.getString("email");
        }
        if(node.has("_id")) {
            id = node.getInt("_id");
        }

        this.users.add(node);
    }

    @Override
    public void requestDone(Object object, int statusCode) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            JSONObject postData = new JSONObject();
            postData.put("email", this.users.get(i).getString("email"));
            PostRequests sendInvitation = new PostRequests(postData);
            sendInvitation.execute(Utils.URL + "/pool/"+poolId+"/invite");

            JSONObject response = sendInvitation.get();
            int responseCode = response.getInt("status_code");
            String responseStr = response.getString("response");
            Log.d("responseCodeInvite", responseCode+"");
            Log.d("responseCodeStr", responseStr);
            if(responseCode == HttpURLConnection.HTTP_OK) {
                new SuccessToast().Show_Toast(this, view, responseStr);
            } else {
                new AlertToast().Show_Toast(this, view, responseStr);
            }

        } catch(Exception e) {
            new AlertToast().Show_Toast(this, view, "Error");
            e.printStackTrace();
        }

    }
}
