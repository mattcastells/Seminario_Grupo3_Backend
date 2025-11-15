package com.sip3.backend.config;

import com.sip3.backend.features.message.repository.ConversationMessageRepository;
import com.sip3.backend.features.payment.repository.PaymentRepository;
import com.sip3.backend.features.pricing.model.PricedService;
import com.sip3.backend.features.pricing.repository.PricedServiceRepository;
import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.model.VerificationStatus;
import com.sip3.backend.features.professional.repository.ProfessionalRepository;
import com.sip3.backend.features.professional.service.ProfessionalService;
import com.sip3.backend.features.review.model.Review;
import com.sip3.backend.features.review.repository.ReviewReplyRepository;
import com.sip3.backend.features.review.repository.ReviewRepository;
import com.sip3.backend.features.serviceorder.repository.ServiceOrderRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProfessionalRepository professionalRepository;
    private final ProfessionalService professionalService;
    private final PasswordEncoder passwordEncoder;
    private final ConversationMessageRepository conversationMessageRepository;
    private final PaymentRepository paymentRepository;
    private final PricedServiceRepository pricedServiceRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReplyRepository reviewReplyRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    private final boolean run = false;

    @Override
    public void run(String... args) throws Exception {
        if (!run) {
            log.info("Skipping data initialization.");
            return;
        }

        log.info("Wiping database...");
        userRepository.deleteAll();
        professionalRepository.deleteAll();
        conversationMessageRepository.deleteAll();
        paymentRepository.deleteAll();
        pricedServiceRepository.deleteAll();
        reviewRepository.deleteAll();
        reviewReplyRepository.deleteAll();
        serviceOrderRepository.deleteAll();
        log.info("Database wiped successfully.");

        log.info("Initializing with sample data...");
        List<User> commonUsers = createCommonUsers();
        List<ProfessionalProfile> professionalProfiles = createProfessionalUsers();
        createReviews(commonUsers, professionalProfiles);
        createPricedServices(professionalProfiles);
        log.info("Recalculating review counts and ratings...");
        professionalService.recalculateAllReviewsCount();
        log.info("Sample data initialization completed!");
    }

    private List<User> createCommonUsers() {
        log.info("Creating 5 common users...");
        List<User> commonUsers = Arrays.asList(
                createUser("user1", "juan.perez@email.com", "+541134567890", "Juan Pérez", "Buenos Aires", Set.of(Role.USER)),
                createUser("user2", "maria.gonzalez@email.com", "+541134567891", "María González", "Córdoba", Set.of(Role.USER)),
                createUser("user3", "carlos.lopez@email.com", "+541134567892", "Carlos López", "Rosario", Set.of(Role.USER)),
                createUser("user4", "ana.martinez@email.com", "+541134567893", "Ana Martínez", "La Plata", Set.of(Role.USER)),
                createUser("user5", "luis.rodriguez@email.com", "+541134567894", "Luis Rodríguez", "Mendoza", Set.of(Role.USER))
        );
        List<User> savedUsers = userRepository.saveAll(commonUsers);
        log.info("Common users created successfully!");
        return savedUsers;
    }

    private List<ProfessionalProfile> createProfessionalUsers() {
        log.info("Creating 25 professional users (5 for each specialty)...");

        // Create Users
        List<User> professionalUsers = Arrays.asList(
            // Plomeros
            createUser("plomero1", "plomero1@email.com", "+5491120000001", "Miguel Fontanero", "Buenos Aires", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("plomero2", "plomero2@email.com", "+5491120000002", "Juan Cañerias", "Buenos Aires", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("plomero3", "plomero3@email.com", "+5491120000003", "Carlos Grifo", "Buenos Aires", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("plomero4", "plomero4@email.com", "+5491120000004", "Luis Destapaciones", "Buenos Aires", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("plomero5", "plomero5@email.com", "+5491120000005", "Mario Sanitario", "Buenos Aires", Set.of(Role.USER, Role.PROFESSIONAL)),
            // Electricistas
            createUser("electricista1", "electricista1@email.com", "+5491120000006", "Pedro Eléctrico", "Córdoba", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("electricista2", "electricista2@email.com", "+5491120000007", "Ana Chispas", "Córdoba", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("electricista3", "electricista3@email.com", "+5491120000008", "Jorge Tensión", "Córdoba", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("electricista4", "electricista4@email.com", "+5491120000009", "Lucía Cables", "Córdoba", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("electricista5", "electricista5@email.com", "+5491120000010", "Ricardo Voltio", "Córdoba", Set.of(Role.USER, Role.PROFESSIONAL)),
            // Pintores
            createUser("pintor1", "pintor1@email.com", "+5491120000011", "Sofía Pintura", "Rosario", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("pintor2", "pintor2@email.com", "+5491120000012", "Laura Color", "Rosario", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("pintor3", "pintor3@email.com", "+5491120000013", "Roberto Brocha", "Rosario", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("pintor4", "pintor4@email.com", "+5491120000014", "Daniel Rodillo", "Rosario", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("pintor5", "pintor5@email.com", "+5491120000015", "Marta Paredes", "Rosario", Set.of(Role.USER, Role.PROFESSIONAL)),
            // Carpinteros
            createUser("carpintero1", "carpintero1@email.com", "+5491120000016", "Roberto Madera", "La Plata", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("carpintero2", "carpintero2@email.com", "+5491120000017", "Fernando Serrucho", "La Plata", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("carpintero3", "carpintero3@email.com", "+5491120000018", "Gabriela Muebles", "La Plata", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("carpintero4", "carpintero4@email.com", "+5491120000019", "Martín Clavo", "La Plata", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("carpintero5", "carpintero5@email.com", "+5491120000020", "Silvia Barniz", "La Plata", Set.of(Role.USER, Role.PROFESSIONAL)),
            // Gasistas
            createUser("gasista1", "gasista1@email.com", "+5491120000021", "Elena Gas", "Mendoza", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("gasista2", "gasista2@email.com", "+5491120000022", "Oscar Fuego", "Mendoza", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("gasista3", "gasista3@email.com", "+5491120000023", "Verónica Caldera", "Mendoza", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("gasista4", "gasista4@email.com", "+5491120000024", "Héctor Conexión", "Mendoza", Set.of(Role.USER, Role.PROFESSIONAL)),
            createUser("gasista5", "gasista5@email.com", "+5491120000025", "Norma Regulador", "Mendoza", Set.of(Role.USER, Role.PROFESSIONAL))
        );
        List<User> savedUsers = userRepository.saveAll(professionalUsers);
        log.info("{} professional users created.", savedUsers.size());

        String baseAvatarUrl = "https://res.cloudinary.com/dtjbknm5h/image/upload/";

        // Create Professional Profiles
        List<ProfessionalProfile> professionalProfiles = Arrays.asList(
            // Plomeros
            createProfessionalProfile(savedUsers.get(0).getId(), "Miguel Fontanero", "Plomero", "Especialista en instalaciones de agua y gas.", Arrays.asList("Instalación de cañerías", "Reparación de filtraciones"), BigDecimal.valueOf(2000), BigDecimal.valueOf(5000), baseAvatarUrl + "v1762220896/plomero1_wdrkk5.jpg"),
            createProfessionalProfile(savedUsers.get(1).getId(), "Juan Cañerias", "Plomero", "Soluciones rápidas para problemas de plomería.", Arrays.asList("Destapaciones", "Cambio de grifería"), BigDecimal.valueOf(1800), BigDecimal.valueOf(4500), baseAvatarUrl + "v1762280804/plomero2_kmcpsk.webp"),
            createProfessionalProfile(savedUsers.get(2).getId(), "Carlos Grifo", "Plomero", "Servicio de plomería residencial y comercial.", Arrays.asList("Instalación de sanitarios", "Detección de fugas"), BigDecimal.valueOf(2200), BigDecimal.valueOf(5500), baseAvatarUrl + "v1762220896/plomero1_wdrkk5.jpg"),
            createProfessionalProfile(savedUsers.get(3).getId(), "Luis Destapaciones", "Plomero", "Experto en destapaciones de todo tipo.", Arrays.asList("Destapaciones con máquina", "Limpieza de cañerías"), BigDecimal.valueOf(2500), BigDecimal.valueOf(6000), baseAvatarUrl + "v1762220896/plomero1_wdrkk5.jpg"),
            createProfessionalProfile(savedUsers.get(4).getId(), "Mario Sanitario", "Plomero", "Instalación y reparación de artefactos sanitarios.", Arrays.asList("Instalación de inodoros", "Reparación de mochilas"), BigDecimal.valueOf(1900), BigDecimal.valueOf(4800), baseAvatarUrl + "v1762220896/plomero1_wdrkk5.jpg"),
            // Electricistas
            createProfessionalProfile(savedUsers.get(5).getId(), "Pedro Eléctrico", "Electricista", "Técnico electricista matriculado.", Arrays.asList("Instalaciones eléctricas", "Reparación de cortocircuitos"), BigDecimal.valueOf(1500), BigDecimal.valueOf(4000), baseAvatarUrl + "v1762220576/electricista1_ewzysj.jpg"),
            createProfessionalProfile(savedUsers.get(6).getId(), "Ana Chispas", "Electricista", "Soluciones eléctricas seguras y eficientes.", Arrays.asList("Tableros eléctricos", "Cableado estructurado"), BigDecimal.valueOf(1600), BigDecimal.valueOf(4200), baseAvatarUrl + "v1762220576/electricista1_ewzysj.jpg"),
            createProfessionalProfile(savedUsers.get(7).getId(), "Jorge Tensión", "Electricista", "Servicios de alta y baja tensión.", Arrays.asList("Medición de puesta a tierra", "Instalación de luminarias"), BigDecimal.valueOf(1700), BigDecimal.valueOf(4300), baseAvatarUrl + "v1762220576/electricista1_ewzysj.jpg"),
            createProfessionalProfile(savedUsers.get(8).getId(), "Lucía Cables", "Electricista", "Especialista en cableado residencial.", Arrays.asList("Renovación de cableado", "Instalación de tomas"), BigDecimal.valueOf(1400), BigDecimal.valueOf(3800), baseAvatarUrl + "v1762220576/electricista1_ewzysj.jpg"),
            createProfessionalProfile(savedUsers.get(9).getId(), "Ricardo Voltio", "Electricista", "Reparaciones eléctricas urgentes.", Arrays.asList("Atención de emergencias 24hs", "Diagnóstico de fallas"), BigDecimal.valueOf(1800), BigDecimal.valueOf(4500), baseAvatarUrl + "v1762220576/electricista1_ewzysj.jpg"),
            // Pintores
            createProfessionalProfile(savedUsers.get(10).getId(), "Sofía Pintura", "Pintor", "Especialista en pintura de interiores y exteriores.", Arrays.asList("Pintura de paredes", "Pintura de fachadas"), BigDecimal.valueOf(1000), BigDecimal.valueOf(3000), baseAvatarUrl + "v1762220577/pintor1_ljcou2.jpg"),
            createProfessionalProfile(savedUsers.get(11).getId(), "Laura Color", "Pintor", "Asesoramiento en colores y decoración.", Arrays.asList("Pintura decorativa", "Estucos"), BigDecimal.valueOf(1200), BigDecimal.valueOf(3400), baseAvatarUrl + "v1762280823/pintor2_eg168e.jpg"),
            createProfessionalProfile(savedUsers.get(12).getId(), "Roberto Brocha", "Pintor", "Acabados perfectos y prolijidad garantizada.", Arrays.asList("Empapelado", "Aplicación de texturados"), BigDecimal.valueOf(1100), BigDecimal.valueOf(3200), baseAvatarUrl + "v1763239649/pintor3_ow8etf.jpg"),
            createProfessionalProfile(savedUsers.get(13).getId(), "Daniel Rodillo", "Pintor", "Rapidez y eficiencia en grandes superficies.", Arrays.asList("Pintura de galpones", "Pintura de oficinas"), BigDecimal.valueOf(900), BigDecimal.valueOf(2800), baseAvatarUrl + "v1763239649/pintor4_yhcayh.webp"),
            createProfessionalProfile(savedUsers.get(14).getId(), "Marta Paredes", "Pintor", "Renovación de ambientes con pintura.", Arrays.asList("Pintura de living", "Pintura de dormitorios"), BigDecimal.valueOf(1050), BigDecimal.valueOf(3100), baseAvatarUrl + "v1763239649/pintor5_hu5au6.jpg"),
            // Carpinteros
            createProfessionalProfile(savedUsers.get(15).getId(), "Roberto Madera", "Carpintero", "Carpintero con 15 años de experiencia.", Arrays.asList("Muebles a medida", "Reparación de puertas"), BigDecimal.valueOf(2500), BigDecimal.valueOf(6000), baseAvatarUrl + "v1762220576/carpintero1_efdjbp.jpg"),
            createProfessionalProfile(savedUsers.get(16).getId(), "Fernando Serrucho", "Carpintero", "Trabajos en madera de alta calidad.", Arrays.asList("Instalación de pisos de madera", "Decks y pérgolas"), BigDecimal.valueOf(2800), BigDecimal.valueOf(7000), baseAvatarUrl + "v1762220576/carpintero1_efdjbp.jpg"),
            createProfessionalProfile(savedUsers.get(17).getId(), "Gabriela Muebles", "Carpintero", "Diseño y fabricación de muebles personalizados.", Arrays.asList("Bibliotecas", "Muebles de cocina"), BigDecimal.valueOf(3000), BigDecimal.valueOf(8000), baseAvatarUrl + "v1762220576/carpintero1_efdjbp.jpg"),
            createProfessionalProfile(savedUsers.get(18).getId(), "Martín Clavo", "Carpintero", "Reparaciones y montajes de carpintería.", Arrays.asList("Armado de muebles", "Colocación de estantes"), BigDecimal.valueOf(2200), BigDecimal.valueOf(5500), baseAvatarUrl + "v1762220576/carpintero1_efdjbp.jpg"),
            createProfessionalProfile(savedUsers.get(19).getId(), "Silvia Barniz", "Carpintero", "Restauración y laqueado de muebles.", Arrays.asList("Laqueado de puertas", "Restauración de antigüedades"), BigDecimal.valueOf(2600), BigDecimal.valueOf(6500), baseAvatarUrl + "v1762220576/carpintero1_efdjbp.jpg"),
            // Gasistas
            createProfessionalProfile(savedUsers.get(20).getId(), "Elena Gas", "Gasista", "Gasista matriculada para instalaciones domiciliarias.", Arrays.asList("Instalación de cocinas", "Reparación de calderas"), BigDecimal.valueOf(2200), BigDecimal.valueOf(5500), baseAvatarUrl + "v1762220576/gasista1_hrrrdu.jpg"),
            createProfessionalProfile(savedUsers.get(21).getId(), "Oscar Fuego", "Gasista", "Revisión y habilitación de instalaciones de gas.", Arrays.asList("Prueba de hermeticidad", "Planos de gas"), BigDecimal.valueOf(2400), BigDecimal.valueOf(6000), baseAvatarUrl + "v1762220576/gasista1_hrrrdu.jpg"),
            createProfessionalProfile(savedUsers.get(22).getId(), "Verónica Caldera", "Gasista", "Especialista en sistemas de calefacción central.", Arrays.asList("Instalación de radiadores", "Mantenimiento de calderas"), BigDecimal.valueOf(2500), BigDecimal.valueOf(6200), baseAvatarUrl + "v1762220576/gasista1_hrrrdu.jpg"),
            createProfessionalProfile(savedUsers.get(23).getId(), "Héctor Conexión", "Gasista", "Instalaciones seguras y bajo normativa.", Arrays.asList("Conexiones de gas", "Ventilación de ambientes"), BigDecimal.valueOf(2100), BigDecimal.valueOf(5300), baseAvatarUrl + "v1762220576/gasista1_hrrrdu.jpg"),
            createProfessionalProfile(savedUsers.get(24).getId(), "Norma Regulador", "Gasista", "Cambio y reparación de reguladores de gas.", Arrays.asList("Instalación de medidores", "Reparación de fugas"), BigDecimal.valueOf(2300), BigDecimal.valueOf(5800), baseAvatarUrl + "v1762220576/gasista1_hrrrdu.jpg")
        );
        List<ProfessionalProfile> savedProfiles = professionalRepository.saveAll(professionalProfiles);
        log.info("{} professional profiles created successfully!", savedProfiles.size());
        return savedProfiles;
    }

    private void createReviews(List<User> users, List<ProfessionalProfile> professionals) {
        log.info("Creating reviews...");
        List<Review> reviews = new ArrayList<>();
        String[] comments = {
            "¡Excelente trabajo! Muy profesional y atento a los detalles. Lo recomiendo sin dudarlo.",
            "Buen servicio, aunque podría mejorar en la puntualidad. El resultado final fue satisfactorio.",
            "No quedé conforme con el trabajo realizado. La comunicación fue difícil y el resultado no fue el esperado.",
            "Un profesional de primera. Resolvió mi problema rápidamente y con mucha amabilidad. ¡Gracias!",
            "Recomendable. Cumplió con lo pactado en tiempo y forma. Volvería a contratarlo."
        };

        for (ProfessionalProfile professional : professionals) {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                // Each user reviews each professional
                reviews.add(Review.builder()
                        .professionalId(professional.getId())
                        .userId(user.getId())
                        .userDisplayName(user.getProfile().getFullName())
                        .rating((int) (Math.random() * 3) + 3) // Rating between 3 and 5
                        .comment(comments[i % comments.length])
                        .createdAt(Instant.now())
                        .build());
            }
        }
        reviewRepository.saveAll(reviews);
        log.info("{} reviews created successfully!", reviews.size());
    }

    private void createPricedServices(List<ProfessionalProfile> professionals) {
        log.info("Creating priced services...");
        List<PricedService> services = new ArrayList<>();
        
        for (ProfessionalProfile professional : professionals) {
            String trade = professional.getProfession();
            switch (trade) {
                case "Plomero":
                    services.add(createPricedService(professional.getId(), trade, "Cambio de cuerito", "Reparación de canilla que gotea.", new BigDecimal("15000")));
                    services.add(createPricedService(professional.getId(), trade, "Destapación de cañerías", "Con máquina destapadora.", new BigDecimal("30000")));
                    services.add(createPricedService(professional.getId(), trade, "Instalación de termotanque", "Incluye materiales básicos.", new BigDecimal("80000")));
                    services.add(createPricedService(professional.getId(), trade, "Reparación de filtración", "Detección y reparación de filtraciones de agua.", new BigDecimal("40000")));
                    services.add(createPricedService(professional.getId(), trade, "Instalación de sanitario", "Instalación de inodoro o bidet.", new BigDecimal("50000")));
                    break;
                case "Electricista":
                    services.add(createPricedService(professional.getId(), trade, "Cambio de enchufe", "Reemplazo de tomacorriente.", new BigDecimal("12000")));
                    services.add(createPricedService(professional.getId(), trade, "Instalación de ventilador de techo", "Conexión y amurado.", new BigDecimal("45000")));
                    services.add(createPricedService(professional.getId(), trade, "Revisión de tablero eléctrico", "Verificación de térmicas y disyuntores.", new BigDecimal("25000")));
                    services.add(createPricedService(professional.getId(), trade, "Instalación de luminaria", "Colocación de lámparas o apliques.", new BigDecimal("18000")));
                    services.add(createPricedService(professional.getId(), trade, "Detección de cortocircuito", "Diagnóstico y solución de fallas eléctricas.", new BigDecimal("35000")));
                    break;
                case "Pintor":
                    services.add(createPricedService(professional.getId(), trade, "Pintura de habitación (m²)", "Precio por metro cuadrado, incluye 2 manos.", new BigDecimal("8000")));
                    services.add(createPricedService(professional.getId(), trade, "Pintura de puerta", "Lijado y pintura de puerta placa.", new BigDecimal("25000")));
                    services.add(createPricedService(professional.getId(), trade, "Pintura de rejas (m lineal)", "Precio por metro lineal de reja.", new BigDecimal("15000")));
                    services.add(createPricedService(professional.getId(), trade, "Enduido y preparación de pared", "Masillado de imperfecciones.", new BigDecimal("10000")));
                    services.add(createPricedService(professional.getId(), trade, "Pintura de cielorraso", "Incluye protección de muebles.", new BigDecimal("30000")));
                    break;
                case "Carpintero":
                    services.add(createPricedService(professional.getId(), trade, "Armado de mueble en caja", "Armado de muebles tipo IKEA.", new BigDecimal("35000")));
                    services.add(createPricedService(professional.getId(), trade, "Colocación de estantes", "Precio por estante, incluye ménsulas.", new BigDecimal("15000")));
                    services.add(createPricedService(professional.getId(), trade, "Reparación de puerta de placard", "Ajuste de bisagras o correderas.", new BigDecimal("20000")));
                    services.add(createPricedService(professional.getId(), trade, "Instalación de zócalos (m lineal)", "Precio por metro lineal.", new BigDecimal("5000")));
                    services.add(createPricedService(professional.getId(), trade, "Corte de madera a medida", "Precio por corte.", new BigDecimal("3000")));
                    break;
                case "Gasista":
                    services.add(createPricedService(professional.getId(), trade, "Instalación de cocina", "Conexión y prueba de funcionamiento.", new BigDecimal("40000")));
                    services.add(createPricedService(professional.getId(), trade, "Revisión de estufa", "Limpieza de pico y piloto.", new BigDecimal("20000")));
                    services.add(createPricedService(professional.getId(), trade, "Prueba de hermeticidad", "Verificación de fugas en la instalación.", new BigDecimal("50000")));
                    services.add(createPricedService(professional.getId(), trade, "Cambio de flexible", "Reemplazo de conexión flexible de gas.", new BigDecimal("15000")));
                    services.add(createPricedService(professional.getId(), trade, "Instalación de calefón", "Conexión y ventilación.", new BigDecimal("70000")));
                    break;
            }
        }
        pricedServiceRepository.saveAll(services);
        log.info("{} priced services created successfully!", services.size());
    }

    private PricedService createPricedService(String professionalId, String trade, String serviceName, String description, BigDecimal basePrice) {
        return PricedService.builder()
                .professionalId(professionalId)
                .trade(trade)
                .serviceName(serviceName)
                .description(description)
                .basePrice(basePrice)
                .isCustom(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
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
            String summary, List<String> services, BigDecimal minRate, BigDecimal maxRate, String avatarUrl) {
        
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
                .rating(null) // Will be calculated from actual reviews
                .reviewsCount(0) // Will be calculated from actual reviews
                .distanceKm(Math.random() * 10) // Within 10km
                .address("Dirección de ejemplo, " + (1000 + (int)(Math.random() * 9000)) + ", Argentina")
                .minRate(minRate)
                .maxRate(maxRate)
                .avatarUrl(avatarUrl) // Use the provided avatar URL
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
