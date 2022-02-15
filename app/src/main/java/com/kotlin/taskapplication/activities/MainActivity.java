package com.kotlin.taskapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kotlin.taskapplication.App;
import com.kotlin.taskapplication.PersonDetailsSliderAdapter;
import com.kotlin.taskapplication.R;
import com.kotlin.taskapplication.entities.PersonEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    String URL="https://randomuser.me/api/?inc=gender,name,nat,location,picture,email&results=20";
    ArrayList<PersonEntity> personEntityArrayList;
    private ViewPager personDetailsViewPager;
    PersonDetailsSliderAdapter personDetailsSliderAdapter;
    ImageView imv_pre_btn, imv_next_btn;
    Context context;
    CardAdapter adapter;
    ArrayList<PersonEntity> list;
    RecyclerView recyclerView;
    App.LoadingDialog loadingDialog;
    int justClickedPos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getColor(R.color.transparent));
        context=MainActivity.this;
        setContentView(R.layout.activity_main);
        loadingDialog=new App.LoadingDialog(this,false);
        loadingDialog.show();
        personDetailsViewPager = findViewById(R.id.person_details_viewpager);
        recyclerView = findViewById(R.id.RV);
        imv_pre_btn = findViewById(R.id.prev_imv);
        imv_next_btn = findViewById(R.id.next_imv);

        imv_pre_btn.setOnClickListener(this);
        imv_next_btn.setOnClickListener(this);



        personEntityArrayList=new ArrayList<PersonEntity>();
        list=new ArrayList<PersonEntity>();

        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new CardAdapter(list,this);
        try
        {
            recyclerView.setAdapter(adapter);
        }
        catch (Exception e)
        {
            Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(App.isInternetAvailable(context))
                    parseApi();
                else
                    Toast.makeText(context, "check your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }, 1230);


    }
    public void parseApi()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                imv_pre_btn.setVisibility(View.VISIBLE);
                imv_next_btn.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("results");
                    int length=jsonArray.length();
                    //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, String.valueOf(length), Toast.LENGTH_SHORT).show();
                    Log.d("check", "length: "+length);

                    personEntityArrayList=new ArrayList<PersonEntity>();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        PersonEntity personEntity=new PersonEntity();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String gender=(String) jsonObject1.get("gender");
                        personEntity.setGender(gender);

                        JSONObject nameJsonObject=jsonObject1.getJSONObject("name");
                        String title=(String) nameJsonObject.get("title");
                        String fname=(String) nameJsonObject.get("first");
                        String lname=(String) nameJsonObject.get("last");
                        personEntity.setTitle(title);
                        personEntity.setFname(fname);
                        personEntity.setLname(lname);


                        JSONObject locationJsonObject=jsonObject1.getJSONObject("location");
                        JSONObject streetJsonObject=locationJsonObject.getJSONObject("street");
                        int streetNumber=(int) streetJsonObject.getInt("number");
                        String streetName=(String) streetJsonObject.get("name");
                        personEntity.setStreetNumber(streetNumber);
                        personEntity.setStreetName(streetName);

                        String city=(String) locationJsonObject.get("city");
                        Log.d("check", "city "+i);
                        String state=(String) locationJsonObject.get("state");
                        String country=(String) locationJsonObject.get("country");
                        String postcode=((Object) locationJsonObject.get("postcode")).toString();
                        personEntity.setCity(city);
                        personEntity.setState(state);
                        personEntity.setCountry(country);
                        personEntity.setPostCode(postcode);


                        JSONObject coordinatesJsonObject=locationJsonObject.getJSONObject("coordinates");
                        //String latitude=(String) coordinatesJsonObject.get("latitude");
                        //String longitude=(String) locationJsonObject.get("longitude");
                        double latitude=2.32;
                        double longitude=4.64;

                        //personEntity.setLat(Double.parseDouble(latitude));
                        //personEntity.setLog(Double.parseDouble(longitude))
                        personEntity.setLat(latitude);
                        personEntity.setLog(longitude);

                        JSONObject timezoneJsonObject=locationJsonObject.getJSONObject("timezone");
                        String offset=(String) timezoneJsonObject.get("offset");
                        String description=(String) timezoneJsonObject.get("description");
                        personEntity.setTimeZoneOffset(offset);
                        personEntity.setTimeZoneDesc(description);

                        String email=(String) jsonObject1.get("email");
                        personEntity.setEmail(email);

                        JSONObject pictureJsonObject=jsonObject1.getJSONObject("picture");
                        String largrPic=(String) pictureJsonObject.get("large");
                        String mediumPic=(String) pictureJsonObject.get("medium");
                        String thumbnailPic=(String) pictureJsonObject.get("thumbnail");
                        personEntity.setLpic(largrPic);
                        personEntity.setMpic(mediumPic);
                        personEntity.setTpic(thumbnailPic);

                        String nat=(String) jsonObject1.get("nat");
                        personEntity.setNationality(nat);

                        personEntityArrayList.add(personEntity);
                        personEntity=null;
                        nat=null;
                        thumbnailPic=null;
                        mediumPic=null;
                        largrPic=null;
                        pictureJsonObject=null;
                        email=null;
                        description=null;
                        offset=null;
                        latitude=0;
                        longitude=0;
                        timezoneJsonObject=null;
                        coordinatesJsonObject=null;
                        city=null;
                        country=null;
                        state=null;
                        postcode=null;
                        streetName=null;
                        streetNumber=0;
                        streetJsonObject=null;
                        locationJsonObject=null;
                        nameJsonObject=null;
                        title=null;
                        fname=null;
                        lname=null;
                        gender=null;
                        jsonObject1=null;



                        Log.d("check", "added "+i);

                    }

                    for(int j=0;j<personEntityArrayList.size();j++)
                    {
                        list.add(personEntityArrayList.get(j));
                    }
                    adapter.notifyDataSetChanged();
                    //adapter=new CardAdapter(personEntityArrayList,context);
                    //recyclerView.setAdapter(adapter);
                    //Toast.makeText(context, personEntityArrayList.toString(), Toast.LENGTH_SHORT).show();
                    personDetailsSliderAdapter = new PersonDetailsSliderAdapter(context, personEntityArrayList);

                    personDetailsViewPager.setAdapter(personDetailsSliderAdapter);
                    loadingDialog.dismiss();


                } catch (JSONException e) {
                    loadingDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
               {
                loadingDialog.dismiss();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.prev_imv) {
            personDetailsViewPager.setCurrentItem(getItem(-1), true);
        }
        else if (v.getId() == R.id.next_imv)
        {
            personDetailsViewPager.setCurrentItem(getItem(+1), true);
        }

        else if (v.getId() == R.id.optionMenu)
        {
            OnMenuClick(v);
        }

    }
    private int getItem(int i) {
        return personDetailsViewPager.getCurrentItem() + i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_menu,menu);
        return true;
    }

    public void OnMenuClick(View view) {

        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                        MainActivity.this,
                        "You Clicked : " + item.getTitle(),
                        Toast.LENGTH_SHORT
                ).show();
                return true;
            }
        });

        popup.show();
    }
    class CardAdapter extends RecyclerView.Adapter<CardAdapter.Holder> {

        private List<PersonEntity> passedList;
        private Context adapterContext;


        public CardAdapter(List<PersonEntity> list, Context context ) {
            this.passedList = list;
            this.adapterContext = context;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.person_details_card_layout,parent,false);
            Holder holder = new Holder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {

            final PersonEntity personEntity=(PersonEntity) passedList.get(position);

            holder.genderNatTv.setText(personEntity.getGender()+" . "+personEntity.getNationality());
            holder.titleFnameLNameCardTv.setText(personEntity.getTitle()+". "
                    +personEntity.getFname()+" "+personEntity.getLname());
            holder.emailTv.setText(personEntity.getEmail());
            Log.e("email:",personEntity.getEmail());

            if(getJustClickedPos()==position)
            {
                (holder.personDetailsCardView).setCardBackgroundColor(getResources().getColor(R.color.card_background));
                (holder.genderNatTv).setTextColor(getResources().getColor(R.color.white));
                (holder.titleFnameLNameCardTv).setTextColor(getResources().getColor(R.color.white));
                (holder.emailTv).setTextColor(getResources().getColor(R.color.white));
            }
            else
            {
                (holder.personDetailsCardView).setCardBackgroundColor(getResources().getColor(R.color.white));
                (holder.genderNatTv).setTextColor(getResources().getColor(R.color.black));
                (holder.titleFnameLNameCardTv).setTextColor(getResources().getColor(R.color.black));
                (holder.emailTv).setTextColor(getResources().getColor(R.color.orange));
            }


            holder.personDetailsCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(adapterContext, passedList.get(holder.getAdapterPosition()).getFname(), Toast.LENGTH_SHORT).show();

                    int count=personDetailsViewPager.getAdapter().getCount();
                    int currentPosition=holder.getAdapterPosition();
                    if(currentPosition>=0&& currentPosition<count)
                    {
                        personDetailsViewPager.setCurrentItem(currentPosition,true);
                    }

                    setJustClickedPos(currentPosition);
                    adapter.notifyDataSetChanged();
                }
            });
        }

        private void select(View v) {
            //CardView parent = (CardView) (v.getParent());
            ((CardView)v).setCardBackgroundColor(getResources().getColor(R.color.card_background));

            /*((TextView)v.findViewById(R.id.genderWithNatTv)).setTextColor(getResources().getColor(R.color.white));
            ((TextView)v.findViewById(R.id.titleFameLnameCardTv)).setTextColor(getResources().getColor(R.color.white));
            ((TextView)v.findViewById(R.id.emailTv)).setTextColor(getResources().getColor(R.color.white));*/
            //parent.findViewById(R.id.bus_description).setVisibility(View.VISIBLE);
        }

        private void deselect(View v) {
            //CardView parent = (CardView) (v.getParent());
            ((CardView)v).setCardBackgroundColor(getResources().getColor(R.color.white));

            /*((TextView)v.findViewById(R.id.genderWithNatTv)).setTextColor(getResources().getColor(R.color.black));
            ((TextView)v.findViewById(R.id.titleFameLnameCardTv)).setTextColor(getResources().getColor(R.color.black));
            ((TextView)v.findViewById(R.id.emailTv)).setTextColor(getResources().getColor(R.color.orange));*/
            //parent.findViewById(R.id.bus_description).setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return passedList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            private CardView personDetailsCardView;
            private TextView genderNatTv;
            private TextView titleFnameLNameCardTv;
            private TextView emailTv;

            public Holder(View itemView) {
                super(itemView);
                personDetailsCardView=(CardView) itemView.findViewById(R.id.personDetailsCardView);
                genderNatTv = itemView.findViewById(R.id.genderWithNatTv);
                titleFnameLNameCardTv=itemView.findViewById(R.id.titleFameLnameCardTv);
                emailTv = itemView.findViewById(R.id.emailTv);

            }

        }
        public  void setJustClickedPos(int pos)
        {
            justClickedPos=pos;

        }
        int getJustClickedPos()
        {
            return justClickedPos;
        }

    }
}