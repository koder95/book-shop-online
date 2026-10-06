package pl.koder95.bso.config;

import java.util.Map;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class JpaTestConfig {
    @Bean
    @ServiceConnection
    MySQLContainer mysqlContainer() {
        return new MySQLContainer("mysql:8.4")
                .withTmpFs(Map.of("/var/lib/mysql", "rw"))
                .withStartupTimeoutSeconds(300)
                .withConnectTimeoutSeconds(300);
    }
}
