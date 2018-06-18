package com.example.nowshadhasan.databaselab.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowshadhasan.databaselab.R;
import com.example.nowshadhasan.databaselab.activity.AddNewDonorActivity;
import com.example.nowshadhasan.databaselab.activity.DonorInfoActivity;
import com.example.nowshadhasan.databaselab.activity.EditDonorActivity;
import com.example.nowshadhasan.databaselab.another.AvailableDonorListAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nowshad Hasan on 7/21/2017.
 */

public class DonorPersonalInfoFragment extends Fragment {

    private String TAG=HomeFragment.class.getSimpleName();

private ProgressDialog progressDialog;

    private int donorID,donationCount;

    private TextView donorNameTextView,donorBloodGroupTextView,donorDeptTextView,donorSessionTextView,donorAddressTextView,donorPhone1TextView,
            donorPhone2TextView,donationNumberTextView,
            dayFromLastDonationTextView,donorAvailabilityTextView;
    private ImageView donorPhone1CallImageView,donorPhone1SMSImageView,donorPhone2CallImageView,donorPhone2SMSImageView,donationUpdateImageView;

    String donorName,donorBloodGroup,donorDept,donorSession,donorAddress,donorPhone1,donorPhone2,donationNumber,dayFromLastDonation,donorAvailability;

    SharedPreferences preferences;

    public DonorPersonalInfoFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_donor_personal_info, container, false);


        donorNameTextView=(TextView)view.findViewById(R.id.donorNameTextView);
        donorBloodGroupTextView=(TextView)view.findViewById(R.id.donorBloodGroupTextView);
        donorDeptTextView=(TextView)view.findViewById(R.id.donorDeptTextView);
        donorSessionTextView=(TextView)view.findViewById(R.id.donorSessionTextView);
        donorAddressTextView=(TextView)view.findViewById(R.id.donorAddressTextView);
        donorPhone1TextView=(TextView)view.findViewById(R.id.donorPhone1TextView);
        donorPhone1CallImageView=(ImageView) view.findViewById(R.id.donorPhone1CallImageView);
        donorPhone1SMSImageView=(ImageView) view.findViewById(R.id.donorPhone1SMSImageView);
        donorPhone2TextView=(TextView)view.findViewById(R.id.donorPhone2TextView);
        donorPhone2CallImageView=(ImageView) view.findViewById(R.id.donorPhone2CallImageView);
        donorPhone2SMSImageView=(ImageView) view.findViewById(R.id.donorPhone2SMSImageView);
        donationNumberTextView=(TextView)view.findViewById(R.id.donationNumberTextView);
        donationUpdateImageView=(ImageView) view.findViewById(R.id.donationUpdateImageView);
        dayFromLastDonationTextView=(TextView)view.findViewById(R.id.dayFromLastDonationTextView);
        donorAvailabilityTextView=(TextView)view.findViewById(R.id.donorAvailabilityTextView);






        return view;



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);

        Intent i = getActivity().getIntent();
        int donorID=i.getIntExtra("donorID",0);


//                        Toast.makeText(getActivity(), "From Info"+donorID,
//                        Toast.LENGTH_SHORT).show();

        showDonorPersonalInfo(donorID);


        donorPhone1CallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(donorPhone1!=null)
                {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+donorPhone1));
                        getActivity().startActivity(callIntent);
                    }
                }
                else if(donorPhone2.equals(""))
                {
                                            Toast.makeText(getActivity(), "Phone number not specified!",
                        Toast.LENGTH_SHORT).show();
                }


            }
        });


        donorPhone2CallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(donorPhone2!=null)
                {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+donorPhone2));
                        getActivity().startActivity(callIntent);
                    }
                }
                else if(donorPhone2.equals(""))
                {
                    Toast.makeText(getActivity(), "Phone number not specified!",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });


        donorPhone1SMSImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(donorPhone1!=null)
                {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", donorPhone1);
                    smsIntent.putExtra("sms_body","I'm from KIN Blood");
                    getActivity().startActivity(smsIntent);
                }
                else if(donorPhone2.equals(""))
                {
                    Toast.makeText(getActivity(), "Phone number not specified!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        donorPhone2SMSImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(donorPhone2!=null)
                {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", donorPhone2);
                    smsIntent.putExtra("sms_body","I'm from KIN Blood");
                    getActivity().startActivity(smsIntent);
                }
                else if(donorPhone2.equals(""))
                {
                    Toast.makeText(getActivity(), "Phone number not specified!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void showDonorPersonalInfo(int donorID)
    {


        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);



        showDialogue();

//        String ipAddress="10.100.170.46";

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/PersonalInfoServlet?DonorID="+donorID;
//String url="http://localhost:8080/DatabaseLab/AvailableDonorServlet?BloodGroup=B+";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

//                                        Toast.makeText(getActivity(), "From Info",
//                        Toast.LENGTH_SHORT).show();

                //  Log.e(TAG,"OK from fragment");
                Log.e(TAG, response.toString());
                hideDialogue();

                try {
                    donorNameTextView.setText(response.getString("donorName"));
                    donorBloodGroupTextView.setText(response.getString("donorBloodGroup"));
                    donorDeptTextView.setText(response.getString("donorDept"));
                    donorSessionTextView.setText(response.getString("donorSession"));
                    donorAddressTextView.setText(response.getString("donorAddress"));
                    donorPhone1TextView.setText(response.getString("donorPhone1"));
                    donorPhone2TextView.setText(response.getString("donorPhone2"));
                    donationNumberTextView.setText(response.getString("donorBloodCount"));
                    dayFromLastDonationTextView.setText(response.getString("donationTimeDiff")+" ago");
                    donorAvailabilityTextView.setText(response.getString("donorAvailability"));


                    donorPhone1=response.getString("donorPhone1");
                    donorPhone2=response.getString("donorPhone2");







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


}
