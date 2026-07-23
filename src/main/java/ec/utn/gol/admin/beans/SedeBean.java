package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.models.Sede;
import ec.utn.gol.admin.services.EstadisticasService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class SedeBean implements Serializable {

    @Inject
    private EstadisticasService service;

    private List<Sede> sedes;
    private Sede sede = new Sede();
    private Sede sedeEditar;

    @PostConstruct
    public void init() {
        cargar();
    }

    public void cargar() {
        try {
            sedes = service.getSedes();
        } catch (Exception e) {
            mensaje("Error al cargar sedes: " + e.getMessage(), true);
        }
    }

    public void guardar() {
        try {
            service.crearSede(sede);
            // Auditoría: la registra automáticamente EstadisticasAPI vía header X-Usuario-Id.
            sede = new Sede();
            cargar();
            mensaje("Sede creada correctamente.", false);
        } catch (Exception e) {
            mensaje("Error al guardar: " + e.getMessage(), true);
        }
    }

    public void prepararEditar(Sede s) {
        this.sedeEditar = new Sede();
        this.sedeEditar.setId(s.getId());
        this.sedeEditar.setNombre(s.getNombre());
        this.sedeEditar.setCiudad(s.getCiudad());
        this.sedeEditar.setPais(s.getPais());
        this.sedeEditar.setCapacidad(s.getCapacidad());
    }

    public void actualizar() {
        try {
            service.actualizarSede(sedeEditar);
            // Auditoría: la registra automáticamente EstadisticasAPI vía header X-Usuario-Id.
            cargar();
            mensaje("Sede actualizada.", false);
        } catch (Exception e) {
            mensaje("Error al actualizar: " + e.getMessage(), true);
        }
    }

    private void mensaje(String texto, boolean error) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(error ? FacesMessage.SEVERITY_ERROR : FacesMessage.SEVERITY_INFO, texto, null));
    }

    public List<Sede> getSedes() {
        return sedes;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public Sede getSedeEditar() {
        return sedeEditar;
    }

    public void setSedeEditar(Sede sedeEditar) {
        this.sedeEditar = sedeEditar;
    }

    public void eliminar(Sede s) {
        try {
            service.eliminarSede(s.getId());
            cargar();
            mensaje("Sede eliminada correctamente.", false);
        } catch (Exception e) {
            mensaje("Error al eliminar: " + e.getMessage(), true);
        }
    }
}
