package dao;

import model.Appointment;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppointmentDAO {

    // Schedule new appointment
    public boolean scheduleAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, " +
                "appointment_time, reason) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            pstmt.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            pstmt.setString(5, appointment.getReason());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        appointment.setAppointmentId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    // Get appointment queue using LinkedList
    public LinkedList<Appointment> getAppointmentQueue() throws SQLException {
        LinkedList<Appointment> appointmentQueue = new LinkedList<>();
        String sql = "SELECT a.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "WHERE a.status = 'Scheduled' " +
                "ORDER BY a.appointment_date, a.appointment_time";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                appointmentQueue.add(extractAppointmentFromResultSet(rs));
            }
        }
        return appointmentQueue;
    }

    // Get appointments by patient ID
    public List<Appointment> getAppointmentsByPatient(int patientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "WHERE a.patient_id = ? " +
                "ORDER BY a.appointment_date DESC, a.appointment_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        }
        return appointments;
    }

    // Get appointments by doctor ID
    public List<Appointment> getAppointmentsByDoctor(int doctorId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.first_name as patient_fname, p.last_name as patient_lname, " +
                "d.first_name as doctor_fname, d.last_name as doctor_lname " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "WHERE a.doctor_id = ? " +
                "ORDER BY a.appointment_date, a.appointment_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(extractAppointmentFromResultSet(rs));
            }
        }
        return appointments;
    }

    // Update appointment status
    public boolean updateAppointmentStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, appointmentId);

            return pstmt.executeUpdate() > 0;
        }
    }

    // Cancel appointment
    public boolean cancelAppointment(int appointmentId) throws SQLException {
        return updateAppointmentStatus(appointmentId, "Cancelled");
    }

    // Check if doctor is available at given date and time
    public boolean isDoctorAvailable(int doctorId, LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM appointments " +
                "WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? " +
                "AND status != 'Cancelled'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setTime(3, Time.valueOf(time));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    //method to extract appointment from ResultSet
    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPatientId(rs.getInt("patient_id"));
        appointment.setDoctorId(rs.getInt("doctor_id"));
        appointment.setPatientName(rs.getString("patient_fname") + " " + rs.getString("patient_lname"));
        appointment.setDoctorName("Dr. " + rs.getString("doctor_fname") + " " + rs.getString("doctor_lname"));
        appointment.setAppointmentDate(rs.getDate("appointment_date").toLocalDate());
        appointment.setAppointmentTime(rs.getTime("appointment_time").toLocalTime());
        appointment.setStatus(rs.getString("status"));
        appointment.setReason(rs.getString("reason"));
        return appointment;
    }
}