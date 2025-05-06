package club.pineclone.gtavops.macro.action.robot;
import io.vproxy.vfx.entity.input.Key;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RobotFactory {

    public static final String COMMON_ROBOT = "common-robot";

    /* 推荐使用这个方法获得robot，以提高执行的上线 */
    private static final Map<String, VCRobotAdapter> compositeRobotAdapters = new HashMap<>();

    public static VCRobotAdapter getRobot() {
        return getRobot(COMMON_ROBOT);
    }

    public static VCRobotAdapter getRobot(String key) {
        return compositeRobotAdapters.computeIfAbsent(key, k -> {
            try {
                return new CompositeRobotAdapter(new Robot());
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
        });
    }



    private static class CompositeRobotAdapter extends VCRobotAdapter {
        public CompositeRobotAdapter(Robot robot) {
            super(robot);
        }

        @Override
        public void simulate(Key key) throws Exception {
            this.simulate(key, 20);
        }

        @Override
        public void simulate(Key key, long delay) throws Exception {
            if (key.key != null) {
                /* 执行按键 */
                try {
                    keyPress(key);
                    Thread.sleep(delay);
                } finally {
                    keyRelease(key);
                }
            } else if (key.button != null) {
                /* 执行鼠标 */
                try {
                    mousePress(key);
                    Thread.sleep(delay);
                } finally {
                    mouseRelease(key);
                }
            } else if (key.scroll != null) {
                /* 执行滚轮 */
                mouseWheel(key);
            }
        }
    }
}
