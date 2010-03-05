
package com.greenriver.commons.dwr;

import com.greenriver.commons.Strings;
import java.util.Set;
import org.directwebremoting.extend.AccessControl;
import org.directwebremoting.impl.DefaultAccessControl;


/**
 * Extends default access control to add support to specify more than one role
 * in the same dwr:auth tag to overcome a bug in dwr 2-3.RC1.
 * The multiple roles are specified separating them by commas.
 * @author Miguel Angel
 */
public class MultiRoleDwrAccessControl extends DefaultAccessControl implements AccessControl {

    @Override
    public void addRoleRestriction(String scriptName, String methodName, String role) {
	String[] roles = role.split(",");

	for (String allowedRole : roles) {
	    allowedRole = allowedRole.trim();
	    if (!Strings.isNullOrEmpty(allowedRole)) {
		super.addRoleRestriction(scriptName, methodName, allowedRole);
	    }
	}
    }

    @Override
    protected Set<String> getRoleRestrictions(String scriptName, String methodName) {
	Set<String> result = super.getRoleRestrictions(scriptName, methodName);
	return result;
    }
}