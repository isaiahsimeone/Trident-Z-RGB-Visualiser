package sim.constant;

public interface Registers {
    /***
     * Credit to Adam Honse for these values (www.github.com/CalcProgrammer1/)
     */
    int REG_DEVICE_NAME   = 0x1000; /* Device String 16 bytes               */
    int REG_COLORS_DIRECT = 0x8000; /* Colors for Direct Mode 15 bytes      */
    int REG_COLORS_EFFECT = 0x8010; /* Colors for Internal Effects 15 bytes */
    int REG_DIRECT        = 0x8020; /* Direct Access Selection Register     */
    int REG_MODE          = 0x8021; /* Mode Selection Register              */
    int REG_APPLY         = 0x80A0; /* Apply Changes Register               */
    int REG_SLOT_INDEX    = 0x80F8; /* Slot Index Register (RAM only)       */
    int REG_I2C_ADDRESS   = 0x80F9; /* I2C Address Register (RAM only)      */
    int APPLY_VAL         = 0x01;   /* Apply Changes Value                  */
}
