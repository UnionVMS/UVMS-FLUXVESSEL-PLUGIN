package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.enums;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public enum HullMaterial {
    WOOD(1),
    METAL(2),
    FIBER_OR_PLASTIC(3),
    OTHER(4),
    UNKNOWN(5),
    POLYESTER(6),
    ALUMINIUM(7);

    private final static Map<Integer, HullMaterial> map =
            Arrays.stream(HullMaterial.values()).collect(toMap(hullMaterial -> hullMaterial.code, hullMaterial -> hullMaterial));


    private int code;

    HullMaterial(int hullMaterialCode) {
        this.code = hullMaterialCode;
    }

    public static HullMaterial fromCode(int hullMaterialCode) {
        return map.get(hullMaterialCode);
    }
}
