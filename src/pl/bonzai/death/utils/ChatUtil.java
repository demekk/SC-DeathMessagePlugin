package pl.bonzai.death.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatUtil
{
    public static Random random;

    public static String nmsver = Bukkit.getServer().getClass().getPackage().getName();

    private static final LinkedHashMap<Integer, String> units;


    public static int getIntegerFromString(String msg) {
        return Integer.parseInt(msg);
    }

    public static boolean isLeather(List<ItemStack> items) {
        boolean is = true;
        for (ItemStack item : items) {
            if (item == null)
                continue;
            if (!item.getType().toString().contains("LEATHER"))
                is = false;
        }
        return is;
    }

    public static void strike(final Location location) {
        final World world = location.getWorld();
        world.strikeLightningEffect(location.add(1.0, 0.0, 0.0));
        world.strikeLightningEffect(location.add(-1.0, 0.0, 0.0));
        world.strikeLightningEffect(location.add(0.0, 0.0, 1.0));
        world.strikeLightningEffect(location.add(1.0, 0.0, -1.0));
        world.setThundering(false);
        world.setStorm(false);
        world.setWeatherDuration(1000000);
    }

    public static String getProgressBar(int current, int max, int totalBars, String symbol, String completedColor, String notCompletedColor) {
        float percent = current / max;
        int progressBars = (int)(totalBars * percent);
        int leftOver = totalBars - progressBars;
        StringBuilder sb = new StringBuilder();
        if (current > max) {
            sb.append(completedColor);
            for (int j = 0; j < totalBars; j++)
                sb.append(symbol);
            return sb.toString();
        }
        sb.append(completedColor);
        int i;
        for (i = 0; i < progressBars; i++)
            sb.append(symbol);
        sb.append(notCompletedColor);
        for (i = 0; i < leftOver; i++)
            sb.append(symbol);
        return sb.toString();
    }

    public static String replace(String text, String searchString, int replacement) {
        return replace(text, searchString, Integer.toString(replacement));
    }

    public static String replace(String text, String searchString, long replacement) {
        return replace(text, searchString, Long.toString(replacement));
    }

    public static String replace(String text, String searchString, boolean replacement) {
        return replace(text, searchString, Boolean.toString(replacement));
    }

    public static String replace(String text, String searchString, String replacement) {
        if (text == null || text.isEmpty() || searchString.isEmpty())
            return text;
        if (replacement == null)
            replacement = "";
        int start = 0;
        int max = -1;
        int end = text.indexOf(searchString, start);
        if (end == -1)
            return text;
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0) ? 0 : increase;
        increase *= (max > 64) ? 64 : ((max < 0) ? 16 : max);
        StringBuilder sb = new StringBuilder(text.length() + increase);
        while (end != -1) {
            sb.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0)
                break;
            end = text.indexOf(searchString, start);
        }
        sb.append(text.substring(start));
        return sb.toString();
    }

    public static boolean locationEqualsBlock(Location loc, Location loc1) {
        return (loc.getBlockX() == loc1.getBlockX() && loc.getBlockY() == loc1.getBlockY() && loc.getBlockZ() == loc1.getBlockZ());
    }
    
    public static Location locFromString(final String str) {
        final String[] str2loc = str.split(":");
        final Location loc = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0, 0.0f, 0.0f);
        loc.setX(Double.parseDouble(str2loc[0]));
        loc.setY(Double.parseDouble(str2loc[1]));
        loc.setZ(Double.parseDouble(str2loc[2]));
        loc.setYaw(Float.parseFloat(str2loc[3]));
        loc.setPitch(Float.parseFloat(str2loc[4]));
        return loc;
    }
    
    public static void giveItems(final Player p, final ItemStack is) {
        if (p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItemNaturally(p.getLocation(), is);
            return;
        }
        p.getInventory().addItem(new ItemStack[] { is });
        p.updateInventory();
    }
    
    public static String locToString2(final Location location) {
        return String.valueOf(new StringBuilder().append(String.valueOf(location.getX())).append(":").append(location.getY()).append(":").append(location.getZ()));
    }

    public static String distanceToString(int i) {
        if (i < 1000)
            return i + " m";
        return (i / 1000.0D) + " km";
    }

    public static String getDurationBreakdown(int second) {
        if (second == 0)
            return "0";
        long days = TimeUnit.SECONDS.toDays(second);
        if (days > 0L)
            second -= (int) TimeUnit.DAYS.toSeconds(days);
        long hours = TimeUnit.SECONDS.toHours(second);
        if (hours > 0L)
            second -= (int)TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(second);
        if (minutes > 0L)
            second -= (int)TimeUnit.MINUTES.toSeconds(minutes);
        StringBuilder sb = new StringBuilder();
        if (days > 0L) {
            sb.append(days);
            sb.append("dni ");
        }
        if (hours > 0L) {
            sb.append(hours);
            sb.append("godz ");
        }
        if (minutes > 0L) {
            sb.append(minutes);
            sb.append("min ");
        }
        if (second > 0) {
            sb.append(second);
            sb.append("sek");
        }
        return sb.toString();
    }
    
    public static int getAmountOfItem(final Material material, final Player player, final short durability) {
        int amount = 0;
        final ItemStack[] items;
        final ItemStack[] array;
        final ItemStack[] contents = array = (items = player.getInventory().getContents());
        for (final ItemStack itemStack : array) {
            if (itemStack != null && itemStack.getType().equals((Object)material) && itemStack.getDurability() == durability) {
                amount += itemStack.getAmount();
            }
        }
        return amount;
    }
    
    public static Location locFromString2(final String s) {
        final String[] split = s.split(":");
        final Location location = new Location((World)Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0);
        location.setX(Double.parseDouble(split[0]));
        location.setY(Double.parseDouble(split[1]));
        location.setZ(Double.parseDouble(split[2]));
        return location;
    }
    
    public static ItemStack getItemStackFromString(final String itemstack) {
        final String[] splits = itemstack.split("@");
        final String type = splits[0];
        final String data = (splits.length == 2) ? splits[1] : null;
        if (data == null) {
            return new ItemStack(Material.getMaterial(type), 1);
        }
        return new ItemStack(Material.getMaterial(type), 1, (short)Integer.parseInt(data));
    }
    
    public static String locToString(final double x, final double y, final double z) {
        return x + ":" + y + ":" + z + ":" + 0.0f + ":" + 0.0f;
    }
    
    public static String locToString(final Location loc) {
        return loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
    }
    
    public static String fixColor(final String s) {
        if (s == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    public static Collection<String> fixColor(final Collection<String> collection) {
        final Collection<String> local = new ArrayList<String>();
        for (final String s : collection) {
            local.add(fixColor(s));
        }
        return local;
    }
    
    public static int floor(final double value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static ItemStack getPlayerHead(final String name) {
        final ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
        meta.setOwner(name);
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static void sendActionbar(final Player p, final String msg) {
        final IChatBaseComponent cmp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', msg) + "\"}");
        final PacketPlayOutChat bar = new PacketPlayOutChat(cmp, (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
    }

    public static void sendActionbarGuild(final Player player, final String s) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(String.valueOf(new StringBuilder().append("{\"text\": \"").append(ChatColor.translateAlternateColorCodes('&', s)).append("\"}"))), (byte)2));
    }
    
    public static void sendActionbarToAllPlayers(final String msg) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            sendActionbar(p, msg);
        }
    }
    
    public static void sendError(final String message) {
        System.out.println("[hellhcCore]" + message);
    }
    
    public static boolean sendTitle(final Player player, String title, String subtitle, final int fadeIn, final int stay, final int fadeOut) {
        if (title == null) {
            title = "";
        }
        if (subtitle == null) {
            subtitle = "";
        }
        title = title.replace("&", "§");
        subtitle = subtitle.replace("&", "§");
        final CraftPlayer craftPlayer = (CraftPlayer)player;
        final PacketPlayOutTitle packetTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, (IChatBaseComponent)null, fadeIn, stay, fadeOut);
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)packetTimes);
        final IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
        final PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)packetTitle);
        final IChatBaseComponent chatSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
        final PacketPlayOutTitle packetSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubtitle);
        craftPlayer.getHandle().playerConnection.sendPacket((Packet)packetSubtitle);
        return false;
    }
    
    public static void sendTitleToAllPlayers(final String title, final String subtitle) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            sendTitle(p, title, subtitle, 40, 70, 30);
        }
    }
    
    public static void sendActionbarToAllAdmins(final String msg, final String permission) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(permission)) {
                sendActionbar(p, msg);
            }
        }
    }
    
    public static double round(final double value, final int decimals) {
        final double p = Math.pow(10.0, decimals);
        return Math.round(value * p) / p;
    }
    
    public static String[] fixColor(final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = fixColor(array[i]);
        }
        return array;
    }
    
    public static boolean sendMessage(final CommandSender sender, final String message, final String permission) {
        if (sender instanceof ConsoleCommandSender) {
            sendMessage(sender, message);
        }
        return permission != null && permission != "" && sender.hasPermission(permission) && sendMessage(sender, message);
    }
    
    public static boolean sendMessage(final CommandSender sender, final String message) {
        if (sender instanceof Player) {
            if (message != null || message != "") {
                sender.sendMessage(fixColor(message));
            }
            message.replace("&", "§");
        }
        else {
            sender.sendMessage(ChatColor.stripColor(fixColor(message)));
        }
        return true;
    }
    
    public static void removeItems(final Player p, final ItemStack... items) {
        final Inventory i = (Inventory)p.getInventory();
        final HashMap<Integer, ItemStack> notStored = (HashMap<Integer, ItemStack>)i.removeItem(items);
        for (Map.Entry<Integer, ItemStack> entry : notStored.entrySet()) {}
    }
    
    public static boolean sendMessage(final Collection<? extends CommandSender> collection, final String message) {
        for (final CommandSender cs : collection) {
            sendMessage(cs, message);
        }
        return true;
    }
    
    public static boolean sendMessage(final Collection<? extends CommandSender> collection, final String message, final String permission) {
        for (final CommandSender cs : collection) {
            sendMessage(cs, message, permission);
        }
        return true;
    }
    
    public static boolean containsIgnoreCase(final String[] array, final String element) {
        for (final String s : array) {
            if (s.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }
    
    public static void copy(final InputStream in, final File file) {
        try {
            final OutputStream out = new FileOutputStream(file);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Material getMaterial(final String materialName) {
        Material returnMaterial = null;
        if (isInteger(materialName)) {
            final int id = Integer.parseInt(materialName);
            returnMaterial = Material.getMaterial(id);
        }
        else {
            returnMaterial = Material.matchMaterial(materialName);
        }
        return returnMaterial;
    }
    
    public static void giveItems(final Player p, final ItemStack... items) {
        final Inventory i = (Inventory)p.getInventory();
        final HashMap<Integer, ItemStack> notStored = (HashMap<Integer, ItemStack>)i.addItem(items);
        for (final Map.Entry<Integer, ItemStack> e : notStored.entrySet()) {
            p.getWorld().dropItemNaturally(p.getLocation(), (ItemStack)e.getValue());
        }
    }
    
    public static Player getDamager(final EntityDamageByEntityEvent e) {
        final Entity damager = e.getDamager();
        if (damager instanceof Player) {
            return (Player)damager;
        }
        if (damager instanceof Projectile) {
            final Projectile p = (Projectile)damager;
            if (p.getShooter() instanceof Player) {
                return (Player)p.getShooter();
            }
        }
        return null;
    }

    public static long parseDateDiff(String time, boolean future) {
        try {
            Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
            Matcher m = timePattern.matcher(time);
            int years = 0;
            int months = 0;
            int weeks = 0;
            int days = 0;
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            boolean found = false;
            while (m.find()) {
                if (m.group() != null && !m.group().isEmpty()) {
                    for (int i = 0; i < m.groupCount(); i++) {
                        if (m.group(i) != null && !m.group(i).isEmpty()) {
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        continue;
                    if (m.group(1) != null && !m.group(1).isEmpty())
                        years = Integer.parseInt(m.group(1));
                    if (m.group(2) != null && !m.group(2).isEmpty())
                        months = Integer.parseInt(m.group(2));
                    if (m.group(3) != null && !m.group(3).isEmpty())
                        weeks = Integer.parseInt(m.group(3));
                    if (m.group(4) != null && !m.group(4).isEmpty())
                        days = Integer.parseInt(m.group(4));
                    if (m.group(5) != null && !m.group(5).isEmpty())
                        hours = Integer.parseInt(m.group(5));
                    if (m.group(6) != null && !m.group(6).isEmpty())
                        minutes = Integer.parseInt(m.group(6));
                    if (m.group(7) == null)
                        break;
                    if (m.group(7).isEmpty())
                        break;
                    seconds = Integer.parseInt(m.group(7));
                    break;
                }
            }
            if (!found)
                return -1L;
            Calendar c = new GregorianCalendar();
            if (years > 0)
                c.add(1, years * (future ? 1 : -1));
            if (months > 0)
                c.add(2, months * (future ? 1 : -1));
            if (weeks > 0)
                c.add(3, weeks * (future ? 1 : -1));
            if (days > 0)
                c.add(5, days * (future ? 1 : -1));
            if (hours > 0)
                c.add(11, hours * (future ? 1 : -1));
            if (minutes > 0)
                c.add(12, minutes * (future ? 1 : -1));
            if (seconds > 0)
                c.add(13, seconds * (future ? 1 : -1));
            Calendar max = new GregorianCalendar();
            max.add(1, 10);
            if (c.after(max))
                return max.getTimeInMillis();
            return c.getTimeInMillis();
        } catch (Exception e) {
            return -1L;
        }
    }

    public static String remaingTime(long value) {
        int seconds = (int)((value - System.currentTimeMillis()) / 1000L);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> e : units.entrySet()) {
            int iDiv = seconds / ((Integer)e.getKey()).intValue();
            if (iDiv >= 1) {
                int x = (int)Math.floor(iDiv);
                sb.append(x).append(e.getValue());
                seconds -= x * ((Integer)e.getKey()).intValue();
            }
        }
        return sb.toString();
    }

    static {
        (units = new LinkedHashMap<>(6)).put(Integer.valueOf(31104000), "y, ");
        units.put(Integer.valueOf(2592000), "msc, ");
        units.put(Integer.valueOf(86400), "d, ");
        units.put(Integer.valueOf(3600), "h, ");
        units.put(Integer.valueOf(60), "min, ");
        units.put(Integer.valueOf(1), "sek");
    }

    public static ItemStack parseItemStack(String string) {
        ItemStack is = new ItemStack(Material.DIAMOND);
        ItemMeta im = is.getItemMeta();
        String[] args = string.split(" ");
        byte b;
        int i;
        String[] arrayOfString1;
        for (i = (arrayOfString1 = args).length, b = 0; b < i; ) {
            String arg = arrayOfString1[b];
            String[] splitArg = arg.split(":");
            String key = splitArg[0];
            String value = splitArg[1];
            if (key.equalsIgnoreCase("material")) {
                Material mat = Material.matchMaterial(value);
                if (mat != null)
                    is.setType(mat);
            } else if (key.equalsIgnoreCase("amount")) {
                if (isInteger(value))
                    is.setAmount(Integer.parseInt(value));
            } else if (key.equalsIgnoreCase("name")) {
                String name = value.replace("_", " ");
                im.setDisplayName(fixColor(name));
            } else if (key.equalsIgnoreCase("lore")) {
                List<String> lore = new LinkedList<>();
                String[] splitLore = value.split("@nl");
                byte b1;
                int j;
                String[] arrayOfString2;
                for (j = (arrayOfString2 = splitLore).length, b1 = 0; b1 < j; ) {
                    String s = arrayOfString2[b1];
                    lore.add(fixColor(s.replace("_", " ")));
                    b1++;
                }
                im.setLore(lore);
            } else if (key.equalsIgnoreCase("data") || key.equalsIgnoreCase("durability")) {
                if (isInteger(value)) {
                    short data = (short)Integer.parseInt(value);
                    is.setDurability(data);
                }
            } else if (key.equalsIgnoreCase("enchants")) {
                String[] enchantmentArray = value.split("@nl");
                byte b1;
                int j;
                String[] arrayOfString2;
                for (j = (arrayOfString2 = enchantmentArray).length, b1 = 0; b1 < j; ) {
                    String s = arrayOfString2[b1];
                    String[] enchantmentSplit = s.split(";");
                    if (enchantmentSplit.length >= 1)
                        try {
                            Enchantment ench = Enchantment.getByName(enchantmentSplit[0]);
                            if (isInteger(enchantmentSplit[1])) {
                                int level = Integer.parseInt(enchantmentSplit[1]);
                                if (ench != null)
                                    im.addEnchant(ench, level, true);
                            }
                        } catch (NumberFormatException numberFormatException) {}
                    b1++;
                }
            }
            b++;
        }
        is.setItemMeta(im);
        return is;
    }
    
    public static boolean isAlphaNumeric(final String s) {
        return s.matches("^[a-zA-Z0-9_]*$");
    }
    
    public static boolean isFloat(final String string) {
        return Pattern.matches("([0-9]*)\\.([0-9]*)", string);
    }
    
    public static boolean isInteger(final String string) {
        return Pattern.matches("-?[0-9]+", string.subSequence(0, string.length()));
    }
    
    static {
        random = new Random();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
    }
}
