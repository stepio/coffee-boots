# Coffee Boots

[![Build Status](https://travis-ci.com/stepio/coffee-boots.svg?branch=master)](https://travis-ci.com/stepio/coffee-boots)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=stepio_coffee-boots&metric=alert_status)](https://sonarcloud.io/dashboard?id=stepio_coffee-boots)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e3997372bb2448ebae5282fddc44784f)](https://app.codacy.com/app/stepio/coffee-boots?utm_source=github.com&utm_medium=referral&utm_content=stepio/coffee-boots&utm_campaign=Badge_Grade_Dashboard)
[![DepShield Badge](https://depshield.sonatype.org/badges/stepio/coffee-boots/depshield.svg)](https://depshield.github.io)
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Coffee Boots projects implements property-based configuring of multiple [Caffeine](https://github.com/ben-manes/caffeine) caches for [Spring Cache](https://github.com/spring-projects/spring-framework/tree/master/spring-context/src/main/java/org/springframework/cache) abstraction. 
It works best with [Spring Boot](https://github.com/spring-projects/spring-boot), implementing [auto-configuration](https://github.com/stepio/coffee-boots/blob/master/src/main/java/io/github/stepio/cache/caffeine/CaffeineSpecSpringAutoConfiguration.java) mechanism.
This means that in most cases you don't have to create any beans yourself, just add [dependency to the latest version](https://search.maven.org/search?q=g:io.github.stepio.coffee-boots%20AND%20a:coffee-boots&core=gav):
```xml
<dependency>
    <groupId>io.github.stepio.coffee-boots</groupId>
    <artifactId>coffee-boots</artifactId>
    <version>1.0.1</version>
</dependency>
```

Let's review a quick example to understand what the project does:
1.  Suppose you use Spring Cache functionality much and have a set of named caches.
    -   Your cached method may look like this:
```java
    @CachePut("myCache")
    public Object getMyCachecObject() {
        // some heavy stuff here
    }
```
2.  Now you know that you need an ability to configure some of the named caches with specific parameters. As an experienced `Spring Boot` user you don't want to hard-code it, you want the framework to do this little magic for you, cause you know that the case is so simple and straight-forward. Ok, you're right, just define the needed value for `coffee-boots.cache.spec.<your property name>` property.
    -   The appropriate configuration in your `application.properties` for the above given example would be the following:
```properties
coffee-boots.cache.spec.myCache=maximumSize=100000,expireAfterWrite=1m
```
3.  Let's imagine that you don't need to use the project anymore. Ok, no problem:
    -   At first you may remove the relevant customizations - if no code changes were introduced, just remove all the properties matching `coffee-boots.*` prefix. At this point your goal is reached as `Coffee Boots` uses Spring's default functionality if no customizations are defined.
    -   If you're not planning to use this functionality in the nearest future, just drop the dependency to `io.github.stepio.coffee-boots:coffee-boots` artifact. Nobody needs unused dependencies.

P.S.: Project's name is almost meaningless - just wanted it to be close to `Caffeine` and `Spring Boot` without violating 3rd party trade marks.

Related issues:
-   [spring-framework/pull/1506](https://github.com/spring-projects/spring-framework/pull/1506)
-   [spring-boot/issues/9301](https://github.com/spring-projects/spring-boot/issues/9301)
