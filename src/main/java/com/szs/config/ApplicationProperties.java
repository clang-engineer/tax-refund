package com.szs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final ApplicationProperties.Security security = new ApplicationProperties.Security();

    public ApplicationProperties() {
    }

    public ApplicationProperties.Security getSecurity() {
        return this.security;
    }

    public static class Security {
        private final ApplicationProperties.Security.Authentication authentication = new ApplicationProperties.Security.Authentication();

        public Security() {
        }

        public ApplicationProperties.Security.Authentication getAuthentication() {
            return this.authentication;
        }


        public static class Authentication {
            private final ApplicationProperties.Security.Authentication.Jwt jwt = new ApplicationProperties.Security.Authentication.Jwt();

            public Authentication() {
            }

            public ApplicationProperties.Security.Authentication.Jwt getJwt() {
                return this.jwt;
            }

            public static class Jwt {
                private String secret;
                private String base64Secret;
                private long tokenValidityInSeconds;

                public Jwt() {
                    this.secret = null;
                    this.base64Secret = null;
                    this.tokenValidityInSeconds = 1800L;
                }

                public String getSecret() {
                    return this.secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getBase64Secret() {
                    return this.base64Secret;
                }

                public void setBase64Secret(String base64Secret) {
                    this.base64Secret = base64Secret;
                }

                public long getTokenValidityInSeconds() {
                    return this.tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }
            }
        }
    }
}
