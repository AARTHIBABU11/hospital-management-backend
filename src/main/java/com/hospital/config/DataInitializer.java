package com.hospital.config;

import com.hospital.entity.*;
import com.hospital.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final BillRepository billRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           DepartmentRepository departmentRepository,
                           DoctorRepository doctorRepository,
                           PatientRepository patientRepository,
                           AppointmentRepository appointmentRepository,
                           MedicalRecordRepository medicalRecordRepository,
                           PrescriptionRepository prescriptionRepository,
                           BillRepository billRepository,
                           NotificationRepository notificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.billRepository = billRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        // 1. Admin
        User admin = new User(
                "admin@hospital.com",
                passwordEncoder.encode("admin123"),
                "Dr. Administrator",
                "+1 (555) 019-2831",
                Role.ROLE_ADMIN
        );
        userRepository.save(admin);

        // 2. Receptionist
        User receptionist = new User(
                "reception@hospital.com",
                passwordEncoder.encode("reception123"),
                "Emily Watson",
                "+1 (555) 014-9922",
                Role.ROLE_RECEPTIONIST
        );
        userRepository.save(receptionist);

        // 3. Departments
        Department cardio = departmentRepository.save(new Department("Cardiology", "Comprehensive care for cardiovascular diseases, coronary interventions, and heart failure.", "Heart", "Dr. Rajesh Sharma"));
        Department neuro = departmentRepository.save(new Department("Neurology", "Advanced brain, spinal cord, and neuromuscular diagnostic and therapeutic care.", "Brain", "Dr. Priya Patel"));
        Department ortho = departmentRepository.save(new Department("Orthopedics", "Specialized bone, joint replacements, trauma surgery, and sports medicine.", "Bone", "Dr. Vikram Sethi"));
        Department peds = departmentRepository.save(new Department("Pediatrics", "Gentle, expert infant, child, and adolescent healthcare with specialized neonatal ICU.", "Baby", "Dr. Ananya Roy"));
        Department derm = departmentRepository.save(new Department("Dermatology", "Clinical dermatology, skin cancer screenings, allergy testing, and cosmetic care.", "Sparkles", "Dr. Meera Nambiar"));
        Department genMed = departmentRepository.save(new Department("General Medicine", "Primary care, disease prevention, infectious diseases, and chronic condition management.", "Stethoscope", "Dr. Arvind Gupta"));
        Department gyn = departmentRepository.save(new Department("Gynecology & Obstetrics", "Women's reproductive health, prenatal diagnostics, and maternal-fetal care.", "Activity", "Dr. Sunita Rao"));
        Department ent = departmentRepository.save(new Department("ENT (Otolaryngology)", "Ear, nose, throat, head and neck micro-surgical solutions.", "Ear", "Dr. Manoj Verma"));

        // 4. Doctors
        Doctor doc1 = createDoctor("Dr. Rajesh Sharma", "dr.sharma@hospital.com", "+1 (555) 101-2001", "Male", cardio,
                "Cardiologist", "MD, DM (Cardiology), FACC", 16, 120.0, "Mon, Wed, Fri", "09:00 AM - 01:00 PM",
                "Senior Consultant Cardiologist with 16+ years experience in interventional cardiology and heart failure management.",
                "https://images.unsplash.com/photo-1622253692010-333f2da6031d?auto=format&fit=crop&q=80&w=400");

        Doctor doc2 = createDoctor("Dr. Priya Patel", "dr.patel@hospital.com", "+1 (555) 102-2002", "Female", neuro,
                "Neurologist", "MBBS, DM (Neurology)", 12, 110.0, "Tue, Thu, Sat", "10:00 AM - 02:00 PM",
                "Specialist in stroke interventions, epilepsy management, and neurological rehabilitation.",
                "https://images.unsplash.com/photo-1594824813576-234e40237c17?auto=format&fit=crop&q=80&w=400");

        Doctor doc3 = createDoctor("Dr. Vikram Sethi", "dr.vikram@hospital.com", "+1 (555) 103-2003", "Male", ortho,
                "Orthopedic Surgeon", "MS (Ortho), Fellowship Joint Replacement", 14, 130.0, "Mon, Tue, Thu", "11:00 AM - 04:00 PM",
                "Pioneer in minimally invasive robotic knee and hip replacement surgeries.",
                "https://images.unsplash.com/photo-1537368910025-700350fe46c7?auto=format&fit=crop&q=80&w=400");

        Doctor doc4 = createDoctor("Dr. Ananya Roy", "dr.ananya@hospital.com", "+1 (555) 104-2004", "Female", peds,
                "Pediatrician", "MD (Pediatrics), DNB", 9, 90.0, "Mon - Fri", "09:30 AM - 01:30 PM",
                "Compassionate child care specialist focusing on developmental pediatrics and immunizations.",
                "https://images.unsplash.com/photo-1559839734-2b71ea197ec2?auto=format&fit=crop&q=80&w=400");

        Doctor doc5 = createDoctor("Dr. Meera Nambiar", "dr.meera@hospital.com", "+1 (555) 105-2005", "Female", derm,
                "Dermatologist", "MD (Dermatology, Venereology & Leprosy)", 10, 85.0, "Wed, Fri, Sat", "02:00 PM - 06:00 PM",
                "Expert in clinical dermatology, autoimmune dermatoses, and advanced laser therapies.",
                "https://images.unsplash.com/photo-1651008376811-b90baee60c1f?auto=format&fit=crop&q=80&w=400");

        Doctor doc6 = createDoctor("Dr. Arvind Gupta", "dr.gupta@hospital.com", "+1 (555) 106-2006", "Male", genMed,
                "Internal Medicine Specialist", "MD (General Medicine)", 18, 95.0, "Mon - Sat", "08:30 AM - 12:30 PM",
                "Comprehensive management of multi-system illnesses, hypertension, and metabolic syndrome.",
                "https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?auto=format&fit=crop&q=80&w=400");

        // 5. Patients
        Patient pat1 = createPatient("John Doe", "patient@hospital.com", "+1 (555) 201-3001", LocalDate.of(1988, 5, 14), 38, "Male", "O+",
                "742 Evergreen Terrace, Springfield", "+1 (555) 999-1122 (Wife - Mary)", "Penicillin", "Mild Hypertension (managed)");

        Patient pat2 = createPatient("Sarah Jenkins", "sarah.j@example.com", "+1 (555) 202-3002", LocalDate.of(1995, 8, 22), 31, "Female", "A+",
                "108 West 4th St, Metro City", "+1 (555) 888-2233 (Father)", "Sulfa drugs", "Seasonal Asthma");

        Patient pat3 = createPatient("Robert King", "robert.k@example.com", "+1 (555) 203-3003", LocalDate.of(1972, 11, 3), 53, "Male", "B+",
                "456 Oakwood Ave, Riverdale", "+1 (555) 777-3344 (Daughter)", "None known", "Type 2 Diabetes Mellitus");

        Patient pat4 = createPatient("Priya Sharma", "priya.s@example.com", "+1 (555) 204-3004", LocalDate.of(2001, 3, 19), 25, "Female", "AB+",
                "89 Pine Road, Fairview", "+1 (555) 666-4455 (Mother)", "Dust mites, Pollen", "None");

        // 6. Appointments
        Appointment app1 = new Appointment();
        app1.setPatient(pat1);
        app1.setDoctor(doc1);
        app1.setDepartment(cardio);
        app1.setAppointmentDate(LocalDate.now());
        app1.setAppointmentTime("10:00 AM");
        app1.setReason("Quarterly cardiovascular checkup & blood pressure monitoring");
        app1.setStatus(AppointmentStatus.CONFIRMED);
        app1.setNotes("Patient reports occasional morning palpitations");
        appointmentRepository.save(app1);

        Appointment app2 = new Appointment();
        app2.setPatient(pat2);
        app2.setDoctor(doc4);
        app2.setDepartment(peds);
        app2.setAppointmentDate(LocalDate.now());
        app2.setAppointmentTime("11:30 AM");
        app2.setReason("Childhood immunization booster consultation");
        app2.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(app2);

        Appointment app3 = new Appointment();
        app3.setPatient(pat3);
        app3.setDoctor(doc6);
        app3.setDepartment(genMed);
        app3.setAppointmentDate(LocalDate.now());
        app3.setAppointmentTime("09:00 AM");
        app3.setReason("Fasting blood glucose routine review");
        app3.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(app3);

        Appointment app4 = new Appointment();
        app4.setPatient(pat1);
        app4.setDoctor(doc3);
        app4.setDepartment(ortho);
        app4.setAppointmentDate(LocalDate.now().plusDays(2));
        app4.setAppointmentTime("02:00 PM");
        app4.setReason("Right knee stiffness after jogging");
        app4.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(app4);

        // 7. Medical Records
        MedicalRecord mr1 = new MedicalRecord();
        mr1.setPatient(pat1);
        mr1.setDoctor(doc1);
        mr1.setAppointment(app1);
        mr1.setVisitDate(LocalDate.now().minusDays(30));
        mr1.setSymptoms("Mild exertional shortness of breath, elevated blood pressure 138/88 mmHg");
        mr1.setDiagnosis("Stage 1 Essential Hypertension");
        mr1.setClinicalNotes("ECG reveals normal sinus rhythm. Advised dietary sodium restriction and 30 mins brisk walking.");
        mr1.setTreatment("Lifestyle adjustments + Telmisartan 40mg daily.");
        mr1.setFollowUpDate(LocalDate.now().plusMonths(1));
        medicalRecordRepository.save(mr1);

        MedicalRecord mr2 = new MedicalRecord();
        mr2.setPatient(pat3);
        mr2.setDoctor(doc6);
        mr2.setAppointment(app3);
        mr2.setVisitDate(LocalDate.now());
        mr2.setSymptoms("Mild fatigue, HbA1c 6.8%");
        mr2.setDiagnosis("Type 2 Diabetes Mellitus - Well Controlled");
        mr2.setClinicalNotes("Renal panel normal. Continue current regimen.");
        mr2.setTreatment("Metformin 500mg BD after meals. Moderate aerobic exercise.");
        mr2.setFollowUpDate(LocalDate.now().plusMonths(3));
        medicalRecordRepository.save(mr2);

        // 8. Prescriptions
        Prescription p1 = new Prescription();
        p1.setPatient(pat1);
        p1.setDoctor(doc1);
        p1.setAppointment(app1);
        p1.setPrescriptionDate(LocalDate.now().minusDays(30));
        p1.setDiagnosis("Stage 1 Hypertension");
        p1.setInstructions("Take early morning after breakfast. Monitor BP weekly.");
        p1.addItem(new PrescriptionItem("Telmisartan", "40mg", "Once daily (Morning)", "30 days", "Take with water after breakfast"));
        p1.addItem(new PrescriptionItem("Atorvastatin", "10mg", "Once daily (Night)", "30 days", "Take before sleep"));
        prescriptionRepository.save(p1);

        Prescription p2 = new Prescription();
        p2.setPatient(pat3);
        p2.setDoctor(doc6);
        p2.setAppointment(app3);
        p2.setPrescriptionDate(LocalDate.now());
        p2.setDiagnosis("Type 2 Diabetes Mellitus");
        p2.setInstructions("Maintain regular meal timings. Do not skip doses.");
        p2.addItem(new PrescriptionItem("Metformin HCl", "500mg", "Twice daily (1-0-1)", "60 days", "Take with or immediately after meals"));
        p2.addItem(new PrescriptionItem("Multivitamin B-Complex", "1 tablet", "Once daily", "30 days", "Take in the morning"));
        prescriptionRepository.save(p2);

        // 9. Bills
        Bill bill1 = new Bill();
        bill1.setInvoiceNumber("INV-882194");
        bill1.setPatient(pat1);
        bill1.setAppointment(app1);
        bill1.setConsultationFee(120.0);
        bill1.setMedicineCharges(45.0);
        bill1.setLabCharges(60.0);
        bill1.setOtherCharges(10.0);
        bill1.calculateTotal();
        bill1.setPaymentStatus(PaymentStatus.PAID);
        bill1.setPaymentMethod(PaymentMethod.CARD);
        bill1.setBillDate(LocalDate.now().minusDays(30));
        bill1.setNotes("Comprehensive executive cardiac panel");
        billRepository.save(bill1);

        Bill bill2 = new Bill();
        bill2.setInvoiceNumber("INV-934012");
        bill2.setPatient(pat3);
        bill2.setAppointment(app3);
        bill2.setConsultationFee(95.0);
        bill2.setMedicineCharges(30.0);
        bill2.setLabCharges(50.0);
        bill2.setOtherCharges(0.0);
        bill2.calculateTotal();
        bill2.setPaymentStatus(PaymentStatus.PENDING);
        bill2.setBillDate(LocalDate.now());
        bill2.setNotes("Routine diabetic profile & consultation");
        billRepository.save(bill2);

        // 10. Notifications
        notificationRepository.save(new Notification("patient@hospital.com", "Upcoming Appointment", "Reminder: You have an appointment with Dr. Rajesh Sharma today at 10:00 AM.", "APPOINTMENT"));
        notificationRepository.save(new Notification("dr.sharma@hospital.com", "Patient Checked In", "John Doe has confirmed arrival for his 10:00 AM consultation.", "APPOINTMENT"));
        notificationRepository.save(new Notification("admin@hospital.com", "Daily System Report", "Daily clinic operations initiated. 4 new bookings processed.", "GENERAL"));
    }

    private Doctor createDoctor(String name, String email, String phone, String gender, Department department,
                                 String spec, String qual, int exp, double fee, String days, String time, String bio, String avatar) {
        User user = new User(email, passwordEncoder.encode("doctor123"), name, phone, Role.ROLE_DOCTOR);
        user = userRepository.save(user);

        Doctor doc = new Doctor();
        doc.setUser(user);
        doc.setName(name);
        doc.setEmail(email);
        doc.setPhone(phone);
        doc.setGender(gender);
        doc.setDepartment(department);
        doc.setSpecialization(spec);
        doc.setQualification(qual);
        doc.setExperienceYears(exp);
        doc.setConsultationFee(fee);
        doc.setAvailableDays(days);
        doc.setAvailableTime(time);
        doc.setBio(bio);
        doc.setAvatarUrl(avatar);
        doc.setStatus("Active");
        return doctorRepository.save(doc);
    }

    private Patient createPatient(String name, String email, String phone, LocalDate dob, int age, String gender,
                                   String bloodGroup, String address, String emergency, String allergies, String history) {
        User user = new User(email, passwordEncoder.encode("patient123"), name, phone, Role.ROLE_PATIENT);
        user = userRepository.save(user);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setName(name);
        patient.setEmail(email);
        patient.setPhone(phone);
        patient.setDob(dob);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setBloodGroup(bloodGroup);
        patient.setAddress(address);
        patient.setEmergencyContact(emergency);
        patient.setAllergies(allergies);
        patient.setMedicalHistory(history);
        patient.setStatus("Active");
        return patientRepository.save(patient);
    }
}
