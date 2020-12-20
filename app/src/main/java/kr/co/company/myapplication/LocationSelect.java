package kr.co.company.myapplication;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationSelect extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnPicked;
    private Double latitude;
    private Double longitude;
    private LocationManager mLocationManager;
    //LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_select);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnPicked= (Button)findViewById(R.id.btnLocationPicked);
        btnPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent parentIntent = getIntent();
                Person me = (Person) parentIntent.getExtras().get("me");
                me.setTargetLocationLatitude(latitude);
                me.setTargetLocationLongtitude(longitude);
                Intent intent= new Intent();
                intent.putExtra("me",me);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 3);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList.size()==0){
                Toast.makeText(this,"검색결과 없음",Toast.LENGTH_LONG).show();
            }
            else {
                Address address = addressList.get(0);
                latitude = address.getLatitude();
                longitude= address.getLongitude();
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        }
    }
    private class GPSListener implements LocationListener {
        /**
         * �꾩튂 �뺣낫媛� �뺤씤�� �� �먮룞 �몄텧�섎뒗 硫붿냼��
         */

        public void onLocationChanged(Location location) {
            //this.onLocationChanged();
            //this.
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.i("GPSListener", msg);

            //textView.setText("�� �꾩튂 : " + latitude + ", " + longitude);
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        // LatLng sydney = new LatLng(27.746974, 85.301582);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           Log.i("퍼미션 요청 전","퍼미션 요청 전");
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1003);
            Log.i("퍼미션 요청 후","퍼미션 요청 후");
            return;
        }else {
            Log.i("퍼미션 그랜티드","퍼미션 그랜티드");
            mMap.setMyLocationEnabled(true);
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            GPSListener gpsListener = new GPSListener();
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                /*
                latitude =   mMap.getMyLocation().getLatitude();
                longitude= mMap.getMyLocation().getLongitude();
                LatLng latLng = new LatLng(latitude,longitude);
                */

                lastLocation = getLastKnownLocation();
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();

                LatLng latLng = new LatLng(latitude, longitude);
                Log.i("지도 생성",""+latitude+longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Kathmandu, Nepal"));
                Log.i("마커를 현재 위치에 찍었다.",latitude+" "+longitude);


                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}

