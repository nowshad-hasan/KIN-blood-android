package com.example.nowshadhasan.databaselab.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ExDonorFragment extends ListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG=ExDonorFragment.class.getSimpleName();

    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialSearchView searchView;
    AvailableDonorListAdapter listAdapter;

    private String bloodGroup="";

    SharedPreferences preferences;

    public ExDonorFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_ex_donor, container, false);



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

    }

    private void populateJsonData(String bloodGroup)
    {

        if(bloodGroup==null)
            bloodGroup="ALL";


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
        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/ExDonorServlet?BloodGroup="+bloodGroup;
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
                    jsonArray = response.getJSONArray("exDonor");
                    //    Log.e(TAG,Integer.toString(jsonArray.length()));

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = null;
                        jsonObject = jsonArray.getJSONObject(i);

                        arrayList1.add(jsonObject.getInt("donorId"));
                        arrayList2.add(jsonObject.getString("donorName")+" , "+jsonObject.getString("donorBloodGroup"));
                        arrayList3.add(jsonObject.getString("donorDept")+" , "+jsonObject.getString("donorSession"));
                        arrayList4.add(jsonObject.getString("donationTimeDiff")+" ago");
                        arrayList5.add(jsonObject.getString("donorPhone1"));
                        Log.e(TAG,jsonObject.getString("donorName"));

                    }
                    hideDialogue();

                    listAdapter = new AvailableDonorListAdapter(getActivity(), arrayList1, arrayList2, arrayList3,arrayList4,arrayList5);
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

    public void makeSearch(String newText) {
        listAdapter.searchList(newText);
    }

    }







