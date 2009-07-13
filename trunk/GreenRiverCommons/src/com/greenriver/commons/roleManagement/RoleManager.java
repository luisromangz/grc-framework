/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.roleManagement;

import java.util.Map;

/**
 *
 * @author luis
 */
public interface RoleManager {

    void addRoleInfo(String roleName, String roleLabel);

    String getRoleLabel(String roleName);

    String [] getRoleLabels();

    String[] getRoleLabelsForRoles(String[] roleNames);

    String[] getRoleNames();

    /**
     * Gets a map with all the role names as keys and role labels as values
     * @return a map
     */
    Map<String, String> getRoleMap();
}
