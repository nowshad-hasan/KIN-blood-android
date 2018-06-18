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
import com.example.nowshadhasan.databaselab.activity.DonorInfoActivity;
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
 * Created by Nowshad Hasan on 7/21/2017.
 */

public class DonorDonationHistoryListAdapter extends BaseAdapter implements DatePickerDialog.OnDateSetListener {


    private String TAG=AvailableDonorListAdapter.class.getSimpleName();
    private int donationID;

    private String editDonationDate,editDonationPlace,getEditDonationDateBackup;

    private Context context;
    private Activity activity;
    private LayoutInflater layoutInflater;

    private ArrayList<Integer> arrayList1;
    private ArrayList<String> arrayList2;
    private ArrayList<String> arrayList3;

    private EditText donationPLaceEditText;
    SharedPreferences preferences;


    public DonorDonationHistoryListAdapter(Activity mainActivity, ArrayList<Integer> arrayList1, ArrayList<String> arrayList2, ArrayList<String> arrayList3){
        context=mainActivity;

        preferences=context.getSharedPreferences("myPref",MODE_PRIVATE);

        activity=mainActivity;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList1=arrayList1;
        this.arrayList2=arrayList2;
        this.arrayList3=arrayList3;







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
            convertView=activity.getLayoutInflater().inflate(R.layout.donor_donation_history_row,parent,false);


        TextView donationDateTextView=(TextView)convertView.findViewById(R.id.donationDateTextView);
        TextView donationPlaceTextView=(TextView)convertView.findViewById(R.id.donationPlaceTextView);
        ImageView editImageView=(ImageView)convertView.findViewById(R.id.editPersonalHistoryImageView);
        ImageView deleteImageView=(ImageView)convertView.findViewById(R.id.deletePersonalHistoryImageView);


        donationDateTextView.setText(arrayList2.get(position));
        donationPlaceTextView.setText(arrayList3.get(position));

        final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");



        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog=new Dialog(context);
                dialog.setTitle("sdrt");
                dialog.setContentView(R.layout.donation_update_dialog);
                dialog.show();

                donationPLaceEditText=(EditText)dialog.findViewById(R.id.donationUpdateEditText);
                Button datePickerButton=(Button) dialog.findViewById(R.id.donationUpdateButton);
                Button OKButton=(Button)dialog.findViewById(R.id.OKButton);
                Button cancelButton=(Button)dialog.findViewById(R.id.cancelButton);

                donationPLaceEditText.setText(arrayList3.get(position));

                datePickerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(context, "sdg",
                                Toast.LENGTH_SHORT).show();

                        Calendar donationCal = Calendar.getInstance();
                        try {
                            donationCal.setTime(dateFormat.parse(arrayList2.get(position)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                DonorDonationHistoryListAdapter.this,
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
                        try {
                            editDonationPlace= URLEncoder.encode(donationPLaceEditText.getText().toString(),"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

//                                                Toast.makeText(context,donationUpdatePlace,
//                        Toast.LENGTH_SHORT).show();

                        donationID=arrayList1.get(position);

                        getEditDonationDateBackup=arrayList2.get(position);


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
                        Toast.LENGTH_SHORT).show();
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

        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/EditExistingDonationServlet?DonationID="+donationID+"&&DonationDate="+editDonationDate+"&&DonationPlace="+editDonationPlace;


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

}
