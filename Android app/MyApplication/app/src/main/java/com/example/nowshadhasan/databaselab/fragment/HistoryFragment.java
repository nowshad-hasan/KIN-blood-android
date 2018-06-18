package com.example.nowshadhasan.databaselab.fragment;



import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.SwipeLayout;
import com.example.nowshadhasan.databaselab.R;
import com.example.nowshadhasan.databaselab.activity.AddNewDonorActivity;
import com.example.nowshadhasan.databaselab.activity.MainActivity;
import com.example.nowshadhasan.databaselab.another.AvailableDonorListAdapter;
import com.example.nowshadhasan.databaselab.another.DonationHistoryListAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends ListFragment implements DatePickerDialog.OnDateSetListener {

    SwipeLayout swipeLayout;
    private String TAG=HistoryFragment.class.getSimpleName();
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String bloodGroup="";
    int year,month,day;
    DonationHistoryListAdapter listAdapter;
    SharedPreferences preferences;



    public HistoryFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_history, container, false);


        MaterialSpinner spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        spinner.setItems("B+","B-", "A+","A-", "O+", "O-", "AB+","AB-","ALL");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Toast.makeText(getActivity(), item+" "+position,
//                        Toast.LENGTH_SHORT).show();
                bloodGroup=item;
                populateJsonData(bloodGroup);

            }
        });



        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences=getActivity().getSharedPreferences("myPref",MODE_PRIVATE);


        if(bloodGroup==null)
            bloodGroup="ALL";
        populateJsonData(bloodGroup);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(bloodGroup==null)
                    bloodGroup="ALL";
                populateJsonData(bloodGroup);
            }
        });


//        Button calender=(Button)getView().findViewById(R.id.calender);
//        calender.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              //  Calendar now = Calendar.getInstance();
////                DialogFragment datePicker = new DatePickerFragment();
////                datePicker.show(getFragmentManager(), "DatePicker");
//
//               Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        HistoryFragment.this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
//
//                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
//
//            }
//        });

    }


    private void populateJsonData(String bloodGroup)
    {
        if(bloodGroup==null)
            bloodGroup="ALL";
        //   Log.e(TAG," First OK from fragment");

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        final ArrayList<Integer> arrayList1=new ArrayList<>();
        final ArrayList<String> arrayList2=new ArrayList<>();
        final ArrayList<String> arrayList3=new ArrayList<>();
        final ArrayList<String> arrayList4=new ArrayList<>();
        final ArrayList<String> arrayList5=new ArrayList<>();


        showDialogue();

//        String ipAddress="10.100.170.46";

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/DonationHistoryServlet?BloodGroup="+bloodGroup;
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
                    jsonArray = response.getJSONArray("donationHistory");
                    //    Log.e(TAG,Integer.toString(jsonArray.length()));

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);

                        arrayList1.add(jsonObject.getInt("donationID"));
                        arrayList2.add(jsonObject.getString("donorName")+" , "+jsonObject.getString("donorBloodGroup"));
                        arrayList3.add(jsonObject.getString("donorDept")+" , "+jsonObject.getString("donorSession"));
                        arrayList4.add(jsonObject.getString("donationDate"));
                        arrayList5.add(jsonObject.getString("donationPlace"));
                      //  arrayList5.add(jsonObject.getString("donorPhone1"));
                        Log.e(TAG,jsonObject.getString("donorName"));

                    }
                    hideDialogue();

                     listAdapter = new DonationHistoryListAdapter(getActivity(), arrayList1, arrayList2, arrayList3,arrayList4,arrayList5);
                    Log.e(TAG,"OK from fragment");
                    setListAdapter(listAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
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
        // Log.e(TAG,"akjsdhflhas");

    }

    private void hideDialogue(){
        if(progressDialog.isShowing())
            progressDialog.hide();
    }
    private void showDialogue(){
        if(!progressDialog.isShowing())
            progressDialog.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
    }

    public void makeSearch(String newText) {
        listAdapter.searchList(newText);
    }
}
