package com.cooper.cooper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.http_requests.HTTPRequestListener;
import com.cooper.cooper.http_requests.LogoutRequest;

import java.net.HttpURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class Account_Fragment extends Fragment implements View.OnClickListener, HTTPRequestListener{

    private View view;

    private Button logout;
    private SharedPreferences sharedPreferences;

    public Account_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_account_layout, container, false);
        this.sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        this.logout = (Button) this.view.findViewById(R.id.logout);
        this.logout.setOnClickListener(this);
        // Inflate the layout for this fragment
        return this.view;
    }

    public void logout() {
        LogoutRequest logoutRequest = new LogoutRequest(this);
        logoutRequest.execute(Utils.URL + "/logout");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                this.logout();
                break;
        }
    }

    @Override
    public void requestDone(Object object, int statusCode) {
        if(HttpURLConnection.HTTP_OK == statusCode) {
            this.sharedPreferences.edit().putBoolean("isLogged", false).apply();
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            this.startActivity(intent);
            new SuccessToast().Show_Toast(this.getActivity(), this.view, (String) object);
        }
    }
}
