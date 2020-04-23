package com.assets.derivedtasks;

import com.assets.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Planned extends Task {
    protected Date happeningDate;

    public Planned() {
        super();
        this.happeningDate = new Date();
    };

    public Planned(String title, boolean status, Date date){
        super(title, status);
        this.happeningDate = date;
    }

    public long getCountdown(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
        String inputString1 = format.format(new Date());
        String inputString2 = format.format(this.happeningDate);

        LocalDate date1 = LocalDate.parse(inputString1, dtf);
        LocalDate date2 = LocalDate.parse(inputString2, dtf);
        long daysBetween = ChronoUnit.DAYS.between(date1, date2);
        return daysBetween;
    }

    public void setHappeningDate(Date happeningDate) {
        if(getCountdown() < 0){
            System.out.println("Invalid date");
        }
        else{
            this.happeningDate = happeningDate;
        }
    }

    @Override
    public void Print(){
        super.Print();
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
        String date = format.format(this.happeningDate);
        System.out.println("|-Days left " + getCountdown());
    }
}
