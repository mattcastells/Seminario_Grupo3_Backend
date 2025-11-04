package com.sip3.backend.config;

import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.model.VerificationStatus;
import com.sip3.backend.features.professional.repository.ProfessionalRepository;
import com.sip3.backend.user.model.Role;
import com.sip3.backend.user.model.User;
import com.sip3.backend.user.model.UserProfile;
import com.sip3.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (userRepository.count() == 0) {
            log.info("Database is empty. Initializing with sample data...");
            createCommonUsers();
            createProfessionalUsers();
            log.info("Sample data initialization completed!");
        } else {
            log.info("Database already contains data. Skipping initialization.");
        }
    }

    private void createCommonUsers() {
        log.info("Creating 5 common users...");
        
        List<User> commonUsers = Arrays.asList(
                createUser("user1", "juan.perez@email.com", "+541134567890", "Juan Pérez", "Buenos Aires", Set.of(Role.USER)),
                createUser("user2", "maria.gonzalez@email.com", "+541134567891", "María González", "Córdoba", Set.of(Role.USER)),
                createUser("user3", "carlos.lopez@email.com", "+541134567892", "Carlos López", "Rosario", Set.of(Role.USER)),
                createUser("user4", "ana.martinez@email.com", "+541134567893", "Ana Martínez", "La Plata", Set.of(Role.USER)),
                createUser("user5", "luis.rodriguez@email.com", "+541134567894", "Luis Rodríguez", "Mendoza", Set.of(Role.USER))
        );

        userRepository.saveAll(commonUsers);
        log.info("Common users created successfully!");
    }

    private void createProfessionalUsers() {
        log.info("Creating 5 professional users...");
        
        // Create professional users
        List<User> professionalUsers = Arrays.asList(
                createUser("plomero1", "miguel.fontanero@email.com", "+541134567895", "Miguel Fontanero", "Buenos Aires", Set.of(Role.PROFESSIONAL)),
                createUser("electricista1", "pedro.electrico@email.com", "+541134567896", "Pedro Eléctrico", "Córdoba", Set.of(Role.PROFESSIONAL)),
                createUser("pintor1", "sofia.pintura@email.com", "+541134567897", "Sofía Pintura", "Rosario", Set.of(Role.PROFESSIONAL)),
                createUser("carpintero1", "roberto.madera@email.com", "+541134567898", "Roberto Madera", "La Plata", Set.of(Role.PROFESSIONAL)),
                createUser("gasista1", "elena.gas@email.com", "+541134567899", "Elena Gas", "Mendoza", Set.of(Role.PROFESSIONAL))
        );

        List<User> savedProfessionals = userRepository.saveAll(professionalUsers);

        // Create professional profiles
        List<ProfessionalProfile> professionalProfiles = Arrays.asList(
                createProfessionalProfile(savedProfessionals.get(0).getId(), "Miguel Fontanero", "Plomero", 
                    "Especialista en instalaciones de agua y gas", Arrays.asList("Instalación de cañerías", "Reparación de filtraciones", "Destapaciones"),
                    BigDecimal.valueOf(2000), BigDecimal.valueOf(5000)),
                
                createProfessionalProfile(savedProfessionals.get(1).getId(), "Pedro Eléctrico", "Electricista", 
                    "Técnico electricista matriculado", Arrays.asList("Instalaciones eléctricas", "Reparación de cortocircuitos", "Tableros eléctricos"),
                    BigDecimal.valueOf(1500), BigDecimal.valueOf(4000)),
                
                createProfessionalProfile(savedProfessionals.get(2).getId(), "Sofía Pintura", "Pintora", 
                    "Especialista en pintura de interiores y exteriores", Arrays.asList("Pintura de paredes", "Pintura de fachadas", "Empapelado"),
                    BigDecimal.valueOf(1000), BigDecimal.valueOf(3000)),
                
                createProfessionalProfile(savedProfessionals.get(3).getId(), "Roberto Madera", "Carpintero", 
                    "Carpintero con 15 años de experiencia", Arrays.asList("Muebles a medida", "Reparación de puertas", "Instalación de pisos"),
                    BigDecimal.valueOf(2500), BigDecimal.valueOf(6000)),
                
                createProfessionalProfile(savedProfessionals.get(4).getId(), "Elena Gas", "Gasista", 
                    "Gasista matriculada para instalaciones domiciliarias", Arrays.asList("Instalación de cocinas", "Reparación de calderas", "Conexiones de gas"),
                    BigDecimal.valueOf(2200), BigDecimal.valueOf(5500))
        );

        professionalRepository.saveAll(professionalProfiles);
        log.info("Professional users and profiles created successfully!");
    }

    private User createUser(String username, String email, String phone, String fullName, String location, Set<Role> roles) {
        UserProfile profile = UserProfile.builder()
                .fullName(fullName)
                .location(location)
                .phone(phone)
                .email(email)
                .avatarUrl("https://res.cloudinary.com/dtjbknm5h/image/upload/v1762223757/user_hzfwna.jpg")
                .preferredPaymentMethods(Arrays.asList("Efectivo", "Transferencia bancaria"))
                .build();

        return User.builder()
                .username(username)
                .email(email)
                .phone(phone)
                .password(passwordEncoder.encode("password123")) // Default password for all users
                .roles(roles)
                .profile(profile)
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    private ProfessionalProfile createProfessionalProfile(String userId, String displayName, String profession, 
            String summary, List<String> services, BigDecimal minRate, BigDecimal maxRate) {
        
        VerificationStatus verificationStatus = VerificationStatus.builder()
                .faceVerified(true)
                .dniFrontVerified(true)
                .dniBackVerified(true)
                .build();

        return ProfessionalProfile.builder()
                .userId(userId)
                .displayName(displayName)
                .profession(profession)
                .summary(summary)
                .biography("Profesional con amplia experiencia en " + profession.toLowerCase() + ". Comprometido con la calidad y la satisfacción del cliente.")
                .experienceYears((int) (Math.random() * 15) + 5) // 5-20 years of experience
                .services(services)
                .tags(Arrays.asList(profession.toLowerCase(), "confiable", "puntual", "garantía"))
                .rating(4.0 + Math.random()) // Rating between 4.0 and 5.0
                .reviewsCount((int) (Math.random() * 50) + 10) // 10-60 reviews
                .distanceKm(Math.random() * 10) // Within 10km
                .address("Dirección de ejemplo, " + (1000 + (int)(Math.random() * 9000)) + ", Argentina")
                .minRate(minRate)
                .maxRate(maxRate)
                .contactEmail(displayName.toLowerCase().replace(" ", ".") + "@email.com")
                .contactPhone("+54113456789" + (int)(Math.random() * 10))
                .paymentMethods(Arrays.asList("Efectivo", "Transferencia bancaria", "MercadoPago"))
                .availableJobs(Arrays.asList("Lunes a Viernes", "Horario flexible"))
                .verificationStatus(verificationStatus)
                .active(true)
                .featured(Math.random() > 0.5) // 50% chance of being featured
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}