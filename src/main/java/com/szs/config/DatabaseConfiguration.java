package com.szs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.szs.repository")
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration {
}
