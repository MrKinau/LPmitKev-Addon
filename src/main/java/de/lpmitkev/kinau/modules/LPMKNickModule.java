package de.lpmitkev.kinau.modules;

import de.lpmitkev.kinau.LPmitKevAddon;
import de.lpmitkev.kinau.server.LPmitKevServer;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.utils.Material;

public class LPMKNickModule extends SimpleModule {

    private LPmitKevServer server;

    public LPMKNickModule(LPmitKevServer server) {
        this.server = server;
    }

    @Override
    public String getDisplayName() {
        return "Nick";
    }

    @Override
    public String getDisplayValue() {
        return server.getNickname();
    }

    @Override
    public String getDefaultValue() {
        return "Kein Nickname";
    }

    @Override
    public String getSettingName() {
        return "Nickname";
    }

    @Override
    public String getDescription() {
        return "Shows your current nickname";
    }

    @Override
    public IconData getIconData() {
        return new IconData(Material.NAME_TAG);
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public int getSortingId() {
        return 0;
    }

    @Override
    public ModuleCategory getCategory() {
        return LPmitKevAddon.LPMITKEV_MODULECATEGORY;
    }

    @Override
    public boolean isShown() {
        if (server.getNickname() == null)
            return false;
        else
            return true;
    }
}
