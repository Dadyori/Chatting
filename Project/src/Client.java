import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void connet() {
        try {
            socket = new Socket("localhost", 9999);
            System.out.println("연결 성공");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ioSetting() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeAll() {
        try {
            socket.close();
            in.close();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dataSend() {
        new Thread(new Runnable() {
            Scanner scanner = new Scanner(System.in);
            boolean isThread = true;
            @Override
            public void run() {
                while (isThread) {
                    try {
                        System.out.print("보내기 >> ");
                        String data = scanner.nextLine();
                        if (data.equals("!bye")){
                            isThread = false;
                        }
                        else
                            out.writeUTF(data);
                    } catch (Exception e){

                    }
                }
            }
        }).start();
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
                            System.out.println("Server : "+data);
                    } catch (Exception e){

                    }
                }
                closeAll();
                System.out.println("종료되었습니다.");
            }
        }).start();
    }

    public Client() {
        connet();
        ioSetting();
        dataSend();
        dataAccept();
    }

    public static void main(String[] args) {
        new Client();
    }
}
