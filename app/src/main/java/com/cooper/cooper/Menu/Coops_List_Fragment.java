package com.cooper.cooper.Menu;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.GetRequests;
import com.cooper.cooper.http_requests.HTTPRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Coops_List_Fragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, HTTPRequestListener{

    private View view;
    private ArrayList<Pool> pool_list;
    private ListView listview_pools;

    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FragmentManager fragmentManager;

    private JSONArray poolsData;

    public Coops_List_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_coops_list, container, false);

        this.fragmentManager = getActivity().getSupportFragmentManager();

        this.fab = view.findViewById(R.id.fab);
        this.fab.setOnClickListener(this);


        //this.pool_list = new ArrayList<>();
        this.poolsData = new JSONArray();
        GetRequests get_pool_list = new GetRequests(this);
        get_pool_list.execute(Utils.URL + "/profile/pools");


        try {
            JSONObject response = get_pool_list.get();
            JSONArray pool_list = new JSONArray(response.getString("response"));
            this.poolsData = pool_list;
            /*for (int i = 0; i < pool_list.length() ; i++) {
                JSONObject object = pool_list.getJSONObject(i);
                this.makePool(object.getJSONObject("node"));
                Log.d("Key", object.toString());
            }*/

            //JSONObject pool_list = new JSONObject(response.getString("response"));

            Log.d("response get pool list", pool_list.toString());
        } catch (Exception e) {
            Log.wtf("Get Pool List Error", e.toString());
        }


        this.listview_pools = (ListView) this.view.findViewById(R.id.coops_list);


        PoolListAdapter poolAdapter = new PoolListAdapter(this.poolsData, this.getActivity());

        listview_pools.setAdapter(poolAdapter);
        listview_pools.setOnItemClickListener(this);
        // Inflate the layout for this fragment
        return this.view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("ID pool", l+"");
        Intent intent = new Intent(this.view.getContext(), Coop_Detail_Act.class);
        intent.putExtra("pool", l);
        this.startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab:
                Intent createCoop = new Intent(this.getActivity(), CreateCoops_Act.class);
                this.startActivity(createCoop);
                break;
        }
    }

    @Override
    public void requestDone(Object objectRes, int statusCode) {

    }
}
