package club.pineclone.gtavops.macro.trigger;

import club.pineclone.gtavops.macro.TriggerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Trigger是"触发器"的抽象，它主要用于描述“如何触发”
 *
 * @see SimpleTrigger 简单触发器，这是触发器的最小单元，它通过桥接模式，组合触发源{@link InputSource}以及触发模式
 * {@link ActivationPolicy}描述了如何触发
 *
 * @see KeyChordTrigger 复合触发器，通过汇集{@link SimpleTrigger}来达到“与”的效果，当所有合集中的简单触发器
 * 被触发，复合触发器才会触发，通过组合触发器可以实现类似[复合快捷键]的效果
 *
 * 也许未来会拓展更多触发器? 这一套设计很不错
 */
public abstract class Trigger {

    private final List<TriggerListener> listeners = new ArrayList<>();

    /* 触发器执行 */
    protected void fireActivate() {
        TriggerEvent event = new TriggerEvent(this);
        listeners.forEach(l -> l.onTriggerActivate(event));
    }

    /* 触发器停止 */
    protected void fireDeactivate() {
        TriggerEvent event = new TriggerEvent(this);
        listeners.forEach(l -> l.onTriggerDeactivate(event));
    }

    /* 添加监听器 */
    public void addListener(TriggerListener listener) {
        listeners.add(listener);
    }

    /* 移除监听器 */
    public void removeListener(TriggerListener listener) {
        listeners.remove(listener);
    }

    public abstract void install();

    public abstract void uninstall();

}
