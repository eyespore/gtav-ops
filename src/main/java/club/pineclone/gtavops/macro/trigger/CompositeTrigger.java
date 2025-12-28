package club.pineclone.gtavops.macro.trigger;

import java.util.*;

/* 组合按键触发器 */
public class CompositeTrigger extends Trigger implements TriggerListener {

    private final List<Trigger> triggers;
    private final Set<Trigger> activeSet = new HashSet<>();
//    private final Map<Trigger, Long> lastActiveTime = new HashMap<>();

    private boolean isActive = false;
    private static final long TOLERANCE_MS = 100;

    public CompositeTrigger(final List<Trigger> triggers) {
        this.triggers = triggers;
        triggers.forEach(t -> {
            t.addListener(this);
//            lastActiveTime.put(t, 0L);
        });
    }

    @Override
    public void onMarcoInstall() {
        triggers.forEach(Trigger::onMarcoInstall);
    }

    @Override
    public void onMarcoUninstall() {
        triggers.forEach(Trigger::onMarcoUninstall);
    }

    @Override
    public void onTriggerEvent(TriggerEvent event) {
        Trigger source = event.getSource();
        TriggerStatus status = event.getTriggerStatus();
        if (status.isAssert()) {
            /* 触发事件 */
            synchronized (activeSet) {
                activeSet.add(source);
//            lastActiveTime.put(source, System.currentTimeMillis());
                if (activeSet.size() == triggers.size() && !isActive) {
                    isActive = true;
                    fire(TriggerEvent.of(this, TriggerStatus.COMPOSITE_ON, event.getInputSourceEvent()));
                }
            }
        } else if (status.isRevoke()) {
            /* 撤销事件 */
            synchronized (activeSet) {
//            long now = System.currentTimeMillis();
//            long lastActive = lastActiveTime.getOrDefault(source, 0L);
//            if (now - lastActive < TOLERANCE_MS) return;
                activeSet.remove(source);
                if (isActive && activeSet.size() < triggers.size()) {
                    isActive = false;
                    fire(TriggerEvent.of(this, TriggerStatus.COMPOSITE_OFF, event.getInputSourceEvent()));
                }
            }
        }
    }
}
