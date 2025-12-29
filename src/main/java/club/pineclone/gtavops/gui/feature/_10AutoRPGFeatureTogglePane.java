package club.pineclone.gtavops.gui.feature;

import club.pineclone.gtavops.common.ResourceHolder;
import club.pineclone.gtavops.common.SessionType;
import club.pineclone.gtavops.config.Config;
import club.pineclone.gtavops.gui.component.TriggerModeButton;
import club.pineclone.gtavops.gui.component.VCycleButton;
import club.pineclone.gtavops.gui.component.VKeyChooseButton;
import club.pineclone.gtavops.gui.component.VSettingStage;
import club.pineclone.gtavops.gui.forked.ForkedKeyChooser;
import club.pineclone.gtavops.gui.forked.ForkedSlider;
import club.pineclone.gtavops.i18n.ExtendedI18n;
import club.pineclone.gtavops.macro.MacroContextHolder;
import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.impl.AutoRPGAction;
import club.pineclone.gtavops.macro.trigger.Trigger;
import club.pineclone.gtavops.macro.trigger.TriggerFactory;
import club.pineclone.gtavops.macro.trigger.TriggerIdentity;
import club.pineclone.gtavops.macro.trigger.TriggerMode;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/* 连发RPG */
public class _10AutoRPGFeatureTogglePane
        extends FeatureTogglePane
        implements ResourceHolder {

    public _10AutoRPGFeatureTogglePane() {
        super(new ExtraFunctionFeatureContext(), new AutoRPGSettingStage());
    }

    @Override
    protected String getTitle() {
        return getI18n().autoRPG.title;
    }

    @Override
    public boolean init() {
        return getConfig().autoRPG.baseSetting.enable;
    }

    @Override
    public void stop(boolean enabled) {
        getConfig().autoRPG.baseSetting.enable = enabled;
    }

    private static class ExtraFunctionFeatureContext
            extends FeatureContext
            implements ResourceHolder, MacroContextHolder {

//        private UUID joinANewSessionMacroId;  /* 快速加入新战局 */
//        private UUID joinABookmarkedJobMacroId;  /* 加入已收藏差事 */
        private UUID macroId;  /* 宏ID */

        private final Config config = getConfig();
        private final Config.AutoRPG aRpgConfig = config.autoRPG;

        @Override
        protected void activate() {
            Config.AutoRPG.BaseSetting baseSetting = aRpgConfig.getBaseSetting();

            Key activateKey = baseSetting.activateKey;
            TriggerMode activateMethod = baseSetting.activateMethod;
            Key heavyWeaponKey = baseSetting.heavyWeaponKey;
            Key specialWeaponKey = baseSetting.specialWeaponKey;
            long triggerInterval = (long) (Math.floor(baseSetting.triggerInterval));
            long mousePressInterval = (long) (Math.floor(baseSetting.mousePressInterval));

            Trigger trigger = TriggerFactory.simple(TriggerIdentity.of(activateMethod, activateKey));
            Action action = new AutoRPGAction(heavyWeaponKey, specialWeaponKey, triggerInterval, mousePressInterval);

            macroId = MACRO_FACTORY.createSimpleMacro(trigger, action);
            MACRO_REGISTRY.install(macroId);
        }

        @Override
        protected void deactivate() {
            MACRO_REGISTRY.uninstall(macroId);
        }
    }

    private static class AutoRPGSettingStage
            extends VSettingStage
            implements ResourceHolder {

        private final ExtendedI18n i18n = getI18n();
        private final ExtendedI18n.AutoRPG aRpgI18n = i18n.autoRPG;

        private final Config config = getConfig();
        private final Config.AutoRPG aRpgConfig = config.autoRPG;

        private static final int FLAG_WITH_KEY_AND_MOUSE = ForkedKeyChooser.FLAG_WITH_KEY  | ForkedKeyChooser.FLAG_WITH_MOUSE;

        /* Join A New Session */
        private final VKeyChooseButton activateKeyBtn = new VKeyChooseButton(FLAG_WITH_KEY_AND_MOUSE);
        private final VKeyChooseButton heavyWeaponKeyBtn = new VKeyChooseButton(ForkedKeyChooser.FLAG_WITH_KEY);
        private final VKeyChooseButton specialWeaponKeyBtn = new VKeyChooseButton(ForkedKeyChooser.FLAG_WITH_KEY);

        private final TriggerModeButton activateMethodBtn = new TriggerModeButton(TriggerModeButton.FLAG_WITH_TOGGLE | TriggerModeButton.FLAG_WITH_HOLD);

        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(400);
            setRange(200, 1200);
        }};

        private final ForkedSlider mousePressIntervalSlider = new ForkedSlider() {{
            setLength(400);
            setRange(200, 1000);
        }};

        public AutoRPGSettingStage() {
            getContent().getChildren().addAll(contentBuilder()
                    .divide(aRpgI18n.baseSetting.title)
                    .button(aRpgI18n.baseSetting.activateMethod, activateMethodBtn)
                    .button(aRpgI18n.baseSetting.activateKey, activateKeyBtn)
                    .button(aRpgI18n.baseSetting.heavyWeaponKey, heavyWeaponKeyBtn)
                    .button(aRpgI18n.baseSetting.specialWeaponKey, specialWeaponKeyBtn)
                    .slider(aRpgI18n.baseSetting.triggerInterval, triggerIntervalSlider)
                    .slider(aRpgI18n.baseSetting.mousePressInterval, mousePressIntervalSlider)
                    .build());
        }

        @Override
        public String getTitle() {
            return aRpgI18n.title;
        }

        @Override
        public void onVSettingStageInit() {
            activateMethodBtn.triggerModeProperty().set(aRpgConfig.baseSetting.activateMethod);
            heavyWeaponKeyBtn.keyProperty().set(aRpgConfig.baseSetting.heavyWeaponKey);
            specialWeaponKeyBtn.keyProperty().set(aRpgConfig.baseSetting.specialWeaponKey);
            activateKeyBtn.keyProperty().set(aRpgConfig.baseSetting.activateKey);
            triggerIntervalSlider.setValue(aRpgConfig.baseSetting.triggerInterval);
            mousePressIntervalSlider.setValue(aRpgConfig.baseSetting.mousePressInterval);
        }

        @Override
        public void onVSettingStageExit() {
            aRpgConfig.baseSetting.activateMethod = activateMethodBtn.triggerModeProperty().get();
            aRpgConfig.baseSetting.activateKey = activateKeyBtn.keyProperty().get();
            aRpgConfig.baseSetting.heavyWeaponKey = heavyWeaponKeyBtn.keyProperty().get();
            aRpgConfig.baseSetting.specialWeaponKey = specialWeaponKeyBtn.keyProperty().get();
            aRpgConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();
            aRpgConfig.baseSetting.mousePressInterval = mousePressIntervalSlider.valueProperty().get();
        }
    }
}
