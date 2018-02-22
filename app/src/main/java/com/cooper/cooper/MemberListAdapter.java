package com.cooper.cooper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 22/02/2018.
 */

public class MemberListAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<String> members;

    public MemberListAdapter(ArrayList<String> members, Activity activity) {
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

        TextView name = (TextView) view.findViewById(R.id.member_name);
        TextView amount = (TextView) view.findViewById(R.id.pending);

        name.setText(this.members.get(i)+"");
        amount.setText("$430");

        return view;
    }
}
