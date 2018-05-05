package com.cooper.cooper.Menu;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooper.cooper.Menu.Pool;
import com.cooper.cooper.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by ASUS on 22/02/2018.
 */

public class PoolListAdapter extends BaseAdapter{

    //private ArrayList<Pool> pools;
    private JSONArray pools;
    private Activity activity;

    public PoolListAdapter(JSONArray pools, Activity activity) {
        this.pools = pools;
        this.activity = activity;
    }

    /*
    JSONObject object = pool_list.getJSONObject(i);
                this.makePool(object.getJSONObject("node"));
     */
    @Override
    public int getCount() {
        return this.pools.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return this.pools.getJSONObject(i).getJSONObject("node").get("_id");
        } catch (JSONException e) {
            return i;
            //e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int i) {
        try {
            return Long.parseLong(this.pools.getJSONObject(i).getJSONObject("node").getString("_id"));
        } catch (JSONException e) {
            return i;
            //e.printStackTrace();
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // View - la row (justamente)
        // si ya fue creada antes a recibimos de parámetro
        // si no es null

        if(view == null) {
            // Crear view por primera vez
            view = activity.getLayoutInflater().inflate(R.layout.coops_list_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.title);
        TextView amount = (TextView) view.findViewById(R.id.totalAmount);
        ImageView background = (ImageView) view.findViewById(R.id.background);

        /*Pool currentPool = pools.get(i);
        name.setText(currentPool.getName());
        amount.setText("$" + currentPool.getTotal());*/
        GetImageContent imageContent = new GetImageContent(this.activity);
        String urlImage = "";
        try {
            name.setText(this.pools.getJSONObject(i).getJSONObject("node").getString("name"));
            amount.setText("$" + this.pools.getJSONObject(i).getJSONObject("node").getString("total"));
            urlImage = this.pools.getJSONObject(i).getJSONObject("node").getString("picture");
        } catch (JSONException e) {
            Log.d("AdpaterPool", e.toString());
            //e.printStackTrace();
        }
        if(urlImage != null) {
            imageContent.execute(urlImage);
        }

        try {
            background.setImageDrawable(imageContent.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return view;
    }

}
