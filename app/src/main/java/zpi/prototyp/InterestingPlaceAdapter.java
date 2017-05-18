package zpi.prototyp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

import java.util.List;

import zpi.controler.trip.TripController;
import zpi.data.model.ControlPoint;
import zpi.data.model.InterestingPlace;
import zpi.data.model.InterestingPlaceType;
import zpi.data.model.Trip;
import zpi.utils.DistanceCalculator;

/**
 * Created by Ania on 2017-05-05.
 */

public class InterestingPlaceAdapter extends BaseAdapter {
    private MainActivity ctx;
    private List<InterestingPlace> ipList;
    private TripController tripController;
    private LatLng userLoc;

    public InterestingPlaceAdapter(MainActivity ctx, List<InterestingPlace> ipList, LatLng userLoc, TripController tc){
        this.ctx = ctx;
        this.ipList = ipList;
        this.userLoc = userLoc;
        this.tripController = tc;
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
        tvaddress.setText(currentCp.getAddress());

        Drawable icon = null;
        switch(cpType){
            case sakralny:
                icon = ctx.getDrawable(R.drawable.ikona_kosciol_szara);
                break;
            case kultury:
                icon = ctx.getDrawable(R.drawable.ikona_budynek_szara);
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
}
