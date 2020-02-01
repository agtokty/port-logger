package org.agtokty.ardunio;

import java.util.EventListener;

public interface IArdunioComPortListener extends EventListener {
    void onEvent(String data);
}
