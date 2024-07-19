package com.nate.stctickets.models;

import java.util.Calendar;
import java.util.Random;

public class TimeGenerator {
    public static Calendar generateRandomTime(String daytime) {
        Random random = new Random();
        int hour = 0, minute = 0;

        switch (daytime.toLowerCase()) {
            case "morning":
                hour = 5 + random.nextInt(7); // 5am to 11am
                break;
            case "afternoon":
                hour = 12 + random.nextInt(5); // 12pm to 4pm
                break;
            case "evening":
                hour = 17 + random.nextInt(6); // 5pm to 10pm
                break;
            default:
                throw new IllegalArgumentException("Invalid daytime: " + daytime);
        }

        minute = random.nextInt(60);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }
}
