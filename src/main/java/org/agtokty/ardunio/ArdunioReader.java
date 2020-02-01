package org.agtokty.ardunio;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArdunioReader {

    private static final Logger logger = LogManager.getLogger(ArdunioReader.class);

    private SerialPort port;

    private static IArdunioComPortListener ardunioComPortListener;

    ArdunioReader(SerialPort port) {
        this.port = port;
    }

    public void setEventListener(IArdunioComPortListener ardunioComPortListener) {
        this.ardunioComPortListener = ardunioComPortListener;
    }

    public void start() throws Exception {
        if (port == null)
            throw new Exception("serial port instance is null!");
        port.openPort();
        port.setBaudRate(9600);
        port.addDataListener(new SerialPortPacketListener() {
            private String messages = "";

            @Override
            public int getPacketSize() {
                return 10;
            }

            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            public void serialEvent(SerialPortEvent event) {

                int eventType = event.getEventType();

                messages += new String(event.getReceivedData());
                while (messages.contains("\n")) {
                    String[] message = messages.split("\\n", 2);
                    messages = (message.length > 1) ? message[1] : "";
                    String line = message[0];
                    logger.info(line);

                    if (ardunioComPortListener != null)
                        ardunioComPortListener.onEvent(line);
                }
            }
        });
    }

    public void checkIfOpen() throws Exception {
        if (port == null)
            return;

        if (port.isOpen() == false) {
            start();
        }
    }
}
