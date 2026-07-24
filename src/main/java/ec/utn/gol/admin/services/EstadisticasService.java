package ec.utn.gol.admin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.utn.gol.admin.beans.LoginBean;
import ec.utn.gol.admin.models.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import java.util.Arrays;
import java.util.List;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import javax.net.ssl.SSLContext;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import java.util.Map;

@ApplicationScoped
public class EstadisticasService {

    @Inject
    private LoginBean loginBean;

    // Adjunta el id del admin logueado (para auditoría, ver AuditoriaHelper) y el
    // JWT de sesión: EstadisticasAPI ahora exige [Authorize(Roles="admin")] en sus
    // endpoints mutantes y en algunos GET (Usuarios, Auditorías), donde antes no
    // validaba ningún token.
    private void agregarHeaderUsuario(org.apache.hc.client5.http.classic.methods.HttpUriRequestBase request) {
        request.setHeader("X-Usuario-Id", String.valueOf(loginBean.getUsuarioId()));
        agregarAuthorization(request);
    }

    private void agregarAuthorization(org.apache.hc.client5.http.classic.methods.HttpUriRequestBase request) {
        String token = loginBean.getToken();
        if (token != null && !token.isBlank()) {
            request.setHeader("Authorization", "Bearer " + token);
        }
    }

    // Antes solo crearPartido comprobaba el código de estado de la respuesta; el
    // resto descartaba la respuesta con `response -> null`, así que un error del
    // backend (400/401/403/409...) se mostraba como "éxito" en la UI. Ahora todas
    // las llamadas mutantes pasan por acá y lanzan si el status no es 2xx.
    private void ejecutarMutante(org.apache.hc.client5.http.classic.methods.HttpUriRequestBase request) throws Exception {
        agregarHeaderUsuario(request);
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            client.execute(request, response -> {
                int status = response.getCode();
                if (status < 200 || status >= 300) {
                    String cuerpo = "";
                    try {
                        cuerpo = new String(response.getEntity().getContent().readAllBytes());
                    } catch (Exception ignored) {
                    }
                    throw new RuntimeException("EstadisticasAPI respondió " + status + ": " + cuerpo);
                }
                return null;
            });
        }
    }

    private CloseableHttpClient crearClienteSinSSL() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, (chain, authType) -> true)
                .build();
        var socketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        var connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(socketFactory)
                .build();
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    private static final String BASE_URL = "https://localhost:7274/api";
    private final ObjectMapper mapper = new ObjectMapper();

    // ========== SELECCIONES ==========
    public List<Seleccion> getSelecciones() throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpGet request = new HttpGet(BASE_URL + "/Selecciones");
            return client.execute(request, response -> {
                Seleccion[] arr = mapper.readValue(response.getEntity().getContent(), Seleccion[].class);
                return Arrays.asList(arr);
            });
        }
    }

    public void crearSeleccion(Seleccion s) throws Exception {
        HttpPost request = new HttpPost(BASE_URL + "/Selecciones");
        request.setEntity(new StringEntity(mapper.writeValueAsString(s), ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    public void actualizarSeleccion(Seleccion s) throws Exception {
        HttpPut request = new HttpPut(BASE_URL + "/Selecciones/" + s.getId());
        request.setEntity(new StringEntity(mapper.writeValueAsString(s), ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    // ========== SEDES ==========
    public List<Sede> getSedes() throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpGet request = new HttpGet(BASE_URL + "/Sedes");
            return client.execute(request, response -> {
                Sede[] arr = mapper.readValue(response.getEntity().getContent(), Sede[].class);
                return Arrays.asList(arr);
            });
        }
    }

    public void crearSede(Sede s) throws Exception {
        HttpPost request = new HttpPost(BASE_URL + "/Sedes");
        request.setEntity(new StringEntity(mapper.writeValueAsString(s), ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    public void actualizarSede(Sede s) throws Exception {
        HttpPut request = new HttpPut(BASE_URL + "/Sedes/" + s.getId());
        request.setEntity(new StringEntity(mapper.writeValueAsString(s), ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    // ========== PARTIDOS ==========
    public List<Partido> getPartidos() throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpGet request = new HttpGet(BASE_URL + "/Partidos");
            return client.execute(request, response -> {
                Partido[] arr = mapper.readValue(response.getEntity().getContent(), Partido[].class);
                return Arrays.asList(arr);
            });
        }
    }

    public void crearPartido(Partido p) throws Exception {
        HttpPost request = new HttpPost(BASE_URL + "/Partidos");
        request.setEntity(new StringEntity(mapper.writeValueAsString(p), ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    public void actualizarPartido(Partido p) throws Exception {
        HttpPut request = new HttpPut(BASE_URL + "/Partidos/" + p.getId());
        request.setEntity(new StringEntity(mapper.writeValueAsString(p), ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    public void registrarResultado(int partidoId, int golesLocal, int golesVisitante) throws Exception {
        HttpPut request = new HttpPut(BASE_URL + "/Partidos/" + partidoId + "/resultado");
        String body = String.format("{\"golesLocal\":%d,\"golesVisitante\":%d}", golesLocal, golesVisitante);
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    // ========== USUARIOS ==========
    public List<Usuario> getUsuarios() throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpGet request = new HttpGet(BASE_URL + "/Usuarios");
            agregarAuthorization(request);
            return client.execute(request, response -> {
                Usuario[] arr = mapper.readValue(response.getEntity().getContent(), Usuario[].class);
                return Arrays.asList(arr);
            });
        }
    }

    public void actualizarUsuario(Usuario u) throws Exception {
        HttpPut request = new HttpPut(BASE_URL + "/Usuarios/" + u.getId());
        request.setEntity(new StringEntity(mapper.writeValueAsString(u), ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    // ========== AUTH ==========
    public Map<String, Object> login(String email, String password) throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpPost request = new HttpPost(BASE_URL + "/Auth/login");
            // Antes se armaba el JSON con String.format: un email/password con
            // comillas o backslash rompía el payload. mapper.writeValueAsString
            // escapa correctamente, igual que en el resto del servicio.
            String body = mapper.writeValueAsString(Map.of("email", email, "password", password));
            request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            return client.execute(request, response -> {
                if (response.getCode() != 200) {
                    return null;
                }
                return mapper.readValue(response.getEntity().getContent(),
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                });
            });
        }
    }

    //============ Registrar Auditoria =========
    // deprecated: reemplazado por header X-Usuario-Id, ver EstadisticasAPI
    // (AuditoriaHelper). Ya no se invoca desde los beans; se deja sin borrar
    // para referencia del flujo anterior y como plan de rollback rápido.
    public void registrarAuditoria(int usuarioId, String accion, String detalle) throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpPost request = new HttpPost(BASE_URL + "/Auditorias");
            String body = String.format(
                    "{\"usuarioId\":%d,\"accion\":\"%s\",\"detalle\":\"%s\"}",
                    usuarioId, accion, detalle);
            request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            client.execute(request, response -> null);
        }
    }

    //========== Get Auditorias ================
    public List<Map<String, Object>> getAuditorias() throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpGet request = new HttpGet(BASE_URL + "/Auditorias");
            agregarAuthorization(request);
            return client.execute(request, response -> {
                return mapper.readValue(response.getEntity().getContent(),
                        new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {
                });
            });
        }
    }

    //========== Get Resumen =====================
    public Map<String, Object> getResumen() throws Exception {
        try (CloseableHttpClient client = crearClienteSinSSL()) {
            HttpGet request = new HttpGet(BASE_URL + "/Reportes/resumen");
            return client.execute(request, response
                    -> mapper.readValue(response.getEntity().getContent(),
                            new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
                    }));
        }
    }

    //========== PARTIDOS ===============
    public void actualizarEstadoPartido(int partidoId, String estado) throws Exception {
        HttpPut request = new HttpPut(BASE_URL + "/Partidos/" + partidoId + "/estado");
        String body = mapper.writeValueAsString(Map.of("estado", estado));
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        ejecutarMutante(request);
    }

    public void eliminarSeleccion(int id) throws Exception {
        ejecutarMutante(new HttpDelete(BASE_URL + "/Selecciones/" + id));
    }

    public void eliminarSede(int id) throws Exception {
        ejecutarMutante(new HttpDelete(BASE_URL + "/Sedes/" + id));
    }

    public void eliminarPartido(int id) throws Exception {
        ejecutarMutante(new HttpDelete(BASE_URL + "/Partidos/" + id));
    }
}
