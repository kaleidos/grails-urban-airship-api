class UrbanAirshipApiGrailsPlugin {
    def version = "1.0"
    def grailsVersion = "2.0 > *"

    def pluginExcludes = [
        "grails-app/controllers/**",
        "grails-app/domain/**",
        "grails-app/i18n/**",
        "grails-app/taglib/**",
        "grails-app/utils/**",
        "grails-app/views/**",
        "web-app/**"
    ]

    def title = "Grails Urban Airship Api Plugin"
    def author = "Antonio de la Torre"
    def authorEmail = "antonio.delatorre@kaleidos.net"
    def description = "Easy integration with the API for the notifications system Urban Airship"

    def documentation = "http://grails.org/plugin/urban-airship-api"

    def license = "APACHE"
    def organization = [ name: "Kaleidos", url: "http://kaleidos.net" ]
    def developers = [ [ name: "Alonso Torres", email: "alonso.torres@kaleidos.net" ]]
    def issueManagement = [ system: "GITHUB", url: "https://github.com/kaleidos/grails-urban-airship/issues" ]

    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

}
