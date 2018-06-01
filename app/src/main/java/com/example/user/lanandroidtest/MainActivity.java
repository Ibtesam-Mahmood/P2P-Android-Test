package com.example.user.lanandroidtest;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

public class MainActivity extends AppCompatActivity implements SalutDataCallback {

    private SalutDataReceiver  mDataReceiver;
    private SalutServiceData mServiceData;

    private Salut network;

    private String deviceName;

    private ToggleButton hostButton;
    private Button refresh;
    private LinearLayout ipLayout;

    private boolean isHosting = false; //Tracks if the application is hosting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets the layout fields
        refresh = findViewById(R.id.button);
        hostButton = findViewById(R.id.host);
        ipLayout =  findViewById(R.id.peerList);

        //Device Name set
        deviceName = Build.MODEL;
        ((TextView) findViewById(R.id.deviceName)).setText(deviceName);

        //Initializes data receiver and port
        mDataReceiver = new SalutDataReceiver(this, this);
        mServiceData =  new SalutServiceData("p2pTest", 25011, deviceName);

        //Defines the network
        network =  new Salut(mDataReceiver, mServiceData, new SalutCallback() {
            @Override
            public void call() {
                Log.e("P2P", "Sorry, but this device does not support WiFi Direct.");
            }
        });

        //Sets the on check listener for the host button
        hostButton.setOnCheckedChangeListener(host());

    }

    //When the host button is toggled the application enters host mode
    public CompoundButton.OnCheckedChangeListener host(){

        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                //What to do when the application enters hosting mode
                isHosting = checked;
                if(checked){ //Hosting

                    //Starts the network
                    network.startNetworkService(new SalutDeviceCallback() {
                        @Override
                        public void call(SalutDevice device) {
                            Log.e("P2P", device.deviceName + " Has connected");
                        }
                    });
                    refresh.setClickable(false); //Ensures that the refresh button cannot be clicked

                }
                else{ //Not hosting

                    network.stopNetworkService(false); //Disables the network
                    refresh.setClickable(true); //Ensures that the refresh button can be clicked

                }
            }
        };

    }

    @Override
    public void onDataReceived(Object o) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Called when the refresh button is pressed
    //Updates the peer list
    public void refreshPeers(View view){


    }
}
