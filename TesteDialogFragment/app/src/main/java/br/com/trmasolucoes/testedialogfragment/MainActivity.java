package br.com.trmasolucoes.testedialogfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    
    public void openDialogFragment(View view){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CustomDiaolgFragment diaolgFragment = new CustomDiaolgFragment(1,2);
        diaolgFragment.show(transaction,"dialog");
    }

    public void turnOffDialog(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CustomDiaolgFragment diaolgFragment = (CustomDiaolgFragment) getSupportFragmentManager().findFragmentByTag("dialog");
        if(diaolgFragment != null){
            diaolgFragment.dismiss();
            transaction.remove(diaolgFragment);
        }
    }
}
