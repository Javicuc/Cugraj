package com.flycat.sensoutensilio;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder>{

    private List<Device> deviceList;
    private Context context;
    public static String DEVICE_ADDRESS = "device_address";

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item,
                parent, false);
        context = parent.getContext();
        return new DevicesAdapter.DeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceViewHolder holder, int position) {
        final Device currentItem = deviceList.get(position);

        holder.name.setText(currentItem.getName());
        holder.identifier.setText(currentItem.getIdentificator());
        holder.sincDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** Crea una instancia de Intent y envia por parametro la activity a inicializar **/
                Intent i = new Intent(context, MainActivity.class);
                /** Agrega un Tag al Intent que contiene la direccion MAC **/
                i.putExtra(DEVICE_ADDRESS, currentItem);
                /** Inicia la actividad colocada en el Intent **/
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView identifier;
        ImageButton sincDevice;

        DeviceViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.device_name);
            identifier = itemView.findViewById(R.id.device_identifier);
            sincDevice = itemView.findViewById(R.id.bluetooth_sinc);
        }
    }

    public DevicesAdapter(List<Device> deviceList){
        this.deviceList = deviceList;
    }

    public void clear(){
        final int size = deviceList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                deviceList.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }


}
