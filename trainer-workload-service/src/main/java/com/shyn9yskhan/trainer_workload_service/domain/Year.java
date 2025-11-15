package com.shyn9yskhan.trainer_workload_service.domain;

import java.util.ArrayList;
import java.util.List;

public class Year {
    private int year;
    private List<MonthSummary> months;

    private List<MonthSummary> initializeMonths() {
        List<MonthSummary> months = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            months.add(new MonthSummary(i,0));
        }
        return months;
    }

    public Year() {
    }

    public Year(int year) {
        validateYear(year);
        this.year = year;
        this.months = initializeMonths();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        validateYear(year);
        this.year = year;
    }

    public List<MonthSummary> getMonths() {
        return months;
    }

    public void addToMonth(int month, int duration) {
        int index = getIndexOfMonth(month);
        MonthSummary monthSummary = months.get(index);
        monthSummary.addToTrainingsSummaryDuration(duration);
    }

    public void removeFromMonth(int month, int duration) {
        int index = getIndexOfMonth(month);
        MonthSummary monthSummary = months.get(index);
        monthSummary.removeFromTrainingsSummaryDuration(duration);
    }

    public MonthSummary getMonth(int month) {
        int index = getIndexOfMonth(month);
        return months.get(index);
    }

    private int getIndexOfMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month");
        }
        return month - 1;
    }

    private void validateYear(int year) {
        if (year < 1900 || year > 2100) {
            throw new IllegalArgumentException(
                    String.format("Year must be between 1900 and 2100, got: %d", year)
            );
        }
    }
}
