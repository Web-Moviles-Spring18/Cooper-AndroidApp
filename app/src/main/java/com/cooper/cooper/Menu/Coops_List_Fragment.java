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

        this.fab1 = view.findViewById(R.id.fab2);
        this.fab1.setOnClickListener(this);

        this.pool_list = new ArrayList<>();
        GetRequests get_pool_list = new GetRequests(this);
        get_pool_list.execute(Utils.URL + "/profile/pools");


        try {
            JSONObject response = get_pool_list.get();
            JSONArray pool_list = new JSONArray(response.getString("response"));
            for (int i = 0; i < pool_list.length() ; i++) {
                JSONObject object = pool_list.getJSONObject(i);
                this.makePool(object.getJSONObject("node"));
                Log.d("Key", object.toString());
            }

            //JSONObject pool_list = new JSONObject(response.getString("response"));

            Log.d("response get pool list", pool_list.toString());
        } catch (Exception e) {
            Log.wtf("Get Pool List Error", e.toString());
        }


        this.listview_pools = (ListView) this.view.findViewById(R.id.coops_list);


        PoolListAdapter poolAdapter = new PoolListAdapter(pool_list, this.getActivity());
        listview_pools.setAdapter(poolAdapter);
        listview_pools.setOnItemClickListener(this);
        // Inflate the layout for this fragment
        return this.view;
    }

    public void makePool(JSONObject node) throws Exception {
        String pool_name = node.getString("name");
        String pool_label = "";//node.getString("label");
        String payment = node.getString("paymentMethod");
        boolean isPrivate = node.getBoolean("private");
        String currency = node.getString("currency");
        String invitation_code = "";//node.getString("invite");
        String ends = node.getString("ends");
        int id = node.getInt("_id");
        double total = node.getDouble("total");

        Pool new_pool = new Pool();
        new_pool.setName(pool_name);
        new_pool.setPayment_method(payment);
        new_pool.setIs_private(isPrivate);
        new_pool.setId(id);
        new_pool.setCurrency(currency);
        new_pool.setInvitation_code(invitation_code);
        new_pool.setEnd_date(ends);
        new_pool.setTotal(total);
        this.pool_list.add(new_pool);
    }
    /*public void createPool(View v){
        Intent intent = new Intent(this, CreateCoops_Act.class);
        this.startActivity(intent);
    }*/


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
                /*Log.d("FAB", "R.id.fab");
                Toast.makeText(getActivity(), "Action1", Toast.LENGTH_LONG).show();*/
               /* MainMenu menu = (MainMenu) this.getActivity();
                menu.replaceCoopCreateFragment(0, new Coops_Create_Fragment());*/
                /*this.fragmentManager.beginTransaction().setCustomAnimations(R.anim.right_enter_animation, R.anim.left_exit_animation)
                 .replace(R.id.viewpager, new Coops_Create_Fragment()).commit();*/
            case R.id.fab2:
                Intent joinCoop = new Intent(this.getActivity(), Join_Coop_Act.class);
                this.startActivity(joinCoop);
        }
    }

    @Override
    public void requestDone(Object objectRes, int statusCode) {
        /*if(objectRes instanceof JSONObject) {
            JSONObject response = (JSONObject) objectRes;
            try {
                //JSONObject response = get_pool_list.get();
                JSONArray pool_list = new JSONArray(response.getString("response"));
                for (int i = 0; i < pool_list.length() ; i++) {
                    JSONObject object = pool_list.getJSONObject(i);
                    this.makePool(object.getJSONObject("node"));
                    Log.d("Key", object.toString());
                }

                //JSONObject pool_list = new JSONObject(response.getString("response"));

                Log.d("response get pool list", pool_list.toString());
            } catch (Exception e) {
                Log.wtf("Get Pool List Error", e.toString());
            }
        }*/

    }
}
