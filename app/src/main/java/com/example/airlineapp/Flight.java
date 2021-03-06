package com.example.airlineapp;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Flight class for flight objects
 */
public class Flight extends AbstractFlight implements Comparable<AbstractFlight> {
  private final String[] flight_info;
  private String airline;
  private Integer flightNumber;
  private String src; // three-letter airport code, departure
  private Calendar depart_date;
  private String dest; // three-letter airport code, arrival
  private Calendar arrive_date;
  private final DateFormat dateFormatter =
          DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

  /**
   * Creates a new <code>Flight</code>
   *
   * @param args
   *        arguments that contains the required flight info
   * @param toString
   *        if the toString method needs to be invoked
   * @throws IllegalArgumentException
   *         one of the arguments doesn't meet the requirement
   */
  public Flight(String[] args, Boolean toString) {
    flight_info = args;
    // 24hr time format expected args length
    int hr24Time = 8;
    // 12hr time format expected args length
    int hr12Time = 10;

    if ((args.length > hr12Time) || (args.length == 9))
      throw new IllegalArgumentException("Too many arguments.");
    if (args.length < hr24Time)
      throw new IllegalArgumentException("Missing command line arguments.");

    setAirline();
    setFlightNumber();
    setSrc();
    if (args.length == hr24Time) {
      setDepart_date24();
      setDest24();
      setArrive_date24();
    } else {
      setDepart_date12();
      setDest12();
      setArrive_date12();
    }

    if (toString) System.out.println(this.toString());
  }

  private void setAirline() {
    this.airline = flight_info[0];
  }

  private void setFlightNumber() {
    try {
      this.flightNumber = Integer.parseInt(flight_info[1]);
    } catch (NumberFormatException e) {
      throw new NumberFormatException("Invalid flight number.");
    }
  }

  private void setSrc() {
    if (flight_info[2].length() == 3) {
      for (int i = 0; i < 3; i++) {
        if (!Character.isLetter(flight_info[2].charAt(i)))
          throw new IllegalArgumentException("Invalid departure airport.");
      }
      if (AirportNames.getName(flight_info[2].toUpperCase()) == null)
        throw new IllegalArgumentException("Departure airport doesn't exist.");
      this.src = flight_info[2].toUpperCase();
    } else
      throw new IllegalArgumentException("Invalid departure airport.");
  }

  // set the depart_date if the input format is "hh:mm" (24-hr format)
  private void setDepart_date24() {
    String[] parts1 = flight_info[3].split("/");
    Integer[] d_date = new Integer[3];
    if (parts1.length != 3)
      throw new IllegalArgumentException("Invalid departure date.");
    else {
      for (int i = 0; i < 3; i++) {
        try {
          d_date[i] = Integer.parseInt(parts1[i]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid departure date.");
        }
      }
    }
    String[] parts2 = flight_info[4].split(":");
    Integer[] d_time = new Integer[3];
    if (parts2.length != 2)
      throw new IllegalArgumentException("Invalid departure time.");
    else {
      for (int i = 0; i < 2; i++) {
        try {
          d_time[i] = Integer.parseInt(parts2[i]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid departure time.");
        }
      }
    }
    this.depart_date = new GregorianCalendar();
    if ((d_date[2] > 999) && (d_date[2] < 10000))
      this.depart_date.set(Calendar.YEAR, d_date[2]);
    else
      throw new IllegalArgumentException("Invalid departure year.");
    if ((d_date[0] < 13) && (d_date[0] > 0))
      this.depart_date.set(Calendar.MONTH, d_date[0] - 1);
    else
      throw new IllegalArgumentException("Invalid departure month.");
    if ((d_date[1] > 0) && (d_date[1] < 32)) {
      if ((d_date[0] == 2) && (d_date[1] > 28) && (!((d_date[1] == 29)
              && (new GregorianCalendar().isLeapYear(d_date[2])))))
        throw new IllegalArgumentException("Invalid departure day.");
      this.depart_date.set(Calendar.DAY_OF_MONTH, d_date[1]);
    } else
      throw new IllegalArgumentException("Invalid departure day.");
    if ((d_time[0] >= 0) && (d_time[0] <= 24))
      this.depart_date.set(Calendar.HOUR_OF_DAY, d_time[0]);
    else
      throw new IllegalArgumentException("Invalid departure hour.");
    if ((d_time[1] >= 0) && (d_time[1] <= 59))
      this.depart_date.set(Calendar.MINUTE, d_time[1]);
    else
      throw new IllegalArgumentException("Invalid departure minute.");
    this.depart_date.set(Calendar.SECOND, 0);
  }

  // set the Dest if the date is "hh:mm" (24-hr format)
  private void setDest24() {
    if (flight_info[5].length() == 3) {
      for (int i = 0; i < 3; i++) {
        if ((!Character.isLetter(flight_info[5].charAt(i))))
          throw new IllegalArgumentException("Invalid arrival airport.");
      }
      if (AirportNames.getName(flight_info[5].toUpperCase()) == null)
        throw new IllegalArgumentException("Arrival airport doesn't exist.");
      this.dest = flight_info[5].toUpperCase();
    } else
      throw new IllegalArgumentException("Invalid arrival airport.");
  }

  // set the arrive_date if the input format is "hh:mm" (24-hr format)
  private void setArrive_date24() {
    String[] parts3 = flight_info[6].split("/");
    Integer[] a_date = new Integer[3];
    if (parts3.length != 3)
      throw new IllegalArgumentException("Invalid arrival date.");
    else {
      for (int i = 0; i < 3; i++) {
        try {
          a_date[i] = Integer.parseInt(parts3[i]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid arrival date.");
        }
      }
    }
    String[] parts4 = flight_info[7].split(":");
    Integer[] a_time = new Integer[3];
    if (parts4.length != 2)
      throw new IllegalArgumentException("Invalid arrival time.");
    else {
      for (int i = 0; i < 2; i++) {
        try {
          a_time[i] = Integer.parseInt(parts4[i]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid arrival time.");
        }
      }
    }
    this.arrive_date = new GregorianCalendar();
    if ((a_date[2] > 999) && (a_date[2] < 10000))
      this.arrive_date.set(Calendar.YEAR, a_date[2]);
    else
      throw new IllegalArgumentException("Invalid arrival year.");
    if ((a_date[0] < 13) && (a_date[0] > 0))
      this.arrive_date.set(Calendar.MONTH, a_date[0] - 1);
    else
      throw new IllegalArgumentException("Invalid arrival month.");
    if ((a_date[1] > 0) && (a_date[1] < 32)) {
      if ((a_date[0] == 2) && (a_date[1] > 28) && (!((a_date[1] == 29)
              && (new GregorianCalendar().isLeapYear(a_date[2])))))
        throw new IllegalArgumentException("Invalid arrival day.");
      this.arrive_date.set(Calendar.DAY_OF_MONTH, a_date[1]);
    } else
      throw new IllegalArgumentException("Invalid arrival day.");
    if ((a_time[0] >= 0) && (a_time[0] <= 24))
      this.arrive_date.set(Calendar.HOUR_OF_DAY, a_time[0]);
    else
      throw new IllegalArgumentException("Invalid arrival hour.");
    if ((a_time[1] >= 0) && (a_time[1] <= 59))
      this.arrive_date.set(Calendar.MINUTE, a_time[1]);
    else
      throw new IllegalArgumentException("Invalid arrival minute.");
    this.arrive_date.set(Calendar.SECOND, 0);

    long differ = this.arrive_date.getTimeInMillis() - this.depart_date.getTimeInMillis();
    if (differ < 0) throw new IllegalArgumentException("This flight travels back in time.");
  }

  // set the depart_date if the input format is "hh:mm AM/PM" (12-hr format)
  private void setDepart_date12() {
    boolean ifAM;
    String[] parts1 = flight_info[3].split("/");
    Integer[] d_date = new Integer[3];
    if (parts1.length != 3)
      throw new IllegalArgumentException("Invalid departure date.");
    else {
      for (int i = 0; i < 3; i++) {
        try {
          d_date[i] = Integer.parseInt(parts1[i]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid departure date.");
        }
      }
    }
    String[] parts2 = flight_info[4].split(":");
    Integer[] d_time = new Integer[3];
    switch (flight_info[5]) {
      case "AM":
        ifAM = true;
        break;
      case "PM":
        ifAM = false;
        break;
      default:
        throw new IllegalArgumentException("Invalid departure time.");
    }
    if (parts2.length != 2)
      throw new IllegalArgumentException("Invalid departure time.");
    else {
      for (int i = 0; i < 2; i++) {
        try {
          if ((i == 0) && !ifAM) {
            if (parts2[i].equals("12")) d_time[i] = Integer.parseInt(parts2[i]);
            else d_time[i] = Integer.parseInt(parts2[i]) + 12;
          } else {
            if ((i == 0) && (parts2[i].equals("12")))
              d_time[i] = Integer.parseInt(parts2[i]) - 12;
            else d_time[i] = Integer.parseInt(parts2[i]);
          }
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid departure time.");
        }
      }
    }
    this.depart_date = new GregorianCalendar();
    if ((d_date[2] > 999) && (d_date[2] < 10000))
      this.depart_date.set(Calendar.YEAR, d_date[2]);
    else
      throw new IllegalArgumentException("Invalid departure year.");
    if ((d_date[0] < 13) && (d_date[0] > 0))
      this.depart_date.set(Calendar.MONTH, d_date[0] - 1);
    else
      throw new IllegalArgumentException("Invalid departure month.");
    if ((d_date[1] > 0) && (d_date[1] < 32)) {
      if ((d_date[0] == 2) && (d_date[1] > 28) && (!((d_date[1] == 29)
              && (new GregorianCalendar().isLeapYear(d_date[2])))))
        throw new IllegalArgumentException("Invalid departure day.");
      this.depart_date.set(Calendar.DAY_OF_MONTH, d_date[1]);
    } else
      throw new IllegalArgumentException("Invalid departure day.");
    if ((d_time[0] >= 0) && (d_time[0] <= 24))
      this.depart_date.set(Calendar.HOUR_OF_DAY, d_time[0]);
    else
      throw new IllegalArgumentException("Invalid departure hour.");
    if ((d_time[1] >= 0) && (d_time[1] <= 59))
      this.depart_date.set(Calendar.MINUTE, d_time[1]);
    else
      throw new IllegalArgumentException("Invalid departure minute.");
    this.depart_date.set(Calendar.SECOND, 0);
  }

  // set the Dest if the input format of the dates is "hh:mm AM/PM" (12-hr format)
  private void setDest12() {
    if (flight_info[6].length() == 3) {
      for (int i = 0; i < 3; i++) {
        if ((!Character.isLetter(flight_info[6].charAt(i))))
          throw new IllegalArgumentException("Invalid arrival airport.");
      }
      if (AirportNames.getName(flight_info[6].toUpperCase()) == null)
        throw new IllegalArgumentException("Arrival airport doesn't exist.");
      this.dest = flight_info[6].toUpperCase();
    } else
      throw new IllegalArgumentException("Invalid arrival airport.");
  }

  // set the arrive_date if the input format is "hh:mm AM/PM" (12-hr format)
  private void setArrive_date12() {
    boolean ifAM;
    String[] parts3 = flight_info[7].split("/");
    Integer[] a_date = new Integer[3];
    if (parts3.length != 3)
      throw new IllegalArgumentException("Invalid arrival date.");
    else {
      for (int i = 0; i < 3; i++) {
        try {
          a_date[i] = Integer.parseInt(parts3[i]);
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid arrival date.");
        }
      }
    }
    String[] parts4 = flight_info[8].split(":");
    switch (flight_info[9]) {
      case "AM":
        ifAM = true;
        break;
      case "PM":
        ifAM = false;
        break;
      default:
        throw new IllegalArgumentException("Invalid arrival time.");
    }
    Integer[] a_time = new Integer[3];
    if (parts4.length != 2)
      throw new IllegalArgumentException("Invalid arrival time.");
    else {
      for (int i = 0; i < 2; i++) {
        try {
          if ((i == 0) && !ifAM) {
            if (parts4[i].equals("12"))
              a_time[i] = Integer.parseInt(parts4[i]);
            else
              a_time[i] = Integer.parseInt(parts4[i]) + 12;
          } else {
            if ((i == 0) && (parts4[i].equals("12)")))
              a_time[i] = Integer.parseInt(parts4[i]) - 12;
            else
              a_time[i] = Integer.parseInt(parts4[i]);
          }
        } catch (NumberFormatException e) {
          throw new NumberFormatException("Invalid arrival time.");
        }
      }
    }
    this.arrive_date = new GregorianCalendar();
    if ((a_date[2] > 999) && (a_date[2] < 10000))
      this.arrive_date.set(Calendar.YEAR, a_date[2]);
    else
      throw new IllegalArgumentException("Invalid arrival year.");
    if ((a_date[0] < 13) && (a_date[0] > 0))
      this.arrive_date.set(Calendar.MONTH, a_date[0] - 1);
    else
      throw new IllegalArgumentException("Invalid arrival month.");
    if ((a_date[1] > 0) && (a_date[1] < 32)) {
      if ((a_date[0] == 2) && (a_date[1] > 28) && (!((a_date[1] == 29)
              && (new GregorianCalendar().isLeapYear(a_date[2])))))
        throw new IllegalArgumentException("Invalid arrival day.");
      this.arrive_date.set(Calendar.DAY_OF_MONTH, a_date[1]);
    } else
      throw new IllegalArgumentException("Invalid arrival day.");
    if ((a_time[0] >= 0) && (a_time[0] <= 24))
      this.arrive_date.set(Calendar.HOUR_OF_DAY, a_time[0]);
    else
      throw new IllegalArgumentException("Invalid arrival hour.");
    if ((a_time[1] >= 0) && (a_time[1] <= 59))
      this.arrive_date.set(Calendar.MINUTE, a_time[1]);
    else
      throw new IllegalArgumentException("Invalid arrival minute.");
    this.arrive_date.set(Calendar.SECOND, 0);

    long differ = this.arrive_date.getTimeInMillis() - this.depart_date.getTimeInMillis();
    if (differ < 0) throw new IllegalArgumentException("This flight travels back in time.");
  }

    /**
   * Returns a number that uniquely identifies this flight.
   * @return unique flight number
   */
  @Override
  public int getNumber() {
    return flightNumber;
  }

  /**
   * Returns the three-letter code of the airport at which this flight
   * originates.
   * @return departure airport code
   */
  @Override
  public String getSource() {
    return AirportNames.getName(src);
  }
  public String getSrc() { return src; }

  /**
   * Returns a textual representation of this flight's departure
   * time.
   * @return departure date and time
   */
  @Override
  public String getDepartureString() {
    return dateFormatter.format(depart_date.getTime());
  }

  /**
   * Returns the three-letter code of the airport at which this flight
   * terminates.
   * @return arrival airport code
   */
  @Override
  public String getDestination() {
    return AirportNames.getName(dest);
  }

  /**
   * Returns a textual representation of this flight's arrival time.
   * @return arrival date and time
   */
  @Override
  public String getArrivalString() {
    return dateFormatter.format(arrive_date.getTime());
  }
  public String getDest() { return dest; }

  /**
   * Returns this flight's departure time as a <code>Date</code>.
   * @return departure date and time
   */
  @Override
  public Date getDeparture() {
    return depart_date.getTime();
  }

  /**
   * Returns this flight's arrival time as a <code>Date</code>.
   * @return arrival date and time
   */
  @Override
  public Date getArrival() {
    return arrive_date.getTime();
  }

  /**
   * Returns the airline name this flight belongs to.
   * @return airline name
   */
  public String getAirline() { return airline; }

  /**
   * return the string of original flight info
   */
  public String dump() {
    StringBuilder result = new StringBuilder(flight_info[0]);
    for (int i = 1; i < flight_info.length; i++)
      result.append(" ").append(flight_info[i]);
    return result.toString();
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   *
   * <p>The implementor must ensure
   * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
   * for all {@code x} and {@code y}.  (This
   * implies that {@code x.compareTo(y)} must throw an exception iff
   * {@code y.compareTo(x)} throws an exception.)
   *
   * <p>The implementor must also ensure that the relation is transitive:
   * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
   * {@code x.compareTo(z) > 0}.
   *
   * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
   * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
   * all {@code z}.
   *
   * <p>It is strongly recommended, but <i>not</i> strictly required that
   * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
   * class that implements the {@code Comparable} interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   *
   * <p>In the foregoing description, the notation
   * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
   * <i>signum</i> function, which is defined to return one of {@code -1},
   * {@code 0}, or {@code 1} according to whether the value of
   * <i>expression</i> is negative, zero, or positive, respectively.
   *
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it
   *                              from being compared to this object.
   */
  @Override
  public int compareTo(AbstractFlight o) {
    if (o == null) throw new NullPointerException("Can't compare flight to null");
    Flight obj = (Flight) o;
    int differ = this.src.compareTo(obj.src);
    if (differ != 0) return differ;
    differ = this.getDeparture().compareTo(obj.getDeparture());
    return differ;
  }

  private String duration() {
    long differ = TimeUnit.MILLISECONDS.toMinutes
            (arrive_date.getTimeInMillis() - depart_date.getTimeInMillis());
    return "\t\tThe duration of this flight is: " + differ + " minutes.";
  }

  public String prettyPrint() {
    return this.toString() + this.duration();
  }

  public String xml() {
    return "  <flight>\n" + "    <number>" + flightNumber + "</number>\n"
            + "    <src>" + src + "</src>\n" + "    <depart>\n"
            + "      <date day=\"" + depart_date.get(Calendar.DAY_OF_MONTH) + "\" month=\""
            + depart_date.get(Calendar.MONTH) + "\" year=\""
            + depart_date.get(Calendar.YEAR) + "\"/>\n"
            + "      <time hour=\"" + depart_date.get(Calendar.HOUR_OF_DAY) + "\" minute=\""
            + depart_date.get(Calendar.MINUTE) + "\"/>\n"
            + "    </depart>\n"
            + "    <dest>" + dest + "</dest>\n"
            + "    <arrive>\n"
            + "      <date day=\"" + arrive_date.get(Calendar.DAY_OF_MONTH) + "\" month=\""
            + arrive_date.get(Calendar.MONTH) + "\" year=\"" + arrive_date.get(Calendar.YEAR)
            + "\"/>\n"
            + "      <time hour=\"" + arrive_date.get(Calendar.HOUR_OF_DAY) + "\" minute=\""
            + arrive_date.get(Calendar.MINUTE) + "\"/>\n"
            + "    </arrive>\n" + "  </flight>\n";
  }

}

