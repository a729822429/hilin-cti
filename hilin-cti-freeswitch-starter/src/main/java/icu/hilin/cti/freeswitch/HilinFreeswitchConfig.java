package icu.hilin.cti.freeswitch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "icu.hilin.freeswitch")
public class HilinFreeswitchConfig {

    private boolean enabled = true;

    /**
     * fs配置信息
     */
    private Server server;

    /**
     * inbound通道配置
     */
    private Inbound inbound;

    /**
     * outbound通道配置
     */
    private Outbound outbound;

    @Data
    public static class Server {
        private String host;
        private int port;
        private String password;
        private int timeout;
        private long heartbeatExpire;
    }

    @Data
    public static class Inbound {

    }

    @Data
    public static class Outbound {
        /**
         * outbound通道监听的端口号
         */
        private int port = 8086;
    }

}
