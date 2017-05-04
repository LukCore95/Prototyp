package zpi.prototyp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.FloatMath;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import zpi.controler.trip.TripControler;
import zpi.data.db.dao.ControlPointDAO;
import zpi.data.db.dao.ControlPointDAOOptimized;
import zpi.data.db.MockDbHelper;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.Point;
import zpi.data.model.Trip;
import zpi.utils.DistanceCalculator;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener, PopupMenu.OnMenuItemClickListener {

    //kod woja
    private NavigationView navView;
    private DrawerLayout drawer;
    private ListView lv;
    private TripControler tripControler;
    private RouteListAdapter adapter;
    private List<ControlPoint> basicRoute;
    //end kod woja

    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private CameraUpdate mCenter;
    private CameraUpdate mZoom;
    private LatLng mLatLng;

    private Route route;
    private Leg leg;
    private ArrayList<LatLng> directionPositionList;
    private PolylineOptions polylineOptions;
    private Polyline mPolyline;
    private List<PatternItem> pattern;

    private ImageButton locIB;
    private ImageButton fullMapIB;
    private boolean locIBisPressed;
    private Info dystansInfo;
    private String dystansText;
    private TextView textUpperToolbarGerman;
    private TextView textUpperToolbarPolish;
    private TextView textBottomToolbar; //bottombar text
    private PopupMenu popup;
    private LatLng routeTo;
    private String deName = "Warenhaus Werheim" ;//first target
    private String plName = "Dom handlowy Renoma"; //first target
    private List<ControlPoint> controlPoints;
    //private Trip currentTrip;
    boolean firstRoute=true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //wczytanie z bazy danych TUTAJ
       // currentTrip=SplashScreen.getTrip();
        tripControler = new TripControler(this, SplashScreen.getTrip());
        controlPoints = tripControler.getRouteControlPoints();

        System.out.println("Wczytano " + controlPoints.size() + " punktów kontrolnych");

        //kod woja
        navView = (NavigationView) findViewById(R.id.navigation_view);
        drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        //end kod woja

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        locIBisPressed = false;

        locIB = (ImageButton) findViewById(R.id.location);
        locIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locIBisPressed) {
                    locIBisPressed = true;
                    locIB.setImageResource(R.mipmap.action_button_cel);
                    centerLocationToMe();
                }
                else {
                    locIBisPressed = false;
                    locIB.setImageResource(R.mipmap.action_button_lokalizacja);
                    centerLocationToDestination();
                }

            }
        });

        /*fullMapIB = (ImageButton) findViewById(R.id.fullMap);
        fullMapIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locIBisPressed) {
                    locIBisPressed = true;
                    fullMapIB.setImageResource(R.mipmap.action_button_mapa);
                    centerLocationToDestination();}
                else {
                    locIBisPressed = false;
                    fullMapIB.setImageResource(R.mipmap.action_button_lokalizacja);
                    showFullMap();

                }

            }
        });*/

        textUpperToolbarGerman = (TextView)findViewById(R.id.textUp);
        textUpperToolbarPolish = (TextView)findViewById(R.id.textDown);
        textBottomToolbar = (TextView) findViewById(R.id.textBottomBar);
        Typeface deutschmeister = Typeface.createFromAsset(getAssets(), "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/roboto/Roboto-Light.ttf");
        textUpperToolbarGerman.setTypeface(deutschmeister);
        textUpperToolbarPolish.setTypeface(roboto);
        textBottomToolbar.setTypeface(roboto);

//        place1 = new LatLng(51.103851, 17.031064);
//        place2 = new LatLng(51.104082, 17.030082);
//        place3 = new LatLng(51.105483, 17.031921);
//        place4 = new LatLng(51.105059, 17.031117);

        //first route - boolean do sprawdzenia najblizszego punktu w pierwszym wywołaniu onLocationChanged
        firstRoute=SplashScreen.firstTrip;
        textUpperToolbarGerman.setText(deName);


        //kod woja
        pattern = Arrays.<PatternItem>asList(new Gap(20), new Dash(40));
        ((TextView) findViewById(R.id.route_points_title)).setTypeface(deutschmeister);
        ((TextView) findViewById(R.id.route_points_explanation)).setTypeface(roboto);

        lv = (ListView) findViewById(R.id.route_points_list);
        //List<ControlPoint> testCPList = new ArrayList<ControlPoint>();

        //db test
        /*MockDbHelper dbHelp = new MockDbHelper(this);
        SQLiteDatabase database = dbHelp.getReadableDatabase();
        ControlPointDAO cpdao = new ControlPointDAOOptimized(database, null);
        ControlPoint cp = cpdao.getControlPoint("Podwale");*/
        //Toast.makeText(this, "Punkcior: " + database.rawQuery("SELECT * FROM ControlPoint", null).getString(1), Toast.LENGTH_LONG).show();
        /*Toast.makeText(this, "Zwrócono punkt: " + cp.getGermanName() + cp.getDate() + cp.getLatitude(), Toast.LENGTH_LONG).show();
        cp = cpdao.getControlPoint("Dom handlowy Renoma");
        Toast.makeText(this, "Zwrócono punkt: " + cp.getGermanName() + cp.getDate() + cp.getLatitude(), Toast.LENGTH_LONG).show();
        cp = cpdao.getControlPoint("Ulica Świdnicka");
        Toast.makeText(this, "Zwrócono punkt: " + cp.getGermanName() + cp.getDate() + cp.getLatitude(), Toast.LENGTH_LONG).show();
        cp = cpdao.getControlPoint("Plac Teatralny");
        Toast.makeText(this, "Zwrócono punkt: " + cp.getGermanName() + cp.getDate() + cp.getLatitude(), Toast.LENGTH_LONG).show();*/
        //database.close();

       // testCPList = controlPoints;

        adapter = new RouteListAdapter(this, tripControler.getCurrentTrip(), tripControler.getUserLoc());
        lv.setAdapter(adapter);
        //lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Trip newTrip = new Trip(tripControler.getCurrentTrip().getRoute(), (ControlPoint) parent.getItemAtPosition(position), 2); //TODO generate an index
                zpi.data.model.Route route = tripControler.getCurrentTrip().getRoute();
                tripControler.setNewTrip(route, (ControlPoint) parent.getItemAtPosition(position));
                adapter.setTrip(tripControler.getCurrentTrip());
                adapter.notifyDataSetChanged();
                controlPoints = tripControler.getRouteControlPoints();
                ControlPoint firstCp = controlPoints.get(0);

                routeTo = firstCp.getGeoLoc();
                deName = firstCp.getGermanName();
                plName= firstCp.getName();
                drawer.closeDrawer(GravityCompat.END, true);
            }
        });

        basicRoute = tripControler.getCurrentTrip().getRoute().getRoutePoints();

        Point currentCp = tripControler.getCurrentCP();
        System.out.println("OBECNY PUNKT: " + currentCp.getName());
        routeTo = currentCp.getGeoLoc();
        deName = (currentCp instanceof ControlPoint)?((ControlPoint) currentCp).getGermanName():"";
        plName = currentCp.getName();
   }

    public void showPopup(View v) {
        /*popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.actionbar_menu);
        popup.show();*/
        drawer.openDrawer(GravityCompat.END, true);
    }

    public void showBottom(View v) {
        popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.bottombar_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.position1:
                if(mLastLocation != null) {
                    deName = controlPoints.get(0).getGermanName();
                    plName = controlPoints.get(0).getName();
                    routeTo = controlPoints.get(0).getGeoLoc();
                    onLocationChanged(mLastLocation);
                }
                return true;
            case R.id.position2:
                if(mLastLocation != null) {
                    deName = controlPoints.get(1).getGermanName();
                    plName = controlPoints.get(1).getName();
                    routeTo = controlPoints.get(1).getGeoLoc();
                    onLocationChanged(mLastLocation);
                }
                return true;
            case R.id.position3:
                if(mLastLocation != null) {
                    deName = controlPoints.get(2).getGermanName();
                    plName = controlPoints.get(2).getName();
                    routeTo = controlPoints.get(2).getGeoLoc();
                    onLocationChanged(mLastLocation);
                }
                return true;
            case R.id.position4:
                if(mLastLocation != null) {
                    deName = controlPoints.get(3).getGermanName();
                    plName = controlPoints.get(3).getName();
                    routeTo = controlPoints.get(3).getGeoLoc();
                    onLocationChanged(mLastLocation);
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission. ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if(mGoogleMap != null) {
                    onMapReady(mGoogleMap);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;

        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.michal_json));
        }
        catch (Resources.NotFoundException e) {}

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
            else {
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setOnMarkerClickListener(this);

        mCenter = CameraUpdateFactory.newLatLng(routeTo);
        mGoogleMap.moveCamera(mCenter);
        mZoom = CameraUpdateFactory.zoomTo(17);
        mGoogleMap.animateCamera(mZoom);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //adding markes
        for(int i=0; i<basicRoute.size(); i++)
        {
            mGoogleMap.addMarker(new MarkerOptions().position(basicRoute.get(i).getGeoLoc()).icon(BitmapDescriptorFactory.fromResource(basicRoute.get(i).getIcon())));
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        if (marker.equals(mMarker))
//        { l
//
            Intent intent = new Intent(MainActivity.this,Details.class);

            intent.putExtra("nazwaPunktu", basicRoute.get(getMarkersID((marker.getId()))).getName());
            startActivity(intent);

//        }
        return false;
    }

    public int getMarkersID(String m)
    {
        String mId="";
        for(int i=0; i<m.length(); i++)
        {
            if(Character.isDigit(m.charAt(i)))
            {
                mId+=m.charAt(i);
            }
        }

        return Integer.parseInt(mId);
    }

    public void centerLocationToMe(){
        if (mLastLocation != null){
            mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,17));
        }
        else{
            Toast.makeText(MainActivity.this, "Spróbuj ponownie", Toast.LENGTH_SHORT).show();
        }
    }

    public void centerLocationToDestination(){
        if (mLastLocation != null){
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routeTo,17));
        }
    }

    public void showFullMap(){
        if (mLastLocation != null){
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(routeTo,16));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        tripControler.setUserLoc(new LatLng(location.getLatitude(), location.getLongitude()));
        adapter.setUserLoc(tripControler.getUserLoc());
        adapter.notifyDataSetChanged();
        //Toast.makeText(this, "wlazlem tu", Toast.LENGTH_LONG).show();
        if(firstRoute)
        {

            int index=0;
            float distance=Float.MAX_VALUE;
            for(int i=0; i<controlPoints.size(); i++)
            {
                Location l= new Location(location);
             //   float tempDistance=l.distanceTo(new Location(controlPoints[i].getLatitude()+ ", " + controlPoints[i].getLongitude()));
                double tempDistance= DistanceCalculator.distance(location.getLatitude(), location.getLongitude(), controlPoints.get(i).getLatitude(), controlPoints.get(i).getLongitude());
                 Toast.makeText(this, tempDistance+" " + controlPoints.get(i).getGermanName(), Toast.LENGTH_LONG).show();
                if(tempDistance<distance) {
                    distance = (float)tempDistance;
                    index = i;

                }
            }

            ControlPoint newStartCP = controlPoints.get(index);
            zpi.data.model.Route route = tripControler.getCurrentTrip().getRoute();
            tripControler.setNewTrip(route, newStartCP);
            controlPoints = tripControler.getRouteControlPoints();
            //adapter = new RouteListAdapter(this, tripControler.getCurrentTrip(), tripControler.getUserLoc());
            adapter.setTrip(tripControler.getCurrentTrip());
            //lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            firstRoute=false;
        }

        Point current = tripControler.getCurrentCP();
        System.out.println("OBECNY PUNKT: " + current.getName());
        routeTo=current.getGeoLoc();
        deName = (current instanceof ControlPoint)?((ControlPoint)current).getGermanName():"";
        plName=current.getName();

        mLastLocation = location;
        mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        textUpperToolbarGerman.setText(deName);
        textUpperToolbarPolish.setText(plName + ": " + dystansText);

        GoogleDirection.withServerKey("AIzaSyAPkePZElcxqKVGIDYRJ-94gvhXYREhLTc")
                .from(mLatLng)
                .to(routeTo)
                .transportMode(TransportMode.WALKING)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        String status = direction.getStatus();
                        if(direction.isOK()) {
                            route = direction.getRouteList().get(0);
                            leg = route.getLegList().get(0);
                            directionPositionList = leg.getDirectionPoint();
                            polylineOptions = DirectionConverter.createPolyline(MainActivity.this, directionPositionList, 5, Color.RED);
                            Polyline temp = mGoogleMap.addPolyline(polylineOptions);
                            if(mPolyline != null){
                                mPolyline.remove();
                                mPolyline = null;
                            }
                            mPolyline = temp;
                            mPolyline.setPattern(pattern);
                            dystansInfo = leg.getDistance();
                            dystansText = dystansInfo.getText();
                            textUpperToolbarGerman.setText(deName);
                            textUpperToolbarPolish.setText(plName + ": " + dystansText);
                        }
                        else {
//                            Toast.makeText(MainActivity.this, status, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
//                        Toast.makeText(MainActivity.this, "Ups...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        tripControler.saveTripState();
    }

}