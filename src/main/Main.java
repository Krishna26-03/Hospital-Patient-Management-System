package main;

import model.*;
import service.*;
import util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final MedicalRecordService medicalRecordService = new MedicalRecordService();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  HOSPITAL PATIENT MANAGEMENT SYSTEM");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        managePatients();
                        break;
                    case 2:
                        manageDoctors();
                        break;
                    case 3:
                        manageAppointments();
                        break;
                    case 4:
                        manageMedicalRecords();
                        break;
                    case 5:
                        displaySystemOverview();
                        break;
                    case 6:
                        running = false;
                        System.out.println("Thank you for using Hospital Management System!");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Manage Patients");
        System.out.println("2. Manage Doctors");
        System.out.println("3. Manage Appointments");
        System.out.println("4. Manage Medical Records");
        System.out.println("5. System Overview");
        System.out.println("6. Exit");
        System.out.println("===============================");
    }

    // ============ INPUT HELPER METHODS ============
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next(); // Clear invalid input
            System.out.print(prompt);
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return input;
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.next(); // Clear invalid input
            System.out.print(prompt);
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        return input;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine().trim();
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD.");
            }
        }
    }

    private static LocalTime getTimeInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String timeStr = scanner.nextLine().trim();
                return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format! Please use HH:MM (24-hour format).");
            }
        }
    }

    // ============ PATIENT MANAGEMENT ============
    private static void managePatients() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Patient Management -----");
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient by Name");
            System.out.println("4. Filter by Blood Group");
            System.out.println("5. View Patient Details");
            System.out.println("6. Update Patient");
            System.out.println("7. Delete Patient");
            System.out.println("8. Back to Main Menu");

            try {
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        registerPatient();
                        break;
                    case 2:
                        patientService.displayAllPatients();
                        break;
                    case 3:
                        searchPatientByName();
                        break;
                    case 4:
                        filterPatientsByBloodGroup();
                        break;
                    case 5:
                        viewPatientDetails();
                        break;
                    case 6:
                        updatePatient();
                        break;
                    case 7:
                        deletePatient();
                        break;
                    case 8:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.err.println("Error in patient management: " + e.getMessage());
            }
        }
    }

    private static void registerPatient() {
        try {
            System.out.println("\n--- Register New Patient ---");

            String firstName = getStringInput("First Name: ");
            while (!ValidationUtil.isValidName(firstName)) {
                System.out.println("Invalid name! Use only letters and spaces.");
                firstName = getStringInput("First Name: ");
            }

            String lastName = getStringInput("Last Name: ");
            while (!ValidationUtil.isValidName(lastName)) {
                System.out.println("Invalid name! Use only letters and spaces.");
                lastName = getStringInput("Last Name: ");
            }

            LocalDate dob = getDateInput("Date of Birth (YYYY-MM-DD): ");

            String gender = getStringInput("Gender (Male/Female/Other): ");
            while (!ValidationUtil.isValidGender(gender)) {
                System.out.println("Invalid gender! Please enter Male, Female, or Other");
                gender = getStringInput("Gender: ");
            }

            String phone = getStringInput("Phone (10 digits): ");
            while (!ValidationUtil.isValidPhone(phone)) {
                System.out.println("Invalid phone number! Must be exactly 10 digits.");
                phone = getStringInput("Phone: ");
            }

            String email = getStringInput("Email (optional): ");
            if (!email.isEmpty() && !ValidationUtil.isValidEmail(email)) {
                System.out.println("Invalid email format!");
                email = getStringInput("Email (optional): ");
            }

            String address = getStringInput("Address: ");

            String bloodGroup = getStringInput("Blood Group (optional, e.g., A+): ");
            if (!bloodGroup.isEmpty() && !ValidationUtil.isValidBloodGroup(bloodGroup)) {
                System.out.println("Invalid blood group! Valid groups: A+, A-, B+, B-, AB+, AB-, O+, O-");
                bloodGroup = getStringInput("Blood Group (optional): ");
            }

            Patient patient = new Patient(firstName, lastName, dob, gender, phone, email, address, bloodGroup);

            if (patientService.registerPatient(patient)) {
                System.out.println("Patient registered successfully! Patient ID: " + patient.getPatientId());
            } else {
                System.out.println("Failed to register patient.");
            }
        } catch (Exception e) {
            System.err.println("Error registering patient: " + e.getMessage());
        }
    }

    private static void searchPatientByName() {
        String name = getStringInput("Enter patient name to search: ");
        List<Patient> results = patientService.searchPatientsByName(name);

        if (results.isEmpty()) {
            System.out.println("No patients found with name: " + name);
        } else {
            System.out.println("\n--- Search Results (" + results.size() + " found) ---");
            for (Patient patient : results) {
                System.out.println("ID: " + patient.getPatientId() +
                        " | Name: " + patient.getFullName() +
                        " | Phone: " + patient.getPhone() +
                        " | Blood: " + patient.getBloodGroup());
            }
        }
    }

    private static void filterPatientsByBloodGroup() {
        String bloodGroup = getStringInput("Enter blood group to filter (e.g., A+): ");
        List<Patient> results = patientService.filterPatientsByBloodGroup(bloodGroup);

        if (results.isEmpty()) {
            System.out.println("No patients found with blood group: " + bloodGroup);
        } else {
            System.out.println("\n--- Patients with Blood Group " + bloodGroup.toUpperCase() + " ---");
            for (Patient patient : results) {
                System.out.println(patient);
            }
        }
    }

    private static void viewPatientDetails() {
        int patientId = getIntInput("Enter Patient ID: ");
        Optional<Patient> patient = patientService.getPatientById(patientId);

        // Using lambda with ifPresentOrElse
        patient.ifPresentOrElse(
                p -> {
                    System.out.println("\n====================================");
                    System.out.println("        PATIENT DETAILS");
                    System.out.println("====================================");
                    System.out.println("ID: " + p.getPatientId());
                    System.out.println("Name: " + p.getFullName());
                    System.out.println("Date of Birth: " + p.getDateOfBirth());
                    System.out.println("Gender: " + p.getGender());
                    System.out.println("Phone: " + p.getPhone());
                    System.out.println("Email: " + (p.getEmail() != null ? p.getEmail() : "N/A"));
                    System.out.println("Address: " + (p.getAddress() != null ? p.getAddress() : "N/A"));
                    System.out.println("Blood Group: " + (p.getBloodGroup() != null ? p.getBloodGroup() : "N/A"));
                    System.out.println("Registration Date: " + p.getRegistrationDate());
                    System.out.println("====================================");

                    // Show medical history
                    medicalRecordService.displayPatientMedicalHistory(patientId, p.getFullName());
                },
                () -> System.out.println("Patient not found with ID: " + patientId)
        );
    }

    private static void updatePatient() {
        int patientId = getIntInput("Enter Patient ID to update: ");
        Optional<Patient> optionalPatient = patientService.getPatientById(patientId);

        // Using lambda with ifPresentOrElse
        optionalPatient.ifPresentOrElse(
                patient -> {
                    System.out.println("\n--- Update Patient (Press Enter to keep current value) ---");

                    String input = getStringInput("First Name [" + patient.getFirstName() + "]: ");
                    if (!input.isEmpty()) {
                        if (ValidationUtil.isValidName(input)) {
                            patient.setFirstName(input);
                        } else {
                            System.out.println("Invalid name! Keeping current value.");
                        }
                    }

                    input = getStringInput("Last Name [" + patient.getLastName() + "]: ");
                    if (!input.isEmpty()) {
                        if (ValidationUtil.isValidName(input)) {
                            patient.setLastName(input);
                        } else {
                            System.out.println("Invalid name! Keeping current value.");
                        }
                    }

                    input = getStringInput("Phone [" + patient.getPhone() + "]: ");
                    if (!input.isEmpty()) {
                        if (ValidationUtil.isValidPhone(input)) {
                            patient.setPhone(input);
                        } else {
                            System.out.println("Invalid phone! Keeping current value.");
                        }
                    }

                    input = getStringInput("Email [" + patient.getEmail() + "]: ");
                    if (!input.isEmpty()) {
                        if (ValidationUtil.isValidEmail(input)) {
                            patient.setEmail(input);
                        } else {
                            System.out.println("Invalid email! Keeping current value.");
                        }
                    }

                    input = getStringInput("Address [" + patient.getAddress() + "]: ");
                    if (!input.isEmpty()) {
                        patient.setAddress(input);
                    }

                    if (patientService.updatePatient(patient)) {
                        System.out.println("Patient updated successfully!");
                    } else {
                        System.out.println("Failed to update patient.");
                    }
                },
                () -> System.out.println("Patient not found!")
        );
    }

    private static void deletePatient() {
        int patientId = getIntInput("Enter Patient ID to delete: ");
        Optional<Patient> patient = patientService.getPatientById(patientId);

        // Using lambda with ifPresentOrElse
        patient.ifPresentOrElse(
                p -> {
                    System.out.println("Patient: " + p.getFullName());
                    System.out.print("Are you sure you want to delete this patient? (yes/no): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("yes")) {
                        if (patientService.deletePatient(patientId)) {
                            System.out.println("Patient deleted successfully.");
                        } else {
                            System.out.println("Failed to delete patient.");
                        }
                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                },
                () -> System.out.println("Patient not found with ID: " + patientId)
        );
    }

    // ============ DOCTOR MANAGEMENT ============
    private static void manageDoctors() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Doctor Management -----");
            System.out.println("1. Add New Doctor");
            System.out.println("2. View All Doctors");
            System.out.println("3. Search by Specialization");
            System.out.println("4. Back to Main Menu");

            try {
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        addDoctor();
                        break;
                    case 2:
                        doctorService.displayAllDoctors();
                        break;
                    case 3:
                        searchDoctorBySpecialization();
                        break;
                    case 4:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.err.println("Error in doctor management: " + e.getMessage());
            }
        }
    }

    private static void addDoctor() {
        try {
            System.out.println("\n--- Add New Doctor ---");

            String firstName = getStringInput("First Name: ");
            while (!ValidationUtil.isValidName(firstName)) {
                System.out.println("Invalid name! Use only letters and spaces.");
                firstName = getStringInput("First Name: ");
            }

            String lastName = getStringInput("Last Name: ");
            while (!ValidationUtil.isValidName(lastName)) {
                System.out.println("Invalid name! Use only letters and spaces.");
                lastName = getStringInput("Last Name: ");
            }

            String specialization = getStringInput("Specialization: ");

            String phone = getStringInput("Phone (10 digits): ");
            while (!ValidationUtil.isValidPhone(phone)) {
                System.out.println("Invalid phone number! Must be exactly 10 digits.");
                phone = getStringInput("Phone: ");
            }

            String email = getStringInput("Email: ");
            while (!email.isEmpty() && !ValidationUtil.isValidEmail(email)) {
                System.out.println("Invalid email format!");
                email = getStringInput("Email: ");
            }

            double fee = getDoubleInput("Consultation Fee: ");
            while (!ValidationUtil.isValidConsultationFee(fee)) {
                System.out.println("Invalid fee! Must be between 1 and 10000.");
                fee = getDoubleInput("Consultation Fee: ");
            }

            String availableDays = getStringInput("Available Days (e.g., Monday,Wednesday,Friday): ");
            LocalDate joiningDate = getDateInput("Joining Date (YYYY-MM-DD): ");

            Doctor doctor = new Doctor(firstName, lastName, specialization, phone, email, fee, availableDays, joiningDate);

            if (doctorService.addDoctor(doctor)) {
                System.out.println("Doctor added successfully! Doctor ID: " + doctor.getDoctorId());
            } else {
                System.out.println("Failed to add doctor.");
            }
        } catch (Exception e) {
            System.err.println("Error adding doctor: " + e.getMessage());
        }
    }

    private static void searchDoctorBySpecialization() {
        String specialization = getStringInput("Enter specialization to search: ");
        List<Doctor> results = doctorService.searchDoctorsBySpecialization(specialization);

        if (results.isEmpty()) {
            System.out.println("No doctors found with specialization: " + specialization);
        } else {
            System.out.println("\n--- Doctors in " + specialization + " ---");
            for (Doctor doctor : results) {
                System.out.println("ID: " + doctor.getDoctorId() +
                        " | Name: " + doctor.getFullName() +
                        " | Fee: $" + doctor.getConsultationFee() +
                        " | Available: " + doctor.getAvailableDays());
            }
        }
    }

    // ============ APPOINTMENT MANAGEMENT ============
    private static void manageAppointments() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Appointment Management -----");
            System.out.println("1. Schedule New Appointment");
            System.out.println("2. View Appointment Queue");
            System.out.println("3. Process Next Appointment");
            System.out.println("4. View Patient Appointments");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Back to Main Menu");

            try {
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        scheduleAppointment();
                        break;
                    case 2:
                        appointmentService.displayAppointmentQueue();
                        break;
                    case 3:
                        processNextAppointment();
                        break;
                    case 4:
                        viewPatientAppointments();
                        break;
                    case 5:
                        cancelAppointment();
                        break;
                    case 6:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.err.println("Error in appointment management: " + e.getMessage());
            }
        }
    }

    private static void scheduleAppointment() {
        try {
            System.out.println("\n--- Schedule New Appointment ---");

            // Show available patients
            System.out.println("\nAvailable Patients:");
            patientService.displayAllPatients();

            int patientId = getIntInput("\nEnter Patient ID: ");
            Optional<Patient> patient = patientService.getPatientById(patientId);
            if (!patient.isPresent()) {
                System.out.println("Patient not found!");
                return;
            }
            System.out.println("Selected Patient: " + patient.get().getFullName());

            // Show available doctors
            System.out.println("\nAvailable Doctors:");
            doctorService.displayAllDoctors();

            int doctorId = getIntInput("\nEnter Doctor ID: ");
            Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
            if (!doctor.isPresent()) {
                System.out.println("Doctor not found!");
                return;
            }
            System.out.println("Selected Doctor: " + doctor.get().getFullName());
            System.out.println("Available Days: " + doctor.get().getAvailableDays());

            LocalDate date = getDateInput("Appointment Date (YYYY-MM-DD): ");

            // Validate future date
            if (!ValidationUtil.isFutureDate(date)) {
                System.out.println("Cannot schedule appointment for past date!");
                return;
            }

            LocalTime time = getTimeInput("Appointment Time (HH:MM): ");
            String reason = getStringInput("Reason for visit: ");

            Appointment appointment = new Appointment(patientId, doctorId, date, time, reason);

            if (appointmentService.scheduleAppointment(appointment)) {
                System.out.println("Appointment scheduled successfully!");
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Date: " + date);
                System.out.println("Time: " + time);
            } else {
                System.out.println("Failed to schedule appointment. Doctor might not be available at this time slot.");
            }
        } catch (Exception e) {
            System.err.println("Error scheduling appointment: " + e.getMessage());
        }
    }

    private static void processNextAppointment() {
        System.out.println("\n--- Processing Next Appointment ---");
        Appointment next = appointmentService.peekNextAppointment();

        if (next == null) {
            System.out.println("No appointments in queue!");
            return;
        }

        System.out.println("Next Appointment:");
        System.out.println("Patient ID: " + next.getPatientId());
        System.out.println("Patient Name: " + next.getPatientName());
        System.out.println("Doctor: " + next.getDoctorName());
        System.out.println("Date: " + next.getAppointmentDate());
        System.out.println("Time: " + next.getAppointmentTime());
        System.out.println("Reason: " + next.getReason());

        System.out.print("\nProcess this appointment? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            Appointment processed = appointmentService.processNextAppointment();

            if (processed != null) {
                // After processing, create medical record
                System.out.println("\n--- Create Medical Record ---");
                String diagnosis = getStringInput("Diagnosis: ");
                String prescription = getStringInput("Prescription: ");
                String notes = getStringInput("Additional Notes: ");

                MedicalRecord record = new MedicalRecord(
                        processed.getPatientId(),
                        processed.getDoctorId(),
                        processed.getAppointmentId(),
                        diagnosis,
                        prescription,
                        notes
                );

                if (medicalRecordService.addMedicalRecord(record)) {
                    System.out.println("Medical record created successfully!");
                } else {
                    System.out.println("Failed to create medical record.");
                }
            }
        } else {
            System.out.println("Processing cancelled.");
        }
    }

    private static void viewPatientAppointments() {
        int patientId = getIntInput("Enter Patient ID: ");
        Optional<Patient> patient = patientService.getPatientById(patientId);

        // Using lambda with ifPresentOrElse
        patient.ifPresentOrElse(
                p -> {
                    List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);

                    if (appointments.isEmpty()) {
                        System.out.println("No appointments found for " + p.getFullName());
                    } else {
                        System.out.println("\n--- Appointments for " + p.getFullName() + " ---");
                        for (Appointment appointment : appointments) {
                            System.out.println("\nAppointment ID: " + appointment.getAppointmentId());
                            System.out.println("Doctor: " + appointment.getDoctorName());
                            System.out.println("Date: " + appointment.getAppointmentDate());
                            System.out.println("Time: " + appointment.getAppointmentTime());
                            System.out.println("Status: " + appointment.getStatus());
                            System.out.println("Reason: " + appointment.getReason());
                            System.out.println("------------------------");
                        }
                    }
                },
                () -> System.out.println("Patient not found!")
        );
    }

    private static void cancelAppointment() {
        int appointmentId = getIntInput("Enter Appointment ID to cancel: ");
        System.out.print("Are you sure you want to cancel this appointment? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            if (appointmentService.cancelAppointment(appointmentId)) {
                System.out.println("Appointment cancelled successfully.");
            } else {
                System.out.println("Failed to cancel appointment.");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }

    // ============ MEDICAL RECORDS MANAGEMENT ============
    private static void manageMedicalRecords() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Medical Records Management -----");
            System.out.println("1. View Patient Medical History");
            System.out.println("2. Add Medical Record");
            System.out.println("3. Search Records by Diagnosis");
            System.out.println("4. Back to Main Menu");

            try {
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        viewPatientMedicalHistory();
                        break;
                    case 2:
                        addMedicalRecord();
                        break;
                    case 3:
                        searchRecordsByDiagnosis();
                        break;
                    case 4:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.err.println("Error in medical records management: " + e.getMessage());
            }
        }
    }

    private static void viewPatientMedicalHistory() {
        int patientId = getIntInput("Enter Patient ID: ");
        Optional<Patient> patient = patientService.getPatientById(patientId);

        // Using lambda with ifPresentOrElse
        patient.ifPresentOrElse(
                p -> medicalRecordService.displayPatientMedicalHistory(patientId, p.getFullName()),
                () -> System.out.println("Patient not found!")
        );
    }

    private static void addMedicalRecord() {
        try {
            System.out.println("\n--- Add Medical Record ---");

            int patientId = getIntInput("Enter Patient ID: ");
            Optional<Patient> patient = patientService.getPatientById(patientId);
            if (!patient.isPresent()) {
                System.out.println("Patient not found!");
                return;
            }
            System.out.println("Patient: " + patient.get().getFullName());

            int doctorId = getIntInput("Enter Doctor ID: ");
            Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
            if (!doctor.isPresent()) {
                System.out.println("Doctor not found!");
                return;
            }
            System.out.println("Doctor: " + doctor.get().getFullName());

            int appointmentId = getIntInput("Enter Appointment ID (0 if none): ");

            String diagnosis = getStringInput("Diagnosis: ");
            String prescription = getStringInput("Prescription: ");
            String notes = getStringInput("Notes: ");

            MedicalRecord record = new MedicalRecord(patientId, doctorId, appointmentId, diagnosis, prescription, notes);

            if (medicalRecordService.addMedicalRecord(record)) {
                System.out.println("Medical record added successfully! Record ID: " + record.getRecordId());
            } else {
                System.out.println("Failed to add medical record.");
            }
        } catch (Exception e) {
            System.err.println("Error adding medical record: " + e.getMessage());
        }
    }

    private static void searchRecordsByDiagnosis() {
        int patientId = getIntInput("Enter Patient ID: ");
        Optional<Patient> patient = patientService.getPatientById(patientId);

        // Using lambda with ifPresentOrElse
        patient.ifPresentOrElse(
                p -> {
                    String keyword = getStringInput("Enter diagnosis keyword: ");
                    List<MedicalRecord> results = medicalRecordService.searchRecordsByDiagnosis(patientId, keyword);

                    if (results.isEmpty()) {
                        System.out.println("No records found matching: " + keyword);
                    } else {
                        System.out.println("\n--- Matching Medical Records for " + p.getFullName() + " ---");
                        for (MedicalRecord record : results) {
                            System.out.println("\nRecord ID: " + record.getRecordId());
                            System.out.println("Date: " + record.getRecordDate());
                            System.out.println("Doctor: " + record.getDoctorName());
                            System.out.println("Diagnosis: " + record.getDiagnosis());
                            System.out.println("Prescription: " + record.getPrescription());
                            System.out.println("Notes: " + record.getNotes());
                            System.out.println("-----------------------------------");
                        }
                    }
                },
                () -> System.out.println("Patient not found!")
        );
    }

    // ============ SYSTEM OVERVIEW ============
    private static void displaySystemOverview() {
        System.out.println("\n=========================================");
        System.out.println("          SYSTEM OVERVIEW");
        System.out.println("=========================================");

        // Patient statistics
        List<Patient> patients = patientService.getAllPatients();
        System.out.println("\n--- Patient Statistics ---");
        System.out.println("Total Patients: " + patients.size());

        // Count by gender
        int maleCount = 0;
        int femaleCount = 0;
        int otherCount = 0;
        for (Patient patient : patients) {
            String gender = patient.getGender().toLowerCase();
            if (gender.equals("male")) {
                maleCount++;
            } else if (gender.equals("female")) {
                femaleCount++;
            } else {
                otherCount++;
            }
        }
        System.out.println("Male: " + maleCount + " | Female: " + femaleCount + " | Other: " + otherCount);

        // Doctor statistics
        List<Doctor> doctors = doctorService.getAllDoctors();
        System.out.println("\n--- Doctor Statistics ---");
        System.out.println("Total Doctors: " + doctors.size());

        // Count by specialization
        for (Doctor doctor : doctors) {
            System.out.println("• " + doctor.getFullName() + " - " + doctor.getSpecialization());
        }

        // Appointment statistics
        LinkedList<Appointment> appointmentQueue = appointmentService.getAppointmentQueue();
        System.out.println("\n--- Appointment Statistics ---");
        System.out.println("Pending Appointments: " + appointmentQueue.size());

        if (appointmentQueue.size() > 0) {
            System.out.println("\nUpcoming Appointments:");
            for (Appointment apt : appointmentQueue) {
                System.out.println("• " + apt.getPatientName() + " with " +
                        apt.getDoctorName() + " on " + apt.getAppointmentDate());
            }
        }

        System.out.println("\n=========================================");
    }
}