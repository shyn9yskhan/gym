package com.shyn9yskhan.trainer_workload_service.domain;

import java.util.ArrayList;
import java.util.List;

public class Year {
    private int year;
    private List<MonthSummary> months;

    private List<MonthSummary> initializeMonths() {
        List<MonthSummary> months = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            months.add(null);
        }
        return months;
    }

    public Year() {
    }

    public Year(int year) {
        this.year = year;
        this.months = initializeMonths();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<MonthSummary> getMonths() {
        return months;
    }

    public void addMonth(MonthSummary monthSummary) {
        if (months == null) throw new IllegalArgumentException("MonthSummary cannot be null");
        int index = getIndexOfMonth(monthSummary);
        MonthSummary existing = months.get(index);
        if (existing != null) {
            if (existing.getMonth() == monthSummary.getMonth()) {
                throw new IllegalStateException(
                        String.format("Month %d already exists in year %d",
                                monthSummary.getMonth(), year)
                );
            }
        }
        months.set(index, monthSummary);
    }

    public void removeMonth(int month) {
        validateMonthNumber(month);
        int index = month - 1;
        months.set(index, null);
    }

    public MonthSummary getMonth(int month) {
        validateMonthNumber(month);
        int index = month - 1;
        return months.get(index);
    }

    private int getIndexOfMonth(MonthSummary monthSummary) {
        if (monthSummary == null) throw new IllegalArgumentException("MonthSummary cannot be null");
        int month = monthSummary.getMonth();
        validateMonthNumber(month);
        return month - 1;
    }

    private void validateYear(int year) {
        if (year < 1900 || year > 2100) {
            throw new IllegalArgumentException(
                    String.format("Year must be between 1900 and 2100, got: %d", year)
            );
        }
    }

    private void validateMonthNumber(int month) {
        if (month < 1 || month > 12) throw new IllegalArgumentException(
                String.format("Month must be between 1 and 12, got: %d", month)
        );
    }
}
