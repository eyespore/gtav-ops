package club.pineclone.gtavops.macro.trigger.policy;

import club.pineclone.gtavops.macro.trigger.TriggerStatus;
import club.pineclone.gtavops.macro.trigger.source.InputSourceEvent;

import java.util.Optional;

public class TogglePolicy implements ActivationPolicy {
    private boolean toggled = false;

    @Override
    public Optional<TriggerStatus> decide(InputSourceEvent event) {
        return switch (event.getOperation()) {
            case KEY_PRESSED, MOUSE_PRESSED -> {
                toggled = !toggled;
                yield toggled ? fire(TriggerStatus.TOGGLE_ON) : fire(TriggerStatus.TOGGLE_OFF);
            }
            default -> Optional.empty();
        };
    }
}
