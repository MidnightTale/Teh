package net.hynse.teh.event;

import net.hynse.teh.Teh;
import net.hynse.teh.display.DamageDisplayUtil;
import net.kyori.adventure.text.format.TextColor;
import net.hynse.teh.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;

public class TehEventListener implements Listener {
 
    private String getIndicatorColorHex(TextColor color) {
        return String.format("#%06X", color.value());
    }

    private Component formatIndicatorComponent(String template, TextColor color, String icon, double amount, String type) {
        String hex = getIndicatorColorHex(color);
        String msg = template
            .replace("{indicator_color}", hex)
            .replace("{icon}", icon)
            .replace("{amount}", String.format(amount % 1 == 0 ? "%.0f" : "%.1f", amount))
            .replace("{type}", type);
        return MiniMessage.miniMessage().deserialize(msg);
    }

    @EventHandler
    void onEntityDamage(EntityDamageEvent e) {
        if (e.isCancelled() ||
            e.getFinalDamage() == 0 ||
            e.getEntity() instanceof org.bukkit.entity.Item ||
            e.getEntity().getType() == org.bukkit.entity.EntityType.ALLAY) return;
        String icon = ConfigManager.getIconForDamageCause(e.getCause());
        String template = ConfigManager.getMessageTemplate("damage");
        if (e.getEntity() instanceof Player player) {
            double hpBefore = player.getHealth();
            double apBefore = player.getAbsorptionAmount();
            double damage = e.getFinalDamage();
            Teh.instance.scheduler.runTaskLaterAtEntity(player, () -> {
                double hpAfter = player.getHealth();
                double apAfter = player.getAbsorptionAmount();
                double hpDiff = hpBefore - hpAfter;
                double apDiff = apBefore - apAfter;
                String type;
                TextColor color;
                double amount;
                if (hpDiff > 0 && apDiff == 0) {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpLostColor();
                    amount = hpDiff;
                } else if (apDiff > 0 && hpDiff == 0) {
                    type = "AP";
                    color = ConfigManager.getIndicatorColors().getApLostColor();
                    amount = apDiff;
                } else if (hpDiff > 0 && apDiff > 0) {
                    type = "HP+AP";
                    color = ConfigManager.getIndicatorColors().getBothLostColor();
                    amount = hpDiff + apDiff;
                } else {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpLostColor();
                    amount = damage;
                }
                Component comp = formatIndicatorComponent(template, color, icon, amount, type);
                DamageDisplayUtil.spawnDamageDisplay(player, comp);
            }, 1);
        } else if (e.getEntity() instanceof LivingEntity living) {
            double hpBefore = living.getHealth();
            double apBefore = living.getAbsorptionAmount();
            double damage = e.getFinalDamage();
            Teh.instance.scheduler.runTaskLaterAtEntity(living, () -> {
                double hpAfter = living.getHealth();
                double apAfter = living.getAbsorptionAmount();
                double hpDiff = hpBefore - hpAfter;
                double apDiff = apBefore - apAfter;
                String type;
                TextColor color;
                double amount;
                if (hpDiff > 0 && apDiff == 0) {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpLostColor();
                    amount = hpDiff;
                } else if (apDiff > 0 && hpDiff == 0) {
                    type = "AP";
                    color = ConfigManager.getIndicatorColors().getApLostColor();
                    amount = apDiff;
                } else if (hpDiff > 0 && apDiff > 0) {
                    type = "HP+AP";
                    color = ConfigManager.getIndicatorColors().getBothLostColor();
                    amount = hpDiff + apDiff;
                } else {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpLostColor();
                    amount = damage;
                }
                Component comp = formatIndicatorComponent(template, color, icon, amount, type);
                DamageDisplayUtil.spawnDamageDisplay(living, comp);
            }, 1);
        } else {
            String type = "HP";
            TextColor color = ConfigManager.getIndicatorColors().getHpLostColor();
            double amount = e.getFinalDamage();
            Component comp = formatIndicatorComponent(template, color, icon, amount, type);
            DamageDisplayUtil.spawnDamageDisplay(e.getEntity(), comp);
        }
    }

    @EventHandler
    void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if (e.isCancelled() ||
            e.getAmount() <= 0 ||
            e.getEntity() instanceof org.bukkit.entity.Item ||
            e.getEntity().getType() == org.bukkit.entity.EntityType.ALLAY) return;
        String template = ConfigManager.getMessageTemplate("heal");
        if (e.getEntity() instanceof Player player) {
            double hpBefore = player.getHealth();
            double apBefore = player.getAbsorptionAmount();
            double amount = e.getAmount();
            Teh.instance.scheduler.runTaskLaterAtEntity(player, () -> {
                double hpAfter = player.getHealth();
                double apAfter = player.getAbsorptionAmount();
                double hpDiff = hpAfter - hpBefore;
                double apDiff = apAfter - apBefore;
                String type;
                TextColor color;
                double amt;
                if (hpDiff > 0 && apDiff == 0) {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpGainColor();
                    amt = hpDiff;
                } else if (apDiff > 0 && hpDiff == 0) {
                    type = "AP";
                    color = ConfigManager.getIndicatorColors().getApGainColor();
                    amt = apDiff;
                } else if (hpDiff > 0 && apDiff > 0) {
                    type = "HP+AP";
                    color = ConfigManager.getIndicatorColors().getBothGainColor();
                    amt = hpDiff + apDiff;
                } else {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpGainColor();
                    amt = amount;
                }
                Component comp = formatIndicatorComponent(template, color, "", amt, type);
                DamageDisplayUtil.spawnDamageDisplay(player, comp);
            }, 1);
        } else if (e.getEntity() instanceof LivingEntity living) {
            double hpBefore = living.getHealth();
            double apBefore = living.getAbsorptionAmount();
            double amount = e.getAmount();
            Teh.instance.scheduler.runTaskLaterAtEntity(living, () -> {
                double hpAfter = living.getHealth();
                double apAfter = living.getAbsorptionAmount();
                double hpDiff = hpAfter - hpBefore;
                double apDiff = apAfter - apBefore;
                String type;
                TextColor color;
                double amt;
                if (hpDiff > 0 && apDiff == 0) {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpGainColor();
                    amt = hpDiff;
                } else if (apDiff > 0 && hpDiff == 0) {
                    type = "AP";
                    color = ConfigManager.getIndicatorColors().getApGainColor();
                    amt = apDiff;
                } else if (hpDiff > 0 && apDiff > 0) {
                    type = "HP+AP";
                    color = ConfigManager.getIndicatorColors().getBothGainColor();
                    amt = hpDiff + apDiff;
                } else {
                    type = "HP";
                    color = ConfigManager.getIndicatorColors().getHpGainColor();
                    amt = amount;
                }
                Component comp = formatIndicatorComponent(template, color, "", amt, type);
                DamageDisplayUtil.spawnDamageDisplay(living, comp);
            }, 1);
        } else {
            String type = "HP";
            TextColor color = ConfigManager.getIndicatorColors().getHpGainColor();
            double amt = e.getAmount();
            Component comp = formatIndicatorComponent(template, color, "", amt, type);
            DamageDisplayUtil.spawnDamageDisplay(e.getEntity(), comp);
        }
    }

    @EventHandler
    void onPlayerExpChange(PlayerExpChangeEvent e) {
        if (e.getAmount() > 0) {
            String template = ConfigManager.getMessageTemplate("xp-gain");
            TextColor color = ConfigManager.getIndicatorColors().getXpGainColor();
            Component comp = formatIndicatorComponent(template, color, "", e.getAmount(), "XP");
            DamageDisplayUtil.spawnDamageDisplay(e.getPlayer(), comp);
        } else if (e.getAmount() < 0) {
            String template = ConfigManager.getMessageTemplate("xp-lost");
            TextColor color = ConfigManager.getIndicatorColors().getXpLostColor();
            Component comp = formatIndicatorComponent(template, color, "", Math.abs(e.getAmount()), "XP");
            DamageDisplayUtil.spawnDamageDisplay(e.getPlayer(), comp);
        }
    }
} 