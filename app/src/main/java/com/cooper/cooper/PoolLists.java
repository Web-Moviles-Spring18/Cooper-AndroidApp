package com.cooper.cooper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cooper.cooper.http_requests.GetRequests;

import org.json.JSONObject;

import java.util.ArrayList;

public class PoolLists extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<Pool> pool_list;
    private ListView listview_pools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_list);

        try {
            GetRequests get_pool_list = new GetRequests();
            get_pool_list.execute("http://services.groupkt.com/country/get/all");

            JSONObject response = get_pool_list.get();
            JSONObject data = new JSONObject("[\n" +
                    "    {\n" +
                    "        \"relation\": {\n" +
                    "            \"identity\": 63,\n" +
                    "            \"start\": 41,\n" +
                    "            \"end\": 61,\n" +
                    "            \"type\": \"participatesIn\",\n" +
                    "            \"properties\": {}\n" +
                    "        },\n" +
                    "        \"node\": {\n" +
                    "            \"label\": \"Pool\",\n" +
                    "            \"name\": \"Barezito\",\n" +
                    "            \"paymentMethod\": \"cash\",\n" +
                    "            \"private\": true,\n" +
                    "            \"currency\": \"mxn\",\n" +
                    "            \"ends\": \"2018-02-23T07:02:09.000Z\"\n" +
                    "        }\n" +
                    "    }]");
            Log.d("response get pool list", data.toString());
        } catch (Exception e) {
            Log.d("Get Pool List Error", e.toString());
        }


        this.listview_pools = (ListView) findViewById(R.id.list_pools);

        ArrayList<String> members = new ArrayList<String>();
        members.add("Marco");
        members.add("Poncho");
        members.add("Luly");
        members.add("Chuy");
        this.pool_list = new ArrayList<Pool>();
        this.pool_list.add(new Pool("Bitches", 1000, true, "cash", "usd", "20/03/2018", "27/03/2018", members));
        this.pool_list.add(new Pool("Bitches", 1000, true, "cash", "usd", "20/03/2018", "27/03/2018", members));
        this.pool_list.add(new Pool("Bitches", 1000, true, "cash", "usd", "20/03/2018", "27/03/2018", members));
        this.pool_list.add(new Pool("Bitches", 1000, true, "cash", "usd", "20/03/2018", "27/03/2018", members));
        this.pool_list.add(new Pool("Bitches", 1000, true, "cash", "usd", "20/03/2018", "27/03/2018", members));
        this.pool_list.add(new Pool("Bitches", 1000, true, "cash", "usd", "20/03/2018", "27/03/2018", members));this.pool_list.add(new Pool("Bitches", 1000, true, "cash", "usd", "20/03/2018", "27/03/2018", members));



        PoolListAdapter poolAdapter = new PoolListAdapter(pool_list, this);
        listview_pools.setAdapter(poolAdapter);
        listview_pools.setOnItemClickListener(this);
    }

    public void createPool(View v){
        Intent intent = new Intent(this, CreatePool.class);
        this.startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, PoolDetails.class);
        this.startActivity(intent);
    }
}
