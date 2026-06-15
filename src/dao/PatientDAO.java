package dao;

import model.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientDAO {

    // Add new patient
    public boolean addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (first_name, last_name, date_of_birth, gender, " +
                "phone, email, address, blood_group) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            pstmt.setString(4, patient.getGender());
            pstmt.setString(5, patient.getPhone());
            pstmt.setString(6, patient.getEmail());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getBloodGroup());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        patient.setPatientId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Get patient by ID
    public Optional<Patient> getPatientById(int patientId) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractPatientFromResultSet(rs));
            }
        }
        return Optional.empty();
    }

    // Get all patients
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY patient_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
        }
        return patients;
    }

    // Search patients by name
    public List<Patient> searchPatientsByName(String name) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE first_name LIKE ? OR last_name LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + name + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
        }
        return patients;
    }

    // Filter patients by blood group
    public List<Patient> filterPatientsByBloodGroup(String bloodGroup) throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE blood_group = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bloodGroup);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
        }
        return patients;
    }

    // Update patient information
    public boolean updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET first_name=?, last_name=?, date_of_birth=?, " +
                "gender=?, phone=?, email=?, address=?, blood_group=? WHERE patient_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setDate(3, Date.valueOf(patient.getDateOfBirth()));
            pstmt.setString(4, patient.getGender());
            pstmt.setString(5, patient.getPhone());
            pstmt.setString(6, patient.getEmail());
            pstmt.setString(7, patient.getAddress());
            pstmt.setString(8, patient.getBloodGroup());
            pstmt.setInt(9, patient.getPatientId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete patient
    public boolean deletePatient(int patientId) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // method to extract patient from ResultSet
    private Patient extractPatientFromResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("patient_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setLastName(rs.getString("last_name"));
        patient.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        patient.setGender(rs.getString("gender"));
        patient.setPhone(rs.getString("phone"));
        patient.setEmail(rs.getString("email"));
        patient.setAddress(rs.getString("address"));
        patient.setBloodGroup(rs.getString("blood_group"));
        patient.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime().toLocalDate());
        return patient;
    }
}
