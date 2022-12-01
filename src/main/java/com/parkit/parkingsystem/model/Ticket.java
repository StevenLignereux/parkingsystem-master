package com.parkit.parkingsystem.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Calendar;
import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;

    private boolean isAlreadyCame = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Date getInTime() {
        return inTime;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public Date getOutTime() {
        return outTime;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }


    public boolean isAlreadyCame() {
        return isAlreadyCame;
    }

    public void setAlreadyCame(boolean alreadyCame) {
        isAlreadyCame = alreadyCame;
    }
}
