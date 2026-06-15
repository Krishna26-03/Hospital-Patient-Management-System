package service;

import dao.DoctorDAO;
import model.Doctor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DoctorService {
    private final DoctorDAO doctorDAO;
    private final Map<Integer, Doctor> doctorCache; // HashMap for quick retrieval

    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
        this.doctorCache = new HashMap<>();
        loadDoctorCache();
    }

    private void loadDoctorCache() {
        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            for (Doctor doctor : doctors) {
                doctorCache.put(doctor.getDoctorId(), doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error loading doctor cache: " + e.getMessage());
        }
    }

    // Add new doctor
    public boolean addDoctor(Doctor doctor) {
        try {
            if (doctorDAO.addDoctor(doctor)) {
                doctorCache.put(doctor.getDoctorId(), doctor);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding doctor: " + e.getMessage());
        }
        return false;
    }

    // Get doctor by ID
    public Optional<Doctor> getDoctorById(int doctorId) {
        Doctor cachedDoctor = doctorCache.get(doctorId);
        if (cachedDoctor != null) {
            return Optional.of(cachedDoctor);
        }

        try {
            Optional<Doctor> doctor = doctorDAO.getDoctorById(doctorId);
            if (doctor.isPresent()) {
                doctorCache.put(doctor.get().getDoctorId(), doctor.get());
            }
            return doctor;
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        try {
            return doctorDAO.getAllDoctors();
        } catch (SQLException e) {
            System.err.println("Error retrieving doctors: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Search doctors by specialization using lambda
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        List<Doctor> result = new ArrayList<>();
        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            for (Doctor doctor : doctors) {
                if (doctor.getSpecialization().toLowerCase().contains(specialization.toLowerCase())) {
                    result.add(doctor);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching doctors: " + e.getMessage());
        }
        return result;
    }

    // Get available doctors using lambda
    public List<Doctor> getAvailableDoctors(String day) {
        List<Doctor> result = new ArrayList<>();
        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            for (Doctor doctor : doctors) {
                if (doctor.getAvailableDays().toLowerCase().contains(day.toLowerCase())) {
                    result.add(doctor);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting available doctors: " + e.getMessage());
        }
        return result;
    }

    // Update doctor
    public boolean updateDoctor(Doctor doctor) {
        try {
            if (doctorDAO.updateDoctor(doctor)) {
                doctorCache.put(doctor.getDoctorId(), doctor);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating doctor: " + e.getMessage());
        }
        return false;
    }

    // Delete doctor
    public boolean deleteDoctor(int doctorId) {
        try {
            if (doctorDAO.deleteDoctor(doctorId)) {
                doctorCache.remove(doctorId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting doctor: " + e.getMessage());
        }
        return false;
    }

    // Display all doctors
    public void displayAllDoctors() {
        List<Doctor> doctors = getAllDoctors();
        System.out.println("\n--- All Doctors ---");
        for (Doctor doctor : doctors) {
            System.out.println(doctor);
        }
    }
}