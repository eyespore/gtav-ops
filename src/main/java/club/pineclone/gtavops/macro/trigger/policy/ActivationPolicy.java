package club.pineclone.gtavops.macro.trigger.policy;

/* 触发模式，例如按住、切换 */

import club.pineclone.gtavops.macro.trigger.TriggerStatus;
import club.pineclone.gtavops.macro.trigger.source.InputSource;
import club.pineclone.gtavops.macro.trigger.source.InputSourceEvent;

import java.util.Optional;

/**
 * @see HoldPolicy
 * @see TogglePolicy
 */
public interface ActivationPolicy {

    /**
     * 对{@link InputSource}传递的事件进行判定，给出是否可以执行下一步的判断
     * @return 1代表可以执行下一步、0代表拒绝执行、-1表示忽略
     */
    Optional<TriggerStatus> decide(InputSourceEvent event);

    static ActivationPolicy toggle() {
        return new TogglePolicy();
    }

    static ActivationPolicy hold() {
        return new HoldPolicy();
    }

    static ActivationPolicy click() {
        return new ClickPolicy();
    }

    default Optional<TriggerStatus> fire(TriggerStatus status) {
        return Optional.of(status);
    }

}
