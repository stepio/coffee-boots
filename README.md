# Coffee Boots

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e3997372bb2448ebae5282fddc44784f)](https://app.codacy.com/app/stepio/coffee-boots?utm_source=github.com&utm_medium=referral&utm_content=stepio/coffee-boots&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/stepio/coffee-boots.svg?branch=master)](https://travis-ci.org/stepio/coffee-boots)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=stepio_coffee-boots&metric=alert_status)](https://sonarcloud.io/dashboard?id=stepio_coffee-boots)
[![DepShield Badge](https://depshield.sonatype.org/badges/stepio/spring-boot-multi-caffeine/depshield.svg)](https://depshield.github.io)
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Supports property-based configuring of multiple [Caffeine](https://github.com/ben-manes/caffeine) caches for [Spring Cache](https://github.com/spring-projects/spring-framework/tree/master/spring-context/src/main/java/org/springframework/cache) abstraction.

Works best with [Spring Boot](https://github.com/spring-projects/spring-boot), implements [auto-configuration](https://github.com/stepio/coffee-boots/blob/master/src/main/java/io/github/stepio/cache/caffeine/CaffeineSpecSpringAutoConfiguration.java) mechanism.

Related issues:
-   [spring-framework/pull/1506](https://github.com/spring-projects/spring-framework/pull/1506)
-   [spring-boot/issues/9301](https://github.com/spring-projects/spring-boot/issues/9301)
