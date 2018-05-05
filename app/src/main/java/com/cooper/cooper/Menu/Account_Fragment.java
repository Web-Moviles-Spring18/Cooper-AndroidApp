package com.cooper.cooper.Menu;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.MainActivity;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.GetRequests;
import com.cooper.cooper.http_requests.HTTPRequestListener;
import com.cooper.cooper.http_requests.LogoutRequest;
import com.cooper.cooper.http_requests.PostRequests;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.net.HttpURLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class Account_Fragment extends Fragment implements View.OnClickListener, HTTPRequestListener{

    private View view;
    private Button logout;
    private Button profilePicture;
    private Button updateProfile;
    private Button updatePassword;

    private TextView nameUser, emailUser, password, confirmPasword;

    private JSONObject accountData;

    private SharedPreferences sharedPreferences;
    private static int RESULT_LOAD_IMAGE = 1;

    public Account_Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_account_layout, container, false);
        this.sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        this.logout = (Button) this.view.findViewById(R.id.logout);
        this.logout.setOnClickListener(this);

        this.profilePicture = (Button) this.view.findViewById(R.id.profilePicture);
        this.profilePicture.setOnClickListener(this);

        this.updateProfile = (Button) this.view.findViewById(R.id.updateProfileBtn);
        this.updateProfile.setOnClickListener(this);
        this.updatePassword = (Button) this.view.findViewById(R.id.updatePasswordBtn);
        this.updatePassword.setOnClickListener(this);

        this.nameUser = (TextView) this.view.findViewById(R.id.account_name);
        this.emailUser = (TextView) this.view.findViewById(R.id.account_email);
        this.password = (TextView) this.view.findViewById(R.id.newPassword);
        this.confirmPasword = (TextView) this.view.findViewById(R.id.confirmPassword);

        this.getAccount();
        // Inflate the layout for this fragment
        return this.view;
    }
    public void getAccount() {
        GetRequests getAccount = new GetRequests();
        getAccount.execute(Utils.URL.concat("/account"));
        try {
            JSONObject object = getAccount.get();
            Log.d("getaccountdata", object.toString());
            this.accountData = new JSONObject(object.getString("response"));
            if(object.getInt("status_code") == HttpURLConnection.HTTP_OK) {
                if(this.accountData.has("name")) {
                    this.nameUser.setText(this.accountData.getString("name"));
                }
                this.emailUser.setText(this.accountData.getString("email"));
            } else {
                new AlertToast().Show_Toast(this.getActivity(), this.view, "There was an error!");
            }
            Log.wtf("accountData", accountData.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void logout() {
        LogoutRequest logoutRequest = new LogoutRequest(this, getResources());
        logoutRequest.execute(Utils.URL + "/logout");


    }

    public void updateProfileData() {
        try {
            JSONObject postData = new JSONObject();
            postData.put("name", this.nameUser);
            postData.put("gender", "Male");
            postData.put("fcmToken", FirebaseInstanceId.getInstance().getToken());
            PostRequests updateProfile = new PostRequests(postData);
            updateProfile.execute(Utils.URL.concat("/account/profile"));

            JSONObject response = updateProfile.get();
            String responseStr = response.getString("response");
            if(response.getInt("status_code") == HttpURLConnection.HTTP_OK) {
                new SuccessToast().Show_Toast(this.getActivity(), this.view, responseStr);
            } else {
                new AlertToast().Show_Toast(this.getActivity(), this.view, responseStr);
            }
        } catch (Exception e) {
            new AlertToast().Show_Toast(this.getActivity(), this.view, "Error");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                this.logout();
                break;
            case R.id.profilePicture:
                Intent intent = new Intent(this.getActivity(), ProfilePicture_Account_Activity.class);
                startActivity(intent);
                break;
            case R.id.updatePasswordBtn:

                break;
            case R.id.updateProfileBtn:
                this.updateProfileData();
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
        } else {
            new AlertToast().Show_Toast(this.getActivity(), this.view, (String) object);
        }
    }
}
