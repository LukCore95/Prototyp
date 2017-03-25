package zpi.prototyp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mMarker;

    Route route;
    Leg leg;
    ArrayList<LatLng> directionPositionList;
    PolylineOptions polylineOptions;
    Polyline mPolyline;

    ImageButton locIB;
    ImageButton foodIB;
    Info dystansInfo;
    String dystansText;
    TextView firstText;
    TextView secondText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        locIB = (ImageButton) findViewById(R.id.location);
        locIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerLocation();
            }
        });

        foodIB = (ImageButton) findViewById(R.id.food);
        foodIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerLocation();
            }
        });

        firstText = (TextView)findViewById(R.id.textUp);
        secondText = (TextView)findViewById(R.id.textDown);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;

        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.michal_json));
        }
        catch (Resources.NotFoundException e) {}

        buildGoogleApiClient();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.setOnMarkerClickListener(this);

        LatLng renoma = new LatLng(51.103976, 17.030741);
        CameraUpdate center = CameraUpdateFactory.newLatLng(renoma);
        mGoogleMap.moveCamera(center);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        mGoogleMap.animateCamera(zoom);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(renoma).icon(BitmapDescriptorFactory.fromResource(R.drawable.renoma)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(mMarker))
        {
            Intent intent = new Intent(MainActivity.this,Details.class);
            startActivity(intent);
        }
        return false;
    }

    public void centerLocation(){
        if (mLastLocation != null){
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        }
        else{
            Toast.makeText(MainActivity.this, "Spróbuj ponownie", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        GoogleDirection.withServerKey("AIzaSyAPkePZElcxqKVGIDYRJ-94gvhXYREhLTc")
                .from(latLng)
                .to(new LatLng(51.103976, 17.030741))
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
                            dystansInfo = leg.getDistance();
                            dystansText = dystansInfo.getText();
                            secondText.setText(dystansText);
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
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}