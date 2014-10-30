package org.apollo.game.model.combat;

/**
 * CombatType.java
 * @author The Wanderer
 */
public abstract class CombatType {
    
    protected static int[] rangedWeapons = {
        //Knives
        863, 864, 865, 866, 867, 868, 869,
        870, 871, 872, 873, 874, 875, 876,
        //Shortbows
        841, 843, 849, 853, 857, 861,
        //Longbows
        839, 845, 847, 851, 855, 859,
        //Javelins
        825, 826, 827, 828, 829, 830,
        831, 832, 833, 834, 835, 836,
        //Darts
        806, 807, 808, 809, 810, 811,
        812, 813, 814, 815, 816, 817,
        //Thrownaxe
        800, 801, 802, 803, 804, 805,
        //Misc
        6724, 2883, 4827, 4212, 4214, 4215, 4216, 4217, 4218, 4219,
        4220, 4221, 4222, 4223, 6522, 837
    };
    
    protected static int[] staffs = {};
    
    Type type;
    
    public enum Type {
        MELEE(),
        RANGED(),
        MAGIC();
    }
    
    public CombatType(Type type) {
        this.type = type;
    }
    
    public static Type getAttackType(int weaponId) {
        if(weaponId == -1) {
            return Type.MELEE;
        }
        for(int i = 0; i < rangedWeapons.length; i++) {
            if(rangedWeapons[i] == weaponId) {
                return Type.RANGED;
            }
        }
        for(int i = 0; i < staffs.length; i++) {
            if(staffs[i] == weaponId) {
                return Type.MAGIC;
            }
        }
        return Type.MELEE;
    }
    
}
