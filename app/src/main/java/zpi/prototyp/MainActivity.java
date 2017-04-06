package zpi.prototyp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener, PopupMenu.OnMenuItemClickListener {

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
    private TextView firstText;
    private TextView secondText;

    private PopupMenu popup;

    private LatLng routeTo;
    private LatLng place1;
    private LatLng place2;
    private LatLng place3;
    private LatLng place4;
    private String deName;
    private String plName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

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

        fullMapIB = (ImageButton) findViewById(R.id.fullMap);
        fullMapIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locIBisPressed = false;
                locIB.setImageResource(R.mipmap.action_button_lokalizacja);
                showFullMap();
            }
        });

        firstText = (TextView)findViewById(R.id.textUp);
        secondText = (TextView)findViewById(R.id.textDown);
        Typeface deutschmeister = Typeface.createFromAsset(getAssets(), "fonts/grobe-deutschmeister/GrobeDeutschmeister.ttf");
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/roboto/Roboto-Light.ttf");
        firstText.setTypeface(deutschmeister);
        secondText.setTypeface(roboto);

        place1 = new LatLng(51.103851, 17.031064);
        place2 = new LatLng(51.104082, 17.030082);
        place3 = new LatLng(51.105483, 17.031921);
        place4 = new LatLng(51.105059, 17.031117);
        routeTo = place1;

        deName = "Warenhaus Wertheim";
        plName = "Renoma";
        firstText.setText(deName);
        pattern = Arrays.<PatternItem>asList(new Gap(20), new Dash(40));

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottombar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.lists:
                        if(mLastLocation != null) {
                            deName = "Schweidnitzer Stadtgraben";
                            plName = "Podwale";
                            routeTo = place2;
                            onLocationChanged(mLastLocation);
                        }
                        return true;
                    case R.id.me:
                        if(mLastLocation != null) {
                            deName = "Warenhaus Wertheim";
                            plName = "Renoma";
                            routeTo = place1;
                            onLocationChanged(mLastLocation);
                        }
                        return true;
                    case R.id.destination:
                        if(mLastLocation != null) {
                            deName = "Schwiednitzer Strasse";
                            plName = "Świdnicka";
                            routeTo = place4;
                            onLocationChanged(mLastLocation);
                        }
                        return true;
                    case R.id.map:
                        if(mLastLocation != null) {
                            deName = "Zwingerplatz";
                            plName = "Pl. Teatralny";
                            routeTo = place3;
                            onLocationChanged(mLastLocation);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    public void showPopup(View v) {
        popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.actionbar_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.position1:
                if(mLastLocation != null) {
                    deName = "Schweidnitzer Stadtgraben";
                    plName = "Podwale";
                    routeTo = place2;
                    onLocationChanged(mLastLocation);
                }
                return true;
            case R.id.position2:
                if(mLastLocation != null) {
                    deName = "Warenhaus Wertheim";
                    plName = "Renoma";
                    routeTo = place1;
                    onLocationChanged(mLastLocation);
                }
                return true;
            case R.id.position3:
                if(mLastLocation != null) {
                    deName = "Schwiednitzer Strasse";
                    plName = "Świdnicka";
                    routeTo = place4;
                    onLocationChanged(mLastLocation);
                }
                return true;
            case R.id.position4:
                if(mLastLocation != null) {
                    deName = "Zwingerplatz";
                    plName = "Pl. Teatralny";
                    routeTo = place3;
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
        googleMap.setOnMarkerClickListener(this);

        mCenter = CameraUpdateFactory.newLatLng(routeTo);
        mGoogleMap.moveCamera(mCenter);
        mZoom = CameraUpdateFactory.zoomTo(17);
        mGoogleMap.animateCamera(mZoom);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.addMarker(new MarkerOptions().position(place1).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_renoma)));
        mGoogleMap.addMarker(new MarkerOptions().position(place2).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_podwale)));
        mGoogleMap.addMarker(new MarkerOptions().position(place3).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_renoma)));
        mGoogleMap.addMarker(new MarkerOptions().position(place4).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_renoma)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        if (marker.equals(mMarker))
//        {
            Toast.makeText(MainActivity.this, "Trwa ładowanie", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,Details.class);
            startActivity(intent);
//        }
        return false;
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
                            firstText.setText(deName);
                            secondText.setText(plName + ": " + dystansText);
                        }
                        else {
                            Toast.makeText(MainActivity.this, status, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(MainActivity.this, "Ups...", Toast.LENGTH_LONG).show();
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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
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
}