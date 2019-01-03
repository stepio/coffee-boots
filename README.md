# Coffee Boots

[![Build Status](https://travis-ci.org/stepio/spring-boot-multi-caffeine.svg?branch=master)](https://travis-ci.org/stepio/spring-boot-multi-caffeine)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=stepio_spring-boot-multi-caffeine&metric=alert_status)](https://sonarcloud.io/dashboard?id=stepio_spring-boot-multi-caffeine)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5fadc77e83b64f7a87587f7138320747)](https://app.codacy.com/app/stepio/spring-boot-multi-caffeine?utm_source=github.com&utm_medium=referral&utm_content=stepio/spring-boot-multi-caffeine&utm_campaign=Badge_Grade_Dashboard)
[![DepShield Badge](https://depshield.sonatype.org/badges/stepio/spring-boot-multi-caffeine/depshield.svg)](https://depshield.github.io)
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Supports property-based configuring of multiple [Caffeine](https://github.com/ben-manes/caffeine) caches for [Spring Cache](https://github.com/spring-projects/spring-framework/tree/master/spring-context/src/main/java/org/springframework/cache) abstraction.

Works best with [Spring Boot](https://github.com/spring-projects/spring-boot), implements [auto-configuration](https://github.com/stepio/coffee-boots/blob/master/src/main/java/io/github/stepio/cache/caffeine/CaffeineSpecSpringAutoConfiguration.java) mechanism.

Related issues:
-   [spring-framework/pull/1506](https://github.com/spring-projects/spring-framework/pull/1506)
-   [spring-boot/issues/9301](https://github.com/spring-projects/spring-boot/issues/9301)
