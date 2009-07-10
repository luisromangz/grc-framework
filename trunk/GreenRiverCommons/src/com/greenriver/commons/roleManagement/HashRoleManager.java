
package com.greenriver.commons.roleManagement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author luis
 */
public class HashRoleManager implements RoleManager {
    private Map<String,String> rolesInfo;

    public HashRoleManager () {
        rolesInfo = new ConcurrentHashMap<String, String>();
    }

    /**
     * @return the rolesInfo
     */
    public Map<String, String> getRolesInfo() {
        return rolesInfo;
    }

    /**
     * @param rolesInfo the rolesInfo to set
     */
    public void setRolesInfo(Map<String, String> rolesInfo) {
        this.rolesInfo = rolesInfo;
    }

    public void addRoleInfo(String roleName, String roleLabel) {
        rolesInfo.put(roleName, roleLabel);
    }

    public String getRoleLabel(String roleName) {
        return rolesInfo.get(roleName);
    }

    public String[] getRoleLabelsForRoles(String[] roleNames) {
        String[] roleLabels = new String[roleNames.length];

        for(int i = 0; i < roleNames.length; i++) {
            roleLabels[i] = getRoleLabel(roleNames[i]);
        }

        return roleLabels;
    }

    public String[] getRoleNames() {
        return rolesInfo.keySet().toArray(new String[]{});
    }

    public String[] getRoleLabels() {
        return rolesInfo.values().toArray(new String[]{});
    }
}
