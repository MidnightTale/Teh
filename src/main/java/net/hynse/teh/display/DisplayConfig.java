package net.hynse.teh.display;

public class DisplayConfig {
    public final boolean seeThrough;
    public final int backgroundColorARGB;
    public final boolean shadowed;
    public final String alignment;
    public final String billboard;

    public DisplayConfig(boolean seeThrough, int backgroundColorARGB, boolean shadowed, String alignment, String billboard) {
        this.seeThrough = seeThrough;
        this.backgroundColorARGB = backgroundColorARGB;
        this.shadowed = shadowed;
        this.alignment = alignment;
        this.billboard = billboard;
    }
} 