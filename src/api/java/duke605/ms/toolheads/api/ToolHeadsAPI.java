package duke605.ms.toolheads.api;

import duke605.ms.toolheads.api.head.Head;
import duke605.ms.toolheads.api.head.Head.ToolType;

import java.util.HashMap;

public class ToolHeadsAPI {

    private HashMap<ToolType, HashMap<String, Head>> heads;
    
    public static final ToolHeadsAPI INSTANCE = new ToolHeadsAPI();

    private ToolHeadsAPI() {
        // Initializing heads
        heads = new HashMap<ToolType, HashMap<String, Head>>();

        heads.put(ToolType.AXE, new HashMap<String, Head>());
        heads.put(ToolType.HOE, new HashMap<String, Head>());
        heads.put(ToolType.PICKAXE, new HashMap<String, Head>());
        heads.put(ToolType.SHOVEL, new HashMap<String, Head>());
    }

    /**
     * Registers a tool head to the list
     *
     * @param head The head being registered to the tool head list
     */
    public static void registerHead(Head head) {
        if (head == null || head.type == null || head.material == null)
            return;

        // Registering head
        INSTANCE.heads.get(head.type).put(head.material, head);
    }

    /**
     * Gets the corresponding head (if there is one) for the tool type and material name
     *
     * @param type The type of tool the head is for
     * @param material The name of the material the head is made out of
     * @return the head or null if no head found
     */
    public static Head getHead(ToolType type, String material) {
        HashMap<String, Head> heads;

        if (type == null || material == null || material.isEmpty() || !INSTANCE.heads.containsKey(type))
            return null;

        // Getting heads safely
        heads = INSTANCE.heads.get(type);

        return heads == null ? null : heads.get(material);
    }
}
