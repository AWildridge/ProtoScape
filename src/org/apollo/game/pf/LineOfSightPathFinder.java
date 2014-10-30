package org.apollo.game.pf;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.region.Region;
/**
 * An implementation of a <code>PathFinder</code> which only searches
 * for one tile in one direction each time, thus finding a path that
 * projectiles would go through.
 * @author Michael Bull
 *
 */

public class LineOfSightPathFinder implements PathFinder {

	@Override
	public Path findPath(Position startPosition, Position endPosition) {
		if(startPosition.getHeight() != endPosition.getHeight()) {
			return null;
		}
		int curX = startPosition.getX();
		int curY = startPosition.getY();
		if(curX == endPosition.getX() && curY == endPosition.getY()) {
			return null;
		}
		Path p = new Path();
		Region reg = World.getWorld().getRegionRepository().getRegionByPosition(startPosition);
		while(curX != endPosition.getX() || curY != endPosition.getY()) {
			int beforeX = curX;
			int beforeY = curY;
			
			if(curX > endPosition.getX()) {
				if(reg.getTile(new Position(curX, curY, startPosition.getHeight())).isWesternTraversalPermitted()
						&& reg.getTile(new Position(curX - 1, curY, startPosition.getHeight())).isEasternTraversalPermitted()) {
					curX -= 1;
				}
			} else if(curX < endPosition.getX()) {
				if(reg.getTile(new Position(curX, curY, startPosition.getHeight())).isEasternTraversalPermitted()
						&& reg.getTile(new Position(curX + 1, curY, startPosition.getHeight())).isWesternTraversalPermitted()) {
					curX += 1;
				}				
			}
			
			if(curY > endPosition.getY()) {
				if(reg.getTile(new Position(curX, curY, startPosition.getHeight())).isSouthernTraversalPermitted()
						&& reg.getTile(new Position(curX, curY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()) {
					curY -= 1;
				}
			} else if(curY < endPosition.getY()) {
				if(reg.getTile(new Position(curX, curY, startPosition.getHeight())).isNorthernTraversalPermitted()
						&& reg.getTile(new Position(curX, curY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()) {
					curY += 1;
				}				
			}
			
			if(curX != beforeX && curY != beforeY) {//diagonal

				if(curX > beforeX && curY > beforeY) {//north east
					if(!reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isNorthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isEasternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX + 1, beforeY, startPosition.getHeight())).isWesternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX + 1, beforeY, startPosition.getHeight())).isNorthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY + 1, startPosition.getHeight())).isEasternTraversalPermitted()) {
						curX = beforeX;
						curY = beforeY;
					}
				}
				
				if(curX > beforeX && curY < beforeY) {//south east
					if(!reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isSouthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isEasternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX + 1, beforeY, startPosition.getHeight())).isWesternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX + 1, beforeY, startPosition.getHeight())).isSouthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY - 1, startPosition.getHeight())).isEasternTraversalPermitted()) {
						curX = beforeX;
						curY = beforeY;
					}
				}
				
				if(curX < beforeX && curY > beforeY) {//north west
					if(!reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isNorthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isWesternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX - 1, beforeY, startPosition.getHeight())).isEasternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX - 1, beforeY, startPosition.getHeight())).isNorthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY + 1, startPosition.getHeight())).isWesternTraversalPermitted()) {
						curX = beforeX;
						curY = beforeY;
					}
				}
				
				if(curX < beforeX && curY < beforeY) {//south west
					if(!reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isSouthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY, startPosition.getHeight())).isWesternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX - 1, beforeY, startPosition.getHeight())).isEasternTraversalPermitted()
							|| !reg.getTile(new Position(beforeX - 1, beforeY, startPosition.getHeight())).isSouthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()
							|| !reg.getTile(new Position(beforeX, beforeY - 1, startPosition.getHeight())).isWesternTraversalPermitted()) {
						curX = beforeX;
						curY = beforeY;
					}
				}
			}
			
			p.addPoint(new Point(curX, curY));
			
			if(curX == beforeX && curY == beforeY) {
				break;
			}
			if(curX == endPosition.getX() && curY == endPosition.getY()) {
				break;
			}
		}
		return p;
	}

}
