package club.pineclone.gtavops.macro.trigger.policy;

import club.pineclone.gtavops.macro.trigger.TriggerStatus;
import club.pineclone.gtavops.macro.trigger.source.InputSourceEvent;

import java.util.Optional;

public class ClickPolicy implements ActivationPolicy {

    @Override
    public Optional<TriggerStatus> decide(InputSourceEvent event) {
        return switch (event.getOperation()) {
            case KEY_PRESSED, MOUSE_PRESSED, MOUSE_WHEEL_MOVED -> fire(TriggerStatus.CLICK);
            default -> Optional.empty();
        };
    }

}
