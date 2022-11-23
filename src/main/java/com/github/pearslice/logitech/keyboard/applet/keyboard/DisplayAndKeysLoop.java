package com.github.pearslice.logitech.keyboard.applet.keyboard;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DisplayAndKeysLoop implements Runnable {

    private final LogitechKeyboardDevice device;
    private final ScheduledThreadPoolExecutor taskScheduler = new ScheduledThreadPoolExecutor(1);
    private List<ButtonListener> buttonListeners;
    private boolean[] buttonsState;
    private boolean[] prevButtonsState;

    public DisplayAndKeysLoop(LogitechKeyboardDevice keyboard, String appletName) {
        buttonListeners = new ArrayList<>();
        char[] name = appletName != null ? (appletName + "\0").toCharArray() : "NoName\0".toCharArray();
        this.device = keyboard;
        this.buttonsState = new boolean[keyboard.getNumberOfButtons()];
        this.prevButtonsState = new boolean[keyboard.getNumberOfButtons()];

        boolean init = device.init(name);
        log.info("Applet {} was {}initialized on {}", new String(name,0, name.length-1), init ? "" : "NOT ", device);

        taskScheduler.scheduleAtFixedRate(this, 100, 10, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        taskScheduler.shutdown();
    }

    public void addButtonListener(ButtonListener listener) {
        this.buttonListeners.add(listener);
    }

    @SneakyThrows
    @Override
    public void run() {
        buttonsState = device.areButtonsPressed();

        for (int i = 0; i < device.getNumberOfButtons(); i++) {
            if (prevButtonsState[i] != buttonsState[i]) {
                prevButtonsState[i] = buttonsState[i];

                int button = i;
                if (buttonsState[i]) {
                    buttonListeners.forEach(l -> l.buttonPressed(button));
                } else {
                    buttonListeners.forEach(l -> l.buttonReleased(button));
                }
            }
        }

        Thread.sleep(10);
        device.update();
    }
}