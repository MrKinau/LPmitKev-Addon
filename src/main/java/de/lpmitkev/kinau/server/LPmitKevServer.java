package de.lpmitkev.kinau.server;

import de.lpmitkev.kinau.LPmitKevAddon;
import de.lpmitkev.kinau.lasertag.Laser;
import de.lpmitkev.kinau.modules.LPMKLaserTagKillsModule;
import de.lpmitkev.kinau.utils.Location;
import io.netty.buffer.Unpooled;
import net.labymod.api.events.TabListEvent;
import net.labymod.chat.packets.PacketPlayServerStatus;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.moduletypes.ColoredTextModule;
import net.labymod.main.LabyMod;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.ModColor;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.PacketBuffer;
import org.lwjgl.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LPmitKevServer extends Server {

    private String nickname = null;
    private Pattern ltLaserKillPattern = Pattern.compile("[\\w]{3,16}[\\s][-]{4}[❂][\\s]{1}[\\w]{3,16}.*");
    private Pattern ltGrenadeKillPattern = Pattern.compile("[\\w]{3,16}[\\s][҉][\\s]{1}[\\w]{3,16}");
    private Pattern ltMineKillPattern = Pattern.compile("[\\w]{3,16}[\\s].*[☄][\\s]{1}[\\w]{3,16}");

    public LPmitKevServer() {
        super("LPmitKev CommunityServer", "lpmitkev.de", "lpohnekev.de");
    }

    @Override
    public void onJoin(ServerData serverData) {
        this.nickname = null;
    }

    @Override
    public ChatDisplayAction handleChatMessage(String unformatted, String formatted) throws Exception {
        if (unformatted.startsWith("[*] Du wurdest als ") && unformatted.endsWith("genickt."))
            this.nickname = unformatted.replace("[*] Du wurdest als ", "").split(" ")[0];
        else if (unformatted.startsWith("[*] Verbinde zur Hub") || unformatted.startsWith("[!] Dein Nickname wurde aufgel"))
            this.nickname = null;
        if(LPmitKevAddon.getInstance().getLasertagModule().isShown()) {
            if (ltLaserKillPattern.matcher(unformatted).matches()) {
                LPMKLaserTagKillsModule lasertagModule = LPmitKevAddon.getInstance().getLasertagModule();
                String player1 = formatted.split(" ")[0].replace("§r", "").replace("§", "");
                String player2 = formatted.split(" ")[2].replace("§r", "").replace("§", "");
                String distance = formatted.split(" ")[3].replace("§r", "").replace("§o", "").replace("§", "");
                String separator1 = formatted.split(" ")[1].split("§r")[1].replace("§r", "").replace("§", "");
                String separator2 = formatted.split(" ")[1].split("§r")[2].replace("§r", "").replace("§", "");
                String separator3 = formatted.split(" ")[1].split("§r")[3].replace("§r", "").replace("§", "");

                List<ColoredTextModule.Text> texts = new ArrayList<ColoredTextModule.Text>();
                texts.add(new ColoredTextModule.Text(player1.substring(1) + " ", getModColor(player1.charAt(0)).getColor().getRGB()));
                texts.add(new ColoredTextModule.Text(separator1.substring(1), getModColor(separator1.charAt(0)).getColor().getRGB()));
                texts.add(new ColoredTextModule.Text(separator2.substring(1), getModColor(separator2.charAt(0)).getColor().getRGB()));
                texts.add(new ColoredTextModule.Text(separator3.substring(1) + " ", getModColor(separator3.charAt(0)).getColor().getRGB()));
                texts.add(new ColoredTextModule.Text(player2.substring(1) + " ", getModColor(player2.charAt(0)).getColor().getRGB()));
                texts.add(new ColoredTextModule.Text(distance.substring(1) + " ", getModColor(distance.charAt(0)).getColor().getRGB(), false, true, false));
                lasertagModule.addText(texts);
                return ChatDisplayAction.HIDE;
            } else if (ltGrenadeKillPattern.matcher(unformatted).matches() || ltMineKillPattern.matcher(unformatted).matches()) {
                LPMKLaserTagKillsModule lasertagModule = LPmitKevAddon.getInstance().getLasertagModule();
                String player1 = formatted.split(" ")[0].replace("§r", "").replace("§", "");
                String player2 = formatted.split(" ")[2].replace("§r", "").replace("§", "");
                String separator = formatted.split(" ")[1].replace("§r", "").replace("§", "");

                List<ColoredTextModule.Text> texts = new ArrayList<ColoredTextModule.Text>();
                texts.add(new ColoredTextModule.Text(player1.substring(1) + " ", getModColor(player1.charAt(0)).getColor().getRGB()));
                texts.add(new ColoredTextModule.Text(separator.substring(1) + " ", getModColor(separator.charAt(0)).getColor().getRGB()));
                texts.add(new ColoredTextModule.Text(player2.substring(1), getModColor(player2.charAt(0)).getColor().getRGB()));
                lasertagModule.addText(texts);
                return ChatDisplayAction.HIDE;
            }
        }
        return ChatDisplayAction.NORMAL;
    }

    @Override
    public void handlePluginMessage(String s, PacketBuffer packetBuffer) throws Exception {
        if (s.equalsIgnoreCase("ServerState")) {
            if(!LPmitKevAddon.getInstance().isDisplayServerStateActive())
                return;
            String text = new String(packetBuffer.array(), "UTF-8");
            PacketPlayServerStatus packetPlayServerStatus = new PacketPlayServerStatus(LPmitKevAddon.getInstance().getApi().getCurrentServer().getIp(), LPmitKevAddon.getInstance().getApi().getCurrentServer().getPort(), text);
            LPmitKevAddon.getInstance().getApi().getLabyModChatClient().getClientConnection().sendPacket(packetPlayServerStatus);

            update();
        } else if (s.equalsIgnoreCase("Laser")) {
            if (!LPmitKevAddon.getInstance().isLaserActive())
                return;
            float x1 = packetBuffer.readFloat();
            float y1 = packetBuffer.readFloat();
            float z1 = packetBuffer.readFloat();
            float x2 = packetBuffer.readFloat();
            float y2 = packetBuffer.readFloat();
            float z2 = packetBuffer.readFloat();
            int cRed = packetBuffer.readByte() - Byte.MIN_VALUE;
            int cGreen = packetBuffer.readByte() - Byte.MIN_VALUE;
            int cBlue = packetBuffer.readByte() - Byte.MIN_VALUE;
            Location start = new Location(x1, y1, z1);
            Location end = new Location(x2, y2, z2);
            new Laser(start, end, new Color(cRed, cGreen, cBlue));
        }
    }

    @Override
    public void handleTabInfoMessage(TabListEvent.Type type, String s, String s1) throws Exception { }

    @Override
    public void fillSubSettings(List<SettingsElement> list) { }

    public void update() {
        if (!LPmitKevAddon.getInstance().getApi().isIngame())
            return;
        if (LPmitKevAddon.getInstance().isLaserActive()) {
            if (LabyMod.getInstance().getCurrentServerData() != null)
                LabyModCore.getMinecraft().sendPluginMessage("LaserActivated", new PacketBuffer(Unpooled.EMPTY_BUFFER));
        } else {
            LabyModCore.getMinecraft().sendPluginMessage("LaserDeactivated", new PacketBuffer(Unpooled.EMPTY_BUFFER));
        }
    }

    public String getNickname() {
        return nickname;
    }

    private ModColor getModColor(char colorCode) {
        for (ModColor modColor : ModColor.values()) {
            if(modColor.getColorChar() == colorCode)
                return modColor;
        }
        return null;
    }
}
