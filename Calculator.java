import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Calculator {

    public static void main(String[] args) throws IOException {
        int port = 8080;

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Context for calculations
        server.createContext("/calc", new CalcHandler());

        // NEW: root context to avoid 404 on base URL
        server.createContext("/", exchange -> {
            String response = "Calculator server running. Use /calc?a=..&b=..&op=add|sub|mul|div";
            exchange.sendResponseHeaders(200, response.getBytes().length); // UPDATED: use getBytes().length
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.setExecutor(null);
        System.out.println("Calculator server running on port " + port);
        server.start();
    }

    static class CalcHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;

            try {
                URI requestURI = exchange.getRequestURI();
                Map<String, String> params = queryToMap(requestURI.getQuery());

                double a = Double.parseDouble(params.getOrDefault("a", "0"));
                double b = Double.parseDouble(params.getOrDefault("b", "0"));
                String op = params.getOrDefault("op", "add");

                double result;

                switch (op) {
                    case "add":
                        result = a + b;
                        break;
                    case "sub":
                        result = a - b;
                        break;
                    case "mul":
                        result = a * b;
                        break;
                    case "div":
                        if (b == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result = a / b;
                        break;
                    default:
                        response = "Invalid operation. Use add, sub, mul, div.";
                        sendResponse(exchange, response);
                        return;
                }

                response = "Result: " + result;

            } catch (Exception e) {
                response = "Error: " + e.getMessage();
            }

            sendResponse(exchange, response);
        }

        private void sendResponse(HttpExchange exchange, String response) throws IOException {
            exchange.sendResponseHeaders(200, response.getBytes().length); // UPDATED: use getBytes().length
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> map = new HashMap<>();
            if (query == null) return map;

            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1) {
                    map.put(pair[0], pair[1]);
                }
            }
            return map;
        }
    }
}
