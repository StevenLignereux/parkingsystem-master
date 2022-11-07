package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import java.text.DecimalFormat;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double difference_In_Time = outHour - inHour;
        double difference_In_Hours = (difference_In_Time / (1000 * 60 * 60));

        if (difference_In_Hours <= 0.5) {
            difference_In_Hours = 0;
        }

        if (ticket.isAlreadyCame()) {
            DecimalFormat decimalFormat = new DecimalFormat("#.###");
            double discountTicket = ticket.getPrice() * 0.95;

            // Add replace(',', '.') because computer in FR so the String result contains ,
            // This throw an error when Double.valuof try to cast the value for the finalPrice
            String resultFormat = decimalFormat.format(discountTicket).replace(',', '.');
            double finalPrice = Double.parseDouble(resultFormat);
            ticket.setPrice(finalPrice);
        }

        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(difference_In_Hours * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(difference_In_Hours * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}