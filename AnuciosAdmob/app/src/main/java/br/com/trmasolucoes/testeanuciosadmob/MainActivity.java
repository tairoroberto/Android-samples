package br.com.trmasolucoes.testeanuciosadmob;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class MainActivity extends ActionBarActivity {

    private LinearLayout linearLayout;
    private AdView adView_1;
    private AdView adView_2;
    private InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        adView_1 = new AdView(MainActivity.this);
        adView_1.setAdUnitId("ca-app-pub-5552810602868197/9339569267");
        adView_1.setAdSize(AdSize.BANNER);

        adView_2 = new AdView(MainActivity.this);
        adView_2.setAdUnitId("ca-app-pub-5552810602868197/9339569267");
        adView_2.setAdSize(AdSize.MEDIUM_RECTANGLE);

        interstitialAd = new InterstitialAd(MainActivity.this);
        interstitialAd.setAdUnitId("ca-app-pub-5552810602868197/9339569267");

        //Adiciona as views no layout
        linearLayout.addView(adView_1);
        linearLayout.addView(adView_2);

        //Faz a requisição do anuncio
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("693D54C0B8CDEBF0D9ACC6F95CB9B1ED")
                .build();

        adView_1.loadAd(adRequest);
        adView_2.loadAd(adRequest);
        interstitialAd. loadAd(adRequest);

        //Liteners
        adView_1.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.i("Script","onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.i("Script", "onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i("Script","onAdLoaded");
            }
        });
    }
    
    public void showInterstitialAd(View view){
        interstitialAd.show();
        Log.i("Script","showInterstitialAd");
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView_1.resume();
        adView_2.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adView_1.pause();
        adView_2.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adView_1.destroy();
        adView_2.destroy();
    }
}
