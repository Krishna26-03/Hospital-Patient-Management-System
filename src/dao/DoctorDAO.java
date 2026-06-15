package dao;

import model.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDAO {

    // Add new doctor
    public boolean addDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (first_name, last_name, specialization, phone, " +
                "email, consultation_fee, available_days, joining_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, doctor.getFirstName());
            pstmt.setString(2, doctor.getLastName());
            pstmt.setString(3, doctor.getSpecialization());
            pstmt.setString(4, doctor.getPhone());
            pstmt.setString(5, doctor.getEmail());
            pstmt.setDouble(6, doctor.getConsultationFee());
            pstmt.setString(7, doctor.getAvailableDays());
            pstmt.setDate(8, Date.valueOf(doctor.getJoiningDate()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        doctor.setDoctorId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Get doctor by ID
    public Optional<Doctor> getDoctorById(int doctorId) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractDoctorFromResultSet(rs));
            }
        }
        return Optional.empty();
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors ORDER BY doctor_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        }
        return doctors;
    }

    // Search doctors by specialization
    public List<Doctor> searchDoctorsBySpecialization(String specialization) throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE specialization LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + specialization + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                doctors.add(extractDoctorFromResultSet(rs));
            }
        }
        return doctors;
    }

    // Update doctor information
    public boolean updateDoctor(Doctor doctor) throws SQLException {
        String sql = "UPDATE doctors SET first_name=?, last_name=?, specialization=?, " +
                "phone=?, email=?, consultation_fee=?, available_days=?, joining_date=? WHERE doctor_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctor.getFirstName());
            pstmt.setString(2, doctor.getLastName());
            pstmt.setString(3, doctor.getSpecialization());
            pstmt.setString(4, doctor.getPhone());
            pstmt.setString(5, doctor.getEmail());
            pstmt.setDouble(6, doctor.getConsultationFee());
            pstmt.setString(7, doctor.getAvailableDays());
            pstmt.setDate(8, Date.valueOf(doctor.getJoiningDate()));
            pstmt.setInt(9, doctor.getDoctorId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete doctor
    public boolean deleteDoctor(int doctorId) throws SQLException {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // method to extract doctor from ResultSet
    private Doctor extractDoctorFromResultSet(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setFirstName(rs.getString("first_name"));
        doctor.setLastName(rs.getString("last_name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setEmail(rs.getString("email"));
        doctor.setConsultationFee(rs.getDouble("consultation_fee"));
        doctor.setAvailableDays(rs.getString("available_days"));
        doctor.setJoiningDate(rs.getDate("joining_date").toLocalDate());
        return doctor;
    }
}