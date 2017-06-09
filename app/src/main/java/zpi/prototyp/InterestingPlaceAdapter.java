package zpi.prototyp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import zpi.controler.trip.TripController;
import zpi.data.model.InterestingPlace;
import zpi.data.model.InterestingPlaceType;
import zpi.utils.DistanceCalculator;

/**
 * Created by Ania on 2017-05-05.
 */

public class InterestingPlaceAdapter extends BaseAdapter {
    private MainActivity ctx;
    private List<InterestingPlace> ipList;
    private List<InterestingPlace> fullList;
    private TripController tripController;
    private LatLng userLoc;

    private static final double RANGE = 0.5;

    public InterestingPlaceAdapter(MainActivity ctx, List<InterestingPlace> ipList, LatLng userLoc, TripController tc){
        this.ctx = ctx;

        this.fullList=ipList;
        this.userLoc = userLoc;
        this.tripController = tc;
        choosePointsOnList();

    }

    public int getCount(){
        return ipList.size();
    }

    public Object getItem(int i){
        if(i >= getCount())
            return null;
        else
            return ipList.get(i);
    }

    public long getItemId(int i){
        //System.out.println("Adapter: getItemId");
        //return ((ControlPoint)getItem(i)).getId();
        return -1; //TODO
    }

    public View getView(int position, View convertView, ViewGroup groupParents){
        //choosePointsOnList();

        InterestingPlace curr = ((InterestingPlace) getItem(position));

        View mV;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView = inflater.inflate(R.layout.ip_list_menu_item, null);
        }
        mV = (View) convertView;

        final InterestingPlace currentCp = (InterestingPlace) getItem(position);

        TextView name = (TextView) mV.findViewById(R.id.ip_list_name);
        TextView type = (TextView) mV.findViewById(R.id.ip_list_type);
        TextView tvdist = (TextView) mV.findViewById(R.id.ip_list_dist);
        TextView tvaddress = (TextView) mV.findViewById(R.id.ip_address);
        ImageView image = (ImageView) mV.findViewById(R.id.imageView2);
        Button navigate = (Button) mV.findViewById(R.id.button_navigate);
        RelativeLayout clickDetails = (RelativeLayout) mV.findViewById(R.id.ip_click_details);

        Typeface roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/roboto/Roboto-Light.ttf");
        name.setTypeface(roboto);
        type.setTypeface(roboto);
        tvdist.setTypeface(roboto);
        tvaddress.setTypeface(roboto);

        if(!tripController.getCurrentCP().equals(currentCp)) {
            navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tripController.setNavigation(currentCp);


                    Toast toast =Toast.makeText(ctx, "   Teraz zmierzasz do: " + currentCp.getName() + "    ", Toast.LENGTH_SHORT);
                    View toastView=toast.getView();
                    toastView.setBackgroundColor(ctx.getResources().getColor(R.color.colorBackground));
                    toast.show();
                    Animation buttonAnim = new AlphaAnimation(1.0f, 0.3f);
                    //buttonAnim.setFillAfter(true);
                    buttonAnim.setDuration(1500);
                    v.setAnimation(buttonAnim);
                    v.startAnimation(buttonAnim);
                    ctx.refreshCurrentTarget();
                    InterestingPlaceAdapter.this.notifyDataSetChanged();
                }
            });
            //navigate.setBackgroundResource(R.drawable.ikona_nawiguj);
            navigate.setAlpha(1.0f);

        }
        else{
            navigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextIndex = tripController.getRouteControlPoints().indexOf(tripController.getCurrentTrip().getLastVisitedPoint())+1;
                    if(nextIndex>tripController.getRouteControlPoints().size())
                        nextIndex--;

                    tripController.setNavigation(tripController.getRouteControlPoints().get(nextIndex));

                    Toast toast1= Toast.makeText(ctx, "    An" +
                            "ulowano nawigację do: " + currentCp.getName() + "   ", Toast.LENGTH_SHORT);
                    View toastView1=toast1.getView();
                    toastView1.setBackgroundColor(ctx.getResources().getColor(R.color.colorBackground));
                    toast1.show();
                    Animation buttonAnim = new AlphaAnimation(0.3f, 1.0f);
                    //buttonAnim.setFillAfter(true);
                    buttonAnim.setDuration(1500);
                    v.setAnimation(buttonAnim);
                    v.startAnimation(buttonAnim);
                    ctx.refreshCurrentTarget();
                    InterestingPlaceAdapter.this.notifyDataSetChanged();
                }
            });
            navigate.setAlpha(0.3f);

        }
        clickDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToInterestingPlace=new Intent(ctx, Interesting_Details.class);
                intentToInterestingPlace.putExtra("nazwaPunktu", currentCp.getName());
                ctx.startActivity(intentToInterestingPlace);
            }
        });

        InterestingPlaceType cpType = currentCp.getType();
        name.setText(currentCp.getName());
        type.setText(InterestingPlaceType.fromTypeToString(cpType));
        if(currentCp.getAddress()!=null)
            tvaddress.setText(currentCp.getAddress());
        else{
            tvaddress.setVisibility(View.GONE);
        }

        Drawable icon = null;
        switch(cpType){
            case sakralny:
                icon = ctx.getDrawable(R.mipmap.ikona_kosciol_szara);
                break;
            case kultury:
                icon = ctx.getDrawable(R.mipmap.ikona_budynek_szara);
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

        return  mV;
    }

    public void setUserLoc(LatLng userLoc){
        this.userLoc = userLoc;
    }

    public void choosePointsOnList()
    {
        if(ctx.getIpNear()&&userLoc!=null) {
            ipList = new ArrayList<InterestingPlace>();

            for (int i = 0; i < fullList.size(); i++) {
                if (DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, fullList.get(i).getLatitude(), fullList.get(i).getLongitude()) < RANGE)
                {
                    ipList.add(fullList.get(i));
                }
            }
        }
        else
        {
            ipList=fullList;
        }
    }
}
