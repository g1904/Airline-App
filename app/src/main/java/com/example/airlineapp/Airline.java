package com.example.airlineapp;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Airline class for holding any type of AbstractFlight
 * @param <T>   any type of AbstractFlight
 */
public class Airline<T extends AbstractFlight> extends AbstractAirline<T> {

    private String airlineName;
    private LinkedHashSet<T> flights;

    /**
     * Returns the name of this airline.
     */
    @Override
    public String getName() { return airlineName; }

    /**
     * Adds a flight to this airline.
     *
     * @param flight
     *        A flight object will be added to the airline
     * @throws IllegalStateException
     *        when adding a flight to an unnamed airline
     *        when adding a flight that already added to the airline
     */
    @Override
    public void addFlight(T flight)
            throws IllegalStateException
    {
        if (ifContains(flight))
            throw new IllegalStateException("Flight " + flight.getNumber() + " already added.");
        else {
            if (airlineName == null)
                throw new IllegalStateException("This airline requires a name first. The flight isn't added.");
            else
                flights.add(flight);
        }
    }

    /**
     * Returns all of this airline's flights.
     */
    @Override
    public Collection<T> getFlights() { return flights; }

    /**
     * Creates a new <code>Airline</code>
     *
     * @param airlineName
     *        The name of the airline (String)
     */
    public Airline(String airlineName)
    {
        flights = new LinkedHashSet<>();
        this.airlineName = airlineName;
    }

    public Boolean ifContains(T flight)
    {
        for (T temp : flights) {
            if (temp.getNumber() == flight.getNumber())
                return true;
        }
        return false;
    }

}
