package ar.edu.itba.hci.smarthomesystem;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    private String id;
    private String name;
    private String meta;

    public Room(String name, String meta) {
        this.name = name;
        this.meta = meta;
    }

    public Room(String id, String name, String meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMeta() {
        return this.meta;
    }

    @Override
    public String toString() {
        return String.format("%s", this.getName());
    }

    protected Room(Parcel in) {
        id = in.readString();
        name = in.readString();
        meta = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(meta);
    }
}
