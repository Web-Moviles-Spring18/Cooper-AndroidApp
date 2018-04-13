package com.cooper.cooper.Authentication;

import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.CustomToast.SuccessToast;
import com.cooper.cooper.MainMenu;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.HTTPRequestListener;
import com.cooper.cooper.http_requests.LoginRequest;

import org.json.JSONObject;

public class Login_Fragment extends Fragment implements OnClickListener, HTTPRequestListener {

    private View view;
    private EditText emailid, password;
    private Button loginButton;
    private TextView forgotPassword, signUp;
    private CheckBox show_hide_password;
    private LinearLayout loginLayout;
    private ProgressBar loadingOption;
    private Animation shakeAnimation;
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_login_layout, container, false);
        this.sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        this.initViews();
        this.setListeners();
        return this.view;
    }

    // Initiate Views
    private void initViews() {
        this.fragmentManager    = getActivity().getSupportFragmentManager();
        this.loadingOption      = (ProgressBar) view.findViewById(R.id.loadingPanel);
        this.emailid            = (EditText) view.findViewById(R.id.account_name);
        this.password           = (EditText) view.findViewById(R.id.login_password);
        this.loginButton        = (Button) view.findViewById(R.id.loginBtn);
        this.forgotPassword     = (TextView) view.findViewById(R.id.forgot_password);
        this.signUp             = (TextView) view.findViewById(R.id.createAccount);
        this.show_hide_password = (CheckBox) view.findViewById(R.id.show_hide_password);
        this.loginLayout        = (LinearLayout) view.findViewById(R.id.login_layout);
        this.loadingOption.setVisibility(View.GONE);
        // Load ShakeAnimation
        this.shakeAnimation     = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_animation);

    }

    // Set Listeners
    private void setListeners() {
        this.loginButton.setOnClickListener(this);
        this.forgotPassword.setOnClickListener(this);
        this.signUp.setOnClickListener(this);

        // Set check listener over checkbox for showing and hiding password
        this.show_hide_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton button,boolean isChecked) {
                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {
                            show_hide_password.setText(R.string.hide_pwd);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());// hide password
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                if(this.checkValidation()) {
                    this.loadingOption.setVisibility(View.VISIBLE);
                    String email = this.emailid.getText().toString();
                    String password = this.password.getText().toString();
                    try {
                        JSONObject login_json = new JSONObject();
                        login_json.put("email", email);
                        login_json.put("password", password);

                        LoginRequest loginRequest = new LoginRequest(this, login_json);
                        loginRequest.execute(Utils.URL + "/login");
                    } catch (Exception e) {
                        new AlertToast().Show_Toast(getActivity(), v, "Error");
                        Log.d("LoginError", e.toString());
                    }
                }
                break;
            case R.id.forgot_password:
                // Replace forgot password fragment with animation
                this.fragmentManager.beginTransaction().setCustomAnimations(R.anim.right_enter_animation, R.anim.left_exit_animation)
                        .replace(R.id.frameContainer, new ForgotPassword_Fragment(), Utils.ForgotPassword_Fragment).commit();
                break;
            case R.id.createAccount:
                // Replace signup frgament with animation
                this.fragmentManager.beginTransaction().setCustomAnimations(R.anim.right_enter_animation, R.anim.left_exit_animation)
                        .replace(R.id.frameContainer, new SignUp_Fragment(), Utils.SignUp_Fragment).commit();
                break;
        }

    }

    // Check Validation before login
    private boolean checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0 || getPassword.equals("") || getPassword.length() == 0) {
            this.loginLayout.startAnimation(shakeAnimation);
            new AlertToast().Show_Toast(getActivity(), view,"Enter both credentials.");
            return false;
        }else if (!m.find()) {
            // Check if email id is valid or not
            new AlertToast().Show_Toast(getActivity(), view,"Your Email is Invalid.");
            return false;
        } else {
            // Else do login and do your stuff
            //Toast.makeText(getActivity(), "Do PostRequests.", Toast.LENGTH_SHORT).show();
            return true;
        }


    }

    @Override
    public void requestDone(Object object, int statusCode) {
        this.loadingOption.setVisibility(View.GONE);
        Log.d("Response Login", (String) object);
        if(statusCode == HttpURLConnection.HTTP_OK) {
            Intent intent = new Intent(getActivity(), MainMenu.class);
            this.startActivity(intent);
            this.sharedPreferences.edit().putBoolean("isLogged", true).apply();
            new SuccessToast().Show_Toast(getActivity(), this.view, (String) object);
        } else if(statusCode == HttpURLConnection.HTTP_BAD_REQUEST){
            this.sharedPreferences.edit().putBoolean("isLogged", false).apply();
            new AlertToast().Show_Toast(getActivity(), this.view, (String) object);
        } else {
            this.sharedPreferences.edit().putBoolean("isLogged", false).apply();
            new AlertToast().Show_Toast(getActivity(), this.view, (String) object);
        }

    }
}
