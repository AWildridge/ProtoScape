/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apollo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apollo.game.model.Item;

import com.thoughtworks.xstream.XStream;

/**
 * Controls all the XML File types in the server
 * @author Sir Sean
 * Warning! XML can be slow so don't bulk up with useless stuff :)
 * 
 */
public class XMLController {

	/**
	 * The XStream Instance
	 * We don't have to implement any drivers as we 
	 * use the default set one, which is very fast
	 * 
	 * Before you cry, i'm not making the XStream 
	 * instance constant as it looks ugly
	 */
	private static final XStream xstream = new XStream();

	/**
	 * All the xstream alias in here
	 * I am not importing it into an another xml as if we
	 * need to refactor it will not edit it
	 */
	static {
		xstream.alias("animation", org.apollo.game.model.Animation.class);
		xstream.alias("CombatNPCDefinition", org.apollo.game.model.def.CombatNPCDefinition.class);
                xstream.alias("skill", org.apollo.game.model.def.CombatNPCDefinition.Skill.class);
                xstream.alias("npcdrop", org.apollo.game.model.NPCDrop.class);
                xstream.alias("item", Item.class);
        }
	
	/**
	 * Writes the XML file, using try and finally will allow
	 * the file output to close if an exception is thrown (will stop memory leaks)
	 * 
	 * @param object The object getting written
	 * @param file The file area and name
	 * @throws IOException
	 */
	public static void writeXML(Object object, File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		try {
			xstream.toXML(object, out);
			out.flush();
		} finally {
			out.close();
		}
	}
	/**
	 * Writes the XML file, using try and finally will allow
	 * the file output to close if an exception is thrown (will stop memory leaks)
	 *
	 * @param object The object getting written
	 * @return The XML
	 */
	public static String writeXML(Object object) {
		return xstream.toXML(object);
	}
	/**
	 * Adds an alias for a name
	 * @param name alias
	 * @param type real class
	 */
	public static void alias(String name, Class<?> type) {
		xstream.alias(name, type);
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
			return (T) xstream.fromXML(in);
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
	public static <T> T readXML(String s){
		return (T) xstream.fromXML(s);
	}
}
