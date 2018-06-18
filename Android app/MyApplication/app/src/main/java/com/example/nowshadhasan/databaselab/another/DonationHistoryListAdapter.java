package com.example.nowshadhasan.databaselab.another;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nowshad Hasan on 7/22/2017.
 */

public class DonationHistoryListAdapter extends BaseAdapter implements DatePickerDialog.OnDateSetListener {



    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private String TAG=AvailableDonorListAdapter.class.getSimpleName();
    private ArrayList<Integer> arrayList1;
    private ArrayList<String> arrayList2;
    private ArrayList<String> arrayList3;
    private ArrayList<String> arrayList4;
    private ArrayList<String> arrayList5;



    private ArrayList<Integer> backUpArrayList1;
    private ArrayList<String> backUpArrayList2;
    private ArrayList<String> backUpArrayList3;
    private ArrayList<String> backUpArrayList4;
    private ArrayList<String> backUpArrayList5;

    private int donationID;

    private EditText donationPLaceEditText;

    private String editDonationDate,editDonationPlace, getEditDonationDateBackup;

    SharedPreferences preferences;



    public  DonationHistoryListAdapter(Activity mainActivity, ArrayList<Integer> arrayList1, ArrayList<String> arrayList2, ArrayList<String> arrayList3, ArrayList<String> arrayList4,ArrayList<String> arrayList5){
        context=mainActivity;

        preferences=context.getSharedPreferences("myPref",MODE_PRIVATE);

        activity=mainActivity;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList1=arrayList1;
        this.arrayList2=arrayList2;
        this.arrayList3=arrayList3;
        this.arrayList4=arrayList4;
        this.arrayList5=arrayList5;


        this.backUpArrayList1 = new ArrayList<>(arrayList1);
        this.backUpArrayList2 = new ArrayList<>(arrayList2);
        this.backUpArrayList3 = new ArrayList<>(arrayList3);
        this.backUpArrayList4 = new ArrayList<>(arrayList4);
        this.backUpArrayList5 = new ArrayList<>(arrayList5);

    }



    @Override
    public int getCount() {
        return arrayList1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=activity.getLayoutInflater().inflate(R.layout.donation_history_row,parent,false);


        TextView donorDetailsTextView=(TextView)convertView.findViewById(R.id.donorDetailsTextView);
        TextView academicDescriptionTextView=(TextView)convertView.findViewById(R.id.academicDescriptionTextView);
        TextView donationDetailsTextView=(TextView)convertView.findViewById(R.id.donationDetailsTextView);
        ImageView editImageView=(ImageView)convertView.findViewById(R.id.editPersonalHistoryImageView);
        ImageView deleteImageView=(ImageView)convertView.findViewById(R.id.deletePersonalHistoryImageView);


        donorDetailsTextView.setText(arrayList2.get(position));
        academicDescriptionTextView.setText(arrayList3.get(position));
        donationDetailsTextView.setText(arrayList4.get(position)+" , "+arrayList5.get(position));



        final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

      //  donationID=arrayList1.get(position);

        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog=new Dialog(context);
                dialog.setTitle("Edit Donation");
                dialog.setContentView(R.layout.donation_update_dialog);
                dialog.show();

                donationPLaceEditText=(EditText)dialog.findViewById(R.id.donationUpdateEditText);
                Button datePickerButton=(Button) dialog.findViewById(R.id.donationUpdateButton);
                Button OKButton=(Button)dialog.findViewById(R.id.OKButton);
                Button cancelButton=(Button)dialog.findViewById(R.id.cancelButton);

                donationPLaceEditText.setText(arrayList5.get(position));

                datePickerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Toast.makeText(context, "sdg",
//                                Toast.LENGTH_SHORT).show();

                        Calendar donationCal = Calendar.getInstance();
                        try {
                            donationCal.setTime(dateFormat.parse(arrayList4.get(position)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                DonationHistoryListAdapter.this,
                                donationCal.get(Calendar.YEAR),
                                donationCal.get(Calendar.MONTH),
                                donationCal.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                        //dpd.onDa
                        dpd.show(activity.getFragmentManager(), "Datepickerdialog");
                    }
                });

                OKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                 //       URLEncoder.encode("apples oranges", "utf-8");
                        try {
                            editDonationPlace=URLEncoder.encode(donationPLaceEditText.getText().toString(),"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        donationID=arrayList1.get(position);

//                                                Toast.makeText(context,donationUpdatePlace,
//                        Toast.LENGTH_SHORT).show();

                        getEditDonationDateBackup=arrayList4.get(position);


                        editExistingDonaton(donationID,editDonationDate,editDonationPlace);
                        dialog.dismiss();

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"You have not permission to delete this item!",
                        Toast.LENGTH_LONG).show();

            }
        });



        return convertView;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        editDonationDate =dayOfMonth+"-"+(monthOfYear+1)+"-"+year;



    }



    private void editExistingDonaton(int donationID,String editDonationDate,String editDonationPlace)
    {

        if(editDonationDate==null)
            editDonationDate=getEditDonationDateBackup;


//        String ipAddress="10.100.170.46";
      //  Toast.makeText(context,"Hey it is in",Toast.LENGTH_SHORT).show();

        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/EditExistingDonationServlet?DonationID="+donationID+"&&DonationPlace="+editDonationPlace+"&&DonationDate="+editDonationDate;

         // Toast.makeText(context,url,Toast.LENGTH_SHORT).show();

        Log.e(TAG,url);

        RequestQueue queue= Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {

                    //   String validity = response.getString("date");

                    if (response.getString("editExistingDonationUpdate").equals("successful")) {
                        // hideDialogue();
                        //Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                        //startActivity(i);
                        Toast.makeText(context, "Blood Donation successfully edited", Toast.LENGTH_SHORT).show();
                    } else {
                        //    hideDialogue();
                        Toast.makeText(context, "Blood Donation failed to edit", Toast.LENGTH_SHORT).show();
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

    public void searchList(String text) {
        if (text.length() == 0) {
            arrayList1 = new ArrayList<>(backUpArrayList1);
            arrayList2 = new ArrayList<>(backUpArrayList2);
            arrayList3 = new ArrayList<>(backUpArrayList3);
            arrayList4 = new ArrayList<>(backUpArrayList4);
            arrayList5 = new ArrayList<>(backUpArrayList5);
            return;
        }
        arrayList1.clear();
        arrayList2.clear();
        arrayList3.clear();
        arrayList4.clear();
        arrayList5.clear();
        for (int i = 0; i < backUpArrayList1.size(); i++) {
            if (backUpArrayList2.get(i).toLowerCase().contains(text.toLowerCase())) {
                arrayList1.add(backUpArrayList1.get(i));
                arrayList2.add(backUpArrayList2.get(i));
                arrayList3.add(backUpArrayList3.get(i));
                arrayList4.add(backUpArrayList4.get(i));
                arrayList5.add(backUpArrayList5.get(i));
            }
        }
        notifyDataSetChanged();
    }

}
