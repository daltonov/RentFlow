package com.rentflow.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentflow.booking.domain.BookingSource;
import com.rentflow.booking.dto.CreateBookingRequest;
import com.rentflow.guest.dto.CreateGuestRequest;
import com.rentflow.guest.service.GuestService;
import com.rentflow.property.dto.CreatePropertyRequest;
import com.rentflow.property.service.PropertyService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class BookingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private GuestService guestService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine")
                    .withDatabaseName("rentflow_test")
                    .withUsername("rentflow")
                    .withPassword("rentflow");

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CreateBookingRequest createBookingRequest() {
        var property = propertyService.create(
                new CreatePropertyRequest(
                        "Квартира для теста",
                        "Москва, Тверская 1",
                        4,
                        new BigDecimal("4500.00")
                )
        );

        var guest = guestService.create(
                new CreateGuestRequest(
                        "Иван",
                        "Петров",
                        "+79991234567"
                )
        );

        return new CreateBookingRequest(
                property.id(),
                guest.id(),
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 5),
                new BigDecimal("18000.00"),
                BookingSource.DIRECT
        );
    }

    @Test
    void migrationsCreateRequiredTables() {
        var tables = jdbcTemplate.queryForList(
                """
                SELECT table_name
                FROM information_schema.tables
                WHERE table_schema = 'public'
                  AND table_type = 'BASE TABLE'
                """,
                String.class
        );

        assertThat(tables).contains(
                "properties",
                "guests",
                "bookings",
                "databasechangelog"
        );
    }

    @Test
    void createsBookingAndReadsItBack() throws Exception {
        var property = propertyService.create(
                new CreatePropertyRequest(
                        "Тестовая квартира",
                        "Москва, Тверская 1",
                        4,
                        new BigDecimal("4500.00")
                )
        );

        var guest = guestService.create(
                new CreateGuestRequest(
                        "Иван",
                        "Петров",
                        "+79991234567"
                )
        );

        var request = new CreateBookingRequest(
                property.id(),
                guest.id(),
                LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 5),
                new BigDecimal("18000.00"),
                BookingSource.DIRECT
        );

        var result = mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andReturn();

        String bookingId = objectMapper
                .readTree(result.getResponse().getContentAsString())
                .get("id")
                .asText();

        assertThat(result.getResponse().getHeader("Location"))
                .isEqualTo("/api/bookings/" + bookingId);

        mockMvc.perform(get("/api/bookings/{id}", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.propertyId").value(property.id().toString()))
                .andExpect(jsonPath("$.guestId").value(guest.id().toString()))
                .andExpect(jsonPath("$.checkIn").value("2026-10-01"))
                .andExpect(jsonPath("$.checkOut").value("2026-10-05"))
                .andExpect(jsonPath("$.totalPrice").value(18000.00))
                .andExpect(jsonPath("$.source").value("DIRECT"));
    }

    @ParameterizedTest
    @CsvSource({
            "2026-10-01, 2026-10-05",
            "2026-10-02, 2026-10-04",
            "2026-09-30, 2026-10-06",
            "2026-09-30, 2026-10-02",
            "2026-10-04, 2026-10-06"
    })
    void rejectsOverlappingBookings(
            LocalDate checkIn,
            LocalDate checkOut) throws Exception {

        var original = createBookingRequest();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(original)))
                .andExpect(status().isCreated());

        var conflicting = new CreateBookingRequest(
                original.propertyId(),
                original.guestId(),
                checkIn,
                checkOut,
                new BigDecimal("18000.00"),
                BookingSource.DIRECT
        );

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflicting)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        Long bookingCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM bookings WHERE property_id = ?",
                Long.class,
                original.propertyId()
        );

        assertThat(bookingCount).isEqualTo(1L);
    }

    @ParameterizedTest
    @CsvSource({
            "2026-09-28, 2026-10-01",
            "2026-10-05, 2026-10-08"
    })
    void allowsAdjacentBookings(
            LocalDate checkIn,
            LocalDate checkOut) throws Exception {

        var original = createBookingRequest();

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(original)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(
                        "/api/properties/{id}/availability",
                        original.propertyId())
                        .param("checkIn", checkIn.toString())
                        .param("checkOut", checkOut.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(true));

        var adjacent = new CreateBookingRequest(
                original.propertyId(),
                original.guestId(),
                checkIn,
                checkOut,
                new BigDecimal("13500.00"),
                BookingSource.DIRECT
        );

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adjacent)))
                .andExpect(status().isCreated());

        Long bookingCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM bookings WHERE property_id = ?",
                Long.class,
                original.propertyId()
        );

        assertThat(bookingCount).isEqualTo(2L);
    }

    @Test
    void concurrentRequestsCreateOnlyOneBooking() throws Exception {
        var request = createBookingRequest();
        String body = objectMapper.writeValueAsString(request);

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        var executor = Executors.newFixedThreadPool(2);

        try {
            Callable<Integer> sendRequest = () -> {
                ready.countDown();

                if (!start.await(10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Start signal was not received");
                }

                return mockMvc.perform(post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                        .andReturn()
                        .getResponse()
                        .getStatus();
            };

            var first = executor.submit(sendRequest);
            var second = executor.submit(sendRequest);

            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();

            start.countDown();

            int firstStatus = first.get(20, TimeUnit.SECONDS);
            int secondStatus = second.get(20, TimeUnit.SECONDS);

            assertThat(List.of(firstStatus, secondStatus))
                    .containsExactlyInAnyOrder(201, 409);
        } finally {
            executor.shutdownNow();
        }

        Long bookingCount = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM bookings WHERE property_id = ?",
                Long.class,
                request.propertyId()
        );

        assertThat(bookingCount).isEqualTo(1L);
    }
}