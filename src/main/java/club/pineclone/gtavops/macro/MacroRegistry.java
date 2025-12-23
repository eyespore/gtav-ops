package club.pineclone.gtavops.macro;

import club.pineclone.gtavops.common.AbstractRegistry;
import club.pineclone.gtavops.jni.WindowTitleListener;
import lombok.Getter;

import java.util.UUID;

public class MacroRegistry
        extends AbstractRegistry<Macro>
        implements WindowTitleListener {

    @Getter private static final MacroRegistry instance = new MacroRegistry();
    @Getter private static volatile boolean globalSuspended = false;

    private static final String GTAV_WINDOW_TITLE = "Grand Theft Auto V";  /* 增强 & 传承标题相同 */

    /* 启用某个宏，通常由GUI中的开关直接控制 */
    public boolean install(UUID uuid) {
        Macro macro = get(uuid);
        if (macro == null) return false;
        macro.install();
        return true;
    }

    /* 停止某个宏 */
    public boolean uninstall(UUID uuid) {
        Macro macro = unregister(uuid);
        if (macro == null) return false;
        macro.uninstall();
        return true;
    }

    /* 挂起所有的宏，GTA OPS通过监听当前用户焦点窗口判断用户是否在游戏内，当用户切出游戏时会将所有的宏挂起 */
    public void suspendAll() {
        globalSuspended = true;
        values().forEach(Macro::suspend);
    }

    /* 恢复所有的宏 */
    public void resumeAll() {
        globalSuspended = false;
        values().forEach(Macro::resume);
    }

    /*
    * 注：通常情况下不应该调用下面这两个方法，每一个宏都关联到了一个FeaturePane，并与FeaturePane的开关形成映射，
    * 直接调用下面的两个方法来启用/禁用所有宏会导致前后端映射出现偏移
    */

    /* 启用所有的宏 */
    public void installAll() {
        values().forEach(Macro::install);
    }

    /* 停止所有的宏 */
    public void uninstallAll() {
        values().forEach(Macro::uninstall);
    }

    @Override
    public void accept(String s) {
        if (s.equals(GTAV_WINDOW_TITLE)) {
            resumeAll();  /* 用户切回游戏 */
        } else {
            suspendAll();  /* 用户切出游戏 */
        }
    }
}
