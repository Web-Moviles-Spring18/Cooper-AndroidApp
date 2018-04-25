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

public class UserListAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<JSONObject> users;

    public UserListAdapter(ArrayList<JSONObject> users, Activity activity) {
        this.activity = activity;
        this.users = users;
    }
    @Override
    public int getCount() {
        return this.users.size();
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
            view = this.activity.getLayoutInflater().inflate(R.layout.users_list_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.user_name);
        try {
            if(this.users.get(i).has("name")) {
                name.setText(this.users.get(i).getString("name"));
            } else {
                name.setText(this.users.get(i).getString("email"));
            }

        } catch (Exception e) {
            name.setText("Not Founded");
        }
        return view;
    }
}
