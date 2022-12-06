package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;



public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        // We have to convert hours in ms
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        double difference_In_Time = outHour - inHour;

        // We convert the difference to get it in hours
        double difference_In_Hours = (difference_In_Time / (1000 * 60 * 60));

        //  We setting the difference at 0 for having free 30 minutes
        if (difference_In_Hours <= 0.5) {
            difference_In_Hours = 0;
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

        // Reduce of 5% when the user is already came
        if (ticket.isAlreadyCame()) {
            double discountTicket = ticket.getPrice() * Fare.DISCOUNT_FOR_REGULAR_USER;
            ticket.setPrice(discountTicket);
        }

    }
}