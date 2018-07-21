package com.example.sayed.pokemon

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    //WORK WITH USER LOCATION


    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        checkPer()
    }

    var ACCESSLOCATION = 123
    fun checkPer() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESSLOCATION)
                return
            }
        }

        GetUserLocation()
    }

    @SuppressLint("MissingPermission")
    fun GetUserLocation() {
        Toast.makeText(this, "User location access on", Toast.LENGTH_LONG).show()
        //TODO: Will implement later

        var myLocation = MyLocationListener()
        var locationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)
        var myThread=myThread()
        myThread.start()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {

            ACCESSLOCATION -> {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GetUserLocation()
                } else {
                    Toast.makeText(this, "We cannot access to your location", Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


    }

    var location:Location?=null

    inner class MyLocationListener:LocationListener{

        constructor(){
            location = Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location?) {
            location=p0
        }

        override fun onStatusChanged(p0: String?, status: Int, extras: Bundle?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderEnabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onProviderDisabled(p0: String?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    var oldLocation:Location?=null

    inner class myThread: Thread {

        constructor():super(){
            oldLocation = Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }

        override fun run() {
            while (true){
                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation = location


                    runOnUiThread {

                        mMap!!.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                                .position(sydney)
                                .title("me")
                                .snippet("Find me here")
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.pikachu)))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 2f))

                        for(i in 0.. listPokemon.size-1){
                            var newPokemon = listPokemon[i]
                            if(newPokemon.isCatch==false){
                                val pokemonLoc = LatLng(newPokemon.lat!!, newPokemon.long!!)
                                mMap!!.addMarker(MarkerOptions()
                                        .position(pokemonLoc)
                                        .title(newPokemon.name!!)
                                        .snippet(newPokemon.des!!)
                                        .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))
                            }
                        }
                    }

                    Thread.sleep(1000)
                }
                catch (ex:Exception){}
            }
        }
    }

    var listPokemon=ArrayList<Pokemon>()

    fun LoadPokemon(){
        listPokemon.add(Pokemon(R.mipmap.pokemon,"charmander","Here is from Japan",55.0,37.77899994482,-122.5))
        listPokemon.add(Pokemon(R.drawable.dane,"charmander","Here is from janina",70.0,47.77899994482,-50.5))
        listPokemon.add(Pokemon(R.drawable.vaporeon,"charmander","Here is from Uropia",90.8,58.77899994482,-80.5))
    }
}


