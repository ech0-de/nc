import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class nc {
    private static boolean crlf = false;
    private static boolean listen = false;

    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                System.out.printf("Usage: java %s [-Cl] [destination] [port]%n", nc.class.getName());
                System.out.println("  -C   Use CRLF for EOL sequence");
                System.out.println("  -l   Bind and listen for incoming connections");
                System.exit(1);
            }

            for (int i = 0; i < args.length - 2; i += 1) {
                listen = listen || args[i].indexOf('l') >= 0;
                crlf = crlf || args[i].indexOf('C') >= 0;
            }

            Socket socket;
            int port = Integer.parseInt(args[args.length - 1]);

            if (listen) {
                ServerSocket serverSocket = new ServerSocket(port, 0);
                socket = serverSocket.accept();
            } else {
                socket = new Socket(args[args.length - 2], port);
            }

            ExecutorService pool = Executors.newFixedThreadPool(2);

            HashSet<Callable<Void>> tasks = new HashSet<Callable<Void>>();
            tasks.add(() -> pipe(System.in, socket.getOutputStream()));
            tasks.add(() -> pipe(socket.getInputStream(), System.out));

            pool.invokeAny(tasks);
            pool.shutdownNow();
            socket.close();
            System.exit(0);
        } catch (NumberFormatException e) {
            System.err.printf("Error: Invalid port specification: %s%n", e.getMessage().split("\"")[1]);
            System.exit(1);
        } catch (Exception e) {
            System.err.printf("Error: %s%n", e.toString());
            System.exit(1);
        }
    }

    private static Void pipe(InputStream is, OutputStream os) throws Exception {
        byte[] buffer = new byte[1024];
        for (int n; (n = is.read(buffer)) >= 0; ) {
            if (n > 0) {
                if (crlf && buffer[n-1] == '\n' && (n == 1 || buffer[n-2] != '\r')) {
                    buffer[n-1] = '\r';
                    if (n < 1024) {
                        buffer[n] = '\n';
                        n += 1;
                    }
                    os.write(buffer, 0, n);
                    if (n == 1024 && buffer[n - 1] != '\n') {
                        os.write('\n');
                    }
                } else {
                    os.write(buffer, 0, n);
                }
            }
        }

        return null;
    }
}
