package zpi.prototyp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zpi.controler.trip.TripController;
import zpi.controler.trip.TripNotificator;
import zpi.data.model.ControlPoint;
import zpi.data.model.DataException;
import zpi.data.model.InterestingPlace;
import zpi.data.model.InterestingPlaceType;
import zpi.data.model.Point;
import zpi.data.model.RestPoint;
import zpi.data.model.RestPointType;
import zpi.utils.DistanceCalculator;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener, PopupMenu.OnMenuItemClickListener {

    //kod woja
    private NavigationView navView;
    private DrawerLayout drawer;
    private ListView lv;
    private TripController tripController;
    private RouteListAdapter adapter;
    private List<ControlPoint> basicRoute;
    private List<InterestingPlace> interestingPlaces;

    private SlidingUpPanelLayout slidingUp;
    private LinearLayout bottombar;
    private InterestingPlaceAdapter ipAdapter;
    private ListView ipList;
    private ListView rpList;
    private RestPointAdapter rpAdapter;
    private List<RestPoint> restPoints;
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
    Location lastUserLoc=null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        //wczytanie z bazy danych TUTAJ

       // currentTrip=SplashScreen.getTrip();
        tripController = new TripController(this, SplashScreen.getTrip());
        controlPoints = tripController.getRouteControlPoints();
        interestingPlaces = SplashScreen.ipList;
        restPoints = SplashScreen.rpList;
        //powiadomienie do pokazania
//            tn= null;
//        try {
//            tn = new TripNotificator(controlPoints);
//            tn.setNotification(this);
//        } catch (DataException e) {
//            e.printStackTrace();
//        }


        System.out.println("Wczytano " + controlPoints.size() + " punktów kontrolnych");

        //kod woja
        navView = (NavigationView) findViewById(R.id.navigation_view);
        drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        slidingUp = (SlidingUpPanelLayout) findViewById(R.id.sliding_up_panel);
        bottombar = (LinearLayout) findViewById(R.id.bottombarlay);
       // slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        bottombar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUp.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                System.out.println("KLiknięto bottombar");
            }
        });
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
        textBottomToolbar = (TextView) findViewById(R.id.textbottombar);
        Typeface deutschmeister = Typeface.createFromAsset(getAssets(), "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
        final Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/roboto/Roboto-Light.ttf");
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
        ipList = (ListView) findViewById(R.id.ip_list_list);
        ipAdapter = new InterestingPlaceAdapter(this, interestingPlaces, tripController.getUserLoc(), tripController);
        ipList.setAdapter(ipAdapter);
        ipList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InterestingPlace ip = (InterestingPlace) parent.getItemAtPosition(position);

                Intent intentToInterestingPlace=new Intent(MainActivity.this, Interesting_Details.class);
                intentToInterestingPlace.putExtra("nazwaPunktu", ip.getName());
                startActivity(intentToInterestingPlace);

            }
        });

        rpList = (ListView) findViewById(R.id.rp_list_list);
        rpAdapter = new RestPointAdapter(this, restPoints, tripController.getUserLoc(), tripController);
        rpList.setAdapter(rpAdapter); //TODO nie działa
        rpList.setDivider(null);
        //TODO click listener
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

        adapter = new RouteListAdapter(this, tripController.getCurrentTrip(), tripController.getUserLoc());
        lv.setAdapter(adapter);
        //lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(parent.getContext());
                alertBuilder.setMessage(getString(R.string.routelist_alert_message))
                        .setTitle(R.string.routePointsMenuTitle);
                alertBuilder.setPositiveButton(R.string.routelist_alert_reply_jump, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tripController.setNavigation((ControlPoint) parent.getItemAtPosition(position));
                        adapter.setTrip(tripController.getCurrentTrip());
                        adapter.notifyDataSetChanged();

                        refreshCurrentTarget();
                    }
                });
                alertBuilder.setNegativeButton(getString(R.string.routelist_alert_reply_newtrip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        zpi.data.model.Route route = tripController.getCurrentTrip().getRoute();
                        tripController.setNewTrip(route, (ControlPoint) parent.getItemAtPosition(position));
                        adapter.setTrip(tripController.getCurrentTrip());
                        controlPoints = tripController.getRouteControlPoints();
                        ControlPoint firstCp = controlPoints.get(0);
                        adapter.notifyDataSetChanged();

                        refreshCurrentTarget();
                    }
                });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        });

        basicRoute = tripController.getCurrentTrip().getRoute().getRoutePoints();

        refreshCurrentTarget();
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

        for(int i=0; i<restPoints.size(); i++)
        {
            int icon=R.mipmap.ikona_pubu;
            if(restPoints.get(i).getType()== RestPointType.restaurant)
            {
                icon=R.mipmap.ikona_restauracji;
            }
            if(restPoints.get(i).getType()== RestPointType.cafe)
            {
                icon=R.mipmap.ikona_kawiarni;
            }

            mGoogleMap.addMarker(new MarkerOptions().position(restPoints.get(i).getGeoLoc()).icon(BitmapDescriptorFactory.fromResource(icon)));


        }
        for(int i=0; i<interestingPlaces.size(); i++)
        {
            int icon = R.mipmap.ip_museum;
            if(interestingPlaces.get(i).getType() == InterestingPlaceType.sakralny)
                icon = R.mipmap.ip_church;

            mGoogleMap.addMarker(new MarkerOptions().position(interestingPlaces.get(i).getGeoLoc()).icon(BitmapDescriptorFactory.fromResource(icon)));
        }
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

        if(getMarkersID(marker.getId()) > interestingPlaces.size()+restPoints.size()) {
            Intent intent = new Intent(MainActivity.this, Details.class);

            intent.putExtra("nazwaPunktu", basicRoute.get(getMarkersID(marker.getId())-(restPoints.size()+interestingPlaces.size())).getName());
            startActivity(intent);
        }
        else
        {
            if(getMarkersID(marker.getId()) >restPoints.size()) {
                Intent intent = new Intent(MainActivity.this, Interesting_Details.class);

                intent.putExtra("nazwaPunktu", interestingPlaces.get(getMarkersID(marker.getId()) - restPoints.size()).getName());
                startActivity(intent);
            }
        }

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


               if (lastUserLoc==null || DistanceCalculator.distance(lastUserLoc.getLatitude(), lastUserLoc.getLongitude(), location.getLatitude(), location.getLongitude()) >= 0.05||firstRoute) {
               LatLng llLoc = new LatLng(location.getLatitude(), location.getLongitude());
               ipAdapter.setUserLoc(llLoc);
               ipAdapter.choosePointsOnList();
               tripController.setUserLoc(new LatLng(location.getLatitude(), location.getLongitude()));
               adapter.setUserLoc(tripController.getUserLoc());
               adapter.notifyDataSetChanged();
               ipAdapter.notifyDataSetChanged();


               try {
                   Resources res = this.getResources();
                   if (tripController.checkIfPointReached() == 1)
                       Toast.makeText(this, res.getString(R.string.target_reached_message), Toast.LENGTH_SHORT).show();
               } catch (Exception e) {
                   e.printStackTrace();
               }
//        try {
//            tn = new TripNotificator(controlPoints);
//            tn.setNotification(this);
//        } catch (DataException e) {
//            e.printStackTrace();
//        }

               if (firstRoute) {

                   int index = 0;
                   float distance = Float.MAX_VALUE;
                   for (int i = 0; i < controlPoints.size(); i++) {
                       Location l = new Location(location);
                       //   float tempDistance=l.distanceTo(new Location(controlPoints[i].getLatitude()+ ", " + controlPoints[i].getLongitude()));
                       double tempDistance = DistanceCalculator.distance(location.getLatitude(), location.getLongitude(), controlPoints.get(i).getLatitude(), controlPoints.get(i).getLongitude());
                       //  Toast.makeText(this, tempDistance+" " + controlPoints.get(i).getGermanName(), Toast.LENGTH_LONG).show();
                       if (tempDistance < distance) {
                           distance = (float) tempDistance;
                           index = i;

                       }
                   }

                   ControlPoint newStartCP = controlPoints.get(index);
                   zpi.data.model.Route route = tripController.getCurrentTrip().getRoute();
                   tripController.setNewTrip(route, newStartCP);
                   controlPoints = tripController.getRouteControlPoints();
                   //adapter = new RouteListAdapter(this, tripController.getCurrentTrip(), tripController.getUserLoc());
                   adapter.setTrip(tripController.getCurrentTrip());
                   //lv.setAdapter(adapter);
                   adapter.notifyDataSetChanged();

                   firstRoute = false;
               }
               try {
                   if (tripController.checkIfPointReached() == 1) {
                       adapter.setTrip(tripController.getCurrentTrip());
                       adapter.notifyDataSetChanged();
                       // Toast.makeText(this, "OBECNY PUNKT: " + adapter.trip.getCurrentTarget().getName(), Toast.LENGTH_LONG).show();
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
               //Point current = tripController.getCurrentCP();
               //System.out.println("OBECNY PUNKT: " + current.getName());
               refreshCurrentTarget();

               mLastLocation = location;
               mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

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
               lastUserLoc=location;

       }
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
    public void onStop(){
        super.onStop();
        tripController.saveTripState();
    }

    public void refreshCurrentTarget(){//System.out.println("REFRESH");
        Point currentCp = tripController.getCurrentCP();
        routeTo = currentCp.getGeoLoc();
        deName = (currentCp instanceof ControlPoint)?((ControlPoint) currentCp).getGermanName():getString(R.string.ip_navigation_bar_title);
       // System.out.println("DENAME: " + deName);
        plName = currentCp.getName();
        textUpperToolbarGerman.setText(deName);
        textUpperToolbarPolish.setText(plName + ": " + dystansText);
        //System.out.println("PLNAME: " + plName);

        ImageView menuIcon = (ImageView) findViewById(R.id.route_points_icon);
        Drawable img = (currentCp instanceof ControlPoint)?getDrawable(((ControlPoint) currentCp).getIcon()):null;
        menuIcon.setImageDrawable(img);

    }

    public void showKrajewskiFace(View v)
    {
       final  ImageView imView=new ImageView(MainActivity.this);
        final RelativeLayout rl=(RelativeLayout) findViewById(R.id.my_root);
        final RelativeLayout.LayoutParams rp= new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        imView.setImageResource(R.drawable.suprise);
        rl.addView(imView, rp);
        new CountDownTimer(2000, 1000) { // 5000 = 5 sec

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                imView.setVisibility(View.INVISIBLE);
                rl.removeView(imView);
            }
        }.start();





    }
}