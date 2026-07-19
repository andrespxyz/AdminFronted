package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.models.Usuario;
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
public class UsuarioBean implements Serializable {

    @Inject
    private EstadisticasService service;

    private List<Usuario> usuarios;
    private Usuario usuarioEditar;

    @PostConstruct
    public void init() { cargar(); }

    public void cargar() {
        try {
            usuarios = service.getUsuarios();
        } catch (Exception e) {
            mensaje("Error al cargar usuarios: " + e.getMessage(), true);
        }
    }

    public void actualizar() {
        try {
            service.actualizarUsuario(usuarioEditar);
            cargar();
            mensaje("Usuario actualizado.", false);
        } catch (Exception e) {
            mensaje("Error al actualizar: " + e.getMessage(), true);
        }
    }

    private void mensaje(String texto, boolean error) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(error ? FacesMessage.SEVERITY_ERROR : FacesMessage.SEVERITY_INFO, texto, null));
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public Usuario getUsuarioEditar() { return usuarioEditar; }
    public void setUsuarioEditar(Usuario usuarioEditar) { this.usuarioEditar = usuarioEditar; }
}