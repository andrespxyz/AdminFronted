package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.services.EstadisticasService;
import ec.utn.gol.admin.services.UTNGolCoinService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class ReportesBean implements Serializable {

    @Inject
    private EstadisticasService estadisticasService;

    @Inject
    private UTNGolCoinService golcoinService;

    private BigDecimal totalCirculacion = BigDecimal.ZERO;
    private List<Map<String, Object>> partidosMasPredicciones;

    @PostConstruct
    public void init() {
        // Antes, si UTNGolCoinAPI fallaba (caído, token vencido, timeout), esto
        // quedaba indistinguible de "0 en circulación" / "sin datos" real — el
        // admin no tenía forma de saber que el reporte estaba fallando.
        try {
            Map<String, Object> circulacion = golcoinService.getCirculacion();
            totalCirculacion = new BigDecimal(circulacion.get("totalCirculacion").toString());
        } catch (Exception e) {
            totalCirculacion = BigDecimal.ZERO;
            mensajeError("No se pudo obtener el total en circulación: " + e.getMessage());
        }

        try {
            partidosMasPredicciones = golcoinService.getPartidosConMasPredicciones();
        } catch (Exception e) {
            partidosMasPredicciones = List.of();
            mensajeError("No se pudo obtener el reporte de partidos con más predicciones: " + e.getMessage());
        }
    }

    private void mensajeError(String texto) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, null));
    }

    public BigDecimal getTotalCirculacion() { return totalCirculacion; }
    public List<Map<String, Object>> getPartidosMasPredicciones() { return partidosMasPredicciones; }
}