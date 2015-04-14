# Grails Urban Airship Plugin

This plugin provides a easy to customize and automatic Grails integration with the [Urban Airship API](http://docs.urbanairship.com/reference/libraries/java/0.1/)

## Usage

To install de plugin you should add the dependency in your BuildConfig.groovy

```groovy
plugins {
    ...
    runtime ":urban-air-ship:1.0" 
}
```

There is a problem with the current version of the Urban Airship API and is necessary to include a custom JAR to work with some of the API's methods.

When [this bug with the interactions](https://github.com/urbanairship/java-library/issues/29) is solved we will remove this external dependency.

Until this is solved you should include this external repository in your service:

```groovy
repositories {
    ....
    mavenRepo "http://dl.bintray.com/kaleidos/maven"
}
```

The plugin provide a new service "urbanAirshipService" that you can use inside your services.

```groovy
public void sendNotification(String alias, String message, Map customFields, Map additionalParams) {
    urbanAirshipService.sendPush(alias, message, customFields, additionalParams)
}
```

## Configuration
```
urbanAirship.appKey = _your app key_
urbanAirship.appSecret = _your app secret_
urbanAirship.appMasterSecret = _your app master secret_
```

## Changelog

### v1.0 (14/04/2015)
- Initial version
