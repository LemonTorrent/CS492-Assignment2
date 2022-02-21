package com.example.android.connectedweather

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.android.connectedweather.data.ForecastPeriod
import java.util.*


const val EXTRA_FORECAST_PERIOD = "ForecastPeriod"
class WeatherDetailActivity : AppCompatActivity() {
    private var weather: ForecastPeriod? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)

        if (intent != null && intent.hasExtra(EXTRA_FORECAST_PERIOD)) {
            weather = intent.getSerializableExtra(EXTRA_FORECAST_PERIOD) as ForecastPeriod

            findViewById<TextView>(R.id.tv_weather_name).text = weather!!.weather[0].quickDescription
            findViewById<TextView>(R.id.tv_weather_min_temp).text = "Low: " + weather!!.main.temp_min.toString()
            findViewById<TextView>(R.id.tv_weather_max_temp).text = "High: " + weather!!.main.temp_max.toString()
            findViewById<TextView>(R.id.tv_weather_pop).text = weather!!.pop.toString()
            findViewById<TextView>(R.id.tv_weather_description).text = weather!!.weather[0].description
            findViewById<TextView>(R.id.tv_weather_cloud_cover).text = weather!!.clouds.cloudPercent.toString() + "%"
            findViewById<TextView>(R.id.tv_weather_wind_speed).text = "\uD83D\uDCA8" + weather!!.wind.speed.toString() + "mph"

            var dateDay =  weather!!.dt_txt.substring(8, 10)
            var dateMonth =  weather!!.dt_txt.substring(5, 7)
            var dateYear =  weather!!.dt_txt.substring(0, 4)
            var dateTime = weather!!.dt_txt.substring(11, 13).toInt()

            val cal = Calendar.getInstance()
            cal.set(dateYear.toInt(), dateMonth.toInt() - 1, dateDay.toInt())
            val monthText = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
            val dayText = cal.get(Calendar.DAY_OF_MONTH).toString()

            var timeDay = "am"

            dateTime += 12

            if (dateTime > 23) {
                if (dateTime > 24) {
                    dateTime -= 24
                } else {
                    dateTime -= 12
                }
                timeDay = "pm"
            } else if (dateTime > 12) {
                dateTime -= 12
            }

            findViewById<TextView>(R.id.tv_weather_time_date).text = "$monthText $dayText at $dateTime$timeDay"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_map_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.action_view_map -> {
                viewWeatherOnWeb()
                true
            }
            R.id.action_share -> {
                shareRepo()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun viewWeatherOnWeb(){
        if (weather != null) {
            val uri = Uri.parse("https://www.google.com/maps/@44.5672487,-123.2892549,14z")
            val intent: Intent = Intent(Intent.ACTION_VIEW, uri)

            startActivity(intent)
        }
    }

    private fun shareRepo() {
        if (weather != null) {

            var dateDay =  weather!!.dt_txt.substring(8, 10)
            var dateMonth =  weather!!.dt_txt.substring(5, 7)
            var dateYear =  weather!!.dt_txt.substring(0, 4)
            var dateTime = weather!!.dt_txt.substring(11, 13).toInt()

            val cal = Calendar.getInstance()
            cal.set(dateYear.toInt(), dateMonth.toInt() - 1, dateDay.toInt())
            val monthText = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()).toString()
            val dayText = cal.get(Calendar.DAY_OF_MONTH).toString()

            var timeDay = "am"

            dateTime += 12

            if (dateTime > 23) {
                if (dateTime > 24) {
                    dateTime -= 24
                } else {
                    dateTime -= 12
                }
                timeDay = "pm"
            } else if (dateTime > 12) {
                dateTime -= 12
            }

            var weatherTime = "$dateMonth $dateDay, $dateYear at $dateTime$timeDay"

            val text = getString(
                R.string.share_text,
                weatherTime,
                weather!!.weather[0].description,
                weather!!.main.temp_max.toString(),
                weather!!.main.temp_min.toString()
            )

            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            startActivity(Intent.createChooser(intent, null))
        }
    }

}