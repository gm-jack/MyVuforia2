##项目配置
###1.Project的build.gradle配置
只需要配置
<pre><code>
allprojects {
    ext {
        gdxVersion = '1.9.5'
	}
	repositories {
    	jcenter()
   		mavenCentral()
    }
}
</code></pre>
###2.Module的build.gradle配置
需要更新一些库，最好翻墙gradle自动下载
<pre><code>
android {
    repositories {
        flatDir {
            dirs 'libs'
        }
    }

    buildTypes {
        release {
            ndk {
                abiFilters "armeabi-v7a"
            }
        }
        debug {
            ndk {
                abiFilters "armeabi-v7a"
            }
        }
    }
}

dependencies {
    //友盟测试SDK时添加
    //compile 'com.umeng.analytics:analytics:latest.integration'
        
    compile "com.badlogicgames.gdx:gdx-backend-android:$gdxVersion"
    compile "com.badlogicgames.gdx:gdx-box2d:$gdxVersion"
    compile group: 'com.google.zxing', name: 'core', version: '3.2.1'
    compile 'jp.co.cyberagent.android.gpuimage:gpuimage-library:1.4.1'
    compile(name: 'argame', ext: 'aar') //如果aar名字不同请修改
    compile(name: 'vuforia', ext: 'aar') //如果aar名字不同请修改
}
</code></pre>
###3.proguard-rules.pro配置混淆
如果未设置混淆，可以不用添加；
<pre><code>
-verbose

-dontwarn android.support.**
-dontwarn com.badlogic.gdx.backends.android.AndroidFragmentApplication
-dontwarn com.badlogic.gdx.utils.GdxBuild
-dontwarn com.badlogic.gdx.physics.box2d.utils.Box2DBuild
-dontwarn com.badlogic.gdx.jnigen.BuildTarget*
-dontwarn com.badlogic.gdx.graphics.g2d.freetype.FreetypeBuild

-keep class com.badlogic.gdx.controllers.android.AndroidControllers
-keep class com.badlogic.gdx.backends.android.AndroidApplication
-keep class com.rtmap.game.AndroidLauncher

-keepclassmembers class com.badlogic.gdx.backends.android.AndroidInput* {
   <init>(com.badlogic.gdx.Application, android.content.Context, java.lang.Object, com.badlogic.gdx.backends.android.AndroidApplicationConfiguration);
}

-keepclassmembers class com.badlogic.gdx.physics.box2d.World {
   boolean contactFilter(long, long);
   void    beginContact(long);
   void    endContact(long);
   void    preSolve(long, long);
   void    postSolve(long, long);
   boolean reportFixture(long);
   float   reportRayFixture(long, float, float, float, float, float);
}

#native-font
-keep class com.rtmap.game.text.** { *; }
</code></pre>
###4.AndroidManifest.xml配置
权限声明：


<pre><code>
< uses-feature android:glEsVersion="0x00020000" />
< uses-feature android:name="android.hardware.camera"/>
< uses-permission android:name="android.permission.CAMERA"/>
< uses-permission android:name="android.permission.INTERNET"/>
< uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
< uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
< uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
< uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
< activit
    android:name="com.rtmap.game.AndroidLauncher"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize"
    android:screenOrientation="portrait"
    android:theme="@style/GdxTheme">
< /activity>
< activity
    android:name="com.rtmap.gm.myvuforia.ImageTargets.ImageTargets"
    android:configChanges="orientation|keyboardHidden|screenSize|smallestScreenSize"
    android:launchMode="singleTop"
    android:theme="@style/SampleAppsTheme">
< /activity>
</code></pre>

###5.SDK的aar库的存放位置
1.	项目的module的根目录下新建libs目录；
2.	将AR SDK文件下的aar目录下aar库放入libs目录下；
3.	重新编译项目。
###6.SDK的入口
<pre><code>
    Intent intent = new Intent(this, AndroidLauncher.class);
    intent.putExtra("ar_phone", phone);//传入的手机号
    startActivity(intent);
</code></pre>
##环境配置
###SDK开发环境
Android Studio 版本 2.3.3;
Gradle 版本 2.3.3
Android 编译版本 22
###运行环境
android 最小运行版本 14
##更新SDK配置
###识别图片配置
在src/main/assets目录下配置AR SDK文件下的assets目录下识别图片的StonesAndChips.xml和StonesAndChips.dat文件，文件名称不可以改变，修改后替换即可。
###游戏开始页规则配置
替换AR SDK文件下的assets目录下m_rule.png文件，名字不可以改变
###背包-卡券规则配置
替换AR SDK文件下的assets目录下beed_rule.png文件，名字不可以改变
