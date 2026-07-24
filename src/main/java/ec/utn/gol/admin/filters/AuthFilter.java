package ec.utn.gol.admin.filters;

import ec.utn.gol.admin.beans.LoginBean;
import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("*.xhtml")
public class AuthFilter implements Filter {

    @Inject
    private LoginBean loginBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        // Permitir acceso libre al login y recursos estáticos. Antes usaba
        // uri.contains(...), que trata como pública cualquier URI que tenga esa
        // subcadena en cualquier posición, no solo esas rutas exactas.
        String contexto = req.getContextPath();
        boolean esPublico = uri.equals(contexto + "/login.xhtml")
                || uri.contains("/jakarta.faces.resource/")
                || uri.startsWith(contexto + "/resources/");

        if (esPublico) {
            chain.doFilter(request, response);
            return;
        }

        // Si no está logueado, redirigir al login
        if (!loginBean.isLogueado()) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }
}