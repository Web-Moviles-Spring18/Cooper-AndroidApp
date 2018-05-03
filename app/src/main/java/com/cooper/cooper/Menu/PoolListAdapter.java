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
        return this.pools.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.pools.get(i).getId();
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

        Pool currentPool = pools.get(i);
        name.setText(currentPool.getName());
        amount.setText("$" + currentPool.getTotal());
        GetImageContent imageContent = new GetImageContent(this.activity);
        imageContent.execute("https://i.imgur.com/UBbxasE.jpg");

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
