@file:Suppress("NAME_SHADOWING") //value variable is shadowed in the getUnit() function near the bottom of the page.

package com.weatherapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.weatherapp.R
import com.weatherapp.models.WeatherResponse
import com.weatherapp.network.WeatherService
import com.weatherapp.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import retrofit.*
import java.text.SimpleDateFormat
import java.util.*

// OpenWeather Link : https://openweathermap.org/api/one-call-3

class MainActivity : AppCompatActivity() {

    // Used to get the user's current location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    // A global variable for Progress Dialog
    private var mProgressDialog: Dialog? = null

    // A global variable for Current Latitude
    private var mLatitude: Double = 0.0

    // A global variable for Current Longitude
    private var mLongitude: Double = 0.0

    // A global variable for the SharedPreferences
    private lateinit var mSharedPreferences: SharedPreferences

    //Celsius and Fahrenheit buttons
    private lateinit var celsiusButton: Button
    private lateinit var fahrenheitButton: Button


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Buttons to switch between Fahrenheit and Celsius
        celsiusButton = findViewById(R.id.buttonCelsius)
        fahrenheitButton = findViewById(R.id.buttonFahrenheit)

        // Initialize the Fused location variable
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize the SharedPreferences variable -- retrieves data from 'WeatherAppPreference'
        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        //  Call the UI method to populate the data in
        //  the UI which are already stored in sharedPreferences earlier.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setupUI()
        }

        //checks if location is enabled
        if (!isLocationEnabled()) {
            Toast.makeText(
                this,
                "Your location provider is turned off. Please turn it on.",
                Toast.LENGTH_SHORT
            ).show()

            // This will redirect you to settings from where you need to turn on the location provider.
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)

        } else {
            permissionRequest()
        }

    }


    //Permission Request -- Dexter
    private fun permissionRequest(){
        //Request Fine and Coarse Location
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                //if permissions are granted then request locations data
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        requestLocationData()
                    }
                    //if permissions are denied, display denied location message
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(
                            this@MainActivity,
                            "You have denied location permission. Please allow it is mandatory.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                //Method called whenever Android asks the application to inform the user of the need for the requested permissions.
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    //shows alert dialog to go to settings to grant location permission
                    showRationalDialogForPermissions()
                }
            }).onSameThread() // connect all of this code
            .check()
    }


    //Adds the recycler (refresh) button to the action bar.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    //gets weather data after pressing the refresh button
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                getLocationWeatherDetails()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**
     * Verifies that the location or GPS is enabled on the user's device.
     */
    private fun isLocationEnabled(): Boolean {
        // This provides access to the system location services.
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //Returns either the GPS provider or the Network provider depending on which one is enabled
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    /**
     * A function used to show the ALERT-DIALOG when the permissions are denied.
     */
    private fun showRationalDialogForPermissions() {

        //when user clicks 'go to settings' this tries to open the app details settings
        AlertDialog.Builder(this)
            .setMessage("You have turned off location permissions required for this feature... Please enable it under the Application Settings.")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                    //opens settings for this particular app
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    /**
     * A function to request the current location. Using the fused location provider client. lint suppressed because we ask for permissions in the permission request function above.
     */
    @SuppressLint("MissingPermission")
    private fun requestLocationData() {

        val mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper()!!)
    }


    /**
     * A location callback object of the fused location provider client to get the current location details.
     */
    private val mLocationCallback = object : LocationCallback() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onLocationResult(locationResult: LocationResult) {

            val mLastLocation: Location = locationResult.lastLocation
            mLatitude = mLastLocation.latitude
            Log.e("Current Latitude", "$mLatitude")
            mLongitude = mLastLocation.longitude
            Log.e("Current Longitude", "$mLongitude")

            getLocationWeatherDetails()
        }
    }


    /**
     * Function is used to get the weather details of the current location based on the latitude and longitude using Retrofit
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLocationWeatherDetails() {

        if (Constants.isNetworkAvailable(this@MainActivity)) {

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            /**
             * Here we map the service interface in which we declare the end point, and the API type
             *i.e GET, POST and so on, along with the request parameters which are required.
             */
            val service: WeatherService = retrofit.create(WeatherService::class.java)

            /** An invocation of a Retrofit method that sends a request to a web-server and returns a response.
             * Here we pass the required API parameters in service.getWeather()
             */
            val listCall: Call<WeatherResponse> = service.getWeather(mLatitude, mLongitude, Constants.METRIC_UNIT, Constants.APP_ID)

            showCustomProgressDialog() // Used to show the progress dialog

            // Callback methods are executed using the Retrofit callback executor.
            listCall.enqueue(object : Callback<WeatherResponse> {
                @RequiresApi(Build.VERSION_CODES.N)
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    response: Response<WeatherResponse>,
                    retrofit: Retrofit
                ) {

                    // Check whether the response is successful or not.
                    if (response.isSuccess) {

                        hideProgressDialog()

                        /** The de-serialized response body of a successful response. */
                        val weatherList: WeatherResponse = response.body()
                        Log.i("Response Result", "$weatherList")

                        // Convert the response object to a string and store it in the SharedPreference.
                        val weatherResponseJsonString = Gson().toJson(weatherList)

                        // Save the converted string to shared preferences
                        val editor = mSharedPreferences.edit()
                        editor.putString(Constants.WEATHER_RESPONSE_DATA, weatherResponseJsonString)
                        editor.apply()

                        setupUI()

                    } else {
                        // If the response is not successful then we check the response code.
                        when (response.code()) {
                            400 -> {
                                Log.e("Error 400", "Bad Request")
                            }
                            404 -> {
                                Log.e("Error 404", "Not Found")
                            }
                            else -> {
                                Log.e("Error", "Generic Error")
                            }
                        }
                    }
                }

                override fun onFailure(t: Throwable) {
                    hideProgressDialog() // Hides the progress dialog
                    Log.e("ERROR", t.message.toString())
                }
            })
        } else {
            Toast.makeText(
                this@MainActivity,
                "No internet connection available.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    /**
     * Method is used to show the Custom Progress Dialog.
     */
    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this)

        mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)

        //Start the dialog and display it on screen.
        mProgressDialog!!.show()
    }


    /**
     * This function is used to dismiss the progress dialog if it is visible to the user after progress is completed.
     */
    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }


    /**
     * This function is used to display the weather data in the UI
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupUI() {

        // Here is the stored response data from the SharedPreference and then it is converted back to the data model object.
        val weatherResponseJsonString = mSharedPreferences.getString(Constants.WEATHER_RESPONSE_DATA, "")

        if (!weatherResponseJsonString.isNullOrEmpty()) {

            val weatherList = Gson().fromJson(weatherResponseJsonString, WeatherResponse::class.java)
            val unit = getUnit(application.resources.configuration.locales.toString())
            val weatherTemp = weatherList.main.temp.toInt().toString() + unit


            // For loop to get the required data and display it in the UI
            for (z in weatherList.weather.indices) {

                Log.i("NAME", weatherList.weather[z].main)
                val weatherListMain = weatherList.weather[z].main

                val weatherDescription = weatherList.weather[z].description
                val humidityText = weatherList.main.humidity.toString() + " per cent"
                val maxTempText = weatherList.main.temp_max.toInt().toString() + unit + " max"
                val minTempText = weatherList.main.temp_min.toInt().toString() + unit + " min"

                val celsiusTemp = weatherList.main.temp.toInt()
                val fahrenheitTemp = (celsiusTemp * 9.0/5.0) + 32.0

                val celsiusTempMin = weatherList.main.temp_min.toInt()
                val celsiusTempMax = weatherList.main.temp_max.toInt()

                tv_main.text = weatherListMain
                tv_main_description.text = weatherDescription
                tv_temp.text = weatherTemp
                tv_humidity.text = humidityText
                tv_min.text = minTempText
                tv_max.text = maxTempText
                tv_speed.text = weatherList.wind.speed.toString()
                tv_name.text = weatherList.name
                tv_country.text = weatherList.sys.country
                tv_sunrise_time.text = unixTime(weatherList.sys.sunrise.toLong())
                tv_sunset_time.text = unixTime(weatherList.sys.sunset.toLong())


                //convert main temp and min/max temps to celsius
                celsiusButton.setOnClickListener {
                    "${celsiusTemp}°C".also { tv_temp.text = it }
                    tv_min.text = minTempText
                    tv_max.text = maxTempText
                }
                //convert main temp and min/max temps to fahrenheit
                fahrenheitButton.setOnClickListener {
                    "${fahrenheitTemp.toInt()}°F".also { tv_temp.text = it }
                    tv_min.text = ((celsiusTempMin * 9.0/5.0) + 32.0).toInt().toString() + "°F" + " min"
                    tv_max.text = ((celsiusTempMax * 9.0/5.0) + 32.0).toInt().toString() + "°F" + " max"
                }


                // Here we update the main icon
                when (weatherList.weather[z].icon) {
                    "01d" -> iv_main.setImageResource(R.drawable.sunny)
                    "02d" -> iv_main.setImageResource(R.drawable.cloud)
                    "03d" -> iv_main.setImageResource(R.drawable.cloud)
                    "04d" -> iv_main.setImageResource(R.drawable.cloud)
                    "04n" -> iv_main.setImageResource(R.drawable.cloud)
                    "10d" -> iv_main.setImageResource(R.drawable.rain)
                    "11d" -> iv_main.setImageResource(R.drawable.storm)
                    "13d" -> iv_main.setImageResource(R.drawable.snowflake)
                    "01n" -> iv_main.setImageResource(R.drawable.cloud)
                    "02n" -> iv_main.setImageResource(R.drawable.cloud)
                    "03n" -> iv_main.setImageResource(R.drawable.cloud)
                    "10n" -> iv_main.setImageResource(R.drawable.cloud)
                    "11n" -> iv_main.setImageResource(R.drawable.rain)
                    "13n" -> iv_main.setImageResource(R.drawable.snowflake)
                }
            }
        }
    }


    /**
     * Function is used to get the temperature unit value.
     */
    private fun getUnit(value: String): String {
        Log.i("unit", value)
        var value = "°C"
        if ("[en_US]" == value || "en_LR" == value || "en_MM" == value) {
            value = "°F"
        }
        return value
    }


    /**
     * The function is used to get the formatted time based on the Format and the LOCALE we pass to it.
     */
    private fun unixTime(timex: Long): String? {

        val date = Date(timex * 1000L)
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("HH:mm:ss")
        sdf.timeZone = TimeZone.getDefault()

        return sdf.format(date)
    }
}