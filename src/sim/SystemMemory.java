package sim;

import java.io.*;

public class SystemMemory {
    private int totalMemory;
    private int freeMemory;
    private BufferedReader reader;

    public SystemMemory() {
        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            reader = new BufferedReader(fileReader);
            reader.mark(256);
            totalMemory = removeNonNumeric(reader.readLine());
            reader.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        update();
    }

    // update free memory
    private void update() {
        try {
            reader.readLine(); // skip line
            freeMemory = removeNonNumeric(reader.readLine());
            reader.reset();
        } catch (IOException ignored) { }
    }

    public int getFreeMemory() {
        update();
        return freeMemory;
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    // utility
    private int removeNonNumeric(String target) {
        // Remove all non numeric characters
        target = target.replaceAll("[^0-9]", "");
        return Integer.parseInt(target);
    }
}
