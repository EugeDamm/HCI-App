package ar.edu.itba.hci.smarthomesystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Routine implements Parcelable, Serializable {

    String id;
    String name;
    Object[] actions;
    String meta;

    public Routine(String id, String name, Object[] actions, String meta) {
        this.id = id;
        this.name = name;
        this.actions = actions;
        this.meta = meta;
    }

    public String getName() { return name; }

    public String getId() { return id; }

    public Object[] getActions() { return actions; }

    public String getMeta() { return meta; }

    public static final Creator<Routine> CREATOR = new Creator<Routine>() {
        @Override
        public Routine createFromParcel(Parcel in) {
            return new Routine(in);
        }

        @Override
        public Routine[] newArray(int size) {
            return new Routine[size];
        }
    };

    protected Routine(Parcel in) {
        id = in.readString();
        name = in.readString();
        actions = in.readArray(this.getClass().getClassLoader());
        meta = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeArray(actions);
        dest.writeString(meta);
    }

    @Override
    public String toString() { return name; }
}
