package com.cooper.cooper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PoolDetails extends AppCompatActivity {

    private ListView members_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_details);

        this.members_listview = (ListView) findViewById(R.id.members);
        ArrayList<String> members = new ArrayList<>();
        members.add("Quinceañero");
        members.add("Chuy");
        MemberListAdapter membersAdapter = new MemberListAdapter(members, this);

        this.members_listview.setAdapter(membersAdapter);
    }
}
