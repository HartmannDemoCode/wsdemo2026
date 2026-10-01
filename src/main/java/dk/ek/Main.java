package dk.ek;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Set<WsContext> clients = ConcurrentHashMap.newKeySet();
    // TODO:
    // 1. Move functionality out of main to a seperate method in Main
    // 2. In js prompt for usename: let person = prompt("Please enter your name:"); and safe the name server side (in hashmap).
    // 3. Let Client massage directly to another client (@Helga some message) Find Helga in the map and send only to her.
    // 4. Refactor to put message logic / game logic into another class

    static void main() {
        Javalin app = Javalin.create(context->{
            context.staticFiles.add("public"); // Show Javalin where to look for static files like html files, css etc.
            context.routes.ws("/ws/dice", ws->{
                ws.onConnect(ctx->{
                    ctx.session.setIdleTimeout(Duration.ofMinutes(30));
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
//                    client.send("Hello client! I am fine thank you");
                    clients.forEach(cl->cl.send(messageFromClient));
                });
            });

        });
    app.start(7071);
    }
}
