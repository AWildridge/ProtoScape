package org.apollo.game.content.skills.farming;

/**
 * These configs belong to the patch in fally.
 * 
 * Credits to this amazing man who gave these config ids: <l>
 * http://pastie.org/private/8shejcuv5ne9sgftlahrfw<li>
 * 
 * @author Rodrigo Molina
 */
public final class FarmingConfigs {

    /**
     * The config ids for all the patches.
     * 
     * 
     * @author Rodrigo Molina
     */
    public final static class PatchConfigIds {
	public static final int ALOTTMENT = 504;
	public static final int ARDY_ALOTTMENT = 505;// for some reason it's a
						     // different config
	public static final int FLOWER_PATCH = 508;
	public static final int HERB_PATCH = 515;
	public static final int TREE_PATCH = 502;
    }

    public static final int HIGH_ALOTTMENT = 0;
    public static final int LOW_ALOTTMENT = 1;
    public static final int LOWER_ALOTTMENT = 2;
    public static final int NO_ALOTTMENT = 3;

    /*
     * These are the starting points
     * 
     * difference is 7
     * 
     * to show watery, add 64 for it to be diseased, add 129 and for it to be
     * dead, you have to add 129 then 64. because dead comes after disease you
     * just have to add 64 when its done so its like adding water and diseassse.
     * thats if there was notin added to it
     * 
     * all these are without the water.
     */
    public final static class AlottmentConfigs {
	public static final int POTATOE_SEED = 0x06;
	public static final int ONION_SEED = 0x0d;
	public static final int CABBAGE_SEED = 0x14;
	public static final int TOMATOE = 0x1b;
	public static final int SWEETCORN = 0x22;
	public static final int STRAWBERRY = 0x2b;
	public static final int WATERMELON = 0x34;
    }

    /*
     * The flower configuration states.
     */
    public final static class FlowerConfigs {
	public static final int MARIGOLD = 0x08;
	public static final int ROSEMARY = 0x0d;
	public static final int NASTURTIUM = 0x12;
	public static final int WOAD_PLANT = 0x17;
	public static final int LIMPWURT_PLANT = 0x1c;
	public static final int SCARE_CROW = 0x21;
	public static final int WHITE_LILY = 0x25;
    }

    /*
     * (needs testing and refining)
     */
    public final static class HerbConfigs {
	public static final int GUAM = 0x04;
	public static final int MARRENTILL = 0x0b;
	public static final int TARROMIN = 0x12;
	public static final int HARRALANDER = 0x19;
	public static final int RANARR = 0x20;
	public static final int TOADFLAX = 0x27;
	public static final int IRIT = 0x2e;
	public static final int AVANTOE = 0x35;
	public static final int KWUARM = 0x44;;
	public static final int SNAPDRAGON = 0x4b;
	public static final int CADANTINE = 0x52;
	public static final int LANTADYME = 0x59;
	public static final int DWARF_WEED = 0x60;
	public static final int TORSTOL = 0x67;
	public static final int GOUTWEED = 0xc0;// (0xc4/c5 fully grown, 0xc6 -
						// 0xc8 diseased, 0xc9 - 0xcb
						// dead)
    }

    /*
     * 0x00 - 0x03: layers of weeds removed (0x00 meaning no weeds removed, 0x03
     * meaning all weeds removed), iff both state bits are 0 -- 0x08 - 0x0e: oak
     * tree (0x0c is ready to check-health, 0x0d is ready to chop down, 0x0e is
     * oak tree stump) -- 0x0f - 0x17: willow tree (0x15 is ready to
     * check-health, 0x16 is ready to chop down, 0x17 is willow tree stump) --
     * 0x18 - 0x22: maple tree (0x20 is ready to check-health, 0x21 is ready to
     * chop down, 0x22 is maple tree stump) -- 0x23 - 0x2f: yew tree (0x2d is
     * ready to check-health, 0x2e is ready to chop down, 0x2f is yew tree
     * stump) -- 0x30 - 0x3e: magic tree (0x3c is ready to check-health, 0x3d is
     * ready to chop down, 0x3e is magic tree stump)
     */
    public static final class TreeConfigs {
	public static final int OAK_TREE = 0x08;
	public static final int WILLOW_TREE = 0x0f;
	public static final int MAPLE_TREE = 0x18;
	public static final int YEW_TREE = 0x23;
	public static final int MAGIC_TREE = 0x30;
    }

    /*
     * The fruit tree configs
     */
    public static final class FruitTreeConfigs {
	public static final int APPLE_TREE = 0x08;
	public static final int BANANA_TREE = 0x23;
	public static final int ORANGE_TREE = 0x48;
	public static final int CURRY_TREE = 0x63;
	public static final int PINEAPPLE_PLANT = 0x88;
	public static final int PAPAYA_TREE = 0xa3;
	public static final int PALM_TREE = 0xc8;
    }
}
