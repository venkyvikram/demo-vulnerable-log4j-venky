package hello;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * A friendly web server.
 *
 * Note this demo does not actually use Spring Boot.
 */
@SuppressWarnings("restriction") // avoid com.sun.net.httpserver warnings
public class Application {
	
	public static void main(String[] args) throws IOException {
		int port = getPort("PORT", 8080);
		HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
		server.createContext("/", Application::greet);
		server.start();
		System.out.printf("Server started on port %d\n", port);
	}
	
	private static void greet(HttpExchange httpExchange) throws IOException {
		String response = "Hello, World!";
		byte[] data = response.getBytes();
		httpExchange.sendResponseHeaders(200, data.length);
		httpExchange.getResponseBody().write(data);
		httpExchange.close();
		System.out.printf("%s %s - %s %s\n", httpExchange.getRemoteAddress(), httpExchange.getProtocol(),
				httpExchange.getRequestMethod(), httpExchange.getRequestURI());
	}

	private static int getPort(String env, int defaultPort) {
		String value = System.getenv(env);
		if (value == null || value.isEmpty()) {
			return defaultPort;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			System.err.printf("Unable to parse port from \"%s\": %s", value, ex.getMessage());
			return defaultPort;
		}
	}
}
