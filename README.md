# Coffee Boots

[![Build Status](https://travis-ci.com/stepio/coffee-boots.svg?branch=master)](https://travis-ci.com/stepio/coffee-boots)
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=stepio_coffee-boots&metric=alert_status)](https://sonarcloud.io/dashboard?id=stepio_coffee-boots)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e3997372bb2448ebae5282fddc44784f)](https://app.codacy.com/app/stepio/coffee-boots?utm_source=github.com&utm_medium=referral&utm_content=stepio/coffee-boots&utm_campaign=Badge_Grade_Dashboard)
[![DepShield Badge](https://depshield.sonatype.org/badges/stepio/coffee-boots/depshield.svg)](https://depshield.github.io)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.stepio.coffee-boots/coffee-boots.svg)](https://mvnrepository.com/artifact/io.github.stepio.coffee-boots/coffee-boots)
[![Javadocs](http://www.javadoc.io/badge/io.github.stepio.coffee-boots/coffee-boots.svg)](http://www.javadoc.io/doc/io.github.stepio.coffee-boots/coffee-boots)
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Coffee Boots project implements (property-based) configuring of multiple [Caffeine](https://github.com/ben-manes/caffeine) caches for [Spring Cache](https://github.com/spring-projects/spring-framework/tree/master/spring-context/src/main/java/org/springframework/cache) abstraction. 
It works best with [Spring Boot](https://github.com/spring-projects/spring-boot), implementing [auto-configuration](https://github.com/stepio/coffee-boots/blob/master/src/main/java/io/github/stepio/cache/caffeine/CaffeineSpecSpringAutoConfiguration.java) mechanism.
This means that in most cases you don't have to create any beans yourself, just add [dependency to the latest version](https://search.maven.org/search?q=g:io.github.stepio.coffee-boots%20AND%20a:coffee-boots&core=gav):
```xml
<dependency>
    <groupId>io.github.stepio.coffee-boots</groupId>
    <artifactId>coffee-boots</artifactId>
    <version>2.2.0</version>
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
2.  Now you know that you need an ability to configure some of the named caches with specific parameters.
    -   Using this project's API you may define your own `Caffeine` the following way:
```java
@Autowired
private CaffeineSupplier caffeineSupplier;

@PostConstruct
public void initialize() {
    Caffeine<Object, Object> myCacheBuilder = Caffeine.<Object, Object>newBuilder()
            .expireAfterWrite(1L, TimeUnit.MINUTES)
            .maximumSize(100000L);
    this.caffeineSupplier.putCaffeine("myCache", myCacheBuilder);
}
```
3.  But in most cases hard-coding the exact caching parameters is not a good idea, so you may get them from properties.
    -   Modifying the above given code to get the caching parameters from `Environment`:
```java
@Autowired
private CaffeineSupplier caffeineSupplier;
@Autowired
private Environment environment;

@PostConstruct
public void initialize() {
    Caffeine<Object, Object> myCacheBuilder = Caffeine.<Object, Object>newBuilder()
            .expireAfterWrite(environment.getProperty("myCache.expireAfterWrite", Long.class, 1L), TimeUnit.MINUTES)
            .maximumSize(environment.getProperty("myCache.maximumSize", Long.class, 100000L));
    this.caffeineSupplier.putCaffeine("myCache", myCacheBuilder);
}
```
4.  After adding 3-5 caches you understand that configuring them this way 1 by 1 is no fun. As an experienced `Spring Boot` user you don't want to hard-code it, you want the framework to do this little magic for you, cause you know that the case is so simple and straight-forward.
Ok, you're right, you may remove the above given customizations and just define the needed value for `coffee-boots.cache.spec.<your_cache_name>` property.
    -   The appropriate configuration in your `application.properties` for the above given example would be the following:
```properties
coffee-boots.cache.spec.myCache=maximumSize=100000,expireAfterWrite=1m
```
5.  Let's imagine that you don't need to use the project anymore. Ok, no problem:
    -   At first you may remove the relevant customizations - if no code changes were introduced, just remove all the properties matching `coffee-boots.*` prefix. At this point your goal is reached as `Coffee Boots` uses Spring's default functionality if no customizations are defined.
    -   If you're not planning to use this functionality in the nearest future, just drop the dependency to `io.github.stepio.coffee-boots:coffee-boots` artifact. Nobody needs unused dependencies.

## Bonus points for advanced users:

6.  If you use Caffeine only with specific environments (profiles) and don't want this project's beans to be created in other cases you can define property `spring.cache.type`. This project works only in 2 cases:
    -   if property `spring.cache.type` is set with value `caffeine`;
    -   if property `spring.cache.type` is not defined at all.

    More information is in [issue#44](https://github.com/stepio/coffee-boots/issues/44) or [commit#a134dc6](https://github.com/stepio/coffee-boots/commit/a134dc60843b46b6a69b00ce4449c510b301f534#diff-798aa55948ec42d85da39f34a917b73f).

7.  Use `coffee-boots.cache.default-spec` to define "basic" cache configuration with common key-value pairs to be reused by all other custom cache configurations. This allows simplifying custom configurations if necessary.

    More information is in [issue#43](https://github.com/stepio/coffee-boots/issues/43) or [commit#2a38d5b](https://github.com/stepio/coffee-boots/commit/2a38d5b3ca7e12c2cbc152e2b5f5bf0aa3233f34#diff-a6f66d25e49c3ad808097932be3df2d0).

8.  If you'd like to get automatic metrics registaration for your custom caches, don't forget to add next dependencies:

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-actuator-autoconfigure</artifactId>
</dependency>
```

This triggers the appropriate auto-configurations with relevant post-processing, more information is in [issue#38](https://github.com/stepio/coffee-boots/issues/38) or [commit#d4f137b](https://github.com/stepio/coffee-boots/commit/d4f137bad73a26a490ec9c2564e2ff512b2eebfe#diff-798aa55948ec42d85da39f34a917b73f).

9.  You may dislike the fact that project-specific `CacheManager` is created alongside with the built-in Spring Boot `CacheManager`. "Overloaded" bean is marked as `@Primary`. This minor overhead allows executing the whole Spring Boot mechanism of cache initialization, including creation of `CacheMetricsRegistrar` bean.
Individual configurations cannot be invoked as they're package-private. Project-specific deep custom configuration is avoided at all costs to simplify support of newer versions of Spring and Spring Boot.

    You may still use earlier version `2.0.0` without the above mentioned advanced features if you really hate this overhead.

    More information is in [issue#38](https://github.com/stepio/coffee-boots/issues/38) or [commit#d4f137b](https://github.com/stepio/coffee-boots/commit/d4f137bad73a26a490ec9c2564e2ff512b2eebfe#diff-798aa55948ec42d85da39f34a917b73f).

## P.S.

Project's name is almost meaningless - just wanted it to be close to `Caffeine` and `Spring Boot` without violating 3rd party trade marks.

Related issues:
-   [spring-framework/pull/1506](https://github.com/spring-projects/spring-framework/pull/1506)
-   [spring-framework/pull/1932](https://github.com/spring-projects/spring-framework/pull/1932/files)
-   [spring-boot/issues/9301](https://github.com/spring-projects/spring-boot/issues/9301)

Example project (my own sandbox): [stepio/coffee-boots-example](https://github.com/stepio/coffee-boots-example).
