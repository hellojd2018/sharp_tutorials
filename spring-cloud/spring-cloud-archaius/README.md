# Relevant Articles

- [Introduction to Netflix Archaius with Spring Cloud](https://www.baeldung.com/netflix-archaius-spring-cloud-integration)
- [Netflix Archaius with Various Database Configurations](https://www.baeldung.com/netflix-archaius-database-configurations)

# Spring Cloud Archaius

#### Basic Config
This service has the basic, out-of-the-box Spring Cloud Netflix Archaius configuration.
```java
org.springframework.cloud.netflix.archaius.ArchaiusAutoConfiguration.configurableEnvironmentConfiguration

com.netflix.config.ConfigurationManager#createDefaultConfigInstance
```

```java

  public static DynamicPropertyFactory getInstance() {
        if (config == null) {
            synchronized (ConfigurationManager.class) {
                if (config == null) {
                    AbstractConfiguration configFromManager = ConfigurationManager.getConfigInstance();
                    if (configFromManager != null) {
                        initWithConfigurationSource(configFromManager);
                        initializedWithDefaultConfig = !ConfigurationManager.isConfigurationInstalled();
                        logger.info("DynamicPropertyFactory is initialized with configuration sources: " + configFromManager);
                    }
                }
            }
        }
        return instance;
    }

```
#### Extra Configs
This service customizes some properties supported by Archaius.

These properties are set up on the main method, since Archaius uses System properties, but they could be added as command line arguments when launching the app.

```java
com.netflix.config.PolledConfigurationSource
com.netflix.config.FixedDelayPollingScheduler.schedule
```

#### Additional Sources
In this service we create a new AbstractConfiguration bean, setting up a new Configuration Properties source.

These properties have precedence over all the other properties in the Archaius Composite Configuration.