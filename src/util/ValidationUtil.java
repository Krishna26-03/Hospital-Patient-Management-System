package util;

import java.time.LocalDate;

public class ValidationUtil {

    // Validate phone number (10 digits)
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        // Check if length is exactly 10
        if (phone.length() != 10) {
            return false;
        }

        // Check if all characters are digits
        for (int i = 0; i < phone.length(); i++) {
            char ch = phone.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }

        return true;
    }

    // Validate email
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return true; // Optional field
        }

        // Check if email contains @
        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return false;
        }

        // Check if @ is not first or last character
        if (atIndex == 0 || atIndex == email.length() - 1) {
            return false;
        }

        // Check if there's a dot after @
        int dotIndex = email.indexOf('.', atIndex);
        if (dotIndex == -1) {
            return false;
        }

        // Check if dot is not immediately after @ and not last character
        if (dotIndex == atIndex + 1 || dotIndex == email.length() - 1) {
            return false;
        }

        // Check for spaces
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == ' ') {
                return false;
            }
        }

        return true;
    }

    // Validate blood group
    public static boolean isValidBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.isEmpty()) {
            return true; // Optional field
        }

        String group = bloodGroup.toUpperCase();
        String[] validGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

        for (String validGroup : validGroups) {
            if (group.equals(validGroup)) {
                return true;
            }
        }

        return false;
    }

    // Validate gender
    public static boolean isValidGender(String gender) {
        if (gender == null || gender.isEmpty()) {
            return false;
        }

        String genderLower = gender.toLowerCase();
        return genderLower.equals("male") ||
                genderLower.equals("female") ||
                genderLower.equals("other");
    }

    // Validate consultation fee
    public static boolean isValidConsultationFee(double fee) {
        return fee > 0 && fee <= 10000;
    }

    // Validate name (only letters and spaces)
    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            // Check if character is letter or space
            if (!((ch >= 'A' && ch <= 'Z') ||
                    (ch >= 'a' && ch <= 'z') ||
                    ch == ' ')) {
                return false;
            }
        }

        return true;
    }

    // Validate date is not in past
    public static boolean isFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return date.isEqual(today) || date.isAfter(today);
    }

    // Validate time format (HH:MM)
    public static boolean isValidTimeFormat(String time) {
        if (time == null || time.isEmpty()) {
            return false;
        }

        // Check length
        if (time.length() != 5) {
            return false;
        }

        // Check colon position
        if (time.charAt(2) != ':') {
            return false;
        }

        // Check hours
        char h1 = time.charAt(0);
        char h2 = time.charAt(1);
        if (!(h1 >= '0' && h1 <= '2') || !(h2 >= '0' && h2 <= '9')) {
            return false;
        }

        // Validate hours range (00-23)
        int hours = (h1 - '0') * 10 + (h2 - '0');
        if (hours < 0 || hours > 23) {
            return false;
        }

        // Check minutes
        char m1 = time.charAt(3);
        char m2 = time.charAt(4);
        if (!(m1 >= '0' && m1 <= '5') || !(m2 >= '0' && m2 <= '9')) {
            return false;
        }

        return true;
    }
}