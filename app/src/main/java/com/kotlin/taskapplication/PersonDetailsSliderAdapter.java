package com.kotlin.taskapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.kotlin.taskapplication.common.P;
import com.kotlin.taskapplication.entities.PersonEntity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class PersonDetailsSliderAdapter extends PagerAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<PersonEntity> personList;



    public PersonDetailsSliderAdapter(Context context, ArrayList<PersonEntity> personList) {
        this.context = context;
        this.personList= personList;
    }


    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (CardView) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.person_details_item_layout, container, false);

        PersonEntity personEntity = personList.get(position);

        String title = personEntity.getTitle();
        String fname = personEntity.getFname();
        String lname = personEntity.getLname();
        TextView textView = v.findViewById(R.id.titleFameLnameTv);
        textView.setSelected(true);
        textView.setText(title+". "+fname+" "+lname);
        title=null;fname=null;lname=null;

        int streetNumber = personEntity.getStreetNumber();
        String streetName = personEntity.getStreetName();
        String city = personEntity.getCity();
        String state = personEntity.getState();


        textView = v.findViewById(R.id.streetNoTv);
        textView.setText(streetNumber+", ");

        textView = v.findViewById(R.id.streetNameCityStateTv);
        textView.setText(streetName+", "+city+", "+state);

        streetNumber=0;streetName=null;city=null;state=null;

        String country = personEntity.getCountry();
        String postcode = personEntity.getPostCode();

        textView = v.findViewById(R.id.countryTv);
        textView.setText(country+", ");

        textView = v.findViewById(R.id.postCodeTv);
        textView.setText(postcode);
        country=null;postcode=null;


        String timezoneOffset = personEntity.getTimeZoneOffset();
        String timezoneDesc = personEntity.getTimeZoneDesc();

        textView = v.findViewById(R.id.timezoneOffsetandDescTv);
        textView.setSelected(true);
        textView.setText(timezoneOffset+" - "+timezoneDesc);
        timezoneOffset=null;timezoneDesc=null;



        String gender = personEntity.getGender();
        textView = v.findViewById(R.id.genderTv);
        textView.setText(gender.toUpperCase());
        gender=null;

        String largePic=personEntity.getLpic();
        String mediumPic=personEntity.getMpic();
        String thumbnailPic=personEntity.getTpic();

        Log.e("med: "+String.valueOf(position),mediumPic);
        try {
            Picasso.get().load(mediumPic)
                    .fit().placeholder(R.drawable.user)
                    .into(((ImageView) v.findViewById(R.id.image_profile_pic)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        largePic=null;mediumPic=null;thumbnailPic=null;


        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }

    @Override
    public void onClick(View view) {

    }
}

