package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.services.EstadisticasService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Map;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

    @Inject
    private EstadisticasService service;

    private Map<String, Object> resumen;

    @PostConstruct
    public void init() {
        try {
            resumen = service.getResumen();
        } catch (Exception e) {
            resumen = Map.of(
                    "totalSelecciones", 0,
                    "totalPartidos", 0,
                    "partidosFinalizados", 0,
                    "totalGoles", 0,
                    "totalUsuarios", 0
            );
        }
    }

    public Map<String, Object> getResumen() {
        return resumen;
    }
}
