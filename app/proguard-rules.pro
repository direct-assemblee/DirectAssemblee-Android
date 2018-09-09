# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /opt/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-renamesourcefileattribute SourceFile

-keepattributes SourceFile,LineNumberTable,EnclosingMethod

# Model ------------------------

-keep class org.ladlb.directassemblee.address.Address { *; }
-keep class org.ladlb.directassemblee.address.AddressEnvelope { *; }
-keep class org.ladlb.directassemblee.address.AddressProperties { *; }
-keep class org.ladlb.directassemblee.address.AddressGeometry { *; }

-keep class org.ladlb.directassemblee.ballot.BallotInfo { *; }
-keep class org.ladlb.directassemblee.ballot.vote.BallotVote { *; }

-keep class org.ladlb.directassemblee.declaration.Declaration { *; }

-keep class org.ladlb.directassemblee.department.Department { *; }

-keep class org.ladlb.directassemblee.deputy.Deputy { *; }
-keep class org.ladlb.directassemblee.deputy.DeputyVote { *; }

-keep class org.ladlb.directassemblee.role.Role { *; }
-keep class org.ladlb.directassemblee.role.RolePosition { *; }

-keep class org.ladlb.directassemblee.theme.Theme { *; }

-keep class org.ladlb.directassemblee.timeline.TimelineItem { *; }

-keep class org.ladlb.directassemblee.vote.Vote { *; }

# Picasso ------------------------

-dontwarn com.squareup.okhttp.**

# Retrofit -----------------------

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# OKHTTP ------------------------

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# MPAndroidChart ----------------

-keep class com.github.mikephil.charting.** { *; }
