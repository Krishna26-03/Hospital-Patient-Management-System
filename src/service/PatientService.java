package service;

import dao.PatientDAO;
import model.Patient;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class PatientService {
    private final PatientDAO patientDAO;
    private final Map<Integer, Patient> patientCache; // HashMap for quick patient record retrieval

    public PatientService() {
        this.patientDAO = new PatientDAO();
        this.patientCache = new HashMap<>();
        loadPatientCache();
    }

    // Load all patients into HashMap cache
    private void loadPatientCache() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            for (Patient patient : patients) {
                patientCache.put(patient.getPatientId(), patient);
            }
        } catch (SQLException e) {
            System.err.println("Error loading patient cache: " + e.getMessage());
        }
    }

    // Register new patient
    public boolean registerPatient(Patient patient) {
        try {
            if (patientDAO.addPatient(patient)) {
                patientCache.put(patient.getPatientId(), patient);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error registering patient: " + e.getMessage());
        }
        return false;
    }

    // Get patient by ID using HashMap (fast retrieval)
    public Optional<Patient> getPatientById(int patientId) {
        // First check cache
        Patient cachedPatient = patientCache.get(patientId);
        if (cachedPatient != null) {
            return Optional.of(cachedPatient);
        }

        // If not in cache, get from database
        try {
            Optional<Patient> patient = patientDAO.getPatientById(patientId);
            if (patient.isPresent()) {
                patientCache.put(patient.get().getPatientId(), patient.get());
            }
            return patient;
        } catch (SQLException e) {
            System.err.println("Error retrieving patient: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        try {
            return patientDAO.getAllPatients();
        } catch (SQLException e) {
            System.err.println("Error retrieving patients: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Search patients by name using lambda Predicate
    public List<Patient> searchPatientsByName(String name) {
        List<Patient> result = new ArrayList<>();
        try {
            List<Patient> patients = patientDAO.getAllPatients();

            // Using lambda expression for filtering
            Predicate<Patient> nameFilter = patient ->
                    patient.getFirstName().toLowerCase().contains(name.toLowerCase()) ||
                            patient.getLastName().toLowerCase().contains(name.toLowerCase());

            for (Patient patient : patients) {
                if (nameFilter.test(patient)) {
                    result.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching patients: " + e.getMessage());
        }
        return result;
    }

    // Filter patients by blood group using lambda
    public List<Patient> filterPatientsByBloodGroup(String bloodGroup) {
        List<Patient> result = new ArrayList<>();
        try {
            List<Patient> patients = patientDAO.getAllPatients();

            for (Patient patient : patients) {
                if (patient.getBloodGroup() != null &&
                        patient.getBloodGroup().equalsIgnoreCase(bloodGroup)) {
                    result.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error filtering patients: " + e.getMessage());
        }
        return result;
    }

    // Advanced search with multiple criteria using generics and lambda
    public <T> List<Patient> searchPatients(Predicate<Patient> criteria) {
        List<Patient> result = new ArrayList<>();
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            for (Patient patient : patients) {
                if (criteria.test(patient)) {
                    result.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in advanced search: " + e.getMessage());
        }
        return result;
    }

    // Update patient
    public boolean updatePatient(Patient patient) {
        try {
            if (patientDAO.updatePatient(patient)) {
                patientCache.put(patient.getPatientId(), patient);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
        }
        return false;
    }

    // Delete patient
    public boolean deletePatient(int patientId) {
        try {
            if (patientDAO.deletePatient(patientId)) {
                patientCache.remove(patientId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
        }
        return false;
    }

    // Get patient count
    public int getPatientCount() {
        return patientCache.size();
    }

    // Display all patients
    public void displayAllPatients() {
        List<Patient> patients = getAllPatients();
        System.out.println("\n--- All Patients ---");
        for (Patient patient : patients) {
            System.out.println(patient);
        }
    }
}