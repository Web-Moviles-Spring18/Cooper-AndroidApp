package com.cooper.cooper.Authentication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cooper.cooper.CustomToast.AlertToast;
import com.cooper.cooper.MainActivity;
import com.cooper.cooper.MainMenu;
import com.cooper.cooper.R;
import com.cooper.cooper.Utils;
import com.cooper.cooper.http_requests.PostRequests;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

public class SignUp_Fragment extends Fragment implements OnClickListener {
    private View view;
    private EditText fullName, emailId,
                            password, confirmPassword;
    private TextView login;
    private Button signUpButton;
    private CheckBox terms_conditions;

    public SignUp_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_signup_layout, container, false);
        this.initViews();
        this.setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        this.fullName = (EditText) view.findViewById(R.id.fullName);
        this.emailId = (EditText) view.findViewById(R.id.userEmailId);
        this.password = (EditText) view.findViewById(R.id.password);
        this.confirmPassword = (EditText) view.findViewById(R.id.newPassword);
        this.signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        this.login = (TextView) view.findViewById(R.id.already_user);
        this.terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

    }

    // Set Listeners
    private void setListeners() {
        this.signUpButton.setOnClickListener(this);
        this.login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                // Call checkValidation method
                if(this.checkValidation()) {
                    try {
                        PostRequests create_user_request = new PostRequests(this.createUserJSON());
                        create_user_request.execute(Utils.URL + "/signup");

                        JSONObject response = create_user_request.get();
                        int response_status_code = response.getInt("status_code");
                        Log.d("status_code", response_status_code+"");
                        if(response_status_code == 200 || response_status_code == 201) {
                            Intent i = new Intent(this.getActivity(), MainMenu.class);
                            startActivity(i);
                        } else {
                            new AlertToast().Show_Toast(getActivity(), v, response.getString("response"));
                        }
                    } catch (Exception e) {
                        new AlertToast().Show_Toast(getActivity(), v, "Error");
                        Log.d("CreateUserError", e.toString());
                    }
                }
                break;
            case R.id.already_user:
                // Replace login fragment
                new MainActivity().replaceLoginFragment();
                break;
        }

    }

    private JSONObject createUserJSON() throws Exception{
        JSONObject signup_json = new JSONObject();
        signup_json.put("name", this.fullName.getText().toString().toUpperCase());
        signup_json.put("email", this.emailId.getText().toString());
        signup_json.put("password", this.password.getText().toString());
        signup_json.put("confirmPassword", this.confirmPassword.getText().toString());
        signup_json.put("fcmToken", FirebaseInstanceId.getInstance().getToken());

        return signup_json;
    }

    // Check Validation Method
    private boolean checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0) {

            new AlertToast().Show_Toast(getActivity(), view,
                    "All fields are required.");
            return false;
            // Check if email id valid or not
        } else if (!m.find()) {
            new AlertToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            return false;
            // Check if both password should be equal
        } else if (!getConfirmPassword.equals(getPassword)) {
            new AlertToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");
        return false;
        // Make sure user should check Terms and Conditions checkbox
        } else if (!terms_conditions.isChecked()) {
            new AlertToast().Show_Toast(getActivity(), view,
                    "Please select Terms and Conditions.");
            return false;
            // Else do signup or do your stuff
        } else {
            //Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
            //.show();
            return true;
        }
    }
}
