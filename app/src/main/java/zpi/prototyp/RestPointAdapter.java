package zpi.prototyp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.model.Line;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import zpi.controler.trip.TripController;
import zpi.data.model.InterestingPlace;
import zpi.data.model.InterestingPlaceType;
import zpi.data.model.RestPoint;
import zpi.data.model.RestPointType;
import zpi.utils.DistanceCalculator;

/**
 * Created by Wojtek on 2017-05-19.
 */

public class RestPointAdapter extends BaseAdapter {
    private static final String HTTP = "http://";

    private MainActivity ctx;
    private List<RestPoint> rpList;
    private List<RestPoint> fullList;
    private TripController tripController;
    private LatLng userLoc;

    private static final double RANGE = 0.2; //0.200? KM RANGE



    //private static final double RANGE = 20; //4 KM RANGE

    public RestPointAdapter(MainActivity ctx, List<RestPoint> rpList, LatLng userLoc, TripController tc){
        this.ctx = ctx;

        //this.fullList=rpList;
        this.fullList = rpList;
        this.userLoc = userLoc;
        this.tripController = tc;
        choosePointsOnList();
        System.out.println("Adapter: " + rpList.size() + " miejsc odpoczynku");

    }

    public int getCount(){
        return rpList.size();
    }

    public Object getItem(int i){
        if(i >= getCount())
            return null;
        else
            return rpList.get(i);
    }

    public long getItemId(int i){
        //System.out.println("Adapter: getItemId");
        //return ((ControlPoint)getItem(i)).getId();
        return -1; //TODO
    }

    public View getView(int position, View convertView, ViewGroup groupParents){
        choosePointsOnList();
        RestPoint curr = ((RestPoint) getItem(position));

        View mV;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView = inflater.inflate(R.layout.rp_list_menu_item, null);
        }
        mV = (View) convertView;

        final RestPoint currentCp = (RestPoint) getItem(position);

        TextView name = (TextView) mV.findViewById(R.id.rp_name);
        TextView tvdist = (TextView) mV.findViewById(R.id.rp_dist);
        TextView tvaddress = (TextView) mV.findViewById(R.id.rp_address);
        TextView desc = (TextView) mV.findViewById(R.id.rp_desc);
        Button web = (Button) mV.findViewById(R.id.rp_website);
        RelativeLayout more_details = (RelativeLayout) mV.findViewById(R.id.rp_more_details);
        //LinearLayout all = (LinearLayout) mV.findViewById(R.id.rp_list_item_layout);
        Button navigate = (Button) mV.findViewById(R.id.rp_navigate);
        ImageView image = (ImageView) mV.findViewById(R.id.rp_icon);
        //more = (ImageView) mV.findViewById(R.id.rp_more);
        //Button navigate = (Button) mV.findViewById(R.id.button_navigate); //TODO navigation
        //RelativeLayout clickDetails = (RelativeLayout) mV.findViewById(R.id.ip_click_details);

        Typeface roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/roboto/Roboto-Light.ttf");
        name.setTypeface(roboto);
        //type.setTypeface(roboto);
        tvdist.setTypeface(roboto);
        tvaddress.setTypeface(roboto);
        desc.setTypeface(roboto);
        /*navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripController.setNavigation(currentCp);

                Toast.makeText(ctx, "Teraz zmierzasz do: " + currentCp.getName(), Toast.LENGTH_SHORT).show();
                Animation buttonAnim = new AlphaAnimation(0.3f, 1.0f);
                buttonAnim.setDuration(1000);
                v.setAnimation(buttonAnim);
                v.startAnimation(buttonAnim);
                ctx.refreshCurrentTarget();
            }
        });*/
        /*clickDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToInterestingPlace=new Intent(ctx, Interesting_Details.class);
                intentToInterestingPlace.putExtra("nazwaPunktu", currentCp.getName());
                ctx.startActivity(intentToInterestingPlace);
            }
        });*/

        RestPointType rpType = currentCp.getType();
        name.setText(currentCp.getName());
        //type.setText(InterestingPlaceType.fromTypeToString(cpType));
        tvaddress.setText(currentCp.getAddress());
        desc.setText(currentCp.getDescription());

        Drawable icon = null;
        switch(rpType){
            case cafe:
                icon = ctx.getDrawable(R.mipmap.ikona_kawiarni);
                break;
            case pub:
                icon = ctx.getDrawable(R.mipmap.ikona_pubu);
                break;
            case restaurant:
                icon = ctx.getDrawable(R.mipmap.ikona_restauracji);
                break;
        }

        image.setImageDrawable(icon);

        if(userLoc != null) {
            int dist = (int)(DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, currentCp.getGeoLoc().latitude, currentCp.getGeoLoc().longitude)*1000);
            if(dist >= 2000)
                tvdist.setText("" + String.format("%.1f", ((float)dist)/1000) + "km");
            else
                tvdist.setText("" + dist + "m");
        }
        else
            tvdist.setText("");

        if(currentCp.getWeb()==null)
            web.setVisibility(View.INVISIBLE);

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(HTTP + currentCp.getWeb()));
                ctx.startActivity(goToWebsite);
            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripController.setNavigation(currentCp);


                Toast.makeText(ctx, "Teraz zmierzasz do: " + currentCp.getName(), Toast.LENGTH_SHORT).show();
                Animation buttonAnim = new AlphaAnimation(0.3f, 1.0f);
                buttonAnim.setDuration(1000);
                v.setAnimation(buttonAnim);
                v.startAnimation(buttonAnim);
                ctx.refreshCurrentTarget();
            }
        });

        return  mV;
    }

    public void setUserLoc(LatLng userLoc){
        this.userLoc = userLoc;
    }

    public void choosePointsOnList()
    {
        if(userLoc!=null) {
            rpList = new ArrayList<RestPoint>();

            for (int i = 0; i < fullList.size(); i++) {
                if (DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, fullList.get(i).getLatitude(), fullList.get(i).getLongitude()) < RANGE)
                {
                    rpList.add(fullList.get(i));
                }
            }
        }
        else
        {
            rpList=fullList;
        }
    }
}
