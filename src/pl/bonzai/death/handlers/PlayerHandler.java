package pl.bonzai.death.handlers;

import com.google.common.base.Strings;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.bonzai.death.json.Config;
import pl.bonzai.death.utils.ChatUtil;

public class PlayerHandler implements Listener {

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        final Player entity = e.getEntity();
        final Player killer = entity.getKiller();
        ChatUtil.sendTitle(entity,
                Config.getInstance().messages.deathMessageTitle.replace("{KILLER}", killer.getName()),
                 Config.getInstance().messages.deathMessageSubTitle.replace("{HEALTH}", getProgressBar((int) killer.getHealth(), (int) killer.getMaxHealth(), 10, '❤', Config.getInstance().bar.completedBarColor, Config.getInstance().bar.notCompletedBarColor)).replace("{ABS}", ""+((CraftPlayer) killer).getHandle().getAbsorptionHearts()),
                30, 50, 10);
    }

    public String getProgressBar(int current, int max, int totalBars, char completedSymbol, String completedColor, String notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + completedSymbol, progressBars)
                + Strings.repeat("" + notCompletedColor + completedSymbol, totalBars - progressBars);
    }
}
