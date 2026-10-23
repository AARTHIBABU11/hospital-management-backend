package com.hospital.dto;

import java.util.Map;

public class DashboardStatsDto {
    private long totalPatients;
    private long totalDoctors;
    private long todayAppointments;
    private long pendingAppointments;
    private double totalRevenue;
    private long availableDoctors;

    private Map<String, Long> appointmentsByMonth;
    private Map<String, Long> patientsByDepartment;
    private Map<String, Double> revenueByMonth;

    public DashboardStatsDto() {}

    public long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }

    public long getTotalDoctors() { return totalDoctors; }
    public void setTotalDoctors(long totalDoctors) { this.totalDoctors = totalDoctors; }

    public long getTodayAppointments() { return todayAppointments; }
    public void setTodayAppointments(long todayAppointments) { this.todayAppointments = todayAppointments; }

    public long getPendingAppointments() { return pendingAppointments; }
    public void setPendingAppointments(long pendingAppointments) { this.pendingAppointments = pendingAppointments; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public long getAvailableDoctors() { return availableDoctors; }
    public void setAvailableDoctors(long availableDoctors) { this.availableDoctors = availableDoctors; }

    public Map<String, Long> getAppointmentsByMonth() { return appointmentsByMonth; }
    public void setAppointmentsByMonth(Map<String, Long> appointmentsByMonth) { this.appointmentsByMonth = appointmentsByMonth; }

    public Map<String, Long> getPatientsByDepartment() { return patientsByDepartment; }
    public void setPatientsByDepartment(Map<String, Long> patientsByDepartment) { this.patientsByDepartment = patientsByDepartment; }

    public Map<String, Double> getRevenueByMonth() { return revenueByMonth; }
    public void setRevenueByMonth(Map<String, Double> revenueByMonth) { this.revenueByMonth = revenueByMonth; }
}
