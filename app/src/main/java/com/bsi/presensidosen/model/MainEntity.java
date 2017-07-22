package com.bsi.presensidosen.model;

/**
 * Created by Mukhlasin on 9/30/2016.
 */

public class MainEntity {
    private long time;
    private long timeToday;

    public MainEntity() {
        time = 0;
        timeToday = 0;
    }

    public void setData(long time, long timeToday)
    {
        setTime(time);
        setTimeToday(timeToday);
    }



    public long getTime()
    {
        return time;
    }
    public void setTime(long time)
    {
        this.time = time;
    }

    public long getTimeToday()
    {
        return timeToday;
    }
    public void setTimeToday(long timeToday)
    {
        this.timeToday = timeToday;
    }

   /* @Override
    public String toString()
    {
        return "MainEntiti [time=" + time + ", timeToday=" + timeToday +"]";
    }*/
}
