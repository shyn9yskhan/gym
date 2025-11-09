package com.shyn9yskhan.trainer_workload_service.document;

import java.util.List;

public class YearDocument {
    private int year;
    private List<MonthSummaryDocument> months;

    public YearDocument() {
    }

    public YearDocument(int year, List<MonthSummaryDocument> months) {
        this.year = year;
        this.months = months;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<MonthSummaryDocument> getMonths() {
        return months;
    }

    public void setMonths(List<MonthSummaryDocument> months) {
        this.months = months;
    }
}
