package model;

import java.time.LocalDate;

public class Doctor {
    private int doctorId;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;
    private double consultationFee;
    private String availableDays;
    private LocalDate joiningDate;

    public Doctor() {}

    public Doctor(String firstName, String lastName, String specialization,
                  String phone, String email, double consultationFee,
                  String availableDays, LocalDate joiningDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.consultationFee = consultationFee;
        this.availableDays = availableDays;
        this.joiningDate = joiningDate;
    }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return "Dr. " + firstName + " " + lastName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public String getAvailableDays() { return availableDays; }
    public void setAvailableDays(String availableDays) { this.availableDays = availableDays; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    @Override
    public String toString() {
        return String.format("Doctor[ID=%d, Name=Dr. %s %s, Specialization=%s, Fee=%.2f]",
                doctorId, firstName, lastName, specialization, consultationFee);
    }
}