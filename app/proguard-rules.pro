# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/waldek/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class title to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file title.
#-renamesourcefileattribute SourceFile

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-keepattributes Signature
-keepattributes *Annotation*
-keep class org.greenrobot.** { *; }
-keep class com.squareup.** { *; }
#-keep class dagger.** { *; }
-keep class okio.** { *; }
-keep class retrofit2.** { *; }
# greendao rules below


#-libraryjars libs/greendao-3.2.2.jar
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.greenrobot.greendao.database.**

#rxjava rules below
-keepattributes *Annotation*
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-dontwarn rx.**
-keep class rx.**
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.**

-dontwarn com.google.errorprone.annotations.**


-dontwarn okio.**
-keep class okio.**

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keep class pl.fullstack.movies.db.entity.**

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
