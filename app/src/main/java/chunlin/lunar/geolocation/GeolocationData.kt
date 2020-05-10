package chunlin.lunar.geolocation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//@Serializable
class GeolocationData(label: String, latitude: Double, longitude: Double) {

    @Expose
    @SerializedName("DeviceLabel")
    val label = label

    @Expose
    @SerializedName("Latitude")
    val latitude = latitude

    @Expose
    @SerializedName("Longitude")
    val longitude = longitude

    val geolocationDisplay = "Lat: $latitude; Lng: $longitude"

}