package com.example.weather

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
class MainActivity : AppCompatActivity() {

    private lateinit var cityTextView: TextView
    private lateinit var forecastRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // 建议把 API Key 放在安全的地方，或者 buildConfig 中
    private val apiKey = "3fc7f3e993a3938b755e60e54*********"
    private val cityCode = "110111" // 房山区

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // 开启边到边显示
        setContentView(R.layout.activity_main)

        initViews()
        setupWindowInsets() // 处理状态栏高度

        // 初始加载
        fetchWeatherData(isRefresh = false)
    }

    private fun initViews() {
        cityTextView = findViewById(R.id.cityTextView)
        forecastRecyclerView = findViewById(R.id.forecastRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        forecastRecyclerView.layoutManager = LinearLayoutManager(this)

        // 设置下拉刷新颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright)
        swipeRefreshLayout.setOnRefreshListener {
            fetchWeatherData(isRefresh = true)
        }
    }

    /**
     * 处理窗口内边距，防止内容被状态栏遮挡
     */
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRootView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // 只需要给内容部分留出 padding，背景图保持全屏
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
    }

    private fun fetchWeatherData(isRefresh: Boolean) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "无网络连接", Toast.LENGTH_LONG).show()
            swipeRefreshLayout.isRefreshing = false
            return
        }

        if (!isRefresh) {
            progressBar.visibility = View.VISIBLE
        }

        RetrofitClient.apiService.getWeather(city = cityCode, apiKey = apiKey)
            .enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    progressBar.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false // 停止刷新动画

                    if (response.isSuccessful) {
                        val weatherResponse = response.body()
                        if (weatherResponse != null && weatherResponse.status == "1") {
                            val forecast = weatherResponse.forecasts.firstOrNull()
                            if (forecast != null) {
                                updateUI(forecast)
                            } else {
                                showError("未找到预报信息")
                            }
                        } else {
                            showError("API 错误: ${weatherResponse?.info}")
                        }
                    } else {
                        showError("请求失败: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false
                    showError("发生错误: ${t.message}")
                }
            })
    }

    private fun updateUI(forecast: Forecast) {
        cityTextView.text = forecast.city
        forecastRecyclerView.adapter = ForecastAdapter(forecast.casts)
        // 可以加一个简单的淡入动画
        forecastRecyclerView.alpha = 0f
        forecastRecyclerView.animate().alpha(1f).duration = 300
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}