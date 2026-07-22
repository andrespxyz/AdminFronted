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

    @Inject
    private LoginBean loginBean;

    private List<Sede> sedes;
    private Sede sede = new Sede();

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
            registrarAuditoriaSeguro("CREAR_SEDE", "Sede: " + sede.getNombre());
            sede = new Sede();
            cargar();
            mensaje("Sede creada correctamente.", false);
        } catch (Exception e) {
            mensaje("Error al guardar: " + e.getMessage(), true);
        }
    }

    private void registrarAuditoriaSeguro(String accion, String detalle) {
        try {
            service.registrarAuditoria(loginBean.getUsuarioId(), accion, detalle);
        } catch (Exception e) {
            // no bloqueamos la operación principal si falla la auditoría
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
}
