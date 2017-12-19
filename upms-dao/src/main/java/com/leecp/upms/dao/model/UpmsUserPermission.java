package com.leecp.upms.dao.model;

import java.io.Serializable;

public class UpmsUserPermission implements Serializable {
    private Integer userPermissionId;

    private Integer userId;

    private Integer permissionId;

    private Byte type;

    private static final long serialVersionUID = 1L;

    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UpmsUserPermission other = (UpmsUserPermission) that;
        return (this.getUserPermissionId() == null ? other.getUserPermissionId() == null : this.getUserPermissionId().equals(other.getUserPermissionId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPermissionId() == null ? other.getPermissionId() == null : this.getPermissionId().equals(other.getPermissionId()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserPermissionId() == null) ? 0 : getUserPermissionId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPermissionId() == null) ? 0 : getPermissionId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }

	@Override
	public String toString() {
		return "UpmsUserPermission [userPermissionId=" + userPermissionId
				+ ", userId=" + userId + ", permissionId=" + permissionId
				+ ", type=" + type + "]";
	}
    
}