package service;

import dao.MedicalRecordDAO;
import model.MedicalRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MedicalRecordService {
    private final MedicalRecordDAO medicalRecordDAO;

    public MedicalRecordService() {
        this.medicalRecordDAO = new MedicalRecordDAO();
    }

    // Add medical record
    public boolean addMedicalRecord(MedicalRecord record) {
        try {
            return medicalRecordDAO.addMedicalRecord(record);
        } catch (SQLException e) {
            System.err.println("Error adding medical record: " + e.getMessage());
            return false;
        }
    }

    // Get medical history for a patient
    public List<MedicalRecord> getPatientMedicalHistory(int patientId) {
        try {
            return medicalRecordDAO.getMedicalRecordsByPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Error retrieving medical history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Get medical records by doctor
    public List<MedicalRecord> getDoctorMedicalRecords(int doctorId) {
        try {
            return medicalRecordDAO.getMedicalRecordsByDoctor(doctorId);
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor records: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Search medical records by diagnosis using lambda expression
    public List<MedicalRecord> searchRecordsByDiagnosis(int patientId, String keyword) {
        List<MedicalRecord> result = new ArrayList<>();
        try {
            List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByPatient(patientId);

            // Using lambda expression - Predicate to filter records
            Predicate<MedicalRecord> diagnosisFilter = record ->
                    record.getDiagnosis().toLowerCase().contains(keyword.toLowerCase());

            for (MedicalRecord record : records) {
                if (diagnosisFilter.test(record)) {  // Lambda usage
                    result.add(record);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching records: " + e.getMessage());
        }
        return result;
    }

    // Filter records by doctor using lambda
    public List<MedicalRecord> filterRecordsByDoctor(int patientId, String doctorName) {
        List<MedicalRecord> result = new ArrayList<>();
        try {
            List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByPatient(patientId);

            // Using lambda expression
            Predicate<MedicalRecord> doctorFilter = record ->
                    record.getDoctorName().toLowerCase().contains(doctorName.toLowerCase());

            for (MedicalRecord record : records) {
                if (doctorFilter.test(record)) {  // Lambda usage
                    result.add(record);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error filtering records: " + e.getMessage());
        }
        return result;
    }

    // Get recent records using lambda
    public List<MedicalRecord> getRecentRecords(int patientId, int limit) {
        List<MedicalRecord> result = new ArrayList<>();
        try {
            List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByPatient(patientId);

            // Using lambda expression with custom functional interface
            RecordFilter limitFilter = (list, max) -> {
                List<MedicalRecord> filtered = new ArrayList<>();
                int count = 0;
                for (MedicalRecord record : list) {
                    if (count >= max) break;
                    filtered.add(record);
                    count++;
                }
                return filtered;
            };

            result = limitFilter.filter(records, limit);  // Lambda usage

        } catch (SQLException e) {
            System.err.println("Error getting recent records: " + e.getMessage());
        }
        return result;
    }

    // Custom functional interface for lambda demonstration
    @FunctionalInterface
    interface RecordFilter {
        List<MedicalRecord> filter(List<MedicalRecord> records, int limit);
    }

    // Display patient medical history
    public void displayPatientMedicalHistory(int patientId, String patientName) {
        List<MedicalRecord> records = getPatientMedicalHistory(patientId);
        System.out.println("\n--- Medical History for " + patientName + " ---");
        if (records.isEmpty()) {
            System.out.println("No medical records found.");
        } else {
            for (MedicalRecord record : records) {
                System.out.println("\nRecord ID: " + record.getRecordId());
                System.out.println("Date: " + record.getRecordDate());
                System.out.println("Doctor: " + record.getDoctorName());
                System.out.println("Diagnosis: " + record.getDiagnosis());
                System.out.println("Prescription: " + record.getPrescription());
                System.out.println("Notes: " + record.getNotes());
                System.out.println("-----------------------------------");
            }
        }
    }
}