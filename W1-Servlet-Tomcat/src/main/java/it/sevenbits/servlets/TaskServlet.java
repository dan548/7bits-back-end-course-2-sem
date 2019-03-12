package it.sevenbits.servlets;

import it.sevenbits.servlets.repository.TaskRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskRepository repo = TaskRepository.getInstance();

        String id = req.getParameter("id");

        Task task = repo.getTask(id);

        if (task == null) {
            resp.setStatus(404);
        } else {
            resp.setStatus(200);
            resp.getWriter().write(String.format("{\n\"id\": %s,\n\"name\": \"%s\"\n}", id, task.getName()));
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TaskRepository repo = TaskRepository.getInstance();

        String id = req.getParameter("id");

        Task task = repo.getTask(id);

        if (task == null) {
            resp.setStatus(404);
        } else {
            repo.deleteTask(id);
            resp.setStatus(200);
            resp.getWriter().write(String.format("{\"id\": %s\"}", id));
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }
}
