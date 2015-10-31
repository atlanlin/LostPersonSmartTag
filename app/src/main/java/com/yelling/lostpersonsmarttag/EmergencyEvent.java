package com.yelling.lostpersonsmarttag;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yelling on 26/10/15.
 */
public class EmergencyEvent implements Parcelable {
    protected String guardian_description, finder_description, cookie, finder_location, finder_name,
            guardian_name, guardian_location, ward_name;
    protected int id, ward_id, is_approved, is_ongoing;
    protected String scan_time;

    public EmergencyEvent(int id, int ward_id, String ward_name, int is_approved, int is_ongoing,
                          String guardian_description, String finder_description, String cookie,
                          String finder_name, String finder_location, String guardian_name,
                          String guardian_location, String scan_time){
        this.id = id;
        this.ward_id = ward_id;
        this.ward_name = ward_name;
        this.guardian_description = guardian_description;
        this.guardian_name = guardian_name;
        this.guardian_location = guardian_location;
        this.finder_description = finder_description;
        this.finder_name = finder_name;
        this.finder_location = finder_location;
        this.scan_time = scan_time;
        this.cookie = cookie;
        this.is_approved = is_approved;
        this.is_ongoing = is_ongoing;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeInt(ward_id);
        out.writeString(ward_name);
        out.writeString(guardian_description);
        out.writeString(guardian_name);
        out.writeString(guardian_location);
        out.writeString(finder_description);
        out.writeString(finder_name);
        out.writeString(finder_location);
        out.writeString(scan_time);
        out.writeString(cookie);
        out.writeInt(is_approved);
        out.writeInt(is_ongoing);
    }

    public static final Parcelable.Creator<EmergencyEvent> CREATOR
            = new Parcelable.Creator<EmergencyEvent>() {
        public EmergencyEvent createFromParcel(Parcel in) {
            return new EmergencyEvent(in);
        }

        public EmergencyEvent[] newArray(int size) {
            return new EmergencyEvent[size];
        }
    };

    private EmergencyEvent(Parcel in) {
        id = in.readInt();
        ward_id = in.readInt();
        ward_name = in.readString();
        guardian_description = in.readString();
        guardian_name = in.readString();
        guardian_location = in.readString();
        finder_description = in.readString();
        finder_name = in.readString();
        finder_location = in.readString();
        scan_time = in.readString();
        cookie = in.readString();
        is_approved = in.readInt();
        is_ongoing = in.readInt();
    }
}
