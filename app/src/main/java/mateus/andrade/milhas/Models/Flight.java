package mateus.andrade.milhas.Models;

import java.io.Serializable;

/**
 * Created by mateusandrade on 02/01/2018.
 */

public class Flight implements Serializable{

    String origin;
    String flightno;
    String deptime;
    String duration;
    String destination;
    String airline;
    String depdate;
    String arrtime;
    String carrierid;
    Fare fare;
    ReturnFlight[] returnfl;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getFlightno() {
        return flightno;
    }

    public void setFlightno(String flightno) {
        this.flightno = flightno;
    }

    public String getDeptime() {
        return deptime;
    }

    public void setDeptime(String deptime) {
        this.deptime = deptime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDepdate() {
        return depdate;
    }

    public void setDepdate(String depdate) {
        this.depdate = depdate;
    }

    public String getArrtime() {
        return arrtime;
    }

    public void setArrtime(String arrtime) {
        this.arrtime = arrtime;
    }

    public Fare getFare() {
        return fare;
    }

    public ReturnFlight[] getReturnfl() {
        return returnfl;
    }

    public void setReturnfl(ReturnFlight[] returnfl) {
        this.returnfl = returnfl;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public String getCarrierid() {
        return carrierid;
    }

    public void setCarrierid(String carrierid) {
        this.carrierid = carrierid;
    }
}

