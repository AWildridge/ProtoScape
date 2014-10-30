package org.apollo.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * NPCDropUpdater.java
 * @author The Wanderer
 */
public class NPCDropUpdater {

    private static final String TABLE_DATA = "<table class=\"sitetable\"";
    private static final String ONE_HUNDRED_PERCENT_TABLE = ">100% Drop:<";
    private static final String ALL_POSSIBLE_LOOT_TABLE = ">All Possible Loot:<";

    public static void main(String args[]) {
        try {
            new NPCDropUpdater();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    static int id;
    static int amount = 0;
    static NPC npc;
    static HashMap<Integer, NPC> npcIDS = new HashMap();

    NPCDropUpdater() {
        for (int i = 0; i < 99999; i++) {
            if (amount >= 1028) {
                break;
            }
            id = i;
            loadPage("http://www.runehq.com/database.php?type=monster&id=" + i);
            System.out.println(i);
            System.out.println();
        }
    }

    public static void loadPage(String page) {
        try {
            URLConnection connection = new URL(page).openConnection();
            connection.connect();
            try {
            	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                npc = new NPC(id);
                String line;
                while ((line = in.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith(TABLE_DATA)) {
                        for (int i = 0; i < 100; i++) {
                            String currentLine = in.readLine().trim();
                            if (currentLine.contains("class=\"sitetablehead\">Monster #")) {
                                boolean flag = false;
                                int number = Integer.parseInt(
                                            currentLine.substring(currentLine.indexOf("#") + 1,
                                            currentLine.indexOf(":")));

                                System.out.println(flag);
                                if (flag) {
                                    flag = false;
                                    return;
                                }

                                parseID(currentLine, npc);
                                parseName(currentLine, npc);
                            }
                            if (currentLine.contains("Combat Level:")) {
                                parseCombatLevel(currentLine, npc);
                            }
                            if (currentLine.contains("Number of Lifepoints:")) {
                                parseHitPoints(currentLine, npc);
                            }
                            if (currentLine.contains("Weakness:")) {
                                parseWeaknesses(currentLine, npc);
                            }
                            if (currentLine.contains("Examine") || currentLine == null) {
                                amount++;
                                break;
                            }
                            if (currentLine.contains(ONE_HUNDRED_PERCENT_TABLE)) {
                                parseOneHundredPercentTable(currentLine, npc);
                            }
                            if (currentLine.contains(ALL_POSSIBLE_LOOT_TABLE)) {
                                parseAllPossibleLootTable(currentLine, npc, in);
                            }
                        }
                        System.out.println(npc.getName());
                        System.out.println(npc.getCombatLevel());
                        System.out.println(npc.getHitPoints());
                        System.out.println(npc.getWeaknesses());
                        for (int j = 0; j < npc.getAlwaysDrop().size(); j++) {
                            System.out.println(npc.getAlwaysDrop().get(j));
                        }
                        for (int j = 0; j < npc.getPossibleDrop().size(); j++) {
                            System.out.println(npc.getPossibleDrop().get(j));
                        }
                        System.out.println(amount);
                    }
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseID(String html, NPC npc) {
        html = html.substring(0, html.lastIndexOf("</td>"));
        html = html.substring(html.lastIndexOf("#") + 1, html.lastIndexOf(":"));
        html.trim();
    }

    public static void parseName(String html, NPC npc) {
        html = html.substring(0, html.lastIndexOf("</td>"));
        html = html.substring(html.lastIndexOf(":") + 1);
        html.trim();
        npc.setName(html);
    }

    public static void parseWeaknesses(String html, NPC npc) {
        html = html.substring(0, html.lastIndexOf("</td>"));
        html = html.substring(html.lastIndexOf(">") + 1);
        npc.setWeaknesses(html);
    }

    public static void parseHitPoints(String html, NPC npc) {
        html = html.substring(0, html.lastIndexOf("</td>"));
        html = html.substring(html.lastIndexOf(">") + 1);
        npc.setHitPoints((int) Math.floor(Double.parseDouble(html) / 10));
    }

    public static void parseCombatLevel(String html, NPC npc) {
        html = html.substring(0, html.lastIndexOf("</td>"));
        html = html.substring(html.lastIndexOf(">") + 1);
        npc.setCombatLevel(Integer.parseInt(html));
    }

    @SuppressWarnings("unchecked")
    public static void parseOneHundredPercentTable(String html, NPC npc) {
        if (html.contains("</a>")) {
            String[] drops = html.split("</a>");
            for (int i = 0; i < drops.length - 2; i++) {
                drops[i] = drops[i].substring(drops[i].lastIndexOf(">") + 1);
                drops[i].trim();
                npc.getAlwaysDrop().add(drops[i]);
            }
        }
    }

    public static void parseAllPossibleLootTable(String html, NPC npc, BufferedReader buffer) throws IOException {
        for (int i = 0; i < 200; i++) {
            if (html.contains("/table")) {
                break;
            }
            if (html.contains("</a>")) {
                html = html.substring(html.lastIndexOf("\">") + 2, html.lastIndexOf("</a>"));
                html = html.trim();
                npc.getPossibleDrop().add(html);
                html = buffer.readLine().trim();
            } else {
                html = buffer.readLine().trim();
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static class NPC {

        int combatLevel, hitPoints, id;

        public NPC(int id) {
            this.id = id;
        }
        String weaknesses, name;
        ArrayList alwaysDrop = new ArrayList();
        ArrayList possibleDrop = new ArrayList();

        public ArrayList getAlwaysDrop() {
            return alwaysDrop;
        }

        public void setAlwaysDrop(ArrayList alwaysDrop) {
            this.alwaysDrop = alwaysDrop;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList getPossibleDrop() {
            return possibleDrop;
        }

        public void setPossibleDrop(ArrayList possibleDrop) {
            this.possibleDrop = possibleDrop;
        }

        public int getCombatLevel() {
            return combatLevel;
        }

        public void setCombatLevel(int combatLevel) {
            this.combatLevel = combatLevel;
        }

        public int getHitPoints() {
            return hitPoints;
        }

        public void setHitPoints(int hitPoints) {
            this.hitPoints = hitPoints;
        }

        public String getWeaknesses() {
            return weaknesses;
        }

        public void setWeaknesses(String weaknesses) {
            this.weaknesses = weaknesses;
        }
    }
}
