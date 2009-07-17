
package com.greenriver.commons.hibernate;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * Extends dialect for 5.x versions of mysql to add a default charset setting
 * of utf8.
 * @author MiguelAngel
 */
public class MySQL5Utf8Dialect extends MySQL5Dialect {

    public MySQL5Utf8Dialect() {
	super();
    }

    @Override
    public String getTableTypeString() {
	return "ENGINE=MyISAM DEFAULT CHARSET=utf8";
    }
}
