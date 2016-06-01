package com.cky.rx.activity;

import android.content.Intent;

import com.cky.rx.R;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.mipmap.ic_splash); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

        configSplash.setTitleSplash(getString(R.string.app_name));
        configSplash.setTitleTextColor(R.color.splah_text_color);
        configSplash.setTitleTextSize(34f); //float value
        configSplash.setAnimTitleDuration(1500);
        configSplash.setAnimTitleTechnique(Techniques.Landing);
        configSplash.setTitleFont("font/Lobster.ttf");


        /*
        * //Customize Circular Reveal
            configSplash.setBackgroundColor(R.color.primary); //any color you want form colors.xml
            configSplash.setAnimCircularRevealDuration(2000); //int ms
            configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
            configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

            //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

            //Customize Logo
            configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
            configSplash.setAnimLogoSplashDuration(2000); //int ms
            configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


            //Customize Path
            configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
            configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
            configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
            configSplash.setAnimPathStrokeDrawingDuration(3000);
            configSplash.setPathSplashStrokeSize(3); //I advise value be <5
            configSplash.setPathSplashStrokeColor(R.color.accent); //any color you want form colors.xml
            configSplash.setAnimPathFillingDuration(3000);
            configSplash.setPathSplashFillColor(R.color.Wheat); //path object filling color


            //Customize Title
            configSplash.setTitleSplash("My Awesome App");
            configSplash.setTitleTextColor(R.color.Wheat);
            configSplash.setTitleTextSize(30f); //float value
            configSplash.setAnimTitleDuration(3000);
            configSplash.setAnimTitleTechnique(Techniques.FlipInX);
            configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/
            */
    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        //淡入 淡出 动画效果
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        SplashActivity.this.finish();
    }
}
