package org.agtokty.ardunio;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Scanner;

public class Main {

    private  static ArdunioReader ardunioReader;

    public static void main(String[] args) throws Exception {


        SerialPort[] ports = SerialPort.getCommPorts();

        int selectedPort = -1;


        for (int i = 0; i < ports.length; i++) {
            if (ports[i].getSystemPortName().equals("tty.usbmodem14201")) {
                selectedPort = i;
            }
        }

        SerialPort port;
        if (selectedPort == -1) {
            System.out.println("Select port : ");
            for (int i = 0; i < ports.length; i++) {
                System.out.println(i + " - " + ports[i].getSystemPortName());
            }
            Scanner in = new Scanner(System.in);
            int index = in.nextInt();
            port = ports[index];
        } else {
            port = ports[selectedPort];
        }


        ardunioReader = new ArdunioReader(port);
        ardunioReader.setEventListener(new IArdunioComPortListener() {
            public void onEvent(String data) {
                System.out.println("--------");
                System.out.println(data);
            }
        });

        ardunioReader.start();

        Main object = new Main();
        object.checkPort();
    }

    private synchronized void checkPort() {
        while (true) {
            try {
                this.wait(2000);
                ardunioReader.checkIfOpen();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
