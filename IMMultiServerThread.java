import java.net.*;
import java.io.*;

public class IMMultiServerThread extends Thread {
    private Socket socket = null;
    private String userName;

    public IMMultiServerThread(Socket socket) {
        super("KKMultiServerThread");
        this.socket = socket;
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            String inputLine, outputLine;
            IMProtocol kkp = new IMProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                this.userName = inputLine;
                System.out.println(this.userName + ":" + outputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}