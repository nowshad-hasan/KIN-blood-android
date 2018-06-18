package com.example.nowshadhasan.databaselab.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.nowshadhasan.databaselab.another.AvailableDonorListAdapter;
import com.example.nowshadhasan.databaselab.another.DonorDonationHistoryListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nowshad Hasan on 7/21/2017.
 */

public class DonorDonationHistoryFragment extends ListFragment {

    private int donorID;
    private ProgressDialog progressDialog;
    private String TAG=HomeFragment.class.getSimpleName();
    DonorDonationHistoryListAdapter listAdapter;

    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_donor_donation_history, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {




       // int donorID=i.getIntExtra("donorID",0);
        super.onActivityCreated(savedInstanceState);

        preferences=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);

//        Toast.makeText(getActivity(), "From History"+donorID,
//                Toast.LENGTH_SHORT).show();

        Intent i = getActivity().getIntent();
        int donorID=i.getIntExtra("donorID",0);

        showDonorDonationHistory(donorID);

    }

    private void showDonorDonationHistory(int donorID)
    {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        final ArrayList<Integer> arrayList1=new ArrayList<>();
        final ArrayList<String> arrayList2=new ArrayList<>();
        final ArrayList<String> arrayList3=new ArrayList<>();



        showDialogue();

//        String ipAddress="10.100.170.46";

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/PersonalHistoryServlet?DonorID="+donorID;
//String url="http://localhost:8080/DatabaseLab/AvailableDonorServlet?BloodGroup=B+";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //  Log.e(TAG,"OK from fragment");
                Log.e(TAG, response.toString());
                hideDialogue();

                JSONArray jsonArray = null;

                try {
                    jsonArray = response.getJSONArray("donorDonationHistory");
                    //    Log.e(TAG,Integer.toString(jsonArray.length()));

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);

                        arrayList1.add(jsonObject.getInt("donationID"));
                        arrayList2.add(jsonObject.getString("donationDate"));
                        arrayList3.add(jsonObject.getString("donationPlace"));

                        Log.e(TAG,jsonObject.getString("donationPlace"));

                    }
                    hideDialogue();

                    listAdapter = new DonorDonationHistoryListAdapter(getActivity(), arrayList1, arrayList2, arrayList3);
                    Log.e(TAG,"OK from fragment");

                    setListAdapter(listAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
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
