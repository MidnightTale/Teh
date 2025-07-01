package net.hynse.teh.display;

import net.kyori.adventure.text.format.TextColor;

public final class IndicatorColors {
    private final TextColor hpLostColor;
    private final TextColor apLostColor;
    private final TextColor bothLostColor;
    private final TextColor hpGainColor;
    private final TextColor apGainColor;
    private final TextColor bothGainColor;
    private final TextColor xpGainColor;
    private final TextColor xpLostColor;

    public IndicatorColors(
        TextColor hpLostColor,
        TextColor apLostColor,
        TextColor bothLostColor,
        TextColor hpGainColor,
        TextColor apGainColor,
        TextColor bothGainColor,
        TextColor xpGainColor,
        TextColor xpLostColor
    ) {
        this.hpLostColor = hpLostColor;
        this.apLostColor = apLostColor;
        this.bothLostColor = bothLostColor;
        this.hpGainColor = hpGainColor;
        this.apGainColor = apGainColor;
        this.bothGainColor = bothGainColor;
        this.xpGainColor = xpGainColor;
        this.xpLostColor = xpLostColor;
    }

    public TextColor getHpLostColor() { return hpLostColor; }
    public TextColor getApLostColor() { return apLostColor; }
    public TextColor getBothLostColor() { return bothLostColor; }
    public TextColor getHpGainColor() { return hpGainColor; }
    public TextColor getApGainColor() { return apGainColor; }
    public TextColor getBothGainColor() { return bothGainColor; }
    public TextColor getXpGainColor() { return xpGainColor; }
    public TextColor getXpLostColor() { return xpLostColor; }
} 