package com.mobiroller.core.models.response;

import java.io.Serializable;
import java.util.List;

public class UserLoginResponse implements Serializable{
    public String id;
    public boolean isBanned;
    public List<UserProfileElement> profileValues;
    public String sessionToken;
    public String roleId;
    public String email;
    public String communityRoleId;
    public String communityPermissionTypeId;
    public boolean changePassword;
}
