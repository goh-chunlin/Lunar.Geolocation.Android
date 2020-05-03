# Lunar.Geolocation.Android

## Important Notes
Firstly, create /res/value/google_maps_api.xml and add API key. For more information of how to create the API key, follow the directions here: https://developers.google.com/maps/documentation/android/start#get-key

```
<resources>
    <string name="google_maps_api_key"
        templateMergeStrategy="preserve"
        translatable="false">AIza****************************</string>
</resources>
```

Secondly, create /apikey.properties and add the required secrets as shown below.

```
AZURE_EVENT_HUB_CONNECTION_STRING="..."
AZURE_EVENT_HUB_NAME="..."
```

For securing safety API key add google_maps_api.xml and apikey.properties to .gitignore.
