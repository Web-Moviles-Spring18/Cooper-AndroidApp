package com.cooper.cooper.Menu;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooper.cooper.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Coops_Create_Fragment extends Fragment {


    public Coops_Create_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coops_create_layout, container, false);
    }

}
