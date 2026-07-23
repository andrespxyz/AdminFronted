package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.models.Seleccion;
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
public class SeleccionBean implements Serializable {

    @Inject
    private EstadisticasService service;

    private List<Seleccion> selecciones;
    private Seleccion seleccion = new Seleccion();
    private Seleccion seleccionEditar;

    @PostConstruct
    public void init() {
        cargar();
    }

    public void cargar() {
        try {
            selecciones = service.getSelecciones();
        } catch (Exception e) {
            mensaje("Error al cargar selecciones: " + e.getMessage(), true);
        }
    }

    public void guardar() {
        try {
            service.crearSeleccion(seleccion);
            // Auditoría: la registra automáticamente EstadisticasAPI vía header X-Usuario-Id.
            seleccion = new Seleccion();
            cargar();
            mensaje("Selección creada correctamente.", false);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje("Error al guardar: " + e.getMessage(), true);
        }
    }

    public void prepararEditar(Seleccion s) {
        this.seleccionEditar = new Seleccion();
        this.seleccionEditar.setId(s.getId());
        this.seleccionEditar.setNombre(s.getNombre());
        this.seleccionEditar.setCodigo(s.getCodigo());
        this.seleccionEditar.setGrupo(s.getGrupo());
        this.seleccionEditar.setEscudo(s.getEscudo());
    }

    public void actualizar() {
        try {
            service.actualizarSeleccion(seleccionEditar);
            // Auditoría: la registra automáticamente EstadisticasAPI vía header X-Usuario-Id.
            cargar();
            mensaje("Selección actualizada.", false);
        } catch (Exception e) {
            mensaje("Error al actualizar: " + e.getMessage(), true);
        }
    }

    private void mensaje(String texto, boolean error) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(error ? FacesMessage.SEVERITY_ERROR : FacesMessage.SEVERITY_INFO, texto, null));
    }

    public List<Seleccion> getSelecciones() {
        return selecciones;
    }

    public Seleccion getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Seleccion seleccion) {
        this.seleccion = seleccion;
    }

    public Seleccion getSeleccionEditar() {
        return seleccionEditar;
    }

    public void setSeleccionEditar(Seleccion seleccionEditar) {
        this.seleccionEditar = seleccionEditar;
    }
}
