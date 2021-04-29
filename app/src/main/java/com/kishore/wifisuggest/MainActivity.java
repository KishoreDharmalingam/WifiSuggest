package com.kishore.wifisuggest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";
    List<WifiNetworkSuggestion> suggestionList = null;
    WifiManager wifiManager = null;
    private EditText mSSIDEditText;
    private EditText mPasswordEditText;
    private Button mConnectButton;
    private Button mRemoveButton;
    private CheckBox isHiddenSSIDcheckBox;
    private String ssidName = "";
    private String password = "";
    SpannableStringBuilder spannableString = null;
    private BroadcastReceiver wifiNetworkSuggestionReceiver = null;
    int                             wifiControl;
    private ProgressDialog mProgressDialog;
    String message = "";
    Boolean isHiddenSSIDChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                100);

        mSSIDEditText = (EditText) findViewById(R.id.editText);
        mPasswordEditText = (EditText) findViewById(R.id.editText2);
        mConnectButton = (Button) findViewById(R.id.button);
        mRemoveButton = (Button) findViewById(R.id.remove);
        isHiddenSSIDcheckBox = (CheckBox) findViewById(R.id.isHiddenSSIDcheckBox);

        mConnectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent();
                    intent.setClassName(BuildConfig.APPLICATION_ID,"com.kishore.configuremodule.ConfigActivity");
                    intent.putExtra("ssidName",mSSIDEditText.getText().toString());
                    intent.putExtra("password",mPasswordEditText.getText().toString());
                    intent.putExtra("isHiddenSSIDChecked",isHiddenSSIDcheckBox.isChecked());
                    startActivityForResult(intent,100);
                }catch (Exception ex){
                    ex.getMessage();

                }

            }
        });

//        mRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeCredentials();
//            }
//        });

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        AppOpsManager appOpsManager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);
        wifiControl = appOpsManager.unsafeCheckOp("android:change_wifi_state",this.getApplicationInfo().uid,this.getPackageName());

    }

    @TargetApi(Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "onActivityResult()::" + requestCode + "::" + resultCode);

        if (requestCode == 100) {
//            Result from ConfigActivity
            if (resultCode == 0) {
                Intent intent = new Intent();
                intent.setClassName(BuildConfig.APPLICATION_ID, "com.kishore.connectFeature.ConnectActivity");
                intent.putExtra("configuration", 1);
                startActivityForResult(intent, 101);
            }

        }
    }
}
