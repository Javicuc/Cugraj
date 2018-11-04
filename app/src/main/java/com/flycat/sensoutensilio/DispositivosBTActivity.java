package com.flycat.sensoutensilio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DispositivosBTActivity extends AppCompatActivity {

    private DevicesAdapter adapter;
    private List<Device> devicesList;
    RecyclerView recyclerView;
    private Switch swBluetoothOnOff;
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dispositivos_bt);
        swBluetoothOnOff = findViewById(R.id.bluetooth_on_off);
        recyclerView = findViewById(R.id.devicesRecyclerView);

        registerReceiver(bluetoothStateChanged, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        /** Asigna el adaptador local Bluetooth a la variable **/
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        setUpRecyclerView();

        if(mBtAdapter.isEnabled()){
            swBluetoothOnOff.setChecked(mBtAdapter.isEnabled());
            getDevices();
        }

        swBluetoothOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    mBtAdapter.enable();
                }else{
                    mBtAdapter.disable();
                }
            }
        });

    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void getDevices() {
        devicesList = new ArrayList<>();
        /** Obtiene el conjunto de dispositos vinculadas actualmente **/
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        /** Recorre los dispositivos vinculados y los agrega a la lista **/
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devicesList.add(new Device(device.getName(),device.getAddress()));
            }
        }else{
            devicesList.clear();
        }
        adapter = new DevicesAdapter(devicesList);
        recyclerView.setAdapter(adapter);
    }

    private BroadcastReceiver bluetoothStateChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    case BluetoothAdapter.STATE_ON:
                        swBluetoothOnOff.setChecked(true);
                        getDevices();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        swBluetoothOnOff.setChecked(false);
                        adapter.clear();
                        break;
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
