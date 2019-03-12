package it.sevenbits.tasks;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TaskServlet extends HttpServlet {

    private TaskRepository repo;

    @Override
    public void init() throws ServletException {
        super.init();
        repo = TaskRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!check(req)) {
            resp.setStatus(401);
            return;
        }

        Task task = getTask(req);

        if (task == null) {
            resp.setStatus(404);
        } else {
            resp.setStatus(200);
            resp.getWriter().write(String.format("{\n\"id\": %s,\n\"name\": \"%s\"\n}", task.getId(), task.getName()));
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!check(req)) {
            resp.setStatus(401);
            return;
        }

        Task task = getTask(req);

        if (task == null) {
            resp.setStatus(404);
        } else {
            repo.deleteTask(task.getId().toString());
            resp.setStatus(200);
            resp.getWriter().write(String.format("{\"id\": %s\"}", task.getId()));
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    private Task getTask(HttpServletRequest req) {
        String id = req.getParameter("id");
        return repo.getTask(id);
    }

    private boolean check(HttpServletRequest req) {
        String sessionId = req.getHeader("Authorization");
        return sessionId != null && !"".equals(sessionId);
    }
}
