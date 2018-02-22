package com.cooper.cooper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class PoolLists extends AppCompatActivity {

    private ArrayList<Pool> pool_list;
    private ListView listview_pools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pool_list_layout);

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
    }

    public void createPool(View v){

    }
}
