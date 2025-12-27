package club.pineclone.gtavops.macro.action.impl.extrafunction;

import club.pineclone.gtavops.common.SessionType;
import club.pineclone.gtavops.macro.action.Action;
import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import io.vproxy.vfx.entity.input.Key;
import io.vproxy.vfx.entity.input.KeyCode;
import io.vproxy.vfx.entity.input.MouseWheelScroll;

/* 快速点火 */
public class JoinSessionAction extends Action {

    private final long mouseScrollInterval;
    private final long enterKeyInterval;
    private final long timeUtilPMenuLoaded;

    private final SessionType sessionType;

    private final VCRobotAdapter robot;

    public static final String ACTION_ID = "better-p-menu";

    private final Key downKey = new Key(KeyCode.DOWN);
    private final Key upKey = new Key(KeyCode.UP);
    private final Key enterKey = new Key(KeyCode.ENTER);
    private final Key rightKey = new Key(KeyCode.RIGHT);

    private final Key mouseScrollUpKey = new Key(new MouseWheelScroll(MouseWheelScroll.Direction.UP, 1));
    private final Key mouseScrollDownKey = new Key(new MouseWheelScroll(MouseWheelScroll.Direction.DOWN, 1));

    private final Key menuKey = new Key(KeyCode.P);  /* P 菜单按键 */

    public JoinSessionAction(
            SessionType sessionType,
            long mouseScrollInterval,
            long enterKeyInterval,
            long timeUtilPMenuLoaded
    ) {
        super(ACTION_ID);

        this.sessionType = sessionType;  /* 加入战局类型 */
        this.mouseScrollInterval = mouseScrollInterval;
        this.enterKeyInterval = enterKeyInterval;
        this.timeUtilPMenuLoaded = timeUtilPMenuLoaded;
        this.robot = RobotFactory.getRobot();
    }

    @Override
    public void activate(ActionEvent event) throws Exception {
        pressP();
        Thread.sleep(200);
        pressRight();
        awaitTimeUtilPMenuLoaded();  /* 等待列表加载 */

        pressEnter();
        for (int i = 0; i < 5; i++) mouseScrollUp();

        Thread.sleep(200);
        pressEnter();


        int times;
        switch (sessionType) {
            case INVITE_ONLY_SESSION -> times = 1;
            case CREW_SESSION -> times = 2;
            case INVITE_ONLY_CREW_SESSION -> times = -2;
            case INVITE_ONLY_FRIENDS_SESSION -> times = -1;
            default -> times = 0;
        }

        if (times >= 0) {
            for (int i = 0; i < times; i++) mouseScrollDown();
        } else {
            times = - times;
            for (int i = 0; i < times; i++) mouseScrollUp();
        }
//
        Thread.sleep(200);
        pressEnter();
        Thread.sleep(200);
        pressEnter();
    }

    private void mouseScrollDown() throws Exception {
        robot.mouseWheel(mouseScrollDownKey);
        Thread.sleep(mouseScrollInterval);
    }

    private void mouseScrollUp() throws Exception {
        robot.mouseWheel(mouseScrollUpKey);
        Thread.sleep(mouseScrollInterval);
    }

    private void pressP() throws Exception {
        robot.simulate(menuKey);
        awaitArrow();
    }

    private void pressDown() throws Exception {
        robot.simulate(downKey);
        awaitArrow();
    }

    private void pressRight() throws Exception {
        robot.simulate(rightKey);
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
        Thread.sleep(mouseScrollInterval);
    }

    private void awaitEnter() throws InterruptedException {
        Thread.sleep(enterKeyInterval);
    }

    private void awaitTimeUtilPMenuLoaded() throws InterruptedException {
        Thread.sleep(timeUtilPMenuLoaded);
    }
}
