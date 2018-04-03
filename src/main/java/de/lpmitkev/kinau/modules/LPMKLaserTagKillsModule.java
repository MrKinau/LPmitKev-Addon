package de.lpmitkev.kinau.modules;

import de.lpmitkev.kinau.LPmitKevAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.ColoredTextModule;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.utils.ModColor;

import java.util.*;

public class LPMKLaserTagKillsModule extends ColoredTextModule {

    private List<List<Text>> texts = new ArrayList<List<Text>>();
    private Timer timer = new Timer();

    public LPMKLaserTagKillsModule() {
        texts.add(Collections.singletonList(new Text("LaserTag-Kills:", ModColor.GOLD.getColor().getRGB(), true, false, false)));
    }

    public void addText(List<Text> toAdd) {
        texts.add(1, toAdd);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                texts.remove(texts.size() - 1);
            }
        }, LPmitKevAddon.getInstance().getLaserTagKillsTTL() * 1000L);
    }

    @Override
    public List<List<Text>> getTexts() {
        List<List<Text>> text = new ArrayList<List<Text>>();
        for(List<Text> line : texts) {
            List<Text> newLine = new ArrayList<Text>();
            newLine.addAll(line);
            text.add(newLine);
        }
        return text;
    }

    @Override
    public int getLines() {
        return texts.size();
    }

    @Override
    public String getDescription() {
        return "Lasertag Kills";
    }

    @Override
    public int getSortingId() {
        return 0;
    }

    @Override
    public IconData getIconData() {
        return new IconData("lpmitkevde/textures/lasertag.png");
    }

    @Override
    public ModuleCategory getCategory() {
        return LPmitKevAddon.LPMITKEV_MODULECATEGORY;
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getSettingName() {
        return "Lasertag Kills";
    }
}
