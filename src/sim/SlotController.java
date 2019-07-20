package sim;

import io.dvlopt.linux.i2c.*;
import java.io.IOException;

public class SlotController implements sim.constant.Modes, sim.constant.Registers {
    /** The bus used to control the slot **/
    private I2CBus bus = null;
    /** The slave address of this DIMM slot **/
    private int slaveAddress = -1;
    /** The name of the Module installed in this slot **/
    private String name = null;
    /** The number denoting this DIMM slot **/
    private int slotNumber = -1;

    /***
     * Controls the Trident Z RGB RAM module at a specific DIMM address.
     * @param slaveAddress RAM Module Address.
     */
    public SlotController(I2CBus bus, int slaveAddress, int slotNumber) throws IOException {
        this.bus            = bus;
        this.slaveAddress   = slaveAddress;
        this.slotNumber     = slotNumber;
        this.mapModule();

        bus.selectSlave(slaveAddress);
        this.setName();

    }

    /**
     * Maps RAM modules on 119
     * @throws IOException
     */
    public void mapModule() throws IOException {
        bus.selectSlave(119);
        try {
            writeRegister(REG_SLOT_INDEX, slotNumber);
            writeRegister(REG_I2C_ADDRESS, 224 + (slotNumber*2));
        } catch (IOException e) {
            // RAM Module has already been mapped
        }
    }

    /**
     * Sets the effect mode for the RAM module in this slot (e.g. 0 = OFF, 1 = STATIC, etc)
     * @see sim.constant.Modes
     * @param mode The effect mod for the RAM module in this slot.
     * @throws IOException
     */
    public void setEffectMode(int mode) throws IOException {
        this.setDirect(0);
        writeRegister(REG_MODE, mode);
        writeRegister(REG_APPLY, APPLY_VAL);
    }

    /**
     * Gets the name of the RAM module which this SlotController manages.
     * @throws IOException
     */
    private void setName() throws IOException {
        char[] charsInName = new char[16];
        for(int i = 0; i < 16; i++) {
            charsInName[i] = (char)readRegister(0x1000+i);
        }
        name = new String(charsInName);
    }

    /**
     * Returns the name of this RAM module
     * @return The name of the RAM module in this DIMM slot.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the slave address which this class will use to control the delegated RAM module.
     * @return Slave address associated with this class.
     */
    public int getSlaveAddress() {
        return slaveAddress;
    }

    /**
     *
     * @param LED Which LED to set the colour of (e.g. 0 denotes the right-most LED, 4 denotes the left-most.)
     * @param red The red component of the colour which the LED will display.
     * @param green The green component of the colour which the LED will display.
     * @param blue The blue component of the colour which the LED will display.
     * @throws IOException
     */
    public void setDirectColour(int LED, int red, int green, int blue) throws IOException {
        this.setDirect(1);
        writeWord(0x00, (((REG_COLORS_DIRECT + (3*LED)) << 8) & 0xFF00) | (((REG_COLORS_DIRECT + (3*LED)) >> 8) & 0x00FF));
        writeBlock(0x03, getRGBBlock(red, green, blue));

    }

    public void setEffect() {

    }

    /**
     * Sets this RAM Module to operate in direct colour mode.
     * @param value 1 iff this RAM module should operate in direct mode, 0 otherwise.
     * @throws IOException
     */
    public void setDirect(int value) throws IOException {
        writeRegister(REG_DIRECT, value);           //1==true, 0==false
        writeRegister(REG_APPLY, APPLY_VAL);
    }


    /**
     * Generates a Block with colouring information used to set LED colours.
     * @param red The red component of the colour which the LED will display.
     * @param green The green component of the colour which the LED will display.
     * @param blue The blue component of the colour which the LED will display.
     * @return Colour information I2CBlock.
     */
    public static SMBus.Block getRGBBlock(int red, int green, int blue) {
        SMBus.Block rgbBlock = new SMBus.Block();
        rgbBlock.set(1, red)
                .set(2, blue)
                .set(3, green);
        return rgbBlock;
    }

    //////////////////
    //////REGISTER IO
    //////////////////


    private int readRegister(int register) throws IOException{
        writeWord(0x00, ((register << 8) & 0xFF00) | ((register >> 8) & 0x00FF));
        return readByte(0x81);
    }

    private int readByte(int command) throws IOException {
        return bus.smbus.readByte(command);
    }

    private void writeRegister(int register, int value) throws IOException {
        writeWord(0x00, ((register << 8) & 0xFF00) | ((register >> 8) & 0x00FF));
        writeByte(0x01, value);
    }

    private void writeWord(int command, int word) throws IOException {
        bus.smbus.writeWord(command, word);
    }

    private void writeByte(int command, int val) throws IOException {
        bus.smbus.writeByte(command, val);
    }

    private void writeBlock(int command, SMBus.Block block) throws IOException {
        bus.smbus.writeI2CBlock(command, block);
    }






}
