package sim;

import io.dvlopt.linux.i2c.I2CBus;
import io.dvlopt.linux.i2c.SMBus;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TridentMain {

    public static void main(String[] args) throws IOException {
        int updateFrequency = 5; // seconds

        // start prog
        I2CBus[] busses = new I2CBus[4];
        SlotController[] dimms = new SlotController[4];

        for (int i = 0; i < 4; i++) {
            busses[i] = new I2CBus("/dev/i2c-1");
            dimms[i] = new SlotController(busses[i], 0x70 + i, i);
        }

        TimerTask task = new TimerTask() {
             @Override
             public void run() {
                 try {
                     blackout(dimms);
                     runner(dimms);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(), updateFrequency * 1000);
    }

    public static void runner(SlotController[] dimms) throws IOException {

        SystemMemory sm = new SystemMemory();

        int lightCount = 20;
        int incrementSize = sm.getTotalMemory() / lightCount;
        int memoryInUse = sm.getTotalMemory() - sm.getFreeMemory();

        int lightsOn = memoryInUse / incrementSize;

        int red = 0, green = 0;
        if (lightsOn <= 14) {
            red = 0;
            green = 255;
        } else if (lightsOn <= 18) {
            red = 255;
            green = 204;
        } else {
            red = 255;
            green = 0;
        }

        System.out.println("There should be " + lightsOn + " lights on");

        // Activate the lights
        for (int i = 20; i >= 0; i--) {
            dimms[0].setDirectColour(5 - lightsOn + i, red, green, 0);
            dimms[1].setDirectColour(10 - lightsOn + i, red, green, 0);
            dimms[2].setDirectColour(15 - lightsOn + i, red, green, 0);
            dimms[3].setDirectColour(20 - lightsOn + i, red, green, 0);
        }
    }

    public static void blackout(SlotController[] dimms) throws IOException {
        for (int i = 0; i < 4; i++) {
            dimms[i].setAllDirect(0,0,0);
        }
    }
}