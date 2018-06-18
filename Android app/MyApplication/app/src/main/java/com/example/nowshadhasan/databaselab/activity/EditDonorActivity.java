package com.example.nowshadhasan.databaselab.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.example.nowshadhasan.databaselab.fragment.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class EditDonorActivity extends AppCompatActivity {

    private String TAG=EditDonorActivity.class.getSimpleName();

    private  String donorName,donorBloodGroup,donorDept,donorSession,donorPhone1,donorPhone2,donorAddress,donorAvailability;
    private EditText donorNameEditText,donorDeptEditText,donorSessionEditText,donorPhone1EditText,donorPhone2EditText,donorAddressEditText;
   private Button editDonorButton;
    private Spinner editDonorBloodGroupSpinner;
   private SwitchCompat editDonorAvailabilitySwitch;
    ProgressDialog progressDialog;
    private int donorID;

    SharedPreferences preferences;

    ArrayAdapter<CharSequence> bloodGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_donor);

        preferences=EditDonorActivity.this.getSharedPreferences("myPref",MODE_PRIVATE);

        Intent i = EditDonorActivity.this.getIntent();
          donorID=i.getIntExtra("donorID",0);


        editDonorBloodGroupSpinner=(Spinner)findViewById(R.id.editDonorBloodGroupSpinner);
        donorNameEditText=(EditText)findViewById(R.id.editDonorNameEditText);
        donorDeptEditText=(EditText)findViewById(R.id.editDonorDeptEditText);
        donorSessionEditText=(EditText)findViewById(R.id.editDonorSessionEditText);
        donorPhone1EditText=(EditText)findViewById(R.id.editDonorPhone1EditText);
        donorPhone2EditText=(EditText)findViewById(R.id.editDonorPhone2EditText);
        donorAddressEditText=(EditText)findViewById(R.id.editDonorAddressEditText) ;
        editDonorAvailabilitySwitch=(SwitchCompat)findViewById(R.id.editDonorAvailabilitySwitch);


        editDonorButton=(Button)findViewById(R.id.editExistingDonorButton);

        bloodGroupAdapter=ArrayAdapter.createFromResource(this,R.array.blood_group,android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editDonorBloodGroupSpinner.setAdapter(bloodGroupAdapter);

        editDonorBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                donorBloodGroup=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        showExistingDonor(donorID);

        editDonorAvailabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
//                    switchStatus.setText("Switch is currently ON");
                    donorAvailability="yes";
                }else{
                  //  switchStatus.setText("Switch is currently OFF");
                    donorAvailability="no";
                }

            }
        });


        editDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                donorName=donorNameEditText.getText().toString();
                donorDept=donorDeptEditText.getText().toString();
                donorSession=donorSessionEditText.getText().toString();
                donorPhone1=donorPhone1EditText.getText().toString();
                donorPhone2=donorPhone2EditText.getText().toString();
                donorAddress=donorAddressEditText.getText().toString();


                ediExistingDonor(donorID,donorName,donorBloodGroup,donorDept,donorSession,donorPhone1,donorPhone2,donorAddress,donorAvailability);
            }
        });


    }

    private void showExistingDonor(int donorID)
    {
        progressDialog=new ProgressDialog(EditDonorActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);



        showDialogue();

//        String ipAddress="10.100.170.46";

        RequestQueue requestQueue= Volley.newRequestQueue(EditDonorActivity.this);
        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/PersonalInfoServlet?DonorID="+donorID;
//String url="http://localhost:8080/DatabaseLab/AvailableDonorServlet?BloodGroup=B+";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, response.toString());
                hideDialogue();



                try {
                    donorNameEditText.setText(response.getString("donorName"));
                    donorBloodGroup=response.getString("donorBloodGroup");

                    if (!donorBloodGroup.equals(null)) {
                        int spinnerPosition = bloodGroupAdapter.getPosition(donorBloodGroup);
                        editDonorBloodGroupSpinner.setSelection(spinnerPosition);
                    }
                    donorAvailability=response.getString("donorAvailability");

                  //  editDonorBloodGroupSpinner.setSelection(response.getString("donorBloodGroup"));
                    donorDeptEditText.setText(response.getString("donorDept"));
                    donorSessionEditText.setText(response.getString("donorSession"));
                    donorAddressEditText.setText(response.getString("donorAddress"));
                    donorPhone1EditText.setText(response.getString("donorPhone1"));
                    donorPhone2EditText.setText(response.getString("donorPhone2"));

                    if(response.getString("donorAvailability").equals("yes"))
                    {
                        editDonorAvailabilitySwitch.setChecked(true);
                    }
                    else
                    {
                        editDonorAvailabilitySwitch.setChecked(false);
                    }

                   // donorAvailabilityTextView.setText(response.getString("donorAvailability"));


                }catch (JSONException ex)
                {
                    ex.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.e(TAG,"OK from fragment");
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                hideDialogue();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }


    private void hideDialogue(){
        if(progressDialog.isShowing())
            progressDialog.hide();
    }
    private void showDialogue(){
        if(!progressDialog.isShowing())
            progressDialog.show();
    }

    private void ediExistingDonor(int donorID,String donorName, String donorBloodGroup, String donorDept, String donorSession, String donorPhone1, String donorPhone2, String donorAddress,String donorAvailability) {



//        String ipAddress="10.100.170.46";



//
//        dummy link- "http://localhost:8080/DatabaseLab/EditExistingDonorServlet?DonorID=7&&DonorName=Nowshad&&DonorBloodGroup=B+&&DonorDept=CSE"
//                + "&&DonorSession=2012-13&&DonorPhone1=165413&&DonorPhone2=35345&&DonorAddress=Surma&&DonorAvailability=yes"


  //      http://localhost:8080/DatabaseLab/EditExistingDonorServlet?DonorID=7&&DonorName=Nowshad&&DonorBloodGroup=B+&&DonorDept=CSE&&DonorSession=2012-13&&DonorPhone1=165413&&DonorPhone2=35345&&DonorAddress=Surma&&DonorAvailability=yes

        String urlNew="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/EditExistingDonorServlet?DonorID="+donorID+
                "&&DonorName="+donorName+"&&DonorBloodGroup="+donorBloodGroup+"&&DonorDept="+donorDept+"&&DonorSession="+donorSession+
                "&&DonorPhone1="+donorPhone1+"&&DonorPhone2="+donorPhone2+"&&DonorAddress="+donorAddress+"&&DonorAvailability="+donorAvailability;

//        Toast.makeText(EditDonorActivity.this, "Existing Donor successfully ert", Toast.LENGTH_SHORT).show();

        RequestQueue queueNew= Volley.newRequestQueue(EditDonorActivity.this);

        JsonObjectRequest jsonObjectRequestNew=new JsonObjectRequest(Request.Method.GET, urlNew, null, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {

                 //   Toast.makeText(EditDonorActivity.this, "Existing Donor successfully ert", Toast.LENGTH_SHORT).show();

                    //   String validity = response.getString("date");

                    if (response.getString("editExistingDonor").equals("successful")) {
                        // hideDialogue();
                        //Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                        //startActivity(i);
                        Toast.makeText(EditDonorActivity.this, "Existing Donor successfully edited", Toast.LENGTH_SHORT).show();

                        Intent editDonorIntent = new Intent(EditDonorActivity.this, MainActivity.class);
                    //    editDonorIntent.putExtra("donorID", donorID); //Optional parameters
                        EditDonorActivity.this.startActivity(editDonorIntent);

                    } else {
                        //    hideDialogue();
                        Toast.makeText(EditDonorActivity.this, "Existing Donor failed to edit", Toast.LENGTH_SHORT).show();
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
        queueNew.add(jsonObjectRequestNew);



    }
}
