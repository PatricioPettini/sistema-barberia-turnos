package com.pato.service;

import com.pato.service.implementations.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarHtml_ok() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.enviarHtml("test@mail.com", "Asunto", "<h1>Hola</h1>");

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void enviarHtml_error() {
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("ERR"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                emailService.enviarHtml("test@mail.com", "Asunto", "<html>")
        );

        assertTrue(ex.getMessage().contains("Error enviando email HTML"));
    }

    @Test
    void cargarRecordatorioTurno_ok() {
        String html = emailService.cargarRecordatorioTurno(
                "Pato",
                "2025-12-01",
                "14:00",
                "Corte",
                "Juan Barber",
                "https://cancelar.com"
        );

        assertTrue(html.contains("Pato"));
        assertTrue(html.contains("2025-12-01"));
        assertTrue(html.contains("14:00"));
        assertTrue(html.contains("Corte"));
        assertTrue(html.contains("Juan Barber"));
        assertTrue(html.contains("https://cancelar.com"));
    }

    @Test
    void cargarRecordatorioTurno_error() {
        EmailService service = new EmailService(mailSender);

        service.setRecordatorioPath("templates/no-existe.html");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.cargarRecordatorioTurno(
                    "a", "b", "c", "d", "e", "f"
            );
        });

        assertTrue(ex.getMessage().contains("Error al cargar template de recordatorio"));
    }

    @Test
    void cargarConfirmacion_ok() {
        String html = emailService.cargarConfirmacion(
                "Pato",
                "2025-12-01",
                "14:00",
                "Afeitado",
                "Juan Barber",
                77L
        );

        assertTrue(html.contains("Pato"));
        assertTrue(html.contains("2025-12-01"));
        assertTrue(html.contains("14:00"));
        assertTrue(html.contains("Afeitado"));
        assertTrue(html.contains("Juan Barber"));
        assertTrue(html.contains("77"));
    }

    @Test
    void cargarConfirmacion_error() {
        EmailService emailService2 = new EmailService(mailSender);

        emailService2.setConfirmacionPath("templates/archivo-inexistente.html");

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                emailService2.cargarConfirmacion(
                        "Pato", "2025-01-01", "14:00",
                        "Corte", "Juan", 99L
                )
        );

        assertTrue(ex.getMessage().contains("Error al cargar template de confirmación"));
    }


}
