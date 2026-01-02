package club.pineclone.gtavops.macro.action.impl.bettermmenu;

import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import club.pineclone.gtavops.macro.trigger.TriggerStatus;
import io.vproxy.base.util.Logger;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.entity.input.KeyCode;
import io.vproxy.vfx.entity.input.MouseWheelScroll;

/* 快速点火 */
public class StartEngineAction extends BetterMMenuAction {

    private final boolean enableDoubleClickToOpenDoor;

    public StartEngineAction(Key menukey,
                             long mouseScrollInterval,
                             long keyPressInterval,
                             long timeUtilMMenuLoaded,
                             boolean enableDoubleClickToOpenDoor) {

        super(menukey, mouseScrollInterval, keyPressInterval, timeUtilMMenuLoaded);
        this.enableDoubleClickToOpenDoor = enableDoubleClickToOpenDoor;
    }

    @Override
    public void activate(ActionEvent event) {
//        Logger.lowLevelDebug(event.toString());
        try {
            boolean shouldOpenVehicleDoor = false;

            if (enableDoubleClickToOpenDoor) {
                /* 启用双击开门 */
                if (event.getTriggerEvent().getTriggerStatus().equals(TriggerStatus.DOUBLE_CLICK)) {
                    /* 双击事件，开启车门 */
                    shouldOpenVehicleDoor = true;
                }
            }

//        Logger.lowLevelDebug("should open door: " + shouldOpenVehicleDoor);
            pressMenuKey();
            Thread.sleep(timeUtilMMenuLoaded);  /* 解决 M 键菜单出现过晚的问题 */

//        mouseScrollDown();
//        mouseScrollDown();

            for (int i = 0; i < 9; i++) mouseScrollUp();

            pressEnter();
            mouseScrollUp();
            pressEnter();
            mouseScrollDown();
            mouseScrollDown();
            if (shouldOpenVehicleDoor) pressEnter();
            for (int i = 0; i < 4; i++) mouseScrollDown();
            pressEnter();
            pressEnter();
            pressMenuKey();
        } catch (InterruptedException ignored) {

        }
    }
}
