package it.sevenbits.tasks;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class TasksServlet extends HttpServlet {

    private TaskRepository repo;

    @Override
    public void init() throws ServletException {
        super.init();
        repo = TaskRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getHeader("Authorization");
        if (sessionId == null || "".equals(sessionId)) {
            resp.setStatus(401);
            return;
        }

        resp.setStatus(200);
        resp.getWriter().write("[\n");
        for (String key : repo.getTasks().keySet()) {
            Task task = repo.getTask(key);
            resp.getWriter().write(String.format("{\n" +
                    "  \"id\": \"%s\",\n" +
                    "  \"name\": \"%s\"\n" +
                    "}", task.getId().toString(), task.getName()));
        }
        resp.getWriter().write("\n]");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sessionId = req.getHeader("Authorization");
        if (sessionId == null || "".equals(sessionId)) {
            resp.setStatus(401);
            return;
        }

        String name = req.getParameter("name");

        Task task = new Task(name, UUID.randomUUID());

        repo.addTask(task);

        resp.getWriter().write(String.format("{\n" +
                "  \"id\": \"%s\",\n" +
                "  \"name\": \"%s\"\n" +
                "}", task.getId().toString(), task.getName()));

        resp.setStatus(201);
        resp.setHeader("Location", "http://localhost:8080/item?id=" + task.getId().toString());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

}
