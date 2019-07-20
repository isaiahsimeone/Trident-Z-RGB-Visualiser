package sim;

import io.dvlopt.linux.i2c.I2CBus;
import io.dvlopt.linux.i2c.SMBus;

import java.io.IOException;

public class TridentMain {

    public static void main(String[] args) throws IOException {

        int[] slaveAddresses = {112, 113, 114, 115};

        I2CBus[] busses = new I2CBus[4];
        SlotController[] dimms = new SlotController[4];

        for (int i = 0; i < 4; i++) {
            busses[i] = new I2CBus("/dev/i2c-0");
            dimms[i] = new SlotController(busses[i], slaveAddresses[i], i);
        }


        for(int i=0;i<4;i++) {

            dimms[i].setEffectMode(4);
            System.out.println(dimms[i].getSlaveAddress());

            //dimms[i].setDirectColour(4, 35, 63, 53);

        }
    }




}
