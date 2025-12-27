package club.pineclone.gtavops.common;

import club.pineclone.gtavops.i18n.ExtendedI18n;

public enum SessionType implements ResourceHolder {

    PUBLIC_SESSION,  /* 公开战局 */
    INVITE_ONLY_SESSION,  /* 邀请战局 */
    CREW_SESSION,  /* 帮会战局 */
    INVITE_ONLY_CREW_SESSION,  /* 非公开帮会战局 */
    INVITE_ONLY_FRIENDS_SESSION;  /* 非公开好友战局 */

    @Override
    public String toString() {
        final ExtendedI18n.InGame inGame = getI18n().inGame;
        return switch (this) {
            case  PUBLIC_SESSION -> inGame.publicSession;
            case INVITE_ONLY_SESSION -> inGame.inviteOnlySession;
            case CREW_SESSION -> inGame.crewSession;
            case INVITE_ONLY_CREW_SESSION -> inGame.inviteOnlyCrewSession;
            case INVITE_ONLY_FRIENDS_SESSION -> inGame.inviteOnlyFriendsSession;
        };
    }
}
