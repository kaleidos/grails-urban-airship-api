grails.project.work.dir = "target/work"

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
        mavenRepo "http://dl.bintray.com/kaleidos/maven"
    }
    dependencies {
        compile 'net.kaleidos:urbanairship-client:0.3.2'
    }

    plugins {
        build(":release:3.0.1", ":rest-client-builder:1.0.3") {
            export = false
        }
    }
}
