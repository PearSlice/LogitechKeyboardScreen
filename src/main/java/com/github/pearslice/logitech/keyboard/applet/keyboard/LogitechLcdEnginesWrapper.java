package com.github.pearslice.logitech.keyboard.applet.keyboard;

import com.sun.jna.Library;

public interface LogitechLcdEnginesWrapper extends Library {

    boolean LogiLcdInit(char[] appletName, int screenType);

    boolean LogiLcdIsConnected(int lcdType);

    boolean LogiLcdMonoSetText(int lineNumber, char[] text);

    boolean LogiLcdIsButtonPressed(int button);

    void LogiLcdUpdate();

    void LogiLcdShutdown();

    boolean LogiLcdMonoSetBackground(byte[] monoBitmap);


//    bool LogiLcdInit(wchar_t* friendlyName, int lcdType);
//    bool LogiLcdIsConnected(int lcdType);
//    bool LogiLcdIsButtonPressed(int button);
//    void LogiLcdUpdate();
//    void LogiLcdShutdown();
//
//    // Monochrome LCD functions
//    bool LogiLcdMonoSetBackground(BYTE monoBitmap[]);
//    bool LogiLcdMonoSetText(int lineNumber, wchar_t* text);
//
//    // Color LCD functions
//    bool LogiLcdColorSetBackground(BYTE colorBitmap[]);
//    bool LogiLcdColorSetTitle(wchar_t* text, int red = 255, int green = 255, int blue = 255);
//    bool LogiLcdColorSetText(int lineNumber, wchar_t* text, int red = 255, int green = 255, int blue = 255);

}
