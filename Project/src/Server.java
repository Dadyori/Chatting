import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        ServerSocket listener = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in); //키보드에서 읽을 Scanner 객체

        try{
            listener = new ServerSocket(9999);
            System.out.println("----- 연결 대기중 -----");
            socket=listener.accept(); //연결요청대기
            System.out.println("연결되었습니다");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true){
                String inputMessage = in.readLine();
                if (inputMessage.equalsIgnoreCase("bye")) {
                    System.out.println("클라이언트에서 연결 종료");
                    break;
                }
                System.out.println("클라이언트: "+inputMessage);
                System.out.print("보내기 >> "); //프롬프트
                String outputMessage = scanner.nextLine(); //한행읽기
                out.write(outputMessage+"\n"); //키보드에서 읽은 문자열 전송
                out.flush(); //out 스트림버퍼의 모든 문자열 전송
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        } finally {
            try {
                scanner.close();
                socket.close();
                listener.close();
            } catch (IOException e){
                System.out.println("채팅 중 오류가 발생하였습니다.");
            }
        }
    }
}
