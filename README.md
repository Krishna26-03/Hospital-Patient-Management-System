# 🏥 Hospital Patient Management System

A console-based **Hospital Patient Management System** built with **Java**, **JDBC**, and **MySQL** that handles patient records, doctor management, appointments, medical records, and prescriptions.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java | Core application logic |
| JDBC | Database connectivity |
| MySQL | Data storage |
| Collections Framework | Data handling and reporting |

---

## ✨ Features

- ✅ Patient registration & management
- ✅ Doctor management
- ✅ Appointment scheduling
- ✅ Medical records management
- ✅ Prescription management
- ✅ Input validation

---

## 📁 Project Structure

```
HospitalPatientManagementSystem/
│
├── .gitignore
├── README.md
│
└── src/
    │
    ├── model/                        ← Data classes (what things are)
    │   ├── Patient.java                   ← Patient entity
    │   ├── Doctor.java                    ← Doctor entity
    │   ├── Appointment.java               ← Appointment entity
    │   └── MedicalRecord.java             ← Medical record entity
    │
    ├── dao/                          ← Database layer (JDBC queries)
    │   ├── PatientDAO.java                ← CRUD for patients
    │   ├── DoctorDAO.java                 ← CRUD for doctors
    │   ├── AppointmentDAO.java            ← CRUD for appointments
    │   └── MedicalRecordDAO.java          ← CRUD for medical records
    │
    ├── service/                      ← Business logic layer
    │   ├── PatientService.java            ← Patient business rules
    │   ├── DoctorService.java             ← Doctor business rules
    │   ├── AppointmentService.java        ← Appointment scheduling logic
    │   └── MedicalRecordService.java      ← Medical record logic
    │
    ├── util/                         ← Helper classes
    │   ├── DatabaseConnection.java        ← DB connection handler
    │   ├── ValidationUtil.java            ← Input validation
    │   └── PrescriptionManager.java       ← Prescription handling
    │
    └── Main.java                     ← Entry point + console menu
```

---

## 🗄️ Database Schema

```
patients                    doctors
────────────────────        ────────────────────
patient_id      PK          doctor_id       PK
first_name                  first_name
last_name                   last_name
email                       email
phone                       phone
date_of_birth               specialization
gender                      department
address                     experience
blood_group                 is_available
registered_on

appointments                         medical_records
─────────────────────────────────    ──────────────────────────
appointment_id  PK                   record_id       PK
patient_id      FK → patients        patient_id      FK → patients
doctor_id       FK → doctors         doctor_id       FK → doctors
appointment_date                     appointment_id  FK → appointments
appointment_time                     diagnosis
status  (PENDING/CONFIRMED/DONE)     treatment
remarks                              prescription
                                     visit_date
                                     notes
```

---

## ⚙️ Setup Instructions

### Step 1 — Database Setup
Open **MySQL Workbench** or terminal and run:
```sql
source schema.sql;
source seed_data.sql;
```

### Step 2 — Configure DB Credentials
Open `src/util/DatabaseConnection.java` and update:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/hospital_management";
private static final String USER     = "root";
private static final String PASSWORD = "your_password";  // ← change this
```

### Step 3 — Add MySQL JDBC Driver
Download `mysql-connector-j-8.x.x.jar` and add to classpath:

**IntelliJ IDEA:**
```
File → Project Structure → Libraries → + → JAR → Select the .jar file
```

### Step 4 — Run the App
Run `Main.java` as the main class.

---

## 🖥️ Console Menu

```
╔══════════════════════════════════════╗
║   Hospital Patient Management System ║
╠══════════════════════════════════════╣
║  1. Patient Management               ║
║  2. Doctor Management                ║
║  3. Appointment Management           ║
║  4. Medical Records                  ║
║  5. Prescription Management          ║
║  0. Exit                             ║
╚══════════════════════════════════════╝
```

### Patient Management
- Register new patient
- View all patients
- Search patient by name/ID
- Update patient details
- View patient history

### Doctor Management
- Add new doctor
- View all doctors
- Search by specialization
- Update doctor details
- Check doctor availability

### Appointment Management
- Schedule new appointment
- View appointments by patient
- View appointments by doctor
- Update appointment status
- Cancel appointment

### Medical Records
- Add medical record
- View patient medical history
- Update diagnosis/treatment
- View records by doctor

### Prescription Management
- Add prescription
- View prescriptions by patient
- Update prescription details

---

## 🏗️ Key Design Concepts

| Concept | Where Used |
|---------|-----------|
| **DAO Pattern** | Separates DB/SQL logic from business logic |
| **Service Layer** | Business rules in `*Service` classes |
| **Encapsulation** | All fields `private`, accessed via getters/setters |
| **Validation** | `ValidationUtil` handles all input validation |
| **Singleton** | `DatabaseConnection` — single shared DB connection |
| **Soft Delete** | Records deactivated, not permanently deleted |

---

## 🔗 Entity Relationships

```
Patient ──────┐
              ├──→ Appointment ──→ MedicalRecord
Doctor  ──────┘         │
                        └──→ Prescription
```

- One **Patient** can have many **Appointments**
- One **Doctor** can have many **Appointments**
- One **Appointment** has one **Medical Record**
- One **Medical Record** can have a **Prescription**

---

## 🔍 Validation Rules (ValidationUtil)

- ✅ Email format validation
- ✅ Phone number validation
- ✅ Date format validation
- ✅ Empty/null field checks
- ✅ Blood group validation

---

## 👨‍💻 Author

**Krishna Patel**
Int. B.Tech-M.Tech CSE | Java Developer
GitHub: https://github.com/Krishna26-03

---

## 📄 License

This project is for educational purposes.
