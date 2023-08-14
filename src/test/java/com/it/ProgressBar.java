package com.it;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgressBar {
    public static void main(String[] args) {
        String startTime = "2023-07-27 10:00:00";
        String endTime = "2023-07-27 12:00:00";
        String currentTime = "2023-07-27 11:30:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);
            Date currentDate = sdf.parse(currentTime);

            long totalTime = endDate.getTime() - startDate.getTime();
            long elapsedTime = currentDate.getTime() - startDate.getTime();

            double progress = (double) elapsedTime / totalTime;

            System.out.println("进度：" + (int) (progress * 100) + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
