package org.apollo.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.NPCDefinitionParser;
import org.apollo.game.model.def.NPCDefinition;

public class NPCDefinitionUpdater {

    private static final String TABLE_DATA = "<table class=\"wikitable infobox\">";
    private static final String TABLE_DATA_TWO = "<table class=\"wikitable infobox plainlinks\">";

    public static void main(String args[]) {
        try {
            new NPCDefinitionUpdater();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    static int id;
    static int amount = 0;
    static NPC npc;

    NPCDefinitionUpdater() throws Exception {
        IndexedFileSystem fs = new IndexedFileSystem(new File("data/fs/317"), true);
        try {
            NPCDefinitionParser npcParser = new NPCDefinitionParser(fs);
            NPCDefinition[] npcDefs = npcParser.parse();
            NPC[] npcs = new NPC[npcDefs.length];
            for (int i = 0; i < npcDefs.length; i++) {
                id = i;
                if (npcDefs[i].getName() == null) {
                    continue;
                }
                loadPage("http://runescape.wikia.com/wiki/" + npcDefs[i].getName().trim().replaceAll(" ", "_"));
                npcs[i] = npc;
                System.out.println(i);
            }
            try {
                FileWriter outFile = new FileWriter("./data/npc1.xml");
                PrintWriter out = new PrintWriter(outFile);
                out.println("<list>");
                for (int i = 0; i < npcDefs.length; i++) {
                    if (npcs[i] != null) {
                        out.println("  <NPCCombatDefinition>");
                        out.println("    <id>" + i + "</id>");
                        out.println("    <aggressive>" + npcs[i].isIsAggressive() + "</aggressive>");
                        out.println("    <poisonous>" + npcs[i].isIsPoisonous() + "</poisonous>");
                        out.println("    <immuneToPoison>" + npcs[i].isIsImmuneToPoison() + "</immuneToPoison>");
                        out.println("    <combatStyles>" + npcs[i].getCombatStyle() + "</combatStyles>");
                        out.println("    <attackSpeed>" + npcs[i].getAttackSpeed() + "</attackSpeed>");
                        out.println("    <maxHit>" + npcs[i].getMaxHit() + "</maxHit>");
                        out.println("  </NPCCombatDefinition>");
                    }
                }
                out.println("</list>");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            fs.close();
        }
    }

    private static void loadPage(String page) {
        try {
            URLConnection connection = new URL(page).openConnection();
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            npc = new NPC(id);
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(TABLE_DATA) || line.startsWith(TABLE_DATA_TWO)) {
                    for (int i = 0; i < 300; i++) {
                        String currentLine = in.readLine();
                        if (currentLine.contains("Quest NPC")) {
                            break;
                        }
                        if (currentLine.contains("Examine")) {
                            break;
                        }
                        if (currentLine.contains("Aggressive")) {
                            boolean isAggressive = Boolean.parseBoolean(parseBooleanHTML(in.readLine()));
                            npc.setIsAggressive(isAggressive);
                        }
                        if (currentLine.contains("Poisonous")) {
                            boolean isPoisonous = Boolean.parseBoolean(parseBooleanHTML(in.readLine()));
                            npc.setIsPoisonous(isPoisonous);
                        }
                        if (currentLine.contains("Immune")) {
                            boolean isImmuneToPoison = Boolean.parseBoolean(parseBooleanHTML(in.readLine()));
                            npc.setIsImmuneToPoison(isImmuneToPoison);
                        }
                        if (currentLine.contains("Combat style")) {
                            String combatStyle = parseStringHTML(in.readLine());
                            npc.setCombatStyle(combatStyle);
                        }
                        if (currentLine.contains("Attack speed")) {
                            String l = parseAttackSpeedHTML(in.readLine());
                            int attackSpeed = Integer.parseInt(l);
                            npc.setAttackSpeed(attackSpeed);
                        }
                        if (currentLine.contains("Max hit")) {
                            if (npc.getId() == 50) {
                                npc.setMaxHit(254);
                            } else {
                                int maxHit = Integer.parseInt(parseMaxHitHTML(in.readLine()));
                                npc.setMaxHit(maxHit);
                            }
                        }
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String parseStringHTML(String html) {
        try {
            html = html.substring(html.lastIndexOf(">") + 1);
            html = html.trim();
            if (html.equals(")")) {
                html = "Melee";
            }

            return html;
        } catch (Exception e) {
            return "null";
        }
    }

    private static String parseMaxHitHTML(String html) {
        try {
            if (html.contains("Dragonfire")) {
                html = html.substring(html.indexOf(">-"));
                return html;
            } else {
                html = html.substring(html.lastIndexOf(">") + 1);
                if (html.contains("(")) {
                    html = html.substring(0, html.indexOf("("));
                }
                if (html.contains("Unknown")) {
                    html = "0";
                }
                if (html.contains("/")) {
                    String[] hits = html.split("/");
                    html = hits[0];
                }
                if (html.contains(", ")) {
                    String[] hits = html.split(", ");
                    html = hits[0];
                }
                if (html.contains("It cannot attack")) {
                    html = "0";
                }
                if (html.contains("Varies")) {
                    html = "0";
                }
                if (html.equals("")) {
                    html = "0";
                }
                html = html.trim();
                return html;
            }
        } catch (Exception e) {
            return "0";
        }
    }

    private static String parseAttackSpeedHTML(String html) {
        try {
            if (!html.contains("Speed") || html.contains("Speedanim.gif")) {
                html = "0";
                return html;
            } else {
                html = html.substring(html.indexOf("Speed") + 5, html.indexOf("Speed") + 6);
                html = html.trim();
                return html;
            }
        } catch (Exception e) {
            return "0";
        }
    }

    private static String parseBooleanHTML(String html) {
        try {
            html = html.substring(html.lastIndexOf(">") + 2);
            if (html.contains("No")) {
                html = "false";
            } else if (html.contains("Yes")) {
                html = "true";
            }
            return html;
        } catch (Exception e) {
            return "false";
        }
    }

    public static class NPC {

        int id, attackSpeed, maxHit;
        String weaknesses, combatStyle;
        boolean isPoisonous, isAggressive, isImmuneToPoison;
        
        public NPC(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMaxHit() {
            return maxHit;
        }

        public void setMaxHit(int maxHit) {
            this.maxHit = maxHit;
        }

        public int getAttackSpeed() {
            return attackSpeed;
        }

        public void setAttackSpeed(int attackSpeed) {
            this.attackSpeed = attackSpeed;
        }

        public String getCombatStyle() {
            return combatStyle;
        }

        public void setCombatStyle(String combatStyle) {
            this.combatStyle = combatStyle;
        }

        public boolean isIsAggressive() {
            return isAggressive;
        }

        public void setIsAggressive(boolean isAggressive) {
            this.isAggressive = isAggressive;
        }

        public boolean isIsImmuneToPoison() {
            return isImmuneToPoison;
        }

        public void setIsImmuneToPoison(boolean isImmuneToPoison) {
            this.isImmuneToPoison = isImmuneToPoison;
        }

        public boolean isIsPoisonous() {
            return isPoisonous;
        }

        public void setIsPoisonous(boolean isPoisonous) {
            this.isPoisonous = isPoisonous;
        }

        public String getWeaknesses() {
            return weaknesses;
        }

        public void setWeaknesses(String weaknesses) {
            this.weaknesses = weaknesses;
        }
    }
}