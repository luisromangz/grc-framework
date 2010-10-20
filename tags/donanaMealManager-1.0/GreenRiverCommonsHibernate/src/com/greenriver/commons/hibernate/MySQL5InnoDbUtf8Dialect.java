
package com.greenriver.commons.hibernate;

import org.hibernate.dialect.MySQLInnoDBDialect;

/**
 * Extends dialect for 5.x versions of MySQL to add a default charset setting
 * of utf8, and ensure we use a transactional table engine.
 *
 * @author MiguelAngel
 */
public class MySQL5InnoDbUtf8Dialect extends MySQLInnoDBDialect {

    public MySQL5InnoDbUtf8Dialect() {
	super();
    }

    @Override
    public String getTableTypeString() {
	return "ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
