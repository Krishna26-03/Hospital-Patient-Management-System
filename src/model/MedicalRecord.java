package model;

import java.time.LocalDateTime;

public class MedicalRecord {
    private int recordId;
    private int patientId;
    private int doctorId;
    private int appointmentId;
    private String patientName;
    private String doctorName;
    private String diagnosis;
    private String prescription;
    private String notes;
    private LocalDateTime recordDate;

    public MedicalRecord() {}

    public MedicalRecord(int patientId, int doctorId, int appointmentId,
                         String diagnosis, String prescription, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
    }

    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDateTime recordDate) { this.recordDate = recordDate; }

    @Override
    public String toString() {
        return String.format("MedicalRecord[ID=%d, Patient=%s, Doctor=%s, Date=%s]",
                recordId, patientName, doctorName, recordDate);
    }
}