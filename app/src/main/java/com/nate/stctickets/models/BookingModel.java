package com.nate.stctickets.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BookingModel implements Parcelable {
    private String ticketID;
    private String userID;
    private String passengerName;
    private String from;
    private String to;
    private String daytime;
    private String date;
    private String day;
    private String qrCode;
    private String seatNumber;
    private String time;
    private String cost;

    public BookingModel() {
    }

    public BookingModel(String ticketID, String userID, String passengerName, String from, String to, String daytime, String date, String day, String qrCode, String seatNumber, String time, String cost) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.passengerName = passengerName;
        this.from = from;
        this.to = to;
        this.daytime = daytime;
        this.date = date;
        this.day = day;
        this.qrCode = qrCode;
        this.seatNumber = seatNumber;
        this.time = time;
        this.cost = cost;
    }

    protected BookingModel(Parcel in) {
        ticketID = in.readString();
        userID = in.readString();
        passengerName = in.readString();
        from = in.readString();
        to = in.readString();
        daytime = in.readString();
        date = in.readString();
        day = in.readString();
        qrCode = in.readString();
        seatNumber = in.readString();
        time = in.readString();
        cost = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ticketID);
        dest.writeString(userID);
        dest.writeString(passengerName);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(daytime);
        dest.writeString(date);
        dest.writeString(day);
        dest.writeString(qrCode);
        dest.writeString(seatNumber);
        dest.writeString(time);
        dest.writeString(cost);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookingModel> CREATOR = new Creator<BookingModel>() {
        @Override
        public BookingModel createFromParcel(Parcel in) {
            return new BookingModel(in);
        }

        @Override
        public BookingModel[] newArray(int size) {
            return new BookingModel[size];
        }
    };

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDaytime() {
        return daytime;
    }

    public void setDaytime(String daytime) {
        this.daytime = daytime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "BookingModel{" +
                "ticketID='" + ticketID + '\'' +
                ", userID='" + userID + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", daytime='" + daytime + '\'' +
                ", date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", seatNumber='" + seatNumber + '\'' +
                ", time='" + time + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
