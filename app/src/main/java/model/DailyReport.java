package model;

public class DailyReport {

    private String type;
    private Double totalSales;

    public DailyReport() {
    }

    public DailyReport(String type, Double totalSales) {
        this.type = type;
        this.totalSales = totalSales;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }
}
