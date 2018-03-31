package com.cooper.cooper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.graphics.Color;
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
import android.widget.Toast;

import com.cooper.cooper.http_requests.PostRequests;

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
        view = inflater.inflate(R.layout.signup_layout, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName = (EditText) view.findViewById(R.id.fullName);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

        login.setTextColor(Color.WHITE);
        terms_conditions.setTextColor(Color.WHITE);
    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
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
                            new MainActivity().replaceLoginFragment();
                        } else {
                            new CustomToast().Show_Toast(getActivity(), v, response.getString("response"));
                        }
                    } catch (Exception e) {
                        new CustomToast().Show_Toast(getActivity(), v, "Error");
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

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");
            return false;
            // Check if email id valid or not
        } else if (!m.find()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
            return false;
            // Check if both password should be equal
        } else if (!getConfirmPassword.equals(getPassword)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");
        return false;
        // Make sure user should check Terms and Conditions checkbox
        } else if (!terms_conditions.isChecked()) {
            new CustomToast().Show_Toast(getActivity(), view,
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
