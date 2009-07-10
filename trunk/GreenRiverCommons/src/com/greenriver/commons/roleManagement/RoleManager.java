/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.roleManagement;

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
}
