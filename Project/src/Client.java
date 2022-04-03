import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client extends JFrame{
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private boolean connectStatus;
    private boolean stop;

    JTextArea text;
    JTextField textField;

    public void ClientFrame () {
        setTitle("클라이언트 채팅 서버");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(600,600,700,500);
        getContentPane().setLayout(null);

        text = new JTextArea();
        text.setEditable(false);
//        text.setBackground(Color.CYAN);
        getContentPane().add(text);

        textField = new JTextField();
        textField.setBounds(5,430,590,30);
        getContentPane().add(textField);
        textField.setColumns(10);

        JButton inputBtn = new JButton("입력");
        inputBtn.setBackground(Color.YELLOW);
        inputBtn.setForeground(Color.BLACK);
        inputBtn.setBounds(595,430,90,30);
        getContentPane().add(inputBtn);

        JPanel panel = new JPanel();
        panel.setBounds(5,5,680,420);
        getContentPane().add(panel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(5,5,680,420);
        getContentPane().add(scrollPane);
        scrollPane.setViewportView(text);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataSend();
            }
        });

        inputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataSend();
            }
        });

        setVisible(true);
        setResizable(false);
        startService();
        textField.requestFocus();
    }

    public void startService() {
        connet();
        ioSetting();
        new Thread(new Runnable() {
            @Override
            public void run() {
                dataAccept();
            }
        }).start();
    }

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
        try {
            String data = textField.getText();
            if (data.isBlank()){
                return;
            }
            text.append("나 >> "+data+"\n");
            if (data.equals("!bye")){
                stop=true;
                closeAll();
            }else {
                out.writeUTF(data);
                textField.setText("");
                textField.requestFocus();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void dataAccept() {
        try {
            while (!stop){
                text.append("SERVER >> "+in.readUTF()+"\n");
            }
            closeAll();
        } catch (EOFException e){
            text.append("서버 접속 해제\n");
            connectStatus=false;
        } catch (SocketException e) {
            text.append("서버 접속 해제\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Client() {
        ClientFrame();
    }

    public static void main(String[] args) {
        new Client();
    }
}
