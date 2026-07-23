package ec.utn.gol.admin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.utn.gol.admin.beans.LoginBean;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UTNGolCoinService {

    private static final String BASE_URL = "http://localhost:8080/UTNGolCoinAPI/api";
    private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    private LoginBean loginBean;

    // RF25 — /reportes/* ahora exige rol admin en UTNGolCoinAPI; se adjunta el
    // token del administrador logueado (emitido por EstadisticasAPI en el login).
    private void agregarHeaderAuth(HttpGet request) {
        request.setHeader("Authorization", "Bearer " + loginBean.getToken());
    }

    public Map<String, Object> getCirculacion() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/reportes/circulacion");
            agregarHeaderAuth(request);
            return client.execute(request, response ->
                mapper.readValue(response.getEntity().getContent(),
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}));
        }
    }

    public List<Map<String, Object>> getPartidosConMasPredicciones() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/reportes/partidos-mas-predicciones");
            agregarHeaderAuth(request);
            return client.execute(request, response ->
                mapper.readValue(response.getEntity().getContent(),
                    new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {}));
        }
    }
}
