package club.pineclone.gtavops.gui.feature;

import club.pineclone.gtavops.config.ConfigHolder;
import club.pineclone.gtavops.config.Configuration;
import club.pineclone.gtavops.gui.component.VOptionalButton;
import club.pineclone.gtavops.gui.forked.ForkedKeyChooser;
import club.pineclone.gtavops.gui.component.VKeyChooseButton;
import club.pineclone.gtavops.gui.component.VSettingStage;
import club.pineclone.gtavops.gui.forked.ForkedSlider;
import club.pineclone.gtavops.i18n.ExtendedI18n;
import club.pineclone.gtavops.i18n.I18nHolder;
import club.pineclone.gtavops.macro.SimpleMacro;
import club.pineclone.gtavops.macro.trigger.*;
import club.pineclone.gtavops.macro.action.ScheduledAction;
import club.pineclone.gtavops.macro.action.impl.SwapGlitchAction;
import club.pineclone.gtavops.macro.action.decorator.SwapMeleeDecorator;
import club.pineclone.gtavops.macro.action.decorator.SwapRangedDecorator;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.ui.toggle.ToggleSwitch;

/* 切枪偷速 */
public class _01SwapGlitchFeatureTogglePane extends FeatureTogglePane {

    private SimpleMacro bindings;  /* 宏执行器 */

    Configuration.SwapGlitch sgConfig;

    ExtendedI18n i18n;
    ExtendedI18n.SwapGlitch sgI18n;  /* 切枪偷速本地化 */

    @Override
    protected String getTitle() {
        return I18nHolder.get().swapGlitch.title;
    }

    public _01SwapGlitchFeatureTogglePane() {
        sgConfig = ConfigHolder.get().swapGlitch;

        i18n = I18nHolder.get();
        sgI18n = i18n.swapGlitch;  /* 切枪偷速本地化 */
    }

    @Override
    protected void activate() {
        ScheduledAction action = buildAction();
        Trigger trigger = buildTrigger();

        boolean swapRanged = sgConfig.swapRangedSetting.enableSwapRanged;  /* 切出偷速前是否切换远程 */
        if (swapRanged) {
            /* 是否开启安全武器轮盘 */
            boolean enableSafetyWeaponWheel = sgConfig.swapRangedSetting.enableSafetyWeaponWheel;
            Key swapRangedHotkey = sgConfig.swapRangedSetting.rangedWeaponKey;
            long safetyWeaponWheelDuration = 0;

            if (enableSafetyWeaponWheel) {
                /* 开启安全武器轮盘 */
                Key safetyWeaponWheelKey = sgConfig.swapRangedSetting.safetyWeaponWheelKey;
                /* 将触发器构建为条件触发器，如果安全轮盘键被摁下，那么屏蔽原始执行 */
                trigger = new ConditionalTrigger(
                        trigger, TriggerFactory.getTrigger(
                                new TriggerIdentity(safetyWeaponWheelKey, TriggerMode.HOLD)));
                safetyWeaponWheelDuration = (long) Math.floor(sgConfig.swapRangedSetting.safetyWeaponWheelDuration);
            }
            action = new SwapRangedDecorator(action, swapRangedHotkey, safetyWeaponWheelDuration);
        }

        bindings = new SimpleMacro(trigger, action);
        bindings.install();  /* 注册宏执行器 */
    }

    /* 构建触发器 */
    private Trigger buildTrigger() {
        /* 激活热键 */
        Key activatekey = sgConfig.baseSetting.activatekey;
        /* 激活模式 切换执行 or 按住执行 */
        TriggerMode mode = sgConfig.baseSetting.activateMethod == 0 ? TriggerMode.HOLD : TriggerMode.TOGGLE;

        /* 触发类型 按键 or 鼠标 or 滚轮触发 */
        return TriggerFactory.getTrigger(new TriggerIdentity(activatekey, mode));  /* 触发器 */
    }

    /* 构建基础动作，并根据配置添加装饰器 */
    private ScheduledAction buildAction() {
        Key weaponWheelHotkey = sgConfig.baseSetting.targetWeaponWheelKey;  /* 武器轮盘热键 */
        long interval = (long) Math.floor(sgConfig.baseSetting.triggerInterval);  /* 偷速间隔 */

        ScheduledAction action = new SwapGlitchAction(weaponWheelHotkey, interval);  /* 基础执行器 */

        boolean swapMelee = sgConfig.swapMeleeSetting.enableSwapMelee;  /* 切入偷速前是否切换近战 */
        if (swapMelee) {  /* 套装饰器 */
            Key swapMeleeHotkey = sgConfig.swapMeleeSetting.meleeWeaponKey;
            long postSwapMeleeDelay = (long) Math.floor(sgConfig.swapMeleeSetting.postSwapMeleeDelay);  /* 偷速间隔 */
            action = new SwapMeleeDecorator(action, swapMeleeHotkey, postSwapMeleeDelay);
        }
        return action;
    }

    @Override
    protected void deactivate() {
        bindings.uninstall();  /* 注销宏执行器 */
    }

    @Override
    public void init() {
        selectedProperty().set(sgConfig.baseSetting.enable);  /* 加载配置 */
    }

    @Override
    public void stop() {
        sgConfig.baseSetting.enable = selectedProperty().get();  /* 保存最后状态 */
        selectedProperty().set(false);  /* 关闭功能 */
    }

    @Override
    public VSettingStage getSettingStage() {
        return new SGSettingStage();
    }

    private class SGSettingStage extends VSettingStage {

        private static final int FLAG_WITH_KEY_AND_MOUSE = ForkedKeyChooser.FLAG_WITH_KEY  | ForkedKeyChooser.FLAG_WITH_MOUSE;
        private static final int FLAG_WITH_ALL = FLAG_WITH_KEY_AND_MOUSE | ForkedKeyChooser.FLAG_WITH_WHEEL_SCROLL;

        private final VKeyChooseButton activateKeyBtn = new VKeyChooseButton(FLAG_WITH_KEY_AND_MOUSE);
        private final VKeyChooseButton weaponWheelKeyBtn = new VKeyChooseButton(FLAG_WITH_ALL);

        private final VKeyChooseButton meleeKeyBtn = new VKeyChooseButton(ForkedKeyChooser.FLAG_WITH_KEY);
        private final VKeyChooseButton preferredRangedKeyBtn = new VKeyChooseButton(ForkedKeyChooser.FLAG_WITH_KEY);

        private final ToggleSwitch enableSwapMeleeToggle = new ToggleSwitch();
        private final ToggleSwitch enableSwapRangedToggle = new ToggleSwitch();  /* 切出偷速时切换远程武器 */

        private final ToggleSwitch enableSafetyWeaponWheelToggle = new ToggleSwitch();  /* 启用安全武器轮盘 */
        private final VKeyChooseButton safetyWeaponWheelBtn = new VKeyChooseButton();  /* 启用安全武器轮盘 */

        private final VOptionalButton activateMethodBtn = new VOptionalButton() {{
            addOptionalItem(i18n.hold);
            addOptionalItem(i18n.toggle);
        }};

        private final ForkedSlider triggerIntervalSlider = new ForkedSlider() {{
            setLength(400);
            setRange(10, 100);
        }};

        private final ForkedSlider postSwapMeleeDelaySlider = new ForkedSlider() {{
            setLength(400);
            setRange(10, 200);
        }};

        private final ForkedSlider safetyWeaponWheelDurationSlider = new ForkedSlider() {{  /* 安全轮盘持续时间 */
            setLength(400);
            setRange(0, 1000);
        }};

        public SGSettingStage() {
            super();
            getContent().getChildren().addAll(contentBuilder()
                    /* 基础设置 */
                    .divide(sgI18n.baseSetting.title)
                    .button(sgI18n.baseSetting.activateMethod, activateMethodBtn)
                    .button(sgI18n.baseSetting.targetWeaponWheelKey, weaponWheelKeyBtn)
                    .button(sgI18n.baseSetting.activateKey, activateKeyBtn)
                    .slider(sgI18n.baseSetting.triggerInterval, triggerIntervalSlider)
                    /* 切换近战武器设置 */
                    .divide(sgI18n.swapMeleeSetting.title)
                    .toggle(sgI18n.swapMeleeSetting.enable, enableSwapMeleeToggle)
                    .button(sgI18n.swapMeleeSetting.meleeKey, meleeKeyBtn)
                    .slider(sgI18n.swapMeleeSetting.postSwapMeleeDelay, postSwapMeleeDelaySlider)
                    /* 切换远程武器设置 */
                    .divide(sgI18n.swapRangedSetting.title)
                    .toggle(sgI18n.swapRangedSetting.enable, enableSwapRangedToggle)
                    .button(sgI18n.swapRangedSetting.preferredRangedWeaponKey, preferredRangedKeyBtn)
                    .toggle(sgI18n.swapRangedSetting.enableBlockKey, enableSafetyWeaponWheelToggle)
                    .button(sgI18n.swapRangedSetting.blockKey, safetyWeaponWheelBtn)
                    .slider(sgI18n.swapRangedSetting.blockDuration, safetyWeaponWheelDurationSlider)
                    .build()
            );
        }

        @Override
        public String getTitle() {
            return sgI18n.title;
        }

        @Override
        public void init() {
            activateKeyBtn.keyProperty().set(sgConfig.baseSetting.activatekey);
            weaponWheelKeyBtn.keyProperty().set(sgConfig.baseSetting.targetWeaponWheelKey);
            meleeKeyBtn.keyProperty().set(sgConfig.swapMeleeSetting.meleeWeaponKey);
            preferredRangedKeyBtn.keyProperty().set(sgConfig.swapRangedSetting.rangedWeaponKey);

            postSwapMeleeDelaySlider.setValue(sgConfig.swapMeleeSetting.postSwapMeleeDelay);

            activateMethodBtn.indexProperty().set(sgConfig.baseSetting.activateMethod);
            triggerIntervalSlider.setValue(sgConfig.baseSetting.triggerInterval);

            enableSwapMeleeToggle.selectedProperty().set(sgConfig.swapMeleeSetting.enableSwapMelee);
            enableSwapRangedToggle.selectedProperty().set(sgConfig.swapRangedSetting.enableSwapRanged);

            enableSafetyWeaponWheelToggle.selectedProperty().set(sgConfig.swapRangedSetting.enableSafetyWeaponWheel);
            safetyWeaponWheelBtn.keyProperty().set(sgConfig.swapRangedSetting.safetyWeaponWheelKey);
            safetyWeaponWheelDurationSlider.setValue(sgConfig.swapRangedSetting.safetyWeaponWheelDuration);
        }

        @Override
        public void stop() {
            sgConfig.baseSetting.targetWeaponWheelKey = weaponWheelKeyBtn.keyProperty().get();
            sgConfig.swapMeleeSetting.meleeWeaponKey = meleeKeyBtn.keyProperty().get();
            sgConfig.swapRangedSetting.rangedWeaponKey = preferredRangedKeyBtn.keyProperty().get();

            sgConfig.baseSetting.activatekey = activateKeyBtn.keyProperty().get();
            sgConfig.baseSetting.activateMethod = activateMethodBtn.indexProperty().get();
            sgConfig.baseSetting.triggerInterval = triggerIntervalSlider.valueProperty().get();
            sgConfig.swapMeleeSetting.postSwapMeleeDelay = postSwapMeleeDelaySlider.valueProperty().get();
            sgConfig.swapMeleeSetting.enableSwapMelee = enableSwapMeleeToggle.selectedProperty().get();
            sgConfig.swapRangedSetting.enableSwapRanged = enableSwapRangedToggle.selectedProperty().get();

            sgConfig.swapRangedSetting.enableSafetyWeaponWheel = enableSafetyWeaponWheelToggle.selectedProperty().get();
            sgConfig.swapRangedSetting.safetyWeaponWheelKey = safetyWeaponWheelBtn.keyProperty().get();
            sgConfig.swapRangedSetting.safetyWeaponWheelDuration = safetyWeaponWheelDurationSlider.valueProperty().get();
        }
    }
}
