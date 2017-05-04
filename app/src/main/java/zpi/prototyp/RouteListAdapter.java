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

/**
 * Created by Wojtek on 2017-04-28.
 */

public class RouteListAdapter extends BaseAdapter {
    List<ControlPoint> list;
    Context ctx;

    public RouteListAdapter(Context ctx, List<ControlPoint> list){
        this.ctx = ctx;
        this.list = list;
    }

    public int getCount(){
        return list.size();
    }

    public Object getItem(int i){
        if(i >= getCount())
            return null;
        else
            return list.get(i);
    }

    public long getItemId(int i){
        //System.out.println("Adapter: getItemId");
        //return ((ControlPoint)getItem(i)).getId();
        return -1; //TODO
    }

    public View getView(int position, View convertView, ViewGroup groupParents){
        ControlPoint curr = ((ControlPoint) getItem(position));
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
        


        return  mV;
    }
}
