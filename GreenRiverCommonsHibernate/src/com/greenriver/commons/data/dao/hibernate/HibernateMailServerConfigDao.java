package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.data.dao.MailServerConfigDao;
import com.greenriver.commons.data.dao.hibernate.base.HibernateUniqueResultDao;
import com.greenriver.commons.data.mailing.MailServerConfig;

/**
 *
 * @author luisro
 */
public class HibernateMailServerConfigDao
        extends HibernateUniqueResultDao<MailServerConfig>
        implements MailServerConfigDao {
}
