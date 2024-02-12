import java.io.*;
import java.net.*;

public class IMClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 3) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number> <username>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        //*GET USERNAME AS ONE OF THE ARGS */
        String userName = args[2];

        try (
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            //*INITIAL MESSAGE IS USERNAME -> SEND TO SERVER TO PROCESS*/
            out.println(userName);

            //*WHILE BUFFER STREAM IS OPEN FROM SERVER */
            while ((fromServer = in.readLine()) != null) {
                //*PRINT MESSAGES FROM SERVER */
                System.out.println("Server: " + fromServer);
                //*IF MESSAGE IS BYE. END CONNECTION */
                if (fromServer.equals("Bye."))
                    break;
                
                //*GET INPUT FROM CLIENT SIDE */
                System.out.print(userName + ": ");
                fromUser = stdIn.readLine();
                //*IF THERE IS A VALUE ENTERED -> SEND TO SERVER */
                if (fromUser != null) {
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}