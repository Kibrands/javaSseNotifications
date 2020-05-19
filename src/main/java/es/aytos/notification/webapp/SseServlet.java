package es.aytos.notification.webapp;

import es.aytos.notification.model.Notification;
import es.aytos.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SseServlet extends HttpServlet {

    @Autowired
    private NotificationService notificationService;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set response content type
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        response.setHeader("Content-Type", "text/event-stream; charset=utf-8");
        request.setAttribute("lastNotification", 0);

        // Actual logic goes here.
        PrintWriter out = response.getWriter();

        final Boolean[] running = {true};

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (running[0]) {
                    Notification notification = notificationService.getNotification();
                    System.out.println(request.getAttribute("lastNotification") + " - " + notification.getId());
                    if (!((Integer) request.getAttribute("lastNotification")).equals(notification.getId())) {
                        System.out.println("Ha cambiado el precio, enviando notifiación...");
                        request.setAttribute("lastNotification", notification.getId());
                        out.write(String.format("data:{\"id\":%d,\"message\":\"%s\"}%n%n", notification.getId(), notification.getMessage()));
                        System.out.println("Ahora la variable de sesión es " + request.getAttribute("lastNotification"));
                    }
                    out.flush();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        running[0] = false;
                    }
                }
            }
        };

        runnable.run();

    }

    public void destroy() {
        // do nothing.
    }

}
