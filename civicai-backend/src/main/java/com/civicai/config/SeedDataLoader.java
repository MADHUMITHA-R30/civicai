package com.civicai.config;

import com.civicai.entity.*;
import com.civicai.enums.ComplaintStatus;
import com.civicai.enums.PriorityLevel;
import com.civicai.enums.Role;
import com.civicai.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SeedDataLoader implements CommandLineRunner {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private IssueCategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private AiAnalysisRepository aiAnalysisRepository;
    @Autowired private ResolutionRepository resolutionRepository;
    @Autowired private ResolutionMediaRepository resolutionMediaRepository;
    @Autowired private ComplaintMediaRepository complaintMediaRepository;
    @Autowired private ComplaintClusterRepository clusterRepository;
    @Autowired private ClusterMemberRepository clusterMemberRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() > 0) {
            return; // Seed data already initialized
        }

        // 1. Departments
        Department elec = departmentRepository.save(new Department("Electrical", "ELEC", "Manages street lights, transformers, and electrical infrastructure."));
        Department roads = departmentRepository.save(new Department("Roads & Infrastructure", "ROADS", "Handles potholes, road maintenance, bridges, and pavements."));
        Department sanitation = departmentRepository.save(new Department("Sanitation & Waste Management", "SAN", "Oversees garbage collection, public bins, and illegal dumping."));
        Department water = departmentRepository.save(new Department("Water & Drainage", "WATER", "Manages water supply pipelines, drainage clogging, and stagnant water."));
        Department env = departmentRepository.save(new Department("Environment & Pollution", "ENV", "Handles noise pollution, smoke emissions, and public greenery."));
        Department safety = departmentRepository.save(new Department("Public Safety", "SAFETY", "Manages traffic signals, road safety hazards, and public risk areas."));
        Department general = departmentRepository.save(new Department("General Civic Services", "GEN", "General public civic inquiries and unclassified issues."));

        // 2. 15 Issue Categories
        IssueCategory cat1 = categoryRepository.save(new IssueCategory("Street Light Not Working", "Broken, non-functional street lamps or dark lanes.", elec));
        IssueCategory cat2 = categoryRepository.save(new IssueCategory("Road Damage / Pothole", "Potholes, broken asphalt, damaged pavements.", roads));
        IssueCategory cat3 = categoryRepository.save(new IssueCategory("Garbage / Waste Problem", "Overflowing waste bins, uncollected trash.", sanitation));
        IssueCategory cat4 = categoryRepository.save(new IssueCategory("Electricity Problem", "Power cuts, dangerous hanging wires, transformer sparks.", elec));
        IssueCategory cat5 = categoryRepository.save(new IssueCategory("Stagnant Water", "Accumulated rainwater or dirty standing water.", water));
        IssueCategory cat6 = categoryRepository.save(new IssueCategory("Drainage / Sewage Problem", "Clogged drains, overflowing sewage, foul water.", water));
        IssueCategory cat7 = categoryRepository.save(new IssueCategory("Water Supply Problem", "No water supply, low pressure, or contaminated water.", water));
        IssueCategory cat8 = categoryRepository.save(new IssueCategory("Environmental Pollution", "Factory smoke, chemical dumping, air pollution.", env));
        IssueCategory cat9 = categoryRepository.save(new IssueCategory("Illegal Dumping", "Unauthorized dumping of debris or toxic waste.", sanitation));
        IssueCategory cat10 = categoryRepository.save(new IssueCategory("Public Toilet Problem", "Unsanitary or non-functional public restrooms.", sanitation));
        IssueCategory cat11 = categoryRepository.save(new IssueCategory("Traffic / Road Safety Problem", "Broken traffic light, missing signboards, high accident zone.", safety));
        IssueCategory cat12 = categoryRepository.save(new IssueCategory("Tree / Public Greenery Problem", "Fallen tree branches, overgrown bushes blocking road.", env));
        IssueCategory cat13 = categoryRepository.save(new IssueCategory("Public Infrastructure Damage", "Damaged bus stops, broken handrails, park benches.", roads));
        IssueCategory cat14 = categoryRepository.save(new IssueCategory("Noise Pollution", "Excessive loud speakers or industrial noise after hours.", env));
        IssueCategory cat15 = categoryRepository.save(new IssueCategory("Other", "General civic issues not listed above.", general));

        // 3. Demo Accounts
        String pass = passwordEncoder.encode("admin123");
        User superAdmin = userRepository.save(new User("System Super Admin", "admin@example.com", "9900000000", pass, Role.ROLE_SUPER_ADMIN));

        User elecAdmin = new User("Electrical Official", "electrical@example.com", "9900000001", pass, Role.ROLE_DEPARTMENT_ADMIN);
        elecAdmin.setDepartment(elec);
        userRepository.save(elecAdmin);

        User roadsAdmin = new User("Roads Official", "roads@example.com", "9900000002", pass, Role.ROLE_DEPARTMENT_ADMIN);
        roadsAdmin.setDepartment(roads);
        userRepository.save(roadsAdmin);

        User waterAdmin = new User("Water Official", "water@example.com", "9900000003", pass, Role.ROLE_DEPARTMENT_ADMIN);
        waterAdmin.setDepartment(water);
        userRepository.save(waterAdmin);

        User sanAdmin = new User("Sanitation Official", "sanitation@example.com", "9900000004", pass, Role.ROLE_DEPARTMENT_ADMIN);
        sanAdmin.setDepartment(sanitation);
        userRepository.save(sanAdmin);

        User envAdmin = new User("Environment Official", "environment@example.com", "9900000005", pass, Role.ROLE_DEPARTMENT_ADMIN);
        envAdmin.setDepartment(env);
        userRepository.save(envAdmin);

        String citizenPass = passwordEncoder.encode("citizen123");
        User citizen1 = userRepository.save(new User("Ramesh Kumar", "citizen@example.com", "9876543210", citizenPass, Role.ROLE_CITIZEN));
        User citizen2 = userRepository.save(new User("Priya Sharma", "citizen2@example.com", "9876543211", citizenPass, Role.ROLE_CITIZEN));
        User citizen3 = userRepository.save(new User("Anand Patel", "citizen3@example.com", "9876543212", citizenPass, Role.ROLE_CITIZEN));

        // 4. Pre-populated Seed Complaints across statuses
        
        // Complaint 1: Resolved Streetlight with Before/After Evidence
        Complaint c1 = new Complaint();
        c1.setComplaintNumber("CIV-2026-00124");
        c1.setCitizen(citizen1);
        c1.setCategory(cat1);
        c1.setDepartment(elec);
        c1.setDescription("Street light near the school entrance on Main Road has not been working for three days. Danger at night for kids.");
        c1.setAddress("Main Bus Stop, Near RMK School Entrance, Sector 3");
        c1.setContactNumber("9876543210");
        c1.setLatitude(13.0827);
        c1.setLongitude(80.2707);
        c1.setPriority(PriorityLevel.HIGH);
        c1.setPriorityScore(85);
        c1.setStatus(ComplaintStatus.RESOLVED);
        c1.setAiConfidence(0.95);
        c1.setIsVoiceReport(true);
        complaintRepository.save(c1);

        // Before Media
        complaintMediaRepository.save(new ComplaintMedia(c1, "IMAGE", "https://images.unsplash.com/photo-1517646287270-a5a9ca602e5c?w=600&auto=format&fit=crop"));

        // Resolution Record
        Resolution r1 = new Resolution(c1, "Electrical department team inspected the street light fixture, replaced broken 100W LED lamp bulb, and re-secured underground wiring.", elecAdmin);
        r1.setCitizenVerificationStatus("VERIFIED");
        r1.setCitizenFeedback("Great work! Light is working brightly now. Thank you!");
        resolutionRepository.save(r1);

        // After Media
        resolutionMediaRepository.save(new ResolutionMedia(r1, "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop"));

        // AI Analysis 1
        AiAnalysis ai1 = new AiAnalysis();
        ai1.setComplaint(c1);
        ai1.setExtractedCategory("Street Light Not Working");
        ai1.setExtractedAddress("Main Bus Stop, RMK School Entrance");
        ai1.setExtractedContact("9876543210");
        ai1.setSeverity("HIGH");
        ai1.setPriorityScore(85);
        ai1.setDepartmentPrediction("Electrical");
        ai1.setConfidence(0.95);
        ai1.setDuplicateProbability(0.12);
        ai1.setPriorityExplanation("High impact near school entrance; safety hazard during night hours.");
        aiAnalysisRepository.save(ai1);

        // Complaint 2: Pothole In Progress
        Complaint c2 = new Complaint();
        c2.setComplaintNumber("CIV-2026-00125");
        c2.setCitizen(citizen2);
        c2.setCategory(cat2);
        c2.setDepartment(roads);
        c2.setDescription("Large 2-foot deep pothole on Park Street near Axis Bank ATM. Multiple two-wheelers slipping.");
        c2.setAddress("Park Street, Near Axis Bank ATM, Sector 5");
        c2.setContactNumber("9876543211");
        c2.setLatitude(13.0878);
        c2.setLongitude(80.2784);
        c2.setPriority(PriorityLevel.CRITICAL);
        c2.setPriorityScore(92);
        c2.setStatus(ComplaintStatus.IN_PROGRESS);
        c2.setAiConfidence(0.91);
        complaintRepository.save(c2);

        complaintMediaRepository.save(new ComplaintMedia(c2, "IMAGE", "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=600&auto=format&fit=crop"));

        // Complaint 3: Duplicate Street Light (Cluster with CIV-2026-00124)
        Complaint c3 = new Complaint();
        c3.setComplaintNumber("CIV-2026-00126");
        c3.setCitizen(citizen3);
        c3.setCategory(cat1);
        c3.setDepartment(elec);
        c3.setDescription("The lamp post near bus stand main road entrance is dark and broken.");
        c3.setAddress("Main Bus Stand, Sector 3");
        c3.setContactNumber("9876543212");
        c3.setLatitude(13.0829);
        c3.setLongitude(80.2709);
        c3.setPriority(PriorityLevel.HIGH);
        c3.setPriorityScore(80);
        c3.setStatus(ComplaintStatus.DUPLICATE);
        c3.setAiConfidence(0.88);
        complaintRepository.save(c3);

        // Cluster record
        ComplaintCluster cluster = clusterRepository.save(new ComplaintCluster("CLUSTER-001", c1, "Street Light Failure near Main Bus Stop"));
        cluster.setReportCount(2);
        clusterRepository.save(cluster);
        clusterMemberRepository.save(new ClusterMember(cluster, c3, 0.89));

        System.out.println(">>> CivicAI Seed Data pre-loaded successfully!");
    }
}
