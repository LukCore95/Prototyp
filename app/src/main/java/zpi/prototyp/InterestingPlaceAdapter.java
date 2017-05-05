package zpi.prototyp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import zpi.data.model.ControlPoint;
import zpi.data.model.InterestingPlace;
import zpi.data.model.Trip;
import zpi.utils.DistanceCalculator;

/**
 * Created by Ania on 2017-05-05.
 */

public class InterestingPlaceAdapter extends BaseAdapter {
    private Context ctx;
    private List<InterestingPlace> ipList;
    private LatLng userLoc;

    public InterestingPlaceAdapter(Context ctx, List<InterestingPlace> ipList, LatLng userLoc){
        this.ctx = ctx;
        this.ipList = ipList;
        this.userLoc = userLoc;
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

        InterestingPlace currentCp = (InterestingPlace) getItem(position);

        TextView name = (TextView) mV.findViewById(R.id.ip_list_name);
        TextView type = (TextView) mV.findViewById(R.id.ip_list_type);
        TextView tvdist = (TextView) mV.findViewById(R.id.ip_list_dist);

        Typeface roboto = Typeface.createFromAsset(ctx.getAssets(), "fonts/roboto/Roboto-Light.ttf");
        name.setTypeface(roboto);
        type.setTypeface(roboto);
        tvdist.setTypeface(roboto);

        name.setText(currentCp.getName());

        if(userLoc != null) {
            int dist = (int)(DistanceCalculator.distance(userLoc.latitude, userLoc.longitude, currentCp.getGeoLoc().latitude, currentCp.getGeoLoc().longitude)*1000);
            if(dist >= 2000)
                tvdist.setText("" + dist/1000 + "km");
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
