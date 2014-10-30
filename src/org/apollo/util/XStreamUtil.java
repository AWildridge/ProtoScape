package org.apollo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apollo.game.model.Animation;
import org.apollo.game.model.BoundaryManager;
import org.apollo.game.model.def.CombatNPCDefinition;
import org.apollo.game.model.def.ItemBonuses;
import org.apollo.game.model.inter.shop.ShopDefinitions;

import com.thoughtworks.xstream.XStream;

/**
 * Class handling all XStream
 * 
 * @author BMFV
 *
 */
public class XStreamUtil {

    private static XStreamUtil instance = new XStreamUtil();
    private static XStream xStream = new XStream();

    public static XStreamUtil getInstance() {
        return instance;
    }

    public static XStream getXStream() {
        return xStream;
    }

    static {
        xStream.alias("bonus", org.apollo.game.model.def.ItemBonuses.class);
        xStream.alias("equipmentDef", org.apollo.game.model.def.EquipmentDefinition.class);
        xStream.alias("equipmentType", org.apollo.game.model.EquipmentConstants.EquipmentType.class);
        xStream.alias("weaponStyle", org.apollo.game.model.def.WeaponStyle.class);
        xStream.alias("shop", org.apollo.game.model.inter.shop.ShopDefinitions.class);
        xStream.alias("animation", Animation.class);
        xStream.alias("NPCDefinition", CombatNPCDefinition.class);
        xStream.alias("entry", java.util.Map.class);
        xStream.alias("boundary", org.apollo.game.model.Boundary.class);
    }

    public static void loadAllFiles() throws FileNotFoundException, IOException {
        ItemBonuses.loadItemBonuses();
        ShopDefinitions.loadShops();
        CombatNPCDefinition.init();
        CombatNPCDefinition.initRSWikiDump();
        BoundaryManager.init();
    }

    /**
     * Reads an object from an XML file.
     * @author Graham Edgecombe
     * @param file The file.
     * @return The object.
     * @throws IOException if an I/O error occurs.
     * Edit Sir Sean: Now uses generic's 
     */
    @SuppressWarnings("unchecked")
    public static <T> T readXML(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return (T) xStream.fromXML(in);
        } finally {
            in.close();
        }
    }

    /**
     * Reads an object from an XML string.
     * @author Graham Edgecombe
     * @param s The XML.
     * @return The object.
     * @author Sir Sean: Now uses generic's
     */
    @SuppressWarnings("unchecked")
    public static <T> T readXML(String s) {
        return (T) xStream.fromXML(s);
    }
}