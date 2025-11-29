package com.work.chatty.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    @Primary
    public DataSource dataSource() {
        String mysqlUrl = System.getenv("MYSQL_URL");
        String mysqlHost = System.getenv("MYSQLHOST");
        String mysqlUser = System.getenv("MYSQLUSER");
        String mysqlPassword = System.getenv("MYSQLPASSWORD");
        String mysqlDatabase = System.getenv("MYSQLDATABASE");
        String mysqlPort = System.getenv("MYSQLPORT");

        logger.info("=== Database Connection Configuration ===");
        logger.info("MYSQL_URL: {}", mysqlUrl != null && !mysqlUrl.isEmpty() ? "SET (hidden)" : "NOT SET");
        logger.info("MYSQLHOST: {}", mysqlHost != null && !mysqlHost.isEmpty() ? mysqlHost : "NOT SET");
        logger.info("MYSQLUSER: {}", mysqlUser != null && !mysqlUser.isEmpty() ? mysqlUser : "NOT SET");
        logger.info("MYSQLDATABASE: {}", mysqlDatabase != null && !mysqlDatabase.isEmpty() ? mysqlDatabase : "NOT SET");
        logger.info("MYSQLPORT: {}", mysqlPort != null && !mysqlPort.isEmpty() ? mysqlPort : "NOT SET");

        String jdbcUrl;
        String username;
        String password;

        if (mysqlUrl != null && !mysqlUrl.isEmpty()) {
            // Railway provides MYSQL_URL
            if (mysqlUrl.startsWith("jdbc:mysql://")) {
                // Already JDBC format
                jdbcUrl = mysqlUrl;
                username = mysqlUser != null && !mysqlUser.isEmpty() ? mysqlUser : "root";
                password = mysqlPassword != null && !mysqlPassword.isEmpty() ? mysqlPassword : "";
            } else if (mysqlUrl.startsWith("mysql://")) {
                // Parse mysql://user:pass@host:port/db format
                try {
                    URI uri = new URI(mysqlUrl);
                    String userInfo = uri.getUserInfo();
                    
                    if (userInfo != null && userInfo.contains(":")) {
                        String[] userPass = userInfo.split(":", 2);
                        username = userPass[0];
                        password = userPass.length > 1 ? userPass[1] : "";
                    } else {
                        username = mysqlUser != null && !mysqlUser.isEmpty() ? mysqlUser : "root";
                        password = mysqlPassword != null && !mysqlPassword.isEmpty() ? mysqlPassword : "";
                    }
                    
                    String host = uri.getHost();
                    int port = uri.getPort() > 0 ? uri.getPort() : 3306;
                    String path = uri.getPath();
                    String database = path != null && path.length() > 1 ? path.substring(1) : 
                                     (mysqlDatabase != null && !mysqlDatabase.isEmpty() ? mysqlDatabase : "railway");
                    
                    jdbcUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=true&requireSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", 
                                           host, port, database);
                } catch (Exception e) {
                    logger.error("Error parsing MYSQL_URL: {}", e.getMessage());
                    jdbcUrl = buildJdbcUrl(mysqlHost, mysqlPort, mysqlDatabase);
                    username = mysqlUser != null && !mysqlUser.isEmpty() ? mysqlUser : "root";
                    password = mysqlPassword != null && !mysqlPassword.isEmpty() ? mysqlPassword : "";
                }
            } else {
                jdbcUrl = buildJdbcUrl(mysqlHost, mysqlPort, mysqlDatabase);
                username = mysqlUser != null && !mysqlUser.isEmpty() ? mysqlUser : "root";
                password = mysqlPassword != null && !mysqlPassword.isEmpty() ? mysqlPassword : "";
            }
        } else {
            // Use individual variables
            jdbcUrl = buildJdbcUrl(mysqlHost, mysqlPort, mysqlDatabase);
            username = mysqlUser != null && !mysqlUser.isEmpty() ? mysqlUser : "root";
            password = mysqlPassword != null && !mysqlPassword.isEmpty() ? mysqlPassword : "";
        }

        logger.info("JDBC URL: {}", jdbcUrl.replaceAll("://[^@]*@", "://****@"));
        logger.info("Username: {}", username);
        logger.info("========================================");

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    private String buildJdbcUrl(String host, String port, String database) {
        String hostName = host != null && !host.isEmpty() ? host : "localhost";
        String portNum = port != null && !port.isEmpty() ? port : "3306";
        String dbName = database != null && !database.isEmpty() ? database : "chatty";
        return String.format("jdbc:mysql://%s:%s/%s?createDatabaseIfNotExist=true&useSSL=true&requireSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                           hostName, portNum, dbName);
    }
}
