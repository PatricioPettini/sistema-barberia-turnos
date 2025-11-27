package com.pato.service.implementations;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Setter
    private String confirmacionPath = "templates/confirmacion-turno.html";
    @Setter
    private String recordatorioPath = "templates/recordatorio-turno.html";

    public void enviarHtml(String para, String asunto, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando email HTML", e);
        }
    }

    public String cargarRecordatorioTurno(String nombre,
                                          String fecha,
                                          String hora,
                                          String servicio,
                                          String barbero,
                                          String linkCancelar) {
        try {
            ClassPathResource resource =
                    new ClassPathResource(recordatorioPath);

            InputStream inputStream = resource.getInputStream();
            String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            html = html.replace("${nombre}", nombre);
            html = html.replace("${fecha}", fecha);
            html = html.replace("${hora}", hora);
            html = html.replace("${servicio}", servicio);
            html = html.replace("${barbero}", barbero);
            html = html.replace("${linkCancelar}", linkCancelar != null ? linkCancelar : "#");

            return html;
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar template de recordatorio", e);
        }
    }

    public String cargarConfirmacion(String nombre, String fecha, String hora, String servicio, String barbero, Long idTurno) {
        try {
            ClassPathResource resource = new ClassPathResource(confirmacionPath);

            InputStream inputStream = resource.getInputStream();
            String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            html = html.replace("${nombre}", nombre);
            html = html.replace("${fecha}", fecha);
            html = html.replace("${hora}", hora);
            html = html.replace("${servicio}", servicio);
            html = html.replace("${barbero}", barbero);
            html = html.replace(
                    "__LINK_CANCELAR__",
                    "http://127.0.0.1:5500/frontend/public/cancelar-turno.html?id=" + idTurno
            );

            return html;

        } catch (Exception e) {
            throw new RuntimeException("Error al cargar template de confirmación", e);
        }
    }


}
