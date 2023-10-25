package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Device {
    private final int deviceNum;
    private final String deviceName;
    private double damage;
}
