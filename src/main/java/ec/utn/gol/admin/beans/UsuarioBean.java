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

    @Inject
    private LoginBean loginBean;

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

    public void actualizar() {
        try {
            service.actualizarUsuario(usuarioEditar);
            registrarAuditoriaSeguro("ACTUALIZAR_USUARIO",
                    "Usuario #" + usuarioEditar.getId() + " — rol: " + usuarioEditar.getRol()
                    + ", activo: " + usuarioEditar.isActivo());
            cargar();
            mensaje("Usuario actualizado.", false);
        } catch (Exception e) {
            mensaje("Error al actualizar: " + e.getMessage(), true);
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
