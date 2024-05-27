package com.stevenpaw.fightvalley.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util_Time {

    /**
     * Gibt das Datum wieder
     * @return Datum dd.MM.yyyy (String)
     */
    public static String getRawDate() {
        Date now = new Date();
        SimpleDateFormat formatdate = new SimpleDateFormat("dd.MM.yyyy");
        formatdate.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return formatdate.format(now);
    }

    /**
     *  Gibt das Datum in Reihenfolge wieder
     * @return Datum yyyy.MM.dd (String)
     */
    public static String getRawDateReverse() {
        Date now = new Date();
        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy.MM.dd");
        formatdate.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return formatdate.format(now);
    }

    /**
     * Gibt die Zeit wieder
     * @return Zeit HH:mm (String)
     */
    public static String getRawTime() {
        Date now = new Date();
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");
        formattime.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return formattime.format(now);
    }

    /**
     * Gibt die Zeit mit Sekunden wieder
     * @return Zeit HH:mm:ss (String)
     */
    public static String getRawTimeWithSeconds() {
        Date now = new Date();
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss");
        formattime.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return formattime.format(now);
    }

    /**
     * Gibt die Zeit mit Formatierung wieder
     * @return Zeit [HH:mm] (String)
     */
    public static String getFormattedTime() {
        Date now = new Date();
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");
        formattime.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return "§8[§7"+formattime.format(now)+"§8]";
    }

    /**
     * Gibt die Zeit inkl. Sekunden mit Formatierung wieder
     * @return Zeit [HH:mm:ss] (String)
     */
    public static String getFormattedTimeWithSeconds() {
        Date now = new Date();
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm:ss");
        formattime.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return "§8[§7"+formattime.format(now)+"§8]";
    }

    /**
     * Gibt die aktuelle Stunde wieder
     * @return Integer = Stunde
     */
    public static int getHour(){
        Date now = new Date();
        SimpleDateFormat formattime = new SimpleDateFormat("HH");
        formattime.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return Integer.parseInt(formattime.format(now));
    }

    /**
     * Gibt die aktuelle Minute wieder
     * @return Integer = Minute
     */
    public static int getMinute(){
        Date now = new Date();
        SimpleDateFormat formattime = new SimpleDateFormat("mm");
        formattime.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return Integer.parseInt(formattime.format(now));
    }
}
