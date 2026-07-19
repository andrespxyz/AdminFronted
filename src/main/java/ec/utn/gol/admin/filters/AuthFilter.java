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

        // Permitir acceso libre al login y recursos estáticos
        boolean esPublico = uri.contains("login.xhtml")
                || uri.contains("jakarta.faces.resource")
                || uri.contains("/resources/");

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