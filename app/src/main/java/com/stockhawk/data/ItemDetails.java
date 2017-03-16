package com.stockhawk.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samuel on 3/15/2017.
 */

public class ItemDetails implements Parcelable {
    public String symbol ;
    public String price ;
    public String absolute_change ;
    public String percentage_change ;
    public String history ;

    public ItemDetails(){}
    protected ItemDetails(Parcel in) {
        symbol = in.readString();
        price = in.readString();
        absolute_change = in.readString();
        percentage_change = in.readString();
        history = in.readString();
    }

    public static final Creator<ItemDetails> CREATOR = new Creator<ItemDetails>() {
        @Override
        public ItemDetails createFromParcel(Parcel in) {
            return new ItemDetails(in);
        }

        @Override
        public ItemDetails[] newArray(int size) {
            return new ItemDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(price);
        dest.writeString(absolute_change);
        dest.writeString(percentage_change);
        dest.writeString(history);
    }
}
