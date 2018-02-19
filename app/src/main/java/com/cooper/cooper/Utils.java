package com.cooper.cooper;

/**
 * Created by marco on 15/02/2018.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Utils {
    //Email Validation pattern
    public static final String regEx = "\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z]{2,4}\b";

    //Fragments Tags
    public static final String Login_Fragment = "Login_Fragment";
    public static final String SignUp_Fragment = "SignUp_Fragment";
    public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";


    public static final String URL = "http://127.0.0.1:3000";
}
