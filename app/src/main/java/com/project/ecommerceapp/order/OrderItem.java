package com.project.ecommerceapp.order;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable {

    private final String id;
    private final String productId;
    private final String quantity;

    public OrderItem(String id, String productId, String quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    protected OrderItem(Parcel in) {
        id = in.readString();
        productId = in.readString();
        quantity = in.readString();
    }

    public static final Creator<OrderItem> CREATOR = new Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productId);
        dest.writeString(quantity);
    }
}