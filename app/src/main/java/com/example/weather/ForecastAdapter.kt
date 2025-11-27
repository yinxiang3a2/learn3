package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ForecastAdapter(private val casts: List<Cast>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvWeek: TextView = view.findViewById(R.id.tvWeek)
        val tvWeather: TextView = view.findViewById(R.id.tvWeather)
        val tvTemp: TextView = view.findViewById(R.id.tvTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val cast = casts[position]
        holder.tvDate.text = cast.date
        holder.tvWeek.text = "星期${cast.week}" // API通常返回数字，这里简单处理，最好写个工具类转中文

        // 组合白天和晚上的天气
        val weatherText = if (cast.dayweather == cast.nightweather) {
            cast.dayweather
        } else {
            "${cast.dayweather} 转 ${cast.nightweather}"
        }
        holder.tvWeather.text = weatherText

        // 温度显示
        holder.tvTemp.text = "${cast.nighttemp}° / ${cast.daytemp}°"
    }

    override fun getItemCount() = casts.size
}