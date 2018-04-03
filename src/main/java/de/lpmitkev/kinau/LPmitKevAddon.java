package de.lpmitkev.kinau;

import de.lpmitkev.kinau.lasertag.LaserRenderTickListener;
import de.lpmitkev.kinau.lasertag.LaserUpdateTickListener;
import de.lpmitkev.kinau.modules.LPMKLaserTagKillsModule;
import de.lpmitkev.kinau.modules.LPMKNickModule;
import de.lpmitkev.kinau.server.LPmitKevServer;
import net.labymod.api.LabyModAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.SliderElement;
import net.labymod.utils.Material;

import java.util.List;

public class LPmitKevAddon extends LabyModAddon {

    private static LPmitKevAddon instance;
    private LPmitKevServer lPmitKevServer;
    private LPMKLaserTagKillsModule lasertagModule;

    private boolean laserActive, displayServerState;
    private int laserTagKillsTTL = 10;

    public static final ModuleCategory LPMITKEV_MODULECATEGORY = new ModuleCategory("LPmitKevServer", true, new IconData("lpmitkevde/textures/server-64.png"));

    @Override
    public void onEnable() {
        instance = this;
        lPmitKevServer = new LPmitKevServer();
        ModuleCategoryRegistry.loadCategory(LPMITKEV_MODULECATEGORY);
        getApi().registerServerSupport(getInstance(), lPmitKevServer);
        getApi().registerModule(new LPMKNickModule(lPmitKevServer));
        getApi().registerModule(lasertagModule = new LPMKLaserTagKillsModule());
        getApi().registerForgeListener(new LaserRenderTickListener());
        getApi().registerForgeListener(new LaserUpdateTickListener());
    }

    @Override
    public void onDisable() { }

    @Override
    public void loadConfig() {
        this.laserActive = !getConfig().has("useNewLaser") || getConfig().get("useNewLaser").getAsBoolean(); // <- default value 'true'
        this.displayServerState = !getConfig().has("displayServerState") || getConfig().get("displayServerState").getAsBoolean(); // <- default value 'true'
        this.laserTagKillsTTL = !getConfig().has("lasertagKillsTtl") ? 10 : getConfig().get("lasertagKillsTtl").getAsInt(); // <- default value '10'
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new HeaderElement("General"));
        list.add(new BooleanElement("Broadcast your current game", this, new IconData("lpmitkevde/textures/broadcast.png"), "displayServerState", displayServerState));
        list.add(new HeaderElement("LaserTag"));
        list.add(new BooleanElement("Enable new laser beams (LaserTag)", this, new IconData("lpmitkevde/textures/lasertag.png"), "useNewLaser", laserActive));
        SliderElement laserTagKillsTTL = new SliderElement("LaserTag kills display time", this, new IconData(Material.WATCH), "lasertagKillsTtl", this.laserTagKillsTTL);
        laserTagKillsTTL.setRange(3, 90);
        laserTagKillsTTL.setSteps(1);
        list.add(laserTagKillsTTL);
    }

    @Override
    public void saveConfig() {
        super.saveConfig();
        loadConfig();
        lPmitKevServer.update();
    }

    public static LPmitKevAddon getInstance() {
        return instance;
    }

    public boolean isDisplayServerStateActive() {
        return displayServerState;
    }

    public boolean isLaserActive() {
        return laserActive;
    }

    public LPMKLaserTagKillsModule getLasertagModule() {
        return lasertagModule;
    }

    public int getLaserTagKillsTTL() {
        return laserTagKillsTTL;
    }
}
