package util;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionManager {

    // Inner class for prescription items
    public static class PrescriptionItem {
        private String medicineName;
        private String dosage;
        private String frequency;
        private String duration;

        public PrescriptionItem(String medicineName, String dosage, String frequency, String duration) {
            this.medicineName = medicineName;
            this.dosage = dosage;
            this.frequency = frequency;
            this.duration = duration;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public String getDosage() {
            return dosage;
        }

        public String getFrequency() {
            return frequency;
        }

        public String getDuration() {
            return duration;
        }

        @Override
        public String toString() {
            return medicineName + " - " + dosage + " - " + frequency + " - " + duration;
        }
    }

    // Parse prescription string into list of items
    public static List<PrescriptionItem> parsePrescription(String prescription) {
        List<PrescriptionItem> items = new ArrayList<>();
        if (prescription == null || prescription.isEmpty()) {
            return items;
        }

        String[] lines = prescription.split("\n");
        for (String line : lines) {
            // Check if line starts with number followed by dot and space
            if (line.length() > 2) {
                // Check if first character is digit
                if (line.charAt(0) >= '1' && line.charAt(0) <= '9') {
                    // Check for dot and space after number
                    int dotIndex = line.indexOf(". ");
                    if (dotIndex > 0) {
                        // Extract the part after "N. "
                        String content = line.substring(dotIndex + 2);
                        String[] parts = content.split(" - ");
                        if (parts.length == 4) {
                            items.add(new PrescriptionItem(parts[0].trim(), parts[1].trim(),
                                    parts[2].trim(), parts[3].trim()));
                        }
                    }
                }
            }
        }
        return items;
    }

    // Format prescription for display
    public static String formatPrescription(String prescription) {
        if (prescription == null || prescription.isEmpty()) {
            return "No prescription";
        }
        return prescription;
    }

    // Create prescription string from items
    public static String createPrescription(List<PrescriptionItem> items) {
        StringBuilder prescription = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            prescription.append(i + 1).append(". ").append(items.get(i).toString());
            if (i < items.size() - 1) {
                prescription.append("\n");
            }
        }
        return prescription.toString();
    }

    // Display prescription in readable format
    public static void displayPrescription(String prescription) {
        System.out.println("\n--- Prescription ---");
        if (prescription == null || prescription.isEmpty()) {
            System.out.println("No medications prescribed.");
        } else {
            List<PrescriptionItem> items = parsePrescription(prescription);
            if (items.isEmpty()) {
                System.out.println(prescription);
            } else {
                System.out.println("Medications:");
                for (PrescriptionItem item : items) {
                    System.out.println("  • " + item.getMedicineName());
                    System.out.println("    Dosage: " + item.getDosage());
                    System.out.println("    Frequency: " + item.getFrequency());
                    System.out.println("    Duration: " + item.getDuration());
                }
            }
        }
    }

    // Validate prescription format
    public static boolean isValidPrescriptionFormat(String prescription) {
        if (prescription == null || prescription.isEmpty()) {
            return false;
        }

        // Check if prescription has any content
        String[] lines = prescription.split("\n");
        for (String line : lines) {
            // Check if line starts with number followed by dot
            if (line.length() > 2) {
                if (line.charAt(0) >= '1' && line.charAt(0) <= '9') {
                    int dotIndex = line.indexOf(". ");
                    if (dotIndex > 0) {
                        return true;
                    }
                }
            }
        }

        // If no structured format found, still accept as free-text
        return prescription.length() > 0;
    }
}