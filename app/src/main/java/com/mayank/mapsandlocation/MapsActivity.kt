package com.mayank.mapsandlocation

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.jar.Manifest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,LocationListener{

    lateinit var locMan : LocationManager
    lateinit var locLis:LocationListener

    override fun onLocationChanged(location: Location?) {

        Toast.makeText(this,"Long = ${location?.longitude}  Lat= ${location?.latitude}",Toast.LENGTH_LONG).show()
        if(mMap!=null)
        {
            mMap.addMarker(MarkerOptions().position(LatLng(location?.latitude!!,location?.longitude!!)).title("Marker At Current Location"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location?.latitude!!,location?.longitude!!),12f))
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        locLis = this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        requestPermission()

    }


    fun requestPermission()
    {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_FINE_LOCATION),1234)
        }else{
            startLocationUpdates()
        }
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locMan = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1f,locLis)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1234)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1]!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermission()
            }else {
                startLocationUpdates()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        with(mMap.uiSettings){
            setAllGesturesEnabled(true)
            isZoomControlsEnabled = true
        }
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-33.865143, 151.209900)
        val melbourne = LatLng(-37.840935, 144.946457)

        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,3f))
        mMap.addPolyline(PolylineOptions()
            .add(sydney,melbourne)
            //.color(R.color.colorPrimary)
            .color(ContextCompat.getColor(baseContext,R.color.colorPrimary))
            .width(10f))

        mMap.addMarker(MarkerOptions().position(melbourne).title("Marker in Melbourne"))
    }
}
