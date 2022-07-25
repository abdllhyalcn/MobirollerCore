package com.mobiroller.core.helpers;


import android.content.Context;

import com.mobiroller.core.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by ealtaca on 13.01.2017.
 */

public class TimeHelper {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time, Context context) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.time_just_now);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return context.getString(R.string.time_a_minute_ago);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " " + context.getString(R.string.time_minute_ago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return context.getString(R.string.time_an_hour_ago);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " " + context.getString(R.string.time_hours_ago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.time_yesterday);
        } else {
            return diff / DAY_MILLIS + " " + context.getString(R.string.time_days_ago);
        }
    }

    private static HashMap<String, String> regexMap = new HashMap<>();
    private static String regex2two = "(?<=[^\\d])(\\d)(?=[^\\d])";
    private static String two = "0$1";

    public static String getDuration(String date) {
        if (regexMap.size() == 0) {
            regexMap.put("PT(\\d\\d)S", "00:$1");
            regexMap.put("PT(\\d\\d)M", "$1:00");
            regexMap.put("PT(\\d\\d)H", "$1:00:00");
            regexMap.put("PT(\\d\\d)M(\\d\\d)S", "$1:$2");
            regexMap.put("PT(\\d\\d)H(\\d\\d)S", "$1:00:$2");
            regexMap.put("PT(\\d\\d)H(\\d\\d)M", "$1:$2:00");
            regexMap.put("PT(\\d\\d)H(\\d\\d)M(\\d\\d)S", "$1:$2:$3");
        }
        String d = date.replaceAll(regex2two, two);
        String regex = getRegex(d);
        if (regex == null) {
            return "";
        }
        String newDate = d.replaceAll(regex, regexMap.get(regex));
        return newDate;
    }


    private static String getRegex(String date) {
        for (String r : regexMap.keySet())
            if (Pattern.matches(r, date))
                return r;
        return null;
    }

    public static String getTimeAgo(Context context, long time_ago) {
        time_ago = time_ago / 1000;
        long cur_time = (Calendar.getInstance().getTimeInMillis()) / 1000;
        long time_elapsed = cur_time - time_ago;
        long seconds = time_elapsed;
        // Seconds
        if (seconds <= 60) {
            return context.getString(R.string.time_just_now);
        }
        //Minutes
        else {
            int minutes = Math.round(time_elapsed / 60);

            if (minutes <= 60) {
                if (minutes == 1) {
                    return context.getString(R.string.time_a_minute_ago);
                } else {
                    return minutes + " " + context.getString(R.string.time_minute_ago);
                }
            }
            //Hours
            else {
                int hours = Math.round(time_elapsed / 3600);
                if (hours <= 24) {
                    if (hours == 1) {
                        return context.getString(R.string.time_an_hour_ago);
                    } else {
                        return hours + " " + context.getString(R.string.time_hours_ago);
                    }
                }
                //Days
                else {
                    int days = Math.round(time_elapsed / 86400);
                    if (days <= 7) {
                        if (days == 1) {
                            return 1 + " " + context.getString(R.string.time_day_ago);
                        } else {
                            return days + " " + context.getString(R.string.time_days_ago);
                        }
                    }
                    //Weeks
                    else {
                        int weeks = Math.round(time_elapsed / 604800);
                        if (weeks <= 4.3) {
                            if (weeks == 1) {
                                return context.getString(R.string.time_a_week_ago);
                            } else {
                                return weeks + " " + context.getString(R.string.time_weeks_ago);
                            }
                        }
                        //Months
                        else {
                            int months = Math.round(time_elapsed / 2600640);
                            if (months <= 12) {
                                if (months == 1) {
                                    return context.getString(R.string.time_a_month_ago);
                                } else {
                                    return months + " " + context.getString(R.string.time_months_ago);
                                }
                            }
                            //Years
                            else {
                                int years = Math.round(time_elapsed / 31207680);
                                if (years == 1) {
                                    return context.getString(R.string.time_a_year_ago);
                                } else {
                                    return years + " " + context.getString(R.string.time_years_ago);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
