package api.util;

import eiteam.esteemedinnovation.api.util.Coord4;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Coord4Test {
    @Test
    public void testNBTIO() {
        Coord4 coord = new Coord4(new BlockPos(-600, 64, 42), 0);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt = coord.writeToNBT(nbt);
        nbtHasKeyAssertions(nbt);

        assertEquals(-600, nbt.getInteger("x"));
        assertEquals(64, nbt.getInteger("y"));
        assertEquals(42, nbt.getInteger("z"));
        assertEquals(0, nbt.getInteger("dimension"));

        Coord4 readCoord = Coord4.readFromNBT(nbt);
        assertEquals(coord, readCoord);
    }

    private void nbtHasKeyAssertions(NBTTagCompound nbt) {
        assertTrue(nbt.hasKey("x"));
        assertTrue(nbt.hasKey("y"));
        assertTrue(nbt.hasKey("z"));
        assertTrue(nbt.hasKey("dimension"));
    }
}
