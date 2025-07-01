package net.hynse.teh;

import org.bukkit.configuration.file.FileConfiguration;
import net.kyori.adventure.text.format.TextColor;
import net.hynse.teh.display.IndicatorColors;
import net.hynse.teh.display.DisplayConfig;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {
    private static IndicatorColors indicatorColors;
    private static DisplayConfig displayConfig;
    private static Map<String, String> messageTemplates = new ConcurrentHashMap<>();
    private static Map<String, String> damageIcons = new ConcurrentHashMap<>();

    public static void reload(Teh plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        indicatorColors = new IndicatorColors(
            getConfigColor(config, "indicator-colors.hp-lost", 0xFF3B30),
            getConfigColor(config, "indicator-colors.ap-lost", 0xFFD600),
            getConfigColor(config, "indicator-colors.both-lost", 0xFF9500),
            getConfigColor(config, "indicator-colors.hp-gain", 0x34C759),
            getConfigColor(config, "indicator-colors.ap-gain", 0x32ADE6),
            getConfigColor(config, "indicator-colors.both-gain", 0xAF52DE),
            getConfigColor(config, "indicator-colors.xp-gain", 0x5EDE3E),
            getConfigColor(config, "indicator-colors.xp-lost", 0xFF3B30)
        );
        boolean seeThrough = config.getBoolean("display-settings.see-through", false);
        String bgHex = config.getString("display-settings.background-color", "#00000000");
        int backgroundColorARGB = 0x00000000;
        try {
            if (bgHex.startsWith("#")) bgHex = bgHex.substring(1);
            backgroundColorARGB = (int)Long.parseLong(bgHex, 16);
        } catch (Exception ignored) {}
        boolean shadowed = config.getBoolean("display-settings.shadowed", false);
        String alignment = config.getString("display-settings.alignment", "CENTER");
        String billboard = config.getString("display-settings.billboard", "CENTER");
        displayConfig = new DisplayConfig(seeThrough, backgroundColorARGB, shadowed, alignment, billboard);
        // Load message templates
        messageTemplates.clear();
        damageIcons.clear();
        if (config.isConfigurationSection("messages")) {
            var messages = config.getConfigurationSection("messages");
            if (messages.isConfigurationSection("damage")) {
                var damage = messages.getConfigurationSection("damage");
                messageTemplates.put("damage", damage.getString("template", "<color:{indicator_color}>{icon} -{amount} {type}</color>"));
                if (damage.isConfigurationSection("icons")) {
                    var icons = damage.getConfigurationSection("icons");
                    for (String key : icons.getKeys(false)) {
                        damageIcons.put(key, icons.getString(key, ""));
                    }
                }
            }
            if (messages.isConfigurationSection("heal")) {
                var heal = messages.getConfigurationSection("heal");
                messageTemplates.put("heal", heal.getString("template", "<color:{indicator_color}>✨ +{amount} {type}</color>"));
            }
            if (messages.isConfigurationSection("xp-gain")) {
                var xpGain = messages.getConfigurationSection("xp-gain");
                messageTemplates.put("xp-gain", xpGain.getString("template", "<color:{indicator_color}>🟢 +{amount} XP</color>"));
            }
            if (messages.isConfigurationSection("xp-lost")) {
                var xpLost = messages.getConfigurationSection("xp-lost");
                messageTemplates.put("xp-lost", xpLost.getString("template", "<color:{indicator_color}>🔴 -{amount} XP</color>"));
            }
        }
    }

    private static TextColor getConfigColor(FileConfiguration config, String path, int defaultHex) {
        String hex = config.getString(path);
        if (hex != null && hex.matches("#?[0-9a-fA-F]{6}")) {
            try {
                return TextColor.color(Integer.parseInt(hex.replace("#", ""), 16));
            } catch (Exception ignored) {}
        }
        return TextColor.color(defaultHex);
    }

    public static IndicatorColors getIndicatorColors() {
        return indicatorColors;
    }
    public static DisplayConfig getDisplayConfig() {
        return displayConfig;
    }

    public static String getMessageTemplate(String type) {
        return messageTemplates.getOrDefault(type, "<color:{indicator_color}>{icon} {amount} {type}</color>");
    }

    public static String getIconForDamageCause(DamageCause cause) {
        return damageIcons.getOrDefault(cause.name(), damageIcons.getOrDefault("DEFAULT", ""));
    }
} 