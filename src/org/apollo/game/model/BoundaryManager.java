package org.apollo.game.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apollo.util.XStreamUtil;

/**
 * Loads the boundary information from a XML file
 * 
 * @author Sir Sean
 * 
 */
public class BoundaryManager {

	/**
	 * The map of the all boundar's in the XML file Before you cry, no i'm not
	 * making it a constant, looks ugly
	 */
	private static List<Boundary> boundaries = new ArrayList<Boundary>();

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(BoundaryManager.class
			.getName());

	/**
	 * Adds all information to the boundary map
	 * 
	 * @throws IOException
	 */
	public static void init() throws IOException {
		logger.info("Loading boundary definitions...");
		File file = new File("data/boundaries.xml");
		if (file.exists()) {
			boundaries = XStreamUtil.readXML(file);
			logger.info("Loaded " + boundaries.size()
					+ " boundary definitions.");
		} else {
			logger.info("Boundary definitions not found.");
		}
	}

	/**
	 * If a position is within a boundary.
	 * 
	 * @param position
	 *            The position.
	 * @param boundary
	 *            The boundary.
	 * @return If the position is within a boundary.
	 */
	public static boolean isWithinBoundary(Position position, String name) {
		for (Boundary boundary : boundaryForName(name)) {
			if (position.getHeight() == boundary.getBottomLeft().getHeight()
					&& position.getHeight() == boundary.getTopRight().getHeight()) {
				if (position.getX() >= boundary.getBottomLeft().getX()
						&& position.getX() <= boundary.getTopRight().getX()
						&& position.getY() >= boundary.getBottomLeft().getY()
						&& position.getY() <= boundary.getTopRight().getY()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * If a position is within a boundary.
	 * 
	 * @param position
	 *            The position.
	 * @param boundary
	 *            The boundary.
	 * @return If the position is within a boundary.
	 */
	public static boolean isWithinBoundaryNoZ(Position position, String name) {
		for (Boundary boundary : boundaryForName(name)) {
			if (position.getX() >= boundary.getBottomLeft().getX()
					&& position.getX() <= boundary.getTopRight().getX()
					&& position.getY() >= boundary.getBottomLeft().getY()
					&& position.getY() <= boundary.getTopRight().getY()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets a boundary by its name.
	 * 
	 * @param name
	 *            The name.
	 * @return The boundary.
	 */
	public static List<Boundary> boundaryForName(String name) {
		List<Boundary> bounds = new ArrayList<Boundary>();
		for (Boundary boundary : boundaries) {
			if (boundary.getName().equalsIgnoreCase(name)) {
				bounds.add(boundary);
			}
		}
		return bounds;
	}
}