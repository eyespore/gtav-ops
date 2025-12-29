package club.pineclone.gtavops.macro.action.impl;

import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.ScheduledAction;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import io.vproxy.vfx.entity.input.Key;
import javafx.scene.input.MouseButton;

/* 连发 RPG */
public class AutoRPGAction extends ScheduledAction {

    private final VCRobotAdapter robot;
    private final Key heavyWeeaponKey;
    private final Key specialWeaponKey;

    private final Key primaryMouseButton = new Key(MouseButton.PRIMARY);

    protected static final String ACTION_ID = "auto-rpg";

    /**
     * 连发 RPG宏，实现自动连发RPG
     * @param heavyWeeaponKey 重型武器键位
     * @param triggerInterval 连发间隔
     * @param specialWeaponKey 特殊武器键位
     * @param mousePressInterval 鼠标按住间隔
     */
    public AutoRPGAction(Key heavyWeeaponKey,
                         Key specialWeaponKey,
                         long triggerInterval,
                         long mousePressInterval) {
        super(ACTION_ID, triggerInterval);
        this.heavyWeeaponKey = heavyWeeaponKey;
        this.specialWeaponKey = specialWeaponKey;
        robot = RobotFactory.getRobot(ACTION_ID);
    }

    @Override
    public void schedule(ActionEvent event) throws Exception {
        /* 连发 RPG 循环 */
        robot.simulate(this.heavyWeeaponKey);
        try {
            robot.mousePress(primaryMouseButton);
            Thread.sleep(500);
        } finally {
            robot.mouseRelease(primaryMouseButton);
        }

        robot.simulate(this.specialWeaponKey);
    }
}
