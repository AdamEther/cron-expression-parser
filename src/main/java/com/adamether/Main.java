package com.adamether;

import com.adamether.cron.CronExpression;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please provide cron expression as an argument. " +
                    "Reference \"*/15 0 1,15 * 1-5 /usr/bin/find\"");
            return;
        }

        CronExpression cronExpression = new CronExpression(args[0]);
        System.out.println(cronExpression);
    }
}