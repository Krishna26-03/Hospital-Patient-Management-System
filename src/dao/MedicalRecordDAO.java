package dao;

import model.MedicalRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    // Add new medical record
    public boolean addMedicalRecord(MedicalRecord record) throws SQLException {
        String sql = "INSERT INTO medical_records (patient_id, doctor_id, appointment_id, " +
                "diagnosis, prescription, notes) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, record.getPatientId());
            pstmt.setInt(2, record.getDoctorId());

            // Handle case where appointment_id might be 0 or invalid
            if (record.getAppointmentId() > 0) {
                pstmt.setInt(3, record.getAppointmentId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setString(4, record.getDiagnosis());
            pstmt.setString(5, record.getPrescription());
            pstmt.setString(6, record.getNotes());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        record.setRecordId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Get medical records by patient ID
    public List<MedicalRecord> getMedicalRecordsByPatient(int patientId) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM medical_records mr " +
                "JOIN patients p ON mr.patient_id = p.patient_id " +
                "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                "WHERE mr.patient_id = ? " +
                "ORDER BY mr.record_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractMedicalRecordFromResultSet(rs));
            }
        }
        return records;
    }

    // Get medical records by doctor ID
    public List<MedicalRecord> getMedicalRecordsByDoctor(int doctorId) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM medical_records mr " +
                "JOIN patients p ON mr.patient_id = p.patient_id " +
                "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                "WHERE mr.doctor_id = ? " +
                "ORDER BY mr.record_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractMedicalRecordFromResultSet(rs));
            }
        }
        return records;
    }

    // Get medical record by ID
    public MedicalRecord getMedicalRecordById(int recordId) throws SQLException {
        String sql = "SELECT mr.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM medical_records mr " +
                "JOIN patients p ON mr.patient_id = p.patient_id " +
                "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                "WHERE mr.record_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractMedicalRecordFromResultSet(rs);
            }
        }
        return null;
    }

    // Get all medical records
    public List<MedicalRecord> getAllMedicalRecords() throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM medical_records mr " +
                "JOIN patients p ON mr.patient_id = p.patient_id " +
                "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                "ORDER BY mr.record_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(extractMedicalRecordFromResultSet(rs));
            }
        }
        return records;
    }

    // Update medical record
    public boolean updateMedicalRecord(MedicalRecord record) throws SQLException {
        String sql = "UPDATE medical_records SET diagnosis=?, prescription=?, notes=? " +
                "WHERE record_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, record.getDiagnosis());
            pstmt.setString(2, record.getPrescription());
            pstmt.setString(3, record.getNotes());
            pstmt.setInt(4, record.getRecordId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // Delete medical record
    public boolean deleteMedicalRecord(int recordId) throws SQLException {
        String sql = "DELETE FROM medical_records WHERE record_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recordId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Search medical records by diagnosis keyword
    public List<MedicalRecord> searchMedicalRecordsByDiagnosis(String keyword) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM medical_records mr " +
                "JOIN patients p ON mr.patient_id = p.patient_id " +
                "JOIN doctors d ON mr.doctor_id = d.doctor_id " +
                "WHERE mr.diagnosis LIKE ? " +
                "ORDER BY mr.record_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(extractMedicalRecordFromResultSet(rs));
            }
        }
        return records;
    }

    // Get medical records count for a patient
    public int getMedicalRecordCount(int patientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM medical_records WHERE patient_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // method to extract medical record from ResultSet
    private MedicalRecord extractMedicalRecordFromResultSet(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setRecordId(rs.getInt("record_id"));
        record.setPatientId(rs.getInt("patient_id"));
        record.setDoctorId(rs.getInt("doctor_id"));
        record.setAppointmentId(rs.getInt("appointment_id"));
        record.setPatientName(rs.getString("patient_fname") + " " + rs.getString("patient_lname"));
        record.setDoctorName("Dr. " + rs.getString("doctor_fname") + " " + rs.getString("doctor_lname"));
        record.setDiagnosis(rs.getString("diagnosis"));
        record.setPrescription(rs.getString("prescription"));
        record.setNotes(rs.getString("notes"));
        record.setRecordDate(rs.getTimestamp("record_date").toLocalDateTime());
        return record;
    }
}