package zpi.prototyp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;;
import android.content.res.Resources;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker myMarker;
    ImageButton loc;
    ImageButton food;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        loc = (ImageButton) findViewById(R.id.location);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerLocation();
            }
        });

        food = (ImageButton) findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerLocation();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;


        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.michal_json));
            if (!success) {
            }
        } catch (Resources.NotFoundException e) {
        }

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
        myMarker = mGoogleMap.addMarker(new MarkerOptions().position(renoma).icon(BitmapDescriptorFactory.fromResource(R.drawable.renoma)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(myMarker))
        {
            Intent intent = new Intent(MainActivity.this,Details.class);
            startActivity(intent);
        }
        return false;
    }

    public void centerLocation(){
        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
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