package com.github.pearslice.logitech.keyboard.applet;

import com.github.pearslice.logitech.keyboard.applet.keyboard.ButtonListener;
import com.github.pearslice.logitech.keyboard.applet.keyboard.DisplayAndKeysLoop;
import com.github.pearslice.logitech.keyboard.applet.keyboard.LogiConstants;
import com.sun.jna.Platform;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Semaphore;

import static com.github.pearslice.logitech.keyboard.applet.keyboard.LogitechKeyboardDevice.G13;

@SpringBootApplication
@Slf4j
public class LogitechKeyboardApplet {

    private static Semaphore end = new Semaphore(0);

    public static final String HELLO = "hello\0";
    public static final String WORLD = "world\0";
    public static final String APPLET_NAME = "TestApplet";
    private static DisplayAndKeysLoop displayAndKeysLoop;


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(LogitechKeyboardApplet.class, args);

        log.info("Starting on {}", System.getProperty("os.name"));
        if (!Platform.isWindows()) {
            log.error("Sorry, only windows is supported at the moment");
            System.exit(-10);
        }




        displayAndKeysLoop = new DisplayAndKeysLoop(G13, APPLET_NAME);
        displayAndKeysLoop.addButtonListener(new ButtonListener() {
            @Override
            public void buttonPressed(int button) {
                log.info("Button {} pressed", button);
                if (button == 0) {
                    G13.setMonoSetText(1, WORLD.toCharArray());
                }
                if(button ==1) {
                    byte[] picture = new byte[LogiConstants.LOGI_LCD_MONO_WIDTH * LogiConstants.LOGI_LCD_MONO_HEIGHT];

                    //Create a zebra bitmap
                    for(int x = 0; x < LogiConstants.LOGI_LCD_MONO_WIDTH; x++) {
                        for(int y = 0; y < LogiConstants.LOGI_LCD_MONO_HEIGHT; y++){
                            byte b = (x / 8) % 2 == 0 ? (byte) 128 : (byte) 0;

                            picture[x + y * LogiConstants.LOGI_LCD_MONO_WIDTH] = b;

                        }
                    }

                    log.info("Trying to draw, length of array {}", picture.length);
                    boolean draw = G13.draw(picture);
                    log.info(draw ? "success": "fail");
                }
            }

            @Override
            public void buttonReleased(int button) {
                log.info("Button {} released", button);
                if (button == 3) {
                    LogitechKeyboardApplet.shutdown();
                }
            }
        });


        G13.setMonoSetText(0, HELLO.toCharArray());


        end.acquire();
        log.info("Bye");
    }

    @PreDestroy
    public static void shutdown() {
        displayAndKeysLoop.shutdown();
        G13.shutdown();
        end.release();
    }


}
