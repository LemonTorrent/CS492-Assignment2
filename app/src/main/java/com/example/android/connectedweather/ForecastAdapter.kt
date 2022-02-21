package com.example.android.connectedweather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.connectedweather.data.ForecastInfo
import com.example.android.connectedweather.data.ForecastPeriod
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text
import java.util.*

class ForecastAdapter(private val onForecastPeriodClick: (ForecastPeriod) -> Unit)
    : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    var forecastPeriods = listOf<ForecastPeriod>()

    fun updateRepoList(newWeatherList: List<ForecastPeriod>?) {
        forecastPeriods = newWeatherList ?: listOf()
        notifyDataSetChanged()
    }

    override fun getItemCount() = this.forecastPeriods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)
        return ViewHolder(view, onForecastPeriodClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.forecastPeriods[position])
    }

    class ViewHolder(view: View, val onClick: (ForecastPeriod) -> Unit)
        : RecyclerView.ViewHolder(view) {
        private val monthTV: TextView = view.findViewById(R.id.tv_month)
        private val dayTV: TextView = view.findViewById(R.id.tv_day)
        private val timeTV: TextView = view.findViewById(R.id.tv_time)
        private val highTempTV: TextView = view.findViewById(R.id.tv_high_temp)
        private val lowTempTV: TextView = view.findViewById(R.id.tv_low_temp)
        private val shortDescTV: TextView = view.findViewById(R.id.tv_short_description)
        private val popTV: TextView = view.findViewById(R.id.tv_pop)

        private var currentForecastPeriod: ForecastPeriod? = null

        init {
            view.setOnClickListener {
                currentForecastPeriod?.let(onClick)
            }
        }

        fun bind(forecastPeriod: ForecastPeriod) {
            currentForecastPeriod = forecastPeriod

            val forecastDay =  forecastPeriod.dt_txt.substring(8, 10)
            val forecastMonth =  forecastPeriod.dt_txt.substring(5, 7)
            val forecastYear =  forecastPeriod.dt_txt.substring(0, 4)
            var forecastTime = forecastPeriod.dt_txt.substring(11, 13).toInt()

            val cal = Calendar.getInstance()
            cal.set(forecastYear.toInt(), forecastMonth.toInt() - 1, forecastDay.toInt())

            var timeDay = "am"
            forecastTime += 12

            if (forecastTime > 23) {
                if (forecastTime > 24) {
                    forecastTime -= 24
                } else {
                    forecastTime -= 12
                }
                timeDay = "pm"
            } else if (forecastTime > 12) {
                forecastTime -= 12
            }

            monthTV.text = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
            dayTV.text = cal.get(Calendar.DAY_OF_MONTH).toString()

            highTempTV.text = forecastPeriod.main.temp_max.toString() + "°F"
            lowTempTV.text = forecastPeriod.main.temp_min.toString() + "°F"
            popTV.text = (forecastPeriod.pop * 100.0).toInt().toString() + "% precip."

            shortDescTV.text = "$forecastTime$timeDay - ${forecastPeriod.weather[0].quickDescription}"
        }
    }
}