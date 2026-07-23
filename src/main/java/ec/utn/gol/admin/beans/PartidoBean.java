package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.models.*;
import ec.utn.gol.admin.services.EstadisticasService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class PartidoBean implements Serializable {

    @Inject
    private EstadisticasService service;

    private List<Partido> partidos;
    private List<Seleccion> selecciones;
    private List<Sede> sedes;
    private Partido partido = new Partido();
    private Date fechaHoraDate;

    private Partido partidoResultado;
    private int golesLocal;
    private int golesVisitante;

    private Partido partidoEstado;
    private String nuevoEstado;

    @PostConstruct
    public void init() {
        try {
            partidos = service.getPartidos();
            selecciones = service.getSelecciones();
            sedes = service.getSedes();
        } catch (Exception e) {
            mensaje("Error al cargar datos: " + e.getMessage(), true);
        }
    }

    public void guardar() {
        if (partido.getSeleccionLocalId() == partido.getSeleccionVisitanteId()) {
            mensaje("El equipo local y el equipo visitante no pueden ser la misma selección.", true);
            return;
        }

        try {
            if (fechaHoraDate != null) {
                partido.setFechaHora(fechaHoraDate.toInstant().toString());
            }
            service.crearPartido(partido);
            partido = new Partido();
            fechaHoraDate = null;
            partidos = service.getPartidos();
            mensaje("Partido creado correctamente.", false);
        } catch (Exception e) {
            mensaje("Error al guardar: " + e.getMessage(), true);
        }
    }

    public void registrarResultado() {
        try {
            service.registrarResultado(partidoResultado.getId(), golesLocal, golesVisitante);
            // Auditoría: la registra automáticamente EstadisticasAPI vía header X-Usuario-Id.
            partidos = service.getPartidos();
            mensaje("Resultado registrado correctamente.", false);
        } catch (Exception e) {
            mensaje("Error al registrar resultado: " + e.getMessage(), true);
        }
    }

    public void prepararResultado(Partido p) {
        this.partidoResultado = p;
        this.golesLocal = 0;
        this.golesVisitante = 0;
    }

    public void prepararEstado(Partido p) {
        this.partidoEstado = p;
        this.nuevoEstado = p.getEstado();
    }

    public void actualizarEstado() {
        try {
            service.actualizarEstadoPartido(partidoEstado.getId(), nuevoEstado);
            // Auditoría: la registra automáticamente EstadisticasAPI vía header X-Usuario-Id.
            partidos = service.getPartidos();
            mensaje("Estado actualizado correctamente.", false);
        } catch (Exception e) {
            mensaje("Error al actualizar estado: " + e.getMessage(), true);
        }
    }

    private void mensaje(String texto, boolean error) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(error ? FacesMessage.SEVERITY_ERROR : FacesMessage.SEVERITY_INFO, texto, null));
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public List<Seleccion> getSelecciones() {
        return selecciones;
    }

    public List<Sede> getSedes() {
        return sedes;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Date getFechaHoraDate() {
        return fechaHoraDate;
    }

    public void setFechaHoraDate(Date fechaHoraDate) {
        this.fechaHoraDate = fechaHoraDate;
    }

    public Partido getPartidoResultado() {
        return partidoResultado;
    }

    public void setPartidoResultado(Partido partidoResultado) {
        this.partidoResultado = partidoResultado;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public Partido getPartidoEstado() {
        return partidoEstado;
    }

    public void setPartidoEstado(Partido partidoEstado) {
        this.partidoEstado = partidoEstado;
    }

    public String getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(String nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }
}
