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
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            //*SEND INITIAL MESSAGE TO CLIENT THAT THE CONNECTION IS ESTABLISHED */
            outputLine = kkp.processInput(null);
            out.println(outputLine);
            
            //**THE FIRST LINE FROM THE USER IS THEIR NAME */
            inputLine = in.readLine();
            this.userName = inputLine;
            
            //**WHILE YOU CAN READ INPUT FROM THE BUFFER STREAM */
            while ((inputLine = in.readLine()) != null) {
                //**ALL MESSAGES IN THIS THREAD ARE FROM THE USERNAME THAT IS SAVED */
                //*print to this thread */
                System.out.println(this.userName + ":" + inputLine);
                //*determine output */
                outputLine = kkp.processInput(inputLine);

                //*If the output is bye close connection */
                if (outputLine.equals("Bye.")){
                    break;
                }else{
                    //*GET INPUT FROM SERVER SIDE */
                    System.out.print("Server: ");
                    outputLine = stdIn.readLine();
                    //*SEND OUTPUT TO CLIENT ON THREAD */
                    out.println(outputLine);
                }
            }
            //*CLOSE CONNECTION IF YOU ARE DONE */
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}