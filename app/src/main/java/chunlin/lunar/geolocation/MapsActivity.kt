package chunlin.lunar.geolocation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azure.messaging.eventhubs.EventData
import com.azure.messaging.eventhubs.EventHubClientBuilder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder





class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var recentLatitudeAndLongitudeRecords : MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        recentLatitudeAndLongitudeRecords = mutableListOf<String>()
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
            val selectedPoint = GeolocationData("Android Data", latitudeAndLongitude.latitude, latitudeAndLongitude.longitude)

            val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

            recentLatitudeAndLongitudeRecords.add(gson.toJson(selectedPoint))

            Log.d("Geolocation", gson.toJson(selectedPoint))

            if (recentLatitudeAndLongitudeRecords.count() >= 10) {
                sendLatitudeAndLongitudeDataToAzure()

                recentLatitudeAndLongitudeRecords.clear()
            }

            mMap.addMarker(MarkerOptions().position(latitudeAndLongitude).title(selectedPoint.geolocationDisplay))
        }
    }

    private fun sendLatitudeAndLongitudeDataToAzure() {
        // Send events to Azure Event Hubs (azure-messaging-eventhubs)
        //
        // Reference:
        // 1. https://docs.microsoft.com/en-us/azure/event-hubs/get-started-java-send-v2
        var producer = EventHubClientBuilder()
            .connectionString(BuildConfig.AZURE_EVENT_HUB_CONNECTION_STRING, BuildConfig.AZURE_EVENT_HUB_NAME)
            .buildProducerClient()

        val batch = producer.createBatch()

        recentLatitudeAndLongitudeRecords.forEach {
            batch.tryAdd(EventData(it))
        }

        if (batch.count > 0) {
            producer.send(batch)
        }

        producer.close()
    }

    private fun showToast(latitudeAndLongitude: LatLng) {
        Toast.makeText(
            this@MapsActivity,
            "Position: (" + latitudeAndLongitude.latitude + ", " + latitudeAndLongitude.longitude + ")",
            Toast.LENGTH_LONG).show()
    }
}
