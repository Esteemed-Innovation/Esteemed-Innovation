package eiteam.esteemedinnovation.data.capabilities;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public class FluidPipeBlockCapabilities {
    public static final PropertyEnum<Mode> MODE = PropertyEnum.create("mode", Mode.class);

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public enum Mode implements IStringSerializable {
        // Transfer mode can input and output only to and from pipes
        TRANSFER,
        // Input mode can can put fluids into anything, but only accept fluids from pipes
        INPUT,
        // Output mode can accept fluids from anything, but only put fluids into pipes
        OUTPUT;

        public static Mode[] META_LOOKUP = new Mode[] { TRANSFER, INPUT, OUTPUT };

        @Override
        public String getName() {
            return toString().toLowerCase();
        }

        public Mode next() {
            return META_LOOKUP[(ordinal() + 1) % META_LOOKUP.length];
        }
    }
}
