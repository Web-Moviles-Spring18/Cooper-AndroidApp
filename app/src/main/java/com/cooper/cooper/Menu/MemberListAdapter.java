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

public class MemberListAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<JSONObject> members;

    public MemberListAdapter(ArrayList<JSONObject> members, Activity activity) {
        this.activity = activity;
        this.members = members;
    }
    @Override
    public int getCount() {
        return this.members.size();
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
            view = this.activity.getLayoutInflater().inflate(R.layout.members_list_layout, null);
        }

        TextView name = (TextView) view.findViewById(R.id.user_name);
        TextView amount = (TextView) view.findViewById(R.id.pending);
        TextView paid = (TextView) view.findViewById(R.id.paid);
        try {
            if(this.members.get(i).has("name")) {
                name.setText(this.members.get(i).getString("name")+"");
                name.setTextSize(18);
            }
            paid.setText("Paid $"+this.members.get(i).getString("paid"));
            amount.setText("Pending $"+this.members.get(i).getString("amount"));
        } catch (Exception e) {
            name.setText("Not Founded");
            amount.setText("Pending $0.00");
        }


        return view;
    }
}
