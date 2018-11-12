# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class cn.cs.callme.sdk.CsAdSDK {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable
#忽略警告
-ignorewarnings
#保证是独立的jar,没有任何项目引用,如果不写就会认为我们所有的代码是无用的,从而把所有的代码压缩掉,导出一个空的jar
-dontshrink
# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class cn.cs.callme.CSAdView {public *;}
-keep class cn.cs.callme.sdk.CsAdSDK {public *;}
-keep class cn.cs.callme.sdk.SplashAD {public *;}
-keep class cn.cs.callme.sdk.SplashAdListener {public *;}
-keep class cn.cs.callme.CSAdDetailActivity {public *;}