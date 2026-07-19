package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.services.EstadisticasService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class AuditoriaBean implements Serializable {

    @Inject
    private EstadisticasService service;

    private List<Map<String, Object>> auditorias;

    @PostConstruct
    public void init() {
        try {
            auditorias = service.getAuditorias();
        } catch (Exception e) {
            auditorias = List.of();
        }
    }

    public List<Map<String, Object>> getAuditorias() { return auditorias; }
}