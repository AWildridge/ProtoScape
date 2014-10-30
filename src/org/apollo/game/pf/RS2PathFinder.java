package org.apollo.game.pf;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.region.Region;

public class RS2PathFinder implements PathFinder {

	@Override
	public Path findPath(Position startPosition, Position endPosition) {
		if(startPosition.getHeight() != endPosition.getHeight()) {
			return null;
		}
		
		Path path = new Path();

		Region reg = World.getWorld().getRegionRepository().getRegionByPosition(startPosition);
		
        int[][] via = new int[104][104];
        int[][] cost = new int[104][104];
        int[] tileQueueX = new int[4000];
        int[] tileQueueY = new int[4000];
        for (int xx = 0; xx < 104; xx++) {
            for (int yy = 0; yy < 104; yy++) {
                cost[xx][yy] = 99999999;
            }
        }
        int regionX = startPosition.getX() >> 3;
        int regionY = startPosition.getY() >> 3;
        int localX = startPosition.getX() - 8 * (regionX - 6);
        int localY = startPosition.getY() - 8 * (regionY - 6);
        int curX = localX;
        int curY = localY;
        via[curX][curY] = 99;
        cost[curX][curY] = 0;
        int head = 0;
        int tail = 0;
        tileQueueX[head] = curX;
        tileQueueY[head++] = curY;
        boolean foundPath = false;
        int pathLength = tileQueueX.length;
        while (tail != head) {
            curX = tileQueueX[tail];
            curY = tileQueueY[tail];
            int absX = (regionX - 6) * 8 + curX;
            int absY = (regionY - 6) * 8 + curY;
            if (absX == endPosition.getX() && absY == endPosition.getY()) {
                foundPath = true;
                break;
            }
            tail = (tail + 1) % pathLength;
            int tileX = absX;
            int tileY = absY;
            int thisCost = cost[curX][curY] + 1;
            if (curY > 0 && via[curX][curY - 1] == 0 // south
                    && reg.getTile(new Position(tileX, tileY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isSouthernTraversalPermitted()) {
                tileQueueX[head] = curX;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX][curY - 1] = 1;
                cost[curX][curY - 1] = thisCost;
            }
            if (curX > 0 && via[curX - 1][curY] == 0 //west
                    && reg.getTile(new Position(tileX - 1, tileY, startPosition.getHeight())).isEasternTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isWesternTraversalPermitted()) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY;
                head = (head + 1) % pathLength;
                via[curX - 1][curY] = 2;
                cost[curX - 1][curY] = thisCost;
            }
            if (curY < 104 - 1 && via[curX][curY + 1] == 0 //north
                    && reg.getTile(new Position(tileX, tileY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isNorthernTraversalPermitted()) {
                tileQueueX[head] = curX;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX][curY + 1] = 4;
                cost[curX][curY + 1] = thisCost;
            }
            if (curX < 104 - 1 && via[curX + 1][curY] == 0 //east
                    && reg.getTile(new Position(tileX + 1, tileY, startPosition.getHeight())).isWesternTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isEasternTraversalPermitted()) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY;
                head = (head + 1) % pathLength;
                via[curX + 1][curY] = 8;
                cost[curX + 1][curY] = thisCost;
            }
            //south west
            if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
                    && reg.getTile(new Position(tileX - 1, tileY - 1, startPosition.getHeight())).isEasternTraversalPermitted()
                    && reg.getTile(new Position(tileX - 1, tileY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isSouthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isWesternTraversalPermitted()
                    && reg.getTile(new Position(tileX - 1, tileY, startPosition.getHeight())).isEasternTraversalPermitted()
                    && reg.getTile(new Position(tileX - 1, tileY, startPosition.getHeight())).isSouthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY - 1, startPosition.getHeight())).isWesternTraversalPermitted()) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX - 1][curY - 1] = 3;
                cost[curX - 1][curY - 1] = thisCost;
            }
            //north west
            if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
            		&& reg.getTile(new Position(tileX - 1, tileY + 1, startPosition.getHeight())).isEasternTraversalPermitted()
            		&& reg.getTile(new Position(tileX - 1, tileY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isNorthernTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isWesternTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY + 1, startPosition.getHeight())).isEasternTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()
            		&& reg.getTile(new Position(tileX - 1, tileY, startPosition.getHeight())).isWesternTraversalPermitted()
            		&& reg.getTile(new Position(tileX - 1, tileY, startPosition.getHeight())).isNorthernTraversalPermitted()) {
                tileQueueX[head] = curX - 1;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX - 1][curY + 1] = 6;
                cost[curX - 1][curY + 1] = thisCost;
            }
            //south east
            if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
            		&& reg.getTile(new Position(tileX + 1, tileY - 1, startPosition.getHeight())).isWesternTraversalPermitted()
            		&& reg.getTile(new Position(tileX + 1, tileY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isEasternTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isSouthernTraversalPermitted()
            		&& reg.getTile(new Position(tileX + 1, tileY, startPosition.getHeight())).isSouthernTraversalPermitted()
            		&& reg.getTile(new Position(tileX + 1, tileY, startPosition.getHeight())).isWesternTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY - 1, startPosition.getHeight())).isNorthernTraversalPermitted()
            		&& reg.getTile(new Position(tileX, tileY - 1, startPosition.getHeight())).isEasternTraversalPermitted()) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY - 1;
                head = (head + 1) % pathLength;
                via[curX + 1][curY - 1] = 9;
                cost[curX + 1][curY - 1] = thisCost;
            }
            //north east
            if (curX < 104 - 1 && curY < 104 - 1
                    && via[curX + 1][curY + 1] == 0
                    && reg.getTile(new Position(tileX + 1, tileY + 1, startPosition.getHeight())).isWesternTraversalPermitted()
                    && reg.getTile(new Position(tileX + 1, tileY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isEasternTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY, startPosition.getHeight())).isNorthernTraversalPermitted()
                    && reg.getTile(new Position(tileX + 1, tileY, startPosition.getHeight())).isWesternTraversalPermitted()
                    && reg.getTile(new Position(tileX + 1, tileY, startPosition.getHeight())).isNorthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY + 1, startPosition.getHeight())).isSouthernTraversalPermitted()
                    && reg.getTile(new Position(tileX, tileY + 1, startPosition.getHeight())).isEasternTraversalPermitted()) {
                tileQueueX[head] = curX + 1;
                tileQueueY[head] = curY + 1;
                head = (head + 1) % pathLength;
                via[curX + 1][curY + 1] = 12;
                cost[curX + 1][curY + 1] = thisCost;
            }
        }
        if (foundPath) {
            tail = 0;
            tileQueueX[tail] = curX;
            tileQueueY[tail++] = curY;
            int l5;
            for (int j5 = l5 = via[curX][curY]; curX != localX || curY != localY; j5 = via[curX][curY]) {
                if (j5 != l5) {
                    l5 = j5;
                    tileQueueX[tail] = curX;
                    tileQueueY[tail++] = curY;
                }
                if ((j5 & 2) != 0) {
                    curX++;
                } else if ((j5 & 8) != 0) {
                    curX--;
                }
                if ((j5 & 1) != 0) {
                    curY++;
                } else if ((j5 & 4) != 0) {
                    curY--;
                }
            }
            int size = tail--;
            int x = (regionX - 6) * 8 + tileQueueX[tail];
            int y = (regionY - 6) * 8 + tileQueueY[tail];
            
            path.addPoint(new Point(x, y));
            //player.getWalkingQueue().addStep(x, y);
            for (int i = 1; i < size; i++) {
            	x = (regionX - 6) * 8 + tileQueueX[--tail];
            	y = (regionY - 6) * 8 + tileQueueY[tail];
            	//player.getWalkingQueue().addStep(x, y);
                path.addPoint(new Point(x, y));;
            }
        }
		return path;
	}

}
