package ec.utn.gol.admin.beans;

import ec.utn.gol.admin.services.EstadisticasService;
import ec.utn.gol.admin.services.UTNGolCoinService;
import jakarta.annotation.PostConstruct;
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
        try {
            Map<String, Object> circulacion = golcoinService.getCirculacion();
            totalCirculacion = new BigDecimal(circulacion.get("totalCirculacion").toString());
        } catch (Exception e) {
            totalCirculacion = BigDecimal.ZERO;
        }

        try {
            partidosMasPredicciones = golcoinService.getPartidosConMasPredicciones();
        } catch (Exception e) {
            partidosMasPredicciones = List.of();
        }
    }

    public BigDecimal getTotalCirculacion() { return totalCirculacion; }
    public List<Map<String, Object>> getPartidosMasPredicciones() { return partidosMasPredicciones; }
}