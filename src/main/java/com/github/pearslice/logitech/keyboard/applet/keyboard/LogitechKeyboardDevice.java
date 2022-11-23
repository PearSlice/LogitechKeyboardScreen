package com.github.pearslice.logitech.keyboard.applet.keyboard;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import lombok.Getter;

public enum LogitechKeyboardDevice {
    G13(LogiConstants.LOGI_LCD_TYPE_MONO, "G13", 4);

    private static final String PATH_TEMPLATE = "%s\\lib\\GameEnginesWrapper\\%s\\LogitechLcdEnginesWrapper.dll";

    private int lcdType;
    private String name;
    @Getter
    private int numberOfButtons;
    private LogitechLcdEnginesWrapper keyboard;

    LogitechKeyboardDevice(int lcdType, String name, int buttonsNumber) {
        this.lcdType = lcdType;
        this.name = name;
        this.numberOfButtons = buttonsNumber;

        String currentDir = System.getProperty("user.dir");
        boolean is64bit = Platform.is64Bit();
        this.keyboard = Native.load(
                PATH_TEMPLATE.formatted(currentDir, is64bit ? "x64" : "x86"),
                LogitechLcdEnginesWrapper.class);
    }

    public String toString() {
        return name;
    }

    public boolean init(char[] appletName) {
        return keyboard.LogiLcdInit(appletName, lcdType);
    }

    public boolean isConnected() {
        return keyboard.LogiLcdIsConnected(lcdType);
    }

    public boolean setMonoSetText(int lineNumber, char[] text) {
        return keyboard.LogiLcdMonoSetText(lineNumber, text);
    }

    public boolean[] areButtonsPressed() {
        boolean buttons[] = new boolean[numberOfButtons];
        buttons[0] = keyboard.LogiLcdIsButtonPressed(LogiConstants.LOGI_LCD_MONO_BUTTON_0);
        buttons[1] = keyboard.LogiLcdIsButtonPressed(LogiConstants.LOGI_LCD_MONO_BUTTON_1);
        buttons[2] = keyboard.LogiLcdIsButtonPressed(LogiConstants.LOGI_LCD_MONO_BUTTON_2);
        buttons[3] = keyboard.LogiLcdIsButtonPressed(LogiConstants.LOGI_LCD_MONO_BUTTON_3);
        return buttons;
    }

    public boolean draw(byte[] bitmap) {
        return keyboard.LogiLcdMonoSetBackground(bitmap);
    }

    public void update() {
        keyboard.LogiLcdUpdate();
    }

    public void shutdown() {
        keyboard.LogiLcdShutdown();
    }

}
