package com.cooper.cooper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.cooper.cooper.Authentication.Login_Fragment;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        fragmentManager = getSupportFragmentManager();
        Log.wtf("SharedPreferences", this.sharedPreferences.getAll().toString());
        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            Log.wtf("isLogged", this.sharedPreferences.getBoolean("isLogged", false)+"");
            if(this.sharedPreferences.getBoolean("isLogged", false)) {
                Intent i = new Intent(this, MainMenu.class);
                this.startActivity(i);
            } else {
                fragmentManager.beginTransaction().replace(R.id.frameContainer, new Login_Fragment(), Utils.Login_Fragment).commit();
            }

        }


    }

    // Replace PostRequests Fragment with animation
    public void replaceLoginFragment() {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.left_enter_animation, R.anim.right_exit_animation).replace(R.id.frameContainer, new Login_Fragment(), Utils.Login_Fragment).commit();
    }

    @Override
    public void onBackPressed() {
        // Find the tag of signup and forgot password fragment
        Fragment SignUp_Fragment = fragmentManager.findFragmentByTag(Utils.SignUp_Fragment);
        Fragment ForgotPassword_Fragment = fragmentManager.findFragmentByTag(Utils.ForgotPassword_Fragment);

        // Check if both are null or not
        // If both are not null then replace login fragment else do backpressed
        // task
        if (SignUp_Fragment != null) {
            this.replaceLoginFragment();
        } else if (ForgotPassword_Fragment != null) {
            this.replaceLoginFragment();
        } else {
            super.onBackPressed();
        }
    }
}

