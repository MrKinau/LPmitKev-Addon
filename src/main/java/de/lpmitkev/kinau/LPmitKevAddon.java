package de.lpmitkev.kinau;

import de.lpmitkev.kinau.lasertag.LaserRenderTickListener;
import de.lpmitkev.kinau.lasertag.LaserUpdateTickListener;
import de.lpmitkev.kinau.modules.LPMKNickModule;
import de.lpmitkev.kinau.server.LPmitKevServer;
import net.labymod.api.LabyModAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.settings.elements.SettingsElement;

import java.util.List;

public class LPmitKevAddon extends LabyModAddon {

    private static LPmitKevAddon instance;
    private LPmitKevServer lPmitKevServer;
    private boolean laserActive, displayServerState;

    public static final ModuleCategory LPMITKEV_MODULECATEGORY = new ModuleCategory("LPmitKevServer", true, new IconData("lpmitkevde/textures/server-64.png"));

    @Override
    public void onEnable() {
        instance = this;
        lPmitKevServer = new LPmitKevServer();
        ModuleCategoryRegistry.loadCategory(LPMITKEV_MODULECATEGORY);
        getApi().registerServerSupport(getInstance(), lPmitKevServer);
        getApi().registerModule(new LPMKNickModule(lPmitKevServer));
        getApi().registerForgeListener(new LaserRenderTickListener());
        getApi().registerForgeListener(new LaserUpdateTickListener());

    }

    @Override
    public void onDisable() { }

    @Override
    public void loadConfig() {
        this.laserActive = !getConfig().has("useNewLaser") || getConfig().get("useNewLaser").getAsBoolean(); // <- default value 'true'
        this.displayServerState = !getConfig().has("displayServerState") || getConfig().get("displayServerState").getAsBoolean(); // <- default value 'true'
    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
        list.add(new BooleanElement("Enable new laser beams (LaserTag)", this, new IconData("lpmitkevde/textures/lasertag.png"), "useNewLaser", laserActive));
        list.add(new BooleanElement("Broadcast your current game", this, new IconData("lpmitkevde/textures/broadcast.png"), "displayServerState", displayServerState));
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
}