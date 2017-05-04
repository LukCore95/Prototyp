package zpi.prototyp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import zpi.data.model.ControlPoint;
import zpi.data.model.Trip;

/**
 * Created by Wojtek on 2017-04-28.
 */

public class RouteListAdapter extends BaseAdapter {
    //List<ControlPoint> list;
    private Trip trip;
    Context ctx;


    public RouteListAdapter(Context ctx, Trip trip){
        this.ctx = ctx;
        this.trip = trip;
    }

    public int getCount(){
        return trip.getModifiedRoute().size();
    }

    public Object getItem(int i){
        if(i >= getCount())
            return null;
        else
            return trip.getModifiedRoute().get(i);
    }

    public long getItemId(int i){
        //System.out.println("Adapter: getItemId");
        //return ((ControlPoint)getItem(i)).getId();
        return -1; //TODO
    }

    public View getView(int position, View convertView, ViewGroup groupParents){
        ControlPoint curr = ((ControlPoint) getItem(position));
        int lastPosition = trip.getLastVisitedPoint()!=null?trip.getModifiedRoute().indexOf(trip.getLastVisitedPoint()):-1;
        int targetPosition = (trip.getCurrentTarget() instanceof ControlPoint)?trip.getModifiedRoute().indexOf(trip.getCurrentTarget()):-1;

        View mV;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView = inflater.inflate(R.layout.route_points_menu_item, null);
        }
        mV = (View) convertView;

        ControlPoint currentCp = (ControlPoint) getItem(position);

        ImageView img = (ImageView) mV.findViewById(R.id.route_points_icon);
        TextView polish = (TextView) mV.findViewById(R.id.route_points_name);
        TextView german = (TextView) mV.findViewById(R.id.route_points_german);
        TextView distance = (TextView) mV.findViewById(R.id.route_points_distance);

        Typeface roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/roboto/Roboto-Light.ttf");
        polish.setTypeface(roboto);
        german.setTypeface(roboto);
        distance.setTypeface(roboto);

        polish.setText(currentCp.getName());
        german.setText(currentCp.getGermanName());
        if(position == targetPosition){
            img.setImageDrawable(ctx.getDrawable(R.drawable.pkt_akt));
        }
        else if(position <= lastPosition){
            img.setImageDrawable(ctx.getDrawable(R.drawable.pkt_odw));
        }
        else{
            img.setImageDrawable(ctx.getDrawable(R.drawable.pkt_n_odw));
        }
        


        return  mV;
    }
}
