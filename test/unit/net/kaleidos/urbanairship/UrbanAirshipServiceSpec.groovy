package net.kaleidos.urbanairship

import com.urbanairship.api.client.APIClient
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UrbanAirshipService)
class UrbanAirshipServiceSpec extends Specification {

    def urbanAirshipService

    void setup() {
        service.grailsApplication.config = [
            "urbanAirship" : [
                appKey : "appKey",
                appSecret : "appSecret",
                appMasterSecret : "appMasterSecret"
            ]
        ]
    }

    void 'message push'() {
        given: "data: alias + message"
            def alias = "alias1"
            def message = "test message with a lot of characters"
            service.afterPropertiesSet()

            def mockApiClient = Mock(APIClient)
            service.metaClass.getApiClient  = { ->
                return mockApiClient
            }

        when:
            service.sendPush(alias, message)

        then:
            true
    }
}
