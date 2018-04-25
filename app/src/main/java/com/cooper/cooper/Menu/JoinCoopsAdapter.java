package com.cooper.cooper.Menu;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cooper.cooper.R;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ASUS on 22/02/2018.
 */

public class JoinCoopsAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<JSONObject> coops;

    public JoinCoopsAdapter(ArrayList<JSONObject> coops, Activity activity) {
        this.activity = activity;
        this.coops = coops;
    }
    @Override
    public int getCount() {
        return this.coops.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            // Crear view por primera vez
            view = this.activity.getLayoutInflater().inflate(R.layout.coops_to_join_list_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.coopName);
        TextView amount = (TextView) view.findViewById(R.id.coopAmount);

        try {
            if(this.coops.get(i).has("name")) {
                name.setText(this.coops.get(i).getString("name"));
            } else {
                name.setText("Not Founded");
            }
            amount.setText(this.coops.get(i).getDouble("total")+"");


        } catch (Exception e) {
            name.setText("Not Founded");
        }
        return view;
    }
}
