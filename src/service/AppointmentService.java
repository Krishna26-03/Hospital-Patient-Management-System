package service;

import dao.AppointmentDAO;
import model.Appointment;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO;
    private LinkedList<Appointment> appointmentQueue; // LinkedList for queue management

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        loadAppointmentQueue();
    }

    // Load appointment queue
    private void loadAppointmentQueue() {
        try {
            this.appointmentQueue = appointmentDAO.getAppointmentQueue();
        } catch (SQLException e) {
            System.err.println("Error loading appointment queue: " + e.getMessage());
            this.appointmentQueue = new LinkedList<>();
        }
    }

    // Schedule new appointment
    public boolean scheduleAppointment(Appointment appointment) {
        try {
            // Validate doctor availability
            if (!appointmentDAO.isDoctorAvailable(appointment.getDoctorId(),
                    appointment.getAppointmentDate(), appointment.getAppointmentTime())) {
                System.err.println("Doctor is not available at this time slot!");
                return false;
            }

            if (appointmentDAO.scheduleAppointment(appointment)) {
                appointmentQueue.add(appointment);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
        }
        return false;
    }

    // Get appointment queue (LinkedList implementation)
    public LinkedList<Appointment> getAppointmentQueue() {
        return appointmentQueue;
    }

    // Process next appointment in queue (FIFO)
    public Appointment processNextAppointment() {
        if (!appointmentQueue.isEmpty()) {
            Appointment nextAppointment = appointmentQueue.poll(); // Remove from front
            try {
                appointmentDAO.updateAppointmentStatus(nextAppointment.getAppointmentId(), "Completed");
                System.out.println("Processing appointment: " + nextAppointment);
            } catch (SQLException e) {
                System.err.println("Error updating appointment status: " + e.getMessage());
            }
            return nextAppointment;
        }
        System.out.println("No appointments in queue!");
        return null;
    }

    // Peek at next appointment without removing
    public Appointment peekNextAppointment() {
        return appointmentQueue.peek();
    }

    // Get appointments by patient
    public List<Appointment> getAppointmentsByPatient(int patientId) {
        try {
            return appointmentDAO.getAppointmentsByPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Error retrieving patient appointments: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Get appointments by doctor
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        try {
            return appointmentDAO.getAppointmentsByDoctor(doctorId);
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor appointments: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Cancel appointment
    public boolean cancelAppointment(int appointmentId) {
        try {
            if (appointmentDAO.cancelAppointment(appointmentId)) {
                // Remove from queue using lambda
                appointmentQueue.removeIf(apt -> apt.getAppointmentId() == appointmentId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error cancelling appointment: " + e.getMessage());
        }
        return false;
    }

    // Check queue size
    public int getQueueSize() {
        return appointmentQueue.size();
    }

    // Display appointment queue
    public void displayAppointmentQueue() {
        System.out.println("\n--- Appointment Queue (" + appointmentQueue.size() + " pending) ---");
        if (appointmentQueue.isEmpty()) {
            System.out.println("No appointments in queue.");
        } else {
            for (Appointment appointment : appointmentQueue) {
                System.out.println(appointment);
            }
        }
    }
}