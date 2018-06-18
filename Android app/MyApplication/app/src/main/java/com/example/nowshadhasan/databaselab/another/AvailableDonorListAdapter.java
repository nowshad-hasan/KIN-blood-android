package com.example.nowshadhasan.databaselab.another;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nowshad on 1/16/17.
 */

public class AvailableDonorListAdapter extends BaseAdapter implements DatePickerDialog.OnDateSetListener {

    SharedPreferences preferences;

    private int donorID;
    private String donationUpdateDate,donationUpdatePlace;
    private Context context;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ArrayList<Integer> arrayList1;
    private ArrayList<String> arrayList2;
    private ArrayList<String> arrayList3;
    private ArrayList<String> arrayList4;
    private ArrayList<String> arrayList5;
    private String TAG=AvailableDonorListAdapter.class.getSimpleName();
    private EditText donationPLaceEditText;

    private ArrayList<Integer> backUpArrayList1;
    private ArrayList<String> backUpArrayList2;
    private ArrayList<String> backUpArrayList3;
    private ArrayList<String> backUpArrayList4;
    private ArrayList<String> backUpArrayList5;
   // private Button btn;

    public AvailableDonorListAdapter(Activity mainActivity, ArrayList<Integer> arrayList1, ArrayList<String> arrayList2, ArrayList<String> arrayList3, ArrayList<String> arrayList4, ArrayList<String> arrayList5){
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
            convertView=activity.getLayoutInflater().inflate(R.layout.row,parent,false);

        TextView textView1=(TextView)convertView.findViewById(R.id.name);
        TextView textView2=(TextView)convertView.findViewById(R.id.email);
        TextView textView3=(TextView)convertView.findViewById(R.id.address);
        ImageView imageViewCall=(ImageView)convertView.findViewById(R.id.imageViewCall);
        ImageView imageViewMessage=(ImageView)convertView.findViewById(R.id.imageViewMessage);
        ImageView imageViewDonationUpdate=(ImageView)convertView.findViewById(R.id.imageViewUpdate);
        ImageView circleImageView=(ImageView)convertView.findViewById(R.id.image_view);
       //   btn=(Button) convertView.findViewById(R.id.donationUpdateButton);

        textView1.setText(arrayList2.get(position));
        textView2.setText(arrayList3.get(position));
        textView3.setText(arrayList4.get(position));

        donorID=arrayList1.get(position);

        final View finalConvertView = convertView;
        imageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                                Toast.makeText(,arrayList2.get(position) ,
//                        Toast.LENGTH_SHORT).show();

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+arrayList5.get(position)));
                    context.startActivity(callIntent);
                }

            }
        });

        imageViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", arrayList5.get(position));
                smsIntent.putExtra("sms_body","I'm from KIN Blood");
                context.startActivity(smsIntent);
            }
        });

       // final View finalConvertView1 = convertView;
       // final View finalConvertView1 = convertView;
        imageViewDonationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        AvailableDonorListAdapter.this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
//                //dpd.onDa
//                dpd.show(activity.getFragmentManager(), "Datepickerdialog");

//                AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(context);
//                View dialogView=layoutInflater.inflate(R.layout.donation_update_dialog,null);
//                dialogBuilder.setView(dialogView);

                final Dialog dialog=new Dialog(context);
                dialog.setTitle("Donation Update");
                dialog.setContentView(R.layout.donation_update_dialog);
                dialog.show();

                  donationPLaceEditText=(EditText)dialog.findViewById(R.id.donationUpdateEditText);
                Button datePickerButton=(Button) dialog.findViewById(R.id.donationUpdateButton);
                Button OKButton=(Button)dialog.findViewById(R.id.OKButton);
                Button cancelButton=(Button)dialog.findViewById(R.id.cancelButton);

                datePickerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                                        Toast.makeText(context, "sdg",
//                        Toast.LENGTH_SHORT).show();

                                        Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AvailableDonorListAdapter.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                //dpd.onDa
                dpd.show(activity.getFragmentManager(), "Datepickerdialog");
                    }
                });

                OKButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        donationUpdatePlace=donationPLaceEditText.getText().toString();

//                                                Toast.makeText(context,donationUpdatePlace,
//                        Toast.LENGTH_SHORT).show();

                        newDonationUpdate(donorID,donationUpdatePlace,donationUpdateDate);
                        dialog.dismiss();

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e(TAG,"Dialog dismiss");

                        dialog.dismiss();
                    }
                });

            }
        });



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,DonorInfoActivity.class);

              //  intent.putExtra("donorID",donorID);
                intent.putExtra("donorID",(arrayList1.get(position)));
                String donorInfoShare="Donor Information: "+arrayList2.get(position)+"\n"+"Donor Academic: "+arrayList3.get(position)+"\n"+"Phone Number: "+arrayList5.get(position);
                intent.putExtra("donorInfoShare",donorInfoShare);
             //   Log.e(TAG,donorInfoShare);
             //   Toast.makeText(context,donorInfoShare,Toast.LENGTH_SHORT).show();
              //  intent.putExtra("Dept",(arrayList5.get(position)));
              //  intent.putExtra("BloodGroup",(arrayList4.get(position)));
              //  intent.putExtra("PhoneNumber",(arrayList3.get(position)));

                context.startActivity(intent);

            }
        });


        String firstLetter = String.valueOf(arrayList2.get(position).charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(arrayList2.get(position));
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px

        circleImageView.setImageDrawable(drawable);



        Log.e(TAG,"OK from AvailableDonorListAdapter");

        return convertView;
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        donationUpdateDate =dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
//                        Toast.makeText(context,date,
//                        Toast.LENGTH_SHORT).show();
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

//String ipAddress="10.100.170.46";

//    http://localhost:8080/DatabaseLab/AddNewDonationUpdateServlet?DonorID=7&&DonationDate=16-7-2016&&DonationPLace=Osmani

    private void newDonationUpdate(int donorID,String donationUpdatePlace,String donationUpdateDate)
    {
        String url="http://"+preferences.getString("IPAddress",null)+":8080/DatabaseLab/AddNewDonationUpdateServlet?DonorID="+donorID+"&&DonationDate="+donationUpdateDate+"&&DonationPlace="+donationUpdatePlace;


        RequestQueue queue= Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                try {

               //   String validity = response.getString("date");

                    if (response.getString("addNewDonationUpdate").equals("successful")) {
                       // hideDialogue();
                        //Intent i = new Intent(getApplicationContext(), HomeScreen.class);
                        //startActivity(i);
                        Toast.makeText(context, "Blood Date successfully updated", Toast.LENGTH_SHORT).show();
                    } else {
                    //    hideDialogue();
                        Toast.makeText(context, "Blood date failed to update", Toast.LENGTH_SHORT).show();
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


