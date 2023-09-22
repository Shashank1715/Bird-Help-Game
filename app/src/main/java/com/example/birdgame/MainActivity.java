package com.example.birdgame;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private Button buttonStart;
    private ImageView volume,bird;
    private Animation animation;
    private MediaPlayer mediaPlayer;
    boolean status= false;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volume= findViewById(R.id.volume);
        bird= findViewById(R.id.bird);
        buttonStart= findViewById(R.id.buttonStart);

        animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.scale_animation);
        bird.setAnimation(animation);

        // google add
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
//        AdView adView = new AdView(this);
//
//        adView.setAdSize(AdSize.BANNER);
//
//        adView.setAdUnitId("ca-app-pub-6230379142169750/2656987653");
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.inter_app_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("Error",loadAdError.toString());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);

                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        mInterstitialAd= null;
                    }
                });
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd!= null)
                 mInterstitialAd.show(MainActivity.this);
                else
                    Log.e("AdPending","Ad is not ready yet");
            }
        }, 10000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer= MediaPlayer.create(MainActivity.this,R.raw.inspire);
        mediaPlayer.start();
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!status){
                    mediaPlayer.setVolume(0,0);
                    volume.setImageResource(R.drawable.volume_off);
                    status= true;
                }
                else {
                    mediaPlayer.setVolume(1,1);
                    volume.setImageResource(R.drawable.volume);
                    status= false;

                }
            }
        });
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                //volume.setImageResource(R.drawable.volume);
                Intent intent= new Intent(MainActivity.this,GameActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Bird Game");
        builder.setMessage("Are you sure to Quit the game?");
        builder.setCancelable(false);
        builder.setNegativeButton("Quit Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}