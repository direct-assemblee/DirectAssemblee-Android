# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

-keep class org.ladlb.directassemblee.model.Address { *; }
-keep class org.ladlb.directassemblee.model.AddressEnvelope { *; }
-keep class org.ladlb.directassemblee.model.AddressProperties { *; }
-keep class org.ladlb.directassemblee.model.AddressGeometry { *; }

-keep class org.ladlb.directassemblee.model.BallotInfo { *; }
-keep class org.ladlb.directassemblee.model.BallotVote { *; }

-keep class org.ladlb.directassemblee.model.Declaration { *; }

-keep class org.ladlb.directassemblee.model.Department { *; }

-keep class org.ladlb.directassemblee.model.Deputy { *; }
-keep class org.ladlb.directassemblee.model.DeputyVote { *; }

-keep class org.ladlb.directassemblee.model.Rate { *; }
-keep class org.ladlb.directassemblee.model.RateGroup { *; }

-keep class org.ladlb.directassemblee.model.Role { *; }
-keep class org.ladlb.directassemblee.model.RolePosition { *; }

-keep class org.ladlb.directassemblee.model.Theme { *; }

-keep class org.ladlb.directassemblee.model.TimelineItem { *; }

-keep class org.ladlb.directassemblee.model.Vote { *; }
