package ec.utn.gol.admin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UTNGolCoinService {

    private static final String BASE_URL = "http://localhost:8080/UTNGolCoinAPI/api";
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String, Object> getCirculacion() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/reportes/circulacion");
            return client.execute(request, response ->
                mapper.readValue(response.getEntity().getContent(),
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}));
        }
    }

    public List<Map<String, Object>> getPartidosConMasPredicciones() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/reportes/partidos-mas-predicciones");
            return client.execute(request, response ->
                mapper.readValue(response.getEntity().getContent(),
                    new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {}));
        }
    }
}