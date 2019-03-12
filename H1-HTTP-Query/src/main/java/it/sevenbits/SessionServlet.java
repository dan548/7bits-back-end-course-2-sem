package it.sevenbits;

import it.sevenbits.session.SessionMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class SessionServlet extends HttpServlet {

    private SessionMap sessions;

    @Override
    public void init() throws ServletException {
        super.init();
        sessions = SessionMap.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        if (req.getCookies() == null) {
            resp.setStatus(403);
            return;
        }

        Optional<Cookie> cookie = Arrays.stream(req.getCookies())
                .filter(c -> "sessionId".equals(c.getName()))
                .findFirst();

        if (!cookie.isPresent()) {
            resp.setStatus(403);
            return;
        }

        String userName = sessions.getSession(cookie.get().getValue());

        if (userName == null) {
            resp.setStatus(404);
            return;
        }

        resp.getWriter().write(
                String.format("<!DOCTYPE html><html><body>Current user is %s</body></html>", userName));
        resp.setContentType("application/json");
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        String userName = req.getParameter("name");

        if (userName == null || "".equals(userName)) {
            resp.setStatus(403);
            return;
        }

        UUID sessionId = UUID.randomUUID();
        sessions.addSession(sessionId.toString(), userName);

        Cookie cookie = new Cookie("sessionId", sessionId.toString());
        resp.addCookie(cookie);

        resp.setContentType("application/json");
        resp.setStatus(200);
    }
}
