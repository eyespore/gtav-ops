package club.pineclone.gtavops.macro.trigger.policy;

import club.pineclone.gtavops.macro.trigger.TriggerStatus;
import club.pineclone.gtavops.macro.trigger.source.InputSourceEvent;

import java.util.Optional;

public class HoldPolicy implements ActivationPolicy {
    @Override
    public Optional<TriggerStatus> decide(InputSourceEvent event) {
        return switch (event.getOperation()) {
            case MOUSE_PRESSED, KEY_PRESSED -> fire(TriggerStatus.HOLD_START);  /* 按键按下 */
            case MOUSE_RELEASED, KEY_RELEASED -> fire(TriggerStatus.HOLD_STOP);  /* 按键抬起 */
            default -> Optional.empty();  /* 默认忽略 */
        };
    }
}
