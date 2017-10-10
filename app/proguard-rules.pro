-ignorewarnings
-ignorewarning
-dontwarn
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontshrink
-verbose
#-dontobfuscate -dontoptimize   混淆失效
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn android.support.v4.**
-dontwarn com.loopj.android.http.**
-dontwarn com.google.gson.**
-dontwarn com.baidu.**
-dontwarn android.annotations
-dontwarn com.alipay.euler.**
-dontwarn java.lang.**
-dontwarn rx.**
-dontwarn  com.iflytek.**
-dontwarn  com.iflytek.cloud.**


-keep class com.iflytek.cloud.thirdparty.** { *; }
-keep class com.zige.robot.bean.** { *; }
-keepattributes Exceptions
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


-keep class com.lidroid.xutils.** {*;}
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class com.android.vending.licensing.ILicensingService
-keep class com.loopj.android.http.**{ *;}
-keep public class * extends com.loopj.android.http.**
-keep class com.baidu.** { *;}
-keep class com.alipay.euler.** {*;}
-keep class * extends java.lang.annotation.Annotation
-keep class com.sinovoice.** { *;}
-keep class vi.com.gdi.bgl.android.** { *;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *;}
-keep class java.lang.** { *;}

#rxbus混淆
-keep class rx.**{ *; }

-keep class com.litesuits.** { *; }
-keep class com.tencent.** { *;}
-keep class com.alipay.** { *;}
-keep class com.ta.** { *;}
-keep class com.ut.** { *;}
-keep class com.nineoldandroids.** { *;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.bgb.scan.model.** {*;}
-keep class com.baidu.**{*;}
-keep class vi.com.gdi.bgl.**{*;}

-keep class org.apache.**{*;}

#跳过混淆所有注解
-keepattributes Signature
-keepattributes *Annotation*



-keep class com.zige.robot.bean.* {
        public <fields>;
        public <methods>;
}
-keep class com.umeng.message.* {
        public <fields>;
        public <methods>;
}

-keep class com.umeng.message.protobuffer.* {
        public <fields>;
        public <methods>;
}

-keep class com.squareup.wire.* {
        public <fields>;
        public <methods>;
}

-keep class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.chmtech.parkbees.R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#保持自定义控件类不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
#保持greenDao不被混淆
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# 保持讯飞语音不被混淆
-dontwarn com.iflytek.**
-keep class com.iflytek.** {*;}


# 保持腾讯tis不被混淆
-dontwarn tencent.tls.**
-keep class tencent.tls.** {*;}

# 保持百度人脸识别不被混淆
-dontwarn com.baidu.aip.**
-keep class com.baidu.aip.** {*;}
-dontwarn com.baidufacerecog.**
-keep class com.baidufacerecog.** {*;}

#环信
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**







