package net.kaleidos.urbanairship

import spock.lang.Specification

import com.urbanairship.api.client.APIClient

@TestFor(UrbanAirshipService)
class UrbanAirshipServiceSpec extends Specification {

    def urbanAirshipService

    void setup() {
        service.grailsApplication.config = [
            urbanAirship : [
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
            service.metaClass.getApiClient  = { -> mockApiClient }

        when:
            service.sendPush(alias, message)

        then:
            true
    }
}
