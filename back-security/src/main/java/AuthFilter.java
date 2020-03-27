import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {
    private HttpServletRequest httpRequest;

    private static final String[] loginRequiredURLs = {
            "/system", "/admin"
    };
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        httpRequest = (HttpServletRequest) request;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (path.startsWith("/admin/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("userName") != null);

        String loginURI = httpRequest.getContextPath() + "/auth/login";
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
        chain.doFilter(request, response);
        /*if (isLoggedIn && isLoginRequest) {
            // the user is already logged in and he's trying to login again
            // then forward to the homepage
//            httpRequest.getRequestDispatcher("/auth/check").forward(request, response);

        } else if (!isLoggedIn && isLoginRequired()) {
            // the user is not logged in, and the requested page requires
            // authentication, then forward to the login page
            *//*String loginPage = "/login";
            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginPage);
            dispatcher.forward(request, response);*//*
        } else {
            // for other requested pages that do not require authentication
            // or the user is already logged in, continue to the destination

        }*/
    }

    private boolean isLoginRequired() {
        String requestURL = httpRequest.getRequestURL().toString();

        for (String loginRequiredURL : loginRequiredURLs) {
            if (requestURL.contains(loginRequiredURL)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() {

    }
}
