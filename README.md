# 🌦️ 1. 城市天气预报应用 (Weather Forecast App)

这是一个基于高德地图开放平台 API 开发的 Android 城市天气预报应用。项目使用了现代 Android 开发技术栈，旨在提供一个美观、流畅且实用的天气查询体验。

---

# ✨ 2. 主要功能

* **城市天气展示**：自动获取并显示指定城市的名称和天气信息
* **多日预报**：展示未来几天的详细天气预报，包括日期、星期、天气状况和温度范围
* **下拉刷新 (Pull-to-Refresh)**：随时下拉更新最新天气数据
* **网络状态检测**：启动时检查网络连接，并在无网络时给出友好提示

---

# 🎨 3. 应用界面 (UI/UX) 特点

* **沉浸式设计**：采用渐变背景，内容延伸至状态栏
* **卡片式布局 (CardView)**：圆角、半透明卡片让界面更美观
* **Loading 动画**：优雅的加载过渡效果

---

# 🚀 4. 如何运行项目

### 1. 获取 API Key

从高德开放平台申请 Key。

### 2. 配置 API

将你的 API Key 配置。

### 3. 安装依赖

确保你的 **build.gradle.kts (Module: app)** 文件中包含以下依赖：

```kotlin
dependencies {
    // UI / 组件库
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // 网络请求
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ... 其他默认依赖 ...
}
```

> **注意**：添加依赖后，记得点击 **Sync Now** 同步项目。
