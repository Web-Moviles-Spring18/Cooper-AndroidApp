package com.cooper.cooper;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ASUS on 22/02/2018.
 */

public class PoolListAdapter extends BaseAdapter{

    private ArrayList<Pool> pools;
    private Activity activity;

    public PoolListAdapter(ArrayList<Pool> pools, Activity activity) {
        this.pools = pools;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.pools.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // View - la row (justamente)
        // si ya fue creada antes a recibimos de parámetro
        // si no es null

        if(view == null) {
            // Crear view por primera vez
            view = activity.getLayoutInflater().inflate(R.layout.pools_list_view, null);
        }

        TextView name = (TextView) view.findViewById(R.id.pool_name);
        TextView amount = (TextView) view.findViewById(R.id.amount);
        TextView quantity_members = (TextView) view.findViewById(R.id.quantity_members);

        Pool currentPool = pools.get(i);
        name.setText(currentPool.getName());
        amount.setText(currentPool.getTotal() + "");
        quantity_members.setText(currentPool.getQuantityMember() + "");

        return view;
    }
}
