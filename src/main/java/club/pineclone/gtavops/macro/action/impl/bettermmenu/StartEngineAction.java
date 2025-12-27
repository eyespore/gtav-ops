package club.pineclone.gtavops.macro.action.impl.bettermmenu;

import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.entity.input.KeyCode;

/* 快速点火 */
public class StartEngineAction extends Action {

    private final long arrowKeyInterval;
    private final long enterKeyInterval;
    private final long timeUtilMMenuLoaded;

    private final VCRobotAdapter robot;

    public static final String ACTION_ID = "better-m-menu";

    private final Key downKey = new Key(KeyCode.DOWN);
    private final Key upKey = new Key(KeyCode.UP);
    private final Key enterKey = new Key(KeyCode.ENTER);
    private final Key menuKey;

    public StartEngineAction(Key menukey, long arrowKeyInterval,
                             long enterKeyInterval, long timeUtilMMenuLoaded) {
        super(ACTION_ID);
        this.arrowKeyInterval = arrowKeyInterval;
        this.enterKeyInterval = enterKeyInterval;
        this.timeUtilMMenuLoaded = timeUtilMMenuLoaded;
        this.robot = RobotFactory.getRobot();
        this.menuKey = menukey;
    }

    @Override
    public void activate(ActionEvent event) throws Exception {
        pressM();

        Thread.sleep(timeUtilMMenuLoaded);  /* 解决M键菜单出现过晚的问题 */

        pressDown();
        pressDown();
        pressEnter();
        pressUp();
        pressEnter();
        pressUp();
        pressUp();
        pressUp();
        pressEnter();
        pressEnter();
        pressM();
    }

    private void pressM() throws Exception {
        robot.simulate(menuKey);
        awaitArrow();
    }

    private void pressDown() throws Exception {
        robot.simulate(downKey);
        awaitArrow();
    }

    private void pressEnter() throws Exception {
        robot.simulate(enterKey);
        awaitEnter();
    }

    private void pressUp() throws Exception {
        robot.simulate(upKey);
        awaitArrow();
    }

    private void awaitArrow() throws InterruptedException {
        Thread.sleep(arrowKeyInterval);
    }

    private void awaitEnter() throws InterruptedException {
        Thread.sleep(enterKeyInterval);
    }

    private void awaitTimeUtilMMenuLoaded() throws InterruptedException {
        Thread.sleep(timeUtilMMenuLoaded);
    }
}
