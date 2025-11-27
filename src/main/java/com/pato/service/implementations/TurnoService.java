package com.pato.service.implementations;

import com.pato.dto.request.TurnoRequest;
import com.pato.dto.response.TurnoResponse;
import com.pato.helpers.mappers.TurnoMapper;
import com.pato.model.Barbero;
import com.pato.model.Servicio;
import com.pato.model.Turno;
import com.pato.model.enums.EstadoTurno;
import com.pato.repository.ITurnoRepository;
import com.pato.service.interfaces.ITurnoService;
import com.pato.validation.TurnoValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoService implements ITurnoService {

    private final BarberoService barberoService;
    private final ServicioService servicioService;
    private final ITurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;
    private final TurnoValidation turnoValidation;
    private final EmailService emailService;

    @Override
    public TurnoResponse crear(TurnoRequest objeto) {

        Barbero barbero = getBarbero(objeto.getIdBarbero());
        Servicio servicio = getServicio(objeto.getIdServicio());

        validarTurno(
                objeto.getFecha(),
                objeto.getHora(),
                servicio,
                barbero
        );

        Turno turno = turnoMapper.toEntity(objeto, barbero, servicio);

        turnoRepository.save(turno);

        String html = emailService.cargarConfirmacion(
                turno.getNombreCliente(),
                turno.getFecha().toString(),
                turno.getHoraDesde().toString().substring(0,5),
                servicio.getNombreServicio(),
                barbero.getNombre() + " " + barbero.getApellido(),
                turno.getIdTurno()
        );

        emailService.enviarHtml(
                turno.getEmailCliente(),
                "Confirmación de tu turno – Style Barber",
                html
        );

        return turnoMapper.toResponse(turno);
    }

    private void validarTurno(LocalDate fecha, LocalTime hora,
                              Servicio servicio, Barbero barbero) {

        turnoValidation.validarHorarioTurno(fecha, hora);

        turnoValidation.validarFechaFutura(fecha, hora);

        turnoValidation.validarHorarioYBarberoDisponible(
                fecha,
                hora,
                servicio.getDuracionMinutos(),
                barbero.getIdBarbero()
        );
    }

    private Barbero getBarbero(Long idBarbero) {
        return barberoService.getBarberoEntity(idBarbero);
    }

    private Servicio getServicio(Long idServicio) {
        return servicioService.getServicioEntity(idServicio);
    }

    @Override
    public TurnoResponse buscarPorId(Long id) {
        Turno turno = getTurnoEntity(id);

        return turnoMapper.toResponse(turno);
    }

    private Turno getTurnoEntity(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("No existe turno con ese id"));
    }

    @Override
    public List<TurnoResponse> listar() {
        return turnoRepository.findAll()
                .stream()
                .map(turnoMapper::toResponse)
                .toList();
    }

    @Override
    public TurnoResponse actualizar(Long id, TurnoRequest objeto) {

        Turno turno = getTurnoEntity(id);

        turnoValidation.validarEstadoTurno(turno.getEstadoTurno());

        Barbero barbero = getBarbero(objeto.getIdBarbero());
        Servicio servicio = getServicio(objeto.getIdServicio());

        validarTurno(
                objeto.getFecha(),
                objeto.getHora(),
                servicio,
                barbero
        );

        aplicarCambiosAlTurno(turno, objeto, barbero, servicio);

        Turno guardado = turnoRepository.save(turno);
        return turnoMapper.toResponse(guardado);
    }

    private void aplicarCambiosAlTurno(Turno turno,
                                       TurnoRequest req,
                                       Barbero barbero,
                                       Servicio servicio) {

        turno.setNombreCliente(req.getNombreCliente());
        turno.setEmailCliente(req.getEmailCliente());
        turno.setTelefonoCliente(req.getTelefonoCliente());
        turno.setFecha(req.getFecha());
        turno.setHoraDesde(req.getHora());
        turno.setHoraHasta(req.getHora().plusMinutes(servicio.getDuracionMinutos()));
        turno.setBarbero(barbero);
        turno.setServicio(servicio);
    }

    @Override
    public TurnoResponse cancelar(Long id) {
        Turno turno= getTurnoEntity(id);

        turno.setEstadoTurno(EstadoTurno.CANCELADO);

        Turno guardado = turnoRepository.save(turno);

        return turnoMapper.toResponse(guardado);

    }

    @Override
    public List<TurnoResponse> listarPorBarberoYFecha(Long idBarbero, LocalDate fecha) {

        List<Turno> turnos = turnoRepository
                .findByBarberoIdBarberoAndFecha(idBarbero, fecha);

        return turnos.stream()
                .map(turnoMapper::toResponse)
                .toList();
    }

    @Override
    public List<TurnoResponse> listarPorFecha(LocalDate fecha) {
        return turnoRepository.findByFecha(fecha)
                .stream()
                .map(turnoMapper::toResponse)
                .toList();
    }

}