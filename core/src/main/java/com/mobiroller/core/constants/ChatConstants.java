package com.mobiroller.core.constants;

import com.mobiroller.core.R;

/**
 * Created by ealtaca on 7/18/17.
 */

public class ChatConstants {

    public static String chatScreenId = null;


    public static final String CHAT_MINIMUM_REQUIRED_VERSION_PATH = "mv";
    public static final int CHAT_MINIMUM_REQUIRED_VERSION = 1;

    public static final String ARG_USERS = "usersDetailV1";
    
        public static final String ARG_USERS_META = "meta";

        public static final String ARG_USERS_BLOCKED_LIST = "blockedList";

        public static final String ARG_USERS_BLOCKED_BY_LIST = "blockedByList";

        public static final String ARG_USERS_FIREBASE_TOKEN = "ft";
    
        public static final String ARG_USERS_ROLES = "roles";
    
            public static final String ARG_USERS_ROLES_SUPER_USER = "su";
            public static final String ARG_USERS_ROLES_ROLE_ID = "ris";
            public static final String ARG_USERS_ROLES_CHAT_ROLE_ID = "cris";
            public static final String ARG_USERS_ROLES_CHAT_PERMISSION_ID = "cpis";

        public static final String ARG_USER_INFO = "info";

            public static final String ARG_USER_INFO_IS_ONLINE = "o";
            public static final String ARG_USER_INFO_IS_BANNED = "ib";
            public static final String ARG_USER_INFO_USERNAME = "un";
            public static final String ARG_USER_INFO_UID = "uid";
            public static final String ARG_USER_INFO_EMAIL = "e";
            public static final String ARG_USER_INFO_STATUS = "s";
            public static final String ARG_USER_INFO_NAME = "n";
            public static final String ARG_USER_INFO_IMAGE_URL = "i";


    public static final String ARG_USER_LIST = "userListV1";

        public static final String ARG_USER_LIST_IS_ONLINE = "o";
        public static final String ARG_USER_LIST_IS_BANNED = "ib";
        public static final String ARG_USER_LIST_USERNAME = "un";
        public static final String ARG_USER_LIST_STATUS = "s";
        public static final String ARG_USER_LIST_NAME = "n";
        public static final String ARG_USER_LIST_NAME_V1 = "fn";
        public static final String ARG_USER_LIST_IMAGE_URL = "i";
        public static final String ARG_USER_LIST_ROLE_ID = "ris";
        public static final String ARG_USER_LIST_CHAT_ROLE_ID = "cris";


    public static final String ARG_USER_SEARCH_INDEX = "searchIndexV1";

        public static final String ARG_USER_SEARCH_INDEX_USERNAME = "un";
        public static final String ARG_USER_SEARCH_INDEX_NAME = "n";


    public static final String ARG_BANNED_USERS = "bannedUsersV1";

    public static final String ARG_MESSAGING_USERS = "messagingUsersV1";
    public static final String ARG_MESSAGING_USER_LIST = "messagingUserListV1";
    public static final String ARG_AUTHORITIES_USERS = "authorizedUsersV1";
    public static final String ARG_LOGS = "logsV1";
    public static final String ARG_GROUP_MESSAGING = "groupMessaging";

    public static final String ARG_CHAT_ROOMS = "chatsV1";
    public static final String ARG_CHAT_REPORTS = "reportsV1";

    public static final String ARG_CHAT_MESSAGES = "messages";

    public static final String ARG_FIREBASE_TOKEN = "firebaseToken";

    public static final String ARG_RECEIVER = "Chat_Receiver";
    public static final String ARG_RECEIVER_UID = "Chat_Receiver_UID";
    public static final String ARG_SENDER_UID = "Chat_Sender_UID";
    public static final String ARG_SCREEN_ID = "Chat_Screen_Id";

    public static final String ARG_FIREBASE_USER_UID = "ARG_FIREBASE_USER_UID";

    public static final String ARG_NOTIFICATION_MODEL = "Chat_Notification_Model";
    public static final String ARG_ARCHIVED_LIST_MODEL = "Chat_Archived_List_Model";
    public static final String ARG_IS_ARCHIVED = "Chat_Is_Archived";
    public static final String CHAT_DIALOG_DATE_FORMAT = "dd MM yyyy";
    public static final String CHAT_LOG_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String CHAT_DATABASE_PATH= "databasePath";

    public static final String ARG_ROLES = "chatRoles";

    public static final String SEARCH_INTENT_EXTRA_FILTER_BY = "filterBy";
    public static final String SEARCH_INTENT_EXTRA_SEARCH_TEXT = "searchText";

    public static void setChatScreenId(String chatScreenId) {
        if (chatScreenId != null) {
            ChatConstants.chatScreenId = chatScreenId;
        }

    }

    public static final String ADMIN_PANEL = "ADMIN_PANEL";
    public static final String ADMIN_ROLE_PANEL = "ADMIN_ROLE_PANEL";
    public static final String CHAT_ADMIN_REPORT_INTENT = "CHAT_ADMIN_REPORT_INTENT";
    public static final String ROLES = "roles";

    private static final int NEW_REPORT_PUSH = R.string.new_report_push;
    private static final int BAN_PUSH = R.string.ban_push;
    private static final int UNBAN_PUSH = R.string.unban_push;
    private static final int AUTHORITY_PUSH = R.string.authority_push;
    private static final int AUTHORITY_REVOKE_PUSH= R.string.authority_revoke_push;

    public static int getPushMessage(int type)
    {
        switch (type)
        {
            case 1:
                return NEW_REPORT_PUSH;
            case 2:
                return BAN_PUSH;
            case 3:
                return UNBAN_PUSH;
            case 4:
                return AUTHORITY_PUSH;
            case 5:
                return AUTHORITY_REVOKE_PUSH;
            default:
                return 0;
        }
    }

}
