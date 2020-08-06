package sim;

import io.dvlopt.linux.i2c.*;
import java.io.IOException;

public class SlotController implements sim.constant.Registers {
    /**
     * The bus used to control the slot
     **/
    private final I2CBus bus;
    /** The slave address of this DIMM slot **/
    private final int slaveAddress;
    /** The name of the Module installed in this slot **/
    private String name = "";
    /** The number denoting this DIMM slot **/
    private final int slotNumber;
    /** The number of LEDs on this mem stick **/
    private final int ledCount = 5;

    /***
     * Controls the Trident Z RGB RAM module at a specific DIMM address.
     * @param slaveAddress RAM Module Address.
     * @param bus The bus associated with the slot
     */
    public SlotController(I2CBus bus, int slaveAddress,
                          int slotNumber) throws IOException {
        this.bus = bus;
        this.slaveAddress = slaveAddress;
        this.slotNumber = slotNumber;
        this.mapModule();
        bus.selectSlave(slaveAddress);
    }

    /**
     * Maps RAM modules on 0x77
     */
    public void mapModule() {
        try {
            bus.selectSlave(0x77);
            writeRegister(REG_SLOT_INDEX, slotNumber);
            writeRegister(REG_I2C_ADDRESS, 0xE0 + (slotNumber * 2));
        } catch (IOException e) {
            // RAM Module has already been mapped
        }
    }

    /**
     * Returns the name of this RAM module
     * @return The name of the RAM module in this DIMM slot.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the slave address which this class will use to control
     * the delegated RAM module.
     * @return Slave address associated with this class.
     */
    public int getSlaveAddress() {
        return slaveAddress;
    }

    /**
     *
     * @param ledIndex Which LED to set the colour of (e.g. 0 denotes the
     *                 right-most LED, 4 denotes the left-most.)
     * @param red The red component of the colour which the LED will show.
     * @param green The green component of the colour which the LED will show.
     * @param blue The blue component of the colour which the LED will show.
     * @throws IOException If writing to the device fails
     */
    public void setDirectColour(int ledIndex, int red, int green, int blue)
            throws IOException {
        // Ignore out of bounds LED indices
        if (ledIndex < 0 || ledIndex >= 5) {
            return;
        }
        this.setDirect(1);
        writeWord((((REG_COLORS_DIRECT + (3*ledIndex)) << 8) & 0xFF00)
                | (((REG_COLORS_DIRECT + (3*ledIndex)) >> 8) & 0x00FF));
        writeBlock(0x03, getRGBBlock(red, green, blue));
    }

    /**
     * Sets this RAM Module to operate in direct colour mode.
     * @param value 1 if this RAM module should operate in direct mode,
     *              0 otherwise.
     * @throws IOException If writing to a register fails
     */
    public void setDirect(int value) throws IOException {
        writeRegister(REG_DIRECT, value);
        writeRegister(REG_APPLY, APPLY_VAL);
    }

    /**
     * Creates a Block with colouring information used to set LED colours.
     * @param red The red component of the colour which the LED will show.
     * @param green The green component of the colour which the LED will show.
     * @param blue The blue component of the colour which the LED will show.
     * @return Colour information I2CBlock.
     */
    public static SMBus.Block getRGBBlock(int red, int green, int blue) {
        SMBus.Block rgbBlock = new SMBus.Block();
        rgbBlock.set(1, red)
                .set(2, blue)
                .set(3, green);
        return rgbBlock;
    }

    /**
     * Sets all LEDs on the RAM stick in this DIMM slot to the specified RGB
     * value
     * @param red The red component of the colour which the LED will show.
     * @param green The green component of the colour which the LED will show.
     * @param blue The blue component of the colour which the LED will show.
     * @return void
     * @throws IOException If writing to the device fails
     */
    public void setAllDirect(int red, int green, int blue)
            throws IOException {
        SMBus.Block block = new SMBus.Block();
        for (int i = 0; i < ledCount * 3; i++) {
            block.set(i, red);
            block.set(i + 1, green);
            block.set(i + 2, blue);
        }
        writeWord((((REG_COLORS_DIRECT  << 8) & 0xFF00)
                | (((REG_COLORS_DIRECT >> 8) & 0x00FF))));
        writeBlock(0x03, block);
    }

    ////////////////////////////////////
    //////////////////////// REGISTER IO
    ////////////////////////////////////

    private int readRegister(int register) throws IOException{
        writeWord(((register << 8) & 0xFF00)
                | ((register >> 8) & 0x00FF));
        return readByte(0x81);
    }

    private int readByte(int command) throws IOException {
        return bus.smbus.readByte(command);
    }

    private void writeRegister(int register, int value) throws IOException {
        writeWord(((register << 8) & 0xFF00)
                | ((register >> 8) & 0x00FF));
        writeByte(value);
    }

    private void writeWord(int word) throws IOException {
        bus.smbus.writeWord(0x00, word);
    }

    private void writeByte(int val) throws IOException {
        bus.smbus.writeByte(0x01, val);
    }

    private void writeBlock(int command, SMBus.Block block)
            throws IOException {
        bus.smbus.writeI2CBlock(command, block);
    }
}
