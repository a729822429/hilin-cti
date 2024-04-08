package icu.hilin.cti.freeswitch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "icu.hilin.freeswitch")
public class HilinFreeswitchConfig {

    private Boolean enabled = true;

    private String host;
    private int port;
    private String password;
    private int timeout;
    private long heartbeatExpire;

}
