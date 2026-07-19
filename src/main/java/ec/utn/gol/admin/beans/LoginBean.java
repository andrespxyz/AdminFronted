package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.services.EstadisticasService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private EstadisticasService service;

    private String email;
    private String password;
    private String nombreUsuario;
    private String rol;
    private int usuarioId;
    private boolean logueado = false;

    public String login() {
        try {
            var resultado = service.login(email, password);
            if (resultado == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas.", null));
                return null;
            }
            this.usuarioId = ((Number) resultado.get("id")).intValue();
            this.nombreUsuario = (String) resultado.get("nombreUsuario");
            this.rol = (String) resultado.get("rol");
            this.logueado = true;

            if (!"administrador".equals(rol)) {
                this.logueado = false;
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Solo administradores pueden acceder.", null));
                return null;
            }
            return "/index?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login?faces-redirect=true";
    }

    public boolean isLogueado() {
        return logueado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
