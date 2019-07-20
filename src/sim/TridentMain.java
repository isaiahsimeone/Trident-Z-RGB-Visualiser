package sim;

import io.dvlopt.linux.i2c.I2CBus;
import io.dvlopt.linux.i2c.SMBus;
import sim.constant.Modes;

import java.io.IOException;

public class TridentMain {

    public static void main(String[] args) throws IOException {
        // Detect all slots here
        // add them to an array of busses.
        int[] slaveAddresses = new int[4];

        I2CBus bus0 = new I2CBus("/dev/i2c-0");
        I2CBus bus1 = new I2CBus("/dev/i2c-0");
        I2CBus bus2 = new I2CBus("/dev/i2c-0");
        I2CBus bus3 = new I2CBus("/dev/i2c-0");


        SlotController[] dimms = new SlotController[4];

        dimms[0] = new SlotController(bus0, 112, 0);
        dimms[1] = new SlotController(bus1, 113, 1);
        dimms[2] = new SlotController(bus2, 114, 2);
        dimms[3] = new SlotController(bus3, 115, 3);


        //set slot colour mode
        for(int i=0;i<4;i++) {
            //System.out.println(dimms[1].getName());
            dimms[i].setEffectMode(0);
            System.out.println(dimms[i].getSlaveAddress());
            dimms[i].setDirect(0);
            //dimms[i].setColour(4, 255, 20, 100);

            //set off?
            dimms[i].setDirectColour(2, 0, 255, 0);
            //dimms[i].setDirectColour(1, 0, 0, 0);
            //dimms[i].setDirectColour(2, 255, 0, 0);
            //dimms[i].setDirectColour(3, 255, 0, 0);
            //dimms[i].setDirectColour(4, 0, 0, 0);
            //dimms[i].setDirectColour(5, 0, 0, 0);
        }
    }




}
