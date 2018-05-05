package com.cooper.cooper.Menu;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooper.cooper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        ImageView imagePool = (ImageView) view.findViewById(R.id.imagePool);

        /*Pool currentPool = pools.get(i);
        name.setText(currentPool.getName());
        amount.setText("$" + currentPool.getTotal());*/
        GetImageContent imageContent = new GetImageContent(this.activity, 800, 600);
        String urlImage = "";
        try {
            urlImage = this.coops.get(i).getString("picture");
        } catch (JSONException e) {
            Log.d("AdpaterPool", e.toString());
            //e.printStackTrace();
        }
        if(urlImage != null) {
            imageContent.execute(urlImage);
        }

        try {
            if(this.coops.get(i).has("name")) {
                name.setText(this.coops.get(i).getString("name"));
            }
            String amountTxt = "$"+this.coops.get(i).getDouble("total");
            amount.setText(amountTxt);
            imagePool.setImageDrawable(imageContent.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
