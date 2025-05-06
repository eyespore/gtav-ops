package club.pineclone.gtavops.macro.action.decorator;

import club.pineclone.gtavops.macro.action.ActionEvent;
import club.pineclone.gtavops.macro.action.robot.RobotFactory;
import club.pineclone.gtavops.macro.action.ScheduledAction;
import club.pineclone.gtavops.macro.action.ScheduledActionDecorator;
import club.pineclone.gtavops.macro.action.robot.VCRobotAdapter;
import io.vproxy.vfx.entity.input.Key;
import javafx.scene.input.MouseButton;

public class SwapRangedDecorator extends ScheduledActionDecorator {

    private final Key weaponKey;  /* 切换远程武器 */
    private final VCRobotAdapter robot;  /* 机器人 */

    private long blockStart;  /* 起始时间 */
    private final long blockDuration;

    public SwapRangedDecorator(ScheduledAction delegate, Key weaponKey) {
        this(delegate, weaponKey, 0);
    }

    public SwapRangedDecorator(ScheduledAction delegate, Key weaponKey, long blockDuration) {
        super(delegate);
        this.weaponKey = weaponKey;
        this.robot = RobotFactory.getRobot(delegate.getActionId());
        this.blockDuration = blockDuration;
    }

    /* 在宏(例如切枪偷速、近战偷速)执行结束之后，尝试切换远程武器 */
    @Override
    public void afterDeactivate(ActionEvent event) throws Exception {
        /* 尝试切换远程 */
        if (System.currentTimeMillis() - blockStart < blockDuration)
            return;

        if (!event.isBlocked()) {
            Thread.sleep(20);
            robot.simulate(weaponKey);  /* 切换到枪 */
            Thread.sleep(20);
            robot.simulate(new Key(MouseButton.PRIMARY));  /* 点左键选择 */

        } else {
            /* 被阻断，记录下阻塞开始时间 */
            blockStart = System.currentTimeMillis();
        }
        delegate.afterDeactivate(event);
    }
}
