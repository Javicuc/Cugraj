package com.flycat.sensoutensilio;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_cuchara:
                    return true;
                case R.id.navigation_cuchillo:
                    return true;
                case R.id.navigation_tenedor:
                    return true;
            }
            return false;
        }
    };

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    /** Identificador unico de servicio - Bluetooth HC-06 */
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(getIntent().hasExtra("DEVICE_ADDRESS")){
            Device deviceItem =  getIntent().getExtras().getParcelable("DEVICE_ADDRESS");
            /**
             * Setea la direccion MAC
             * Intenta crear y conectar el socket Bluetooth
             **/
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = btAdapter.getRemoteDevice(deviceItem.getIdentificator());
            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "La creación del Socket fallo", Toast.LENGTH_LONG).show();
            }
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {}
            }
            /** Crea un nuevo hilo e inicia la interacción con el modulo Blueetooth **/
            MyConexionBT = new ConnectedThread(btSocket);
            MyConexionBT.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_historial) {
            return true;
        }
        else if (id == R.id.action_graficas) {
            return true;
        }
        else if (id == R.id.action_ayuda) {
            return true;
        }
        else if (id == R.id.action_sincronizar) {
            Intent intent = new Intent(MainActivity.this, DispositivosBTActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            return true;
        }
        else if (id == R.id.action_configuracion) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
