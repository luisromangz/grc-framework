package com.greenriver.commons.web.services.mailServerConfig;

import com.greenriver.commons.data.mailing.MailServerConfig;
import com.greenriver.commons.web.services.Result;

/**
 * This interfaces defines the service methods required to config a mail server.
 * 
 * @author luisro
 */
public interface MailServerConfigService {
    
    Result<MailServerConfig> getConfig();
    
    Result saveConfig(MailServerConfig newConfig);
    
}
