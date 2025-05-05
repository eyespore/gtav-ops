package club.pineclone.gtavops.gui.component;

import club.pineclone.gtavops.gui.theme.ExtendedFontUsages;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.theme.Theme;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class VTextField extends TextField {

    public VTextField() {
        setBackground(new Background(new BackgroundFill(
                Theme.current().sceneBackgroundColor(),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));

        setBorder(new Border(new BorderStroke(
                Theme.current().windowBorderColorDark(),
                BorderStrokeStyle.SOLID,
                new CornerRadii(8),
                new BorderWidths(0.5))));

        FontManager.get().setFont(ExtendedFontUsages.textField, this);
        String textColor = formatAsHex(Theme.current().normalTextColor());
        String promptColor = formatAsHex(Theme.current().normalTextColor().darker());

        setStyle("-fx-text-fill: " + textColor + "; -fx-prompt-text-fill: " + promptColor + ";");
    }

    private String formatAsHex(Color color) {
       return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }
}
