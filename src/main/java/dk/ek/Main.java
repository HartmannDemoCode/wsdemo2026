package dk.ek;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Set<WsContext> clients = ConcurrentHashMap.newKeySet();
    static void main() {
        Javalin app = Javalin.create(context->{
            context.staticFiles.add("public"); // Show Javalin where to look for static files like html files, css etc.
            context.routes.ws("/ws/dice", ws->{
                ws.onConnect(ctx->{
                    clients.add(ctx);
                    System.out.println("New client connected with ID: "+ctx.sessionId);
                });
                ws.onClose(ctx->{
                    clients.remove(ctx);
                    System.out.println("CLient removed with ID: "+ctx.sessionId);
                });
                ws.onMessage(client->{
                    String messageFromClient = client.message();
                    System.out.println(messageFromClient);
                    client.send("Hello client! I am fine thank you");
                });
            });

        });
    app.start(7071);
    }
}
