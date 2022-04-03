import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private DataInputStream in;
    private DataOutputStream out;
    private ServerSocket listener;
    private Socket socket;

    public void serverSetting(){
        try {
            listener = new ServerSocket(9999);
            System.out.println("---- 연결 대기 중 ----");
            socket = listener.accept();
            System.out.println("연결되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ioSetting() {
        try {
            in=new DataInputStream(socket.getInputStream());
            out=new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeAll(){
        try{
            listener.close();
            socket.close();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dataAccept() {
        new Thread(new Runnable() {
            boolean isThread = true;
            @Override
            public void run() {
                while (isThread) {
                    try {
                        String data = in.readUTF();
                        if (data.equals("!bye"))
                            isThread = false;
                        else
                            System.out.println("Client : "+data);
                    } catch (Exception e){
                    }
                }
                closeAll();
                System.out.println("종료되었습니다.");
            }
        }).start();
    }

    public void dataSend() {
        new Thread(new Runnable() {
            Scanner scanner = new Scanner(System.in);
            boolean isThread = true;
            @Override
            public void run() {
                while (isThread){
                    try {
                        System.out.print("보내기 >> ");
                        String data = scanner.nextLine();
                        if (data.equals("!bye"))
                            isThread=false;
                        else
                            out.writeUTF(data);
                    } catch (Exception e ){

                    }
                }
            }
        }).start();
    }

    public Server() {
        serverSetting();
        ioSetting();
        dataAccept();
        dataSend();
    }

    public static void main(String[] args) {
        new Server();
    }
}
