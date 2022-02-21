package com.example.android.connectedweather

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.android.connectedweather.data.ForecastWind
import com.example.android.connectedweather.data.ForecastClouds
import com.example.android.connectedweather.data.ForecastPeriod
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {
    // api.openweathermap.org/data/2.5/forecast?q={city name},{state code},{country code}&appid={API key}
    private val apiBaseUrl = "https://api.openweathermap.org"
    private val apiKey = "null"
    private val cityKey = "Corvallis"
    private val stateKey = "or"
    private val countryKey = "840"
    private val tag = "MainActivity"
    private val units = "imperial"
    private lateinit var requestQueue: RequestQueue
    private var repoListAdapter = ForecastAdapter(::onForecastPeriodClick)

    private lateinit var forecastListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        //val forecastListRV = findViewById<RecyclerView>(R.id.rv_forecast_list)
        forecastListRV = findViewById<RecyclerView>(R.id.rv_forecast_list)
        forecastListRV.layoutManager = LinearLayoutManager(this)
        forecastListRV.setHasFixedSize(true)

        val forecastDataItems = this.initForecastPeriods()
        //forecastListRV.adapter = ForecastAdapter()
        forecastListRV.adapter = repoListAdapter

    }


    private fun initForecastPeriods(): MutableList<ForecastPeriod> {
        doRepoSearch(cityKey, stateKey, countryKey)

        return mutableListOf(
            /*ForecastPeriod(
                dt_txt = "2022-02-02 21:00:00",
                //highTemp = 278.64,
                //lowTemp = 278.59,
                pop = 0.07,
                //shortDesc = "overcast clouds"


            )*/
        )

    }

    fun doRepoSearch(city: String, state: String, country: String) {
        val url = "$apiBaseUrl/data/2.5/forecast?q=$city,$state,$country&units=$units&appid=$apiKey"

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<ApiSearchResults> =
            moshi.adapter(ApiSearchResults::class.java)

        val req = StringRequest(
            Request.Method.GET,
            url,
            {

                var results = jsonAdapter.fromJson(it)
                Log.d(tag, results.toString())
                repoListAdapter.updateRepoList(results?.list)

                loadingIndicator.visibility = View.INVISIBLE
                forecastListRV.visibility = View.VISIBLE
                searchErrorTV.visibility = View.INVISIBLE
            },
            {
                Log.d(tag, "Error fetching from $url: ${it.message}")

                loadingIndicator.visibility = View.INVISIBLE
                forecastListRV.visibility = View.INVISIBLE
                searchErrorTV.visibility = View.VISIBLE
            }
        )

        loadingIndicator.visibility = View.VISIBLE
        forecastListRV.visibility = View.INVISIBLE
        searchErrorTV.visibility = View.INVISIBLE

        requestQueue.add(req)
    }


    private fun onForecastPeriodClick(weather: ForecastPeriod) {
        val intent = Intent(this, WeatherDetailActivity::class.java).apply {
            putExtra(EXTRA_FORECAST_PERIOD, weather)
        }
        startActivity(intent)
    }


    private data class ApiSearchResults(
        val list: List<ForecastPeriod>

    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_map -> {
                viewMainWeatherOnWeb()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_detail, menu)
        return true
    }

    private fun viewMainWeatherOnWeb() {
        if (forecastListRV != null) {
            val uri = Uri.parse("https://www.google.com/maps/@44.5672487,-123.2892549,14z")
            val intent: Intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

}
