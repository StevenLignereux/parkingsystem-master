package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.math.BigDecimal;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double difference_In_Time = outHour - inHour;
        double difference_In_Hours = (difference_In_Time / (1000 * 60 * 60));

        /**
         * Add free 30 minutes
         */
        if (difference_In_Hours <= 0.5) {
            difference_In_Hours = 0;
        }

        /**
         *  If the user is already came
         */
        if (ticket.isAlreadyCame()) {
            double discount = ticket.getPrice() * 0.95;
            ticket.setPrice(discount);
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