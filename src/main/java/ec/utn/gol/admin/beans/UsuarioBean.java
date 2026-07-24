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
    public void init() {
        cargar();
    }

    public void cargar() {
        try {
            usuarios = service.getUsuarios();
        } catch (Exception e) {
            mensaje("Error al cargar usuarios: " + e.getMessage(), true);
        }
    }

    // A diferencia de antes (setUsuarioEditar(u) apuntaba directo al objeto de la
    // lista `usuarios`), esto clona: si el admin abre el diálogo y lo cierra sin
    // guardar, la fila de la tabla ya no queda mutada en memoria con datos que
    // nunca se enviaron al backend.
    public void prepararEditar(Usuario u) {
        this.usuarioEditar = new Usuario();
        this.usuarioEditar.setId(u.getId());
        this.usuarioEditar.setNombreUsuario(u.getNombreUsuario());
        this.usuarioEditar.setEmail(u.getEmail());
        this.usuarioEditar.setRol(u.getRol());
        this.usuarioEditar.setFechaRegistro(u.getFechaRegistro());
        this.usuarioEditar.setActivo(u.isActivo());
    }

    public void actualizar() {
        try {
            service.actualizarUsuario(usuarioEditar);
            // Auditoría: la registra automáticamente EstadisticasAPI vía header X-Usuario-Id.
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuarioEditar() {
        return usuarioEditar;
    }

    public void setUsuarioEditar(Usuario usuarioEditar) {
        this.usuarioEditar = usuarioEditar;
    }
}
