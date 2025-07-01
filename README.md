# Floating Damage/Heal/XP Indicator
- Animated floating text for damage, healing, and XP
- Per-player toggle: `/toggleteh`
- Fully customizable colors, icons, and messages (`config.yml`)
- MiniMessage support (true color, icons)
- Reload config instantly: `/tehreload`

## Quick Config Example
```yaml
indicator-colors:
  hp-lost: '#FF3B30'
  hp-gain: '#34C759'
  xp-gain: '#5EDE3E'
messages:
  damage:
    template: "<color:{indicator_color}>{icon} -{amount} {type}</color>"
    icons:
      ENTITY_ATTACK: "🗡"
  heal:
    template: "<color:{indicator_color}>✨ +{amount} {type}</color>"
  xp-gain:
    template: "<color:{indicator_color}>🟢 +{amount} XP</color>"
```
