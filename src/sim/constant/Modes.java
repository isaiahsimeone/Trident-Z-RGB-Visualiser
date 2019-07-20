package sim.constant;

public interface Modes {
    /***
     * Credit to Adam Honse for these values. (www.github.com/CalcProgrammer1/)
     */
    public static final int MODE_OFF                       = 0;       /* OFF mode                             */
    public static final int MODE_STATIC                    = 1;       /* Static color mode                    */
    public static final int MODE_BREATHING                 = 2;       /* Breathing effect mode                */
    public static final int MODE_FLASHING                  = 3;       /* Flashing effect mode                 */
    public static final int MODE_SPECTRUM_CYCLE            = 4;       /* Spectrum Cycle mode                  */
    public static final int MODE_RAINBOW                   = 5;       /* Rainbow effect mode                  */
    public static final int MODE_SPECTRUM_CYCLE_BREATHING  = 6;       /* Rainbow Breathing effect mode        */
    public static final int MODE_CHASE_FADE                = 7;       /* Chase with Fade effect mode          */
    public static final int MODE_SPECTRUM_CYCLE_CHASE_FADE = 8;       /* Chase with Fade; Rainbow effect mode */
    public static final int MODE_CHASE                     = 9;       /* Chase effect mode                    */
    public static final int MODE_SPECTRUM_CYCLE_CHASE      = 10;      /* Chase with Rainbow effect mode       */
    public static final int MODE_SPECTRUM_CYCLE_WAVE       = 11;      /* Wave effect mode                     */
    public static final int MODE_CHASE_RAINBOW_PULSE       = 12;      /* Chase with Rainbow Pulse effect mode */
    public static final int MODE_RANDOM_FLICKER            = 13;      /* Random flicker effect mode           */

}
