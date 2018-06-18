package com.example.nowshadhasan.databaselab.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowshadhasan.databaselab.R;
import com.example.nowshadhasan.databaselab.another.AvailableDonorListAdapter;

import org.json.JSONException;
import org.json.JSONObject;


// This activity is for adding new donor.

public class AddNewDonorActivity extends AppCompatActivity {

    private String TAG=AddNewDonorActivity.class.getSimpleName();

  private  Spinner addNewDonorBloodGroupSpinner;
  private  ArrayAdapter<CharSequence> bloodGroupAdapter;
  //  String donorBloodGroup="";
   private  String donorName,donorBloodGroup,donorDept,donorSession,donorPhone1,donorPhone2,donorAddress;
   private EditText donorNameEditText,donorDeptEditText,donorSessionEditText,donorPhone1EditText,donorPhone2EditText,donorAddressEditText;

    Button addNewDonorButton;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_donor);

        preferences=AddNewDonorActivity.this.getSharedPreferences("myPref",MODE_PRIVATE);

        addNewDonorBloodGroupSpinner=(Spinner)findViewById(R.id.newDonorBloodGroupSpinner);
        donorNameEditText=(EditText)findViewById(R.id.addNewDonorNameEditText);
        donorDeptEditText=(EditText)findViewById(R.id.addNewDonorDeptEditText);
        donorSessionEditText=(EditText)findViewById(R.id.addNewDonorSessionEditText);
        donorPhone1EditText=(EditText)findViewById(R.id.addNewDonorPhone1EditText);
        donorPhone2EditText=(EditText)findViewById(R.id.addNewDonorPhone2EditText);
        donorAddressEditText=(EditText)findViewById(R.id.addNewDonorAddressEditText) ;
        addNewDonorButton=(Button)findViewById(R.id.addNewDonorButton);


        bloodGroupAdapter=ArrayAdapter.createFromResource(this,R.array.blood_group,android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        addNewDonorBloodGroupSpinner.setAdapter(bloodGroupAdapter);

        addNewDonorBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        donorBloodGroup=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});




        addNewDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                donorName=donorNameEditText.getText().toString();
                donorDept=donorDeptEditText.getText().toString();
                donorSession=donorSessionEditText.getText().toString();
                donorPhone1=donorPhone1EditText.getText().toString();
                donorPhone2=donorPhone2EditText.getText().toString();
                donorAddress=donorAddressEditText.getText().toString();

                addNewDonor(donorName,donorBloodGroup,donorDept,donorSession,donorPhone1,donorPhone2,donorAddress);
            }
        });



    }

    private void addNewDonor(String donorName,String donorBloodGroup,String donorDept,String donorSession,String donorPhone1,String donorPhone2,String donorAddress)
    {
//        String ipAddress="10.100.170.46";



     //   AddNewDonorServlet?DonorName=Apu&&DonorBloodGroup=B+&&DonorDept=CSE&&DonorSession=2012-13&&DonorPhone1=165413&&DonorPhone2=35345&&DonorAddress=Surma

        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/AddNewDonorServlet?" +
                "DonorName="+donorName+"&&DonorBloodGroup="+donorBloodGroup+"&&DonorDept="+donorDept+"&&DonorSession="+donorSession+
                "&&DonorPhone1="+donorPhone1+"&&DonorPhone2="+donorPhone2+"&&DonorAddress="+donorAddress;


        RequestQueue queue= Volley.newRequestQueue(AddNewDonorActivity.this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {

                    //   String validity = response.getString("date");

                    if (response.getString("addNewDonor").equals("successful")) {
                        // hideDialogue();
                        //Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                        //startActivity(i);
                        Toast.makeText(AddNewDonorActivity.this, "New Donor successfully added", Toast.LENGTH_SHORT).show();
                    } else {
                        //    hideDialogue();
                        Toast.makeText(AddNewDonorActivity.this, "Blood date failed to update", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+error.getMessage());
                //  hideDialogue();
            }
        }

        );
        queue.add(jsonObjectRequest);


    }
}
