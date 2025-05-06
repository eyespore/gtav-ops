package club.pineclone.gtavops;

import club.pineclone.gtavops.gui.theme.BaseTheme;
import io.vproxy.vfx.theme.Theme;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Theme.setTheme(new BaseTheme());
        Application.launch(MainFX.class, args);
    }
}
