package com.flycat.sensoutensilio;

import android.os.Parcel;
import android.os.Parcelable;

public class Device  implements Parcelable {

    private String name;
    private String identificator;

    public Device(){

    }

    public Device(String name, String identificator) {
        this.name = name;
        this.identificator = identificator;
    }

    public String getName() {
        return name;
    }

    public String getIdentificator() {
        return identificator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Device(Parcel in) {
        name = in.readString();
        identificator = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(identificator);
    }
}
