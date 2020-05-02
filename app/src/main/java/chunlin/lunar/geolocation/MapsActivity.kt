package chunlin.lunar.geolocation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val singapore = LatLng(1.368878, 103.797873)
        mMap.addMarker(MarkerOptions().position(singapore).title("Here is Singapore!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 14f))

        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMapClickListener { latitudeAndLongitude ->
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latitudeAndLongitude))
            showToast(latitudeAndLongitude)
        }
    }

    fun showToast(latitudeAndLongitude: LatLng)
    {
        Toast.makeText(
            this@MapsActivity,
            "Position: (" + latitudeAndLongitude.latitude + ", " + latitudeAndLongitude.longitude + ")",
            Toast.LENGTH_LONG).show()
    }
}
