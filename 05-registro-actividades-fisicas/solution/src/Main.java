import com.sun.net.httpserver.HttpServer;
import controller.ActivityController;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Clase principal que arranca el servidor HTTP.
 * Configura el servidor y registra los handlers.
 */
public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            // Crear servidor HTTP en puerto 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Registrar el controlador para el contexto /api/activities
            ActivityController controller = new ActivityController();
            server.createContext("/api/activities", controller);
            
            // Configurar executor por defecto (un thread por request)
            server.setExecutor(null);
            
            // Iniciar servidor
            server.start();
            
            System.out.println("╔════════════════════════════════════════════════╗");
            System.out.println("║   Servidor HTTP iniciado correctamente        ║");
            System.out.println("║   Puerto: " + PORT + "                                  ║");
            System.out.println("║   URL: http://localhost:" + PORT + "                ║");
            System.out.println("║   Endpoints disponibles:                       ║");
            System.out.println("║     - POST   /api/activities                   ║");
            System.out.println("║     - GET    /api/activities                   ║");
            System.out.println("║     - GET    /api/activities/{id}              ║");
            System.out.println("║     - PUT    /api/activities/{id}              ║");
            System.out.println("║     - DELETE /api/activities/{id}              ║");
            System.out.println("║     - GET    /api/activities/stats/weekly      ║");
            System.out.println("║     - GET    /api/activities/stats/monthly     ║");
            System.out.println("║     - GET    /api/activities/totals            ║");
            System.out.println("║                                                ║");
            System.out.println("║   Presiona Ctrl+C para detener el servidor    ║");
            System.out.println("╚════════════════════════════════════════════════╝");
            
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}