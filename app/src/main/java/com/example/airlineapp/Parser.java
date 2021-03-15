package com.example.airlineapp;

import edu.pdx.cs410J.AirportNames;

import java.util.LinkedList;
import java.util.List;

public class Parser {
    private String[] args;
    private List<String> options;
    private String dumpFile;
    private String prettyFile;
    private String xmlFile;
    private String host;
    private int port;
    private String searchAirline;
    private String searchSrc;
    private String searchDest;


    /**
     * get the array of flight info
     */
    public String[] getArgs() { return args; }
    /**
     * get the list of chosen options
     */
    public List<String> getOptions() { return options; }
    /**
     * keep the file name for reading and writing
     */
    public String getDumpFile() { return dumpFile; }
    public String getPrettyFile() { return prettyFile; }
    public String getXmlFile() { return xmlFile; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getSearchAirline() { return searchAirline; }
    public String getSearchSrc() { return searchSrc; }
    public String getSearchDest() { return searchDest; }

    /**
     * parse the command line arguments into organized data
     * @throws  IllegalArgumentException
     *          when invalid option occurs or the file name is missing
     */
    public Parser(String[] input) throws IllegalArgumentException {
        String[] args = new String[input.length];
        options = new LinkedList<>();
        int index = 0;
        for (int i = 0; i < input.length; i++) {
            if (input[i].charAt(0) == '-') {
                switch (input[i]) {
                    case "-print":
                    case "-README":
                    case "-search":
                        options.add(input[i]);
                        break;
                    case "-host":
                        options.add("-host");
                        i++;
                        if (i < input.length)
                            this.host = input[i];
                        else
                            throw new IllegalArgumentException("Missing host name");
                        break;
                    case "-port":
                        options.add("-port");
                        i++;
                        if (i < input.length) {
                            try {
                                this.port = Integer.parseInt(input[i]);
                            } catch (NumberFormatException e) {
                                throw new IllegalArgumentException("Port has to be an int.");
                            }
                        } else
                            throw new IllegalArgumentException("Missing port.");
                        break;

                    default:
                        throw new IllegalArgumentException("Invalid option.");
                }
            } else {
                args[index] = input[i];
                index++;
            }
        }

        if (options.contains("-search") && !options.contains("-host"))
            throw new IllegalArgumentException("-seach must be used with a host.");

        if ( (options.contains("-host") && !options.contains("-port")) || (!options.contains("-host") && options.contains("-port")) )
            throw new IllegalArgumentException("-host and -port must be both stated.");

        if (options.contains("-search")) {
            if (index > 3)
                throw new IllegalArgumentException("-search: too many arguments.");
            else if (index < 3)
                throw new IllegalArgumentException("-search requires more info.");
            else {
                searchAirline = args[0];
                searchSrc = args[1].toUpperCase();
                searchDest = args[2].toUpperCase();
                verify(searchSrc, searchDest);
            }
        }

        if (index > 0) {
            this.args = new String[index];
            System.arraycopy(args, 0, this.args, 0, index);
        } else this.args = new String[1];

    }

    public static void verify(String src, String dest) {
        if (dest.length() == 3) {
            for (int i = 0; i < 3; i++) {
                if ((!Character.isLetter(dest.charAt(i))))
                    throw new IllegalArgumentException("-search: Invalid arrival airport.");
            }
            if (AirportNames.getName(dest.toUpperCase()) == null)
                throw new IllegalArgumentException("-search: Arrival airport doesn't exist.");
        } else
            throw new IllegalArgumentException("-search: Invalid arrival airport.");
        if (src.length() == 3) {
            for (int i = 0; i < 3; i++) {
                if (!Character.isLetter(src.charAt(i)))
                    throw new IllegalArgumentException("-search: Invalid departure airport.");
            }
            if (AirportNames.getName(src.toUpperCase()) == null)
                throw new IllegalArgumentException("-search: Departure airport doesn't exist.");
        } else
            throw new IllegalArgumentException("-search: Invalid departure airport.");
    }

}
