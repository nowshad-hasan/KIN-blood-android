package com.example.nowshadhasan.databaselab.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowshadhasan.databaselab.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {


    Button buttonLogin;
    CheckBox checkboxRememberMe;
    EditText editTextEmail;
    EditText editTextPassword;


    final private String PREF_NAME = "myPref";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private String email, password;
    private String URL;
    private String TAG = LoginActivity.class.getSimpleName();

    private String loginStatus;

    private String IPAddress = "165.227.187.240";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences=getApplicationContext().getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor=preferences.edit();

        editor.putString("IPAddress", IPAddress);
        editor.commit();


        preferences = getApplicationContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();


        if (preferences.getBoolean("isLoggedIn", false) == true) {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            LoginActivity.this.startActivity(intent);
            LoginActivity.this.finish();
        }


        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        checkboxRememberMe = (CheckBox) findViewById(R.id.checkboxRememberMe);
        editTextEmail = (EditText) findViewById(R.id.inputEmail);
        editTextPassword = (EditText) findViewById(R.id.inputPassword);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                URL = "http://" + preferences.getString("IPAddress", null) + ":8080/DatabaseLab/LoginServlet?userEmail=" + email + "&&userPass=" + password;


              //  Toast.makeText(LoginActivity.this,URL,Toast.LENGTH_SHORT).show();

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //  Log.e(TAG,"OK from fragment");
                        Log.e(TAG, response.toString());


                        try {

                            loginStatus = response.getString("loginStatus");


                            if (loginStatus.equals("success")) {
//                                userID=response.getInt("userID");
//                                userRole=response.getString("userRole");
//                                editor.putInt("userID",userID);
//                                editor.putString("userRole",userRole);
//                                editor.putString("userEmail",email);
                                editor.putBoolean("isLoggedIn", checkboxRememberMe.isChecked());
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            } else if (loginStatus.equals("failed")) {
                                Toast.makeText(LoginActivity.this, "Invalid user email or password", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Log.e(TAG,"OK from fragment");
                        VolleyLog.e(TAG, "Error: " + error.getMessage());

                    }
                }
                );
                requestQueue.add(jsonObjectRequest);
                // Log.e(TAG,"akjsdhflhas");


            }
        });


    }
}
