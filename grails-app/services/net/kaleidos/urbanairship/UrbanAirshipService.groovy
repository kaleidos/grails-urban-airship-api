package net.kaleidos.urbanairship

import com.urbanairship.api.client.APIClient
import com.urbanairship.api.client.APIError
import com.urbanairship.api.client.APIErrorDetails
import com.urbanairship.api.client.APIRequestException
import com.urbanairship.api.client.model.APIClientResponse
import com.urbanairship.api.client.model.APIPushResponse
import com.urbanairship.api.push.model.DeviceType
import com.urbanairship.api.push.model.DeviceTypeData
import com.urbanairship.api.push.model.PushPayload
import com.urbanairship.api.push.model.audience.Selectors
import com.urbanairship.api.push.model.notification.Notification
import com.urbanairship.api.push.model.notification.actions.Actions
import com.urbanairship.api.push.model.notification.actions.DeepLinkAction
import com.urbanairship.api.push.model.notification.android.AndroidDevicePayload
import com.urbanairship.api.push.model.notification.interactive.Interactive
import com.urbanairship.api.push.model.notification.ios.IOSDevicePayload

import org.springframework.beans.factory.InitializingBean

class UrbanAirshipService implements InitializingBean {

    static transactional = false

    def grailsApplication
    def urbanAirshipConfig

    void afterPropertiesSet() {
        // urbanAirshipConfig = grailsApplication.config.urbanAirship
        urbanAirshipConfig = grailsApplication.config.urbanAirship

        log.debug """Make sure key and secret are set Key:${urbanAirshipConfig.appKey}
                    Secret:Is null? ${urbanAirshipConfig.secretKey==null}
                    Master Secret:Is null? ${urbanAirshipConfig.secretMasterKey==null}
                    """
    }

    void sendPush(String alias, String alert, Map customFields=null, Map additionalParams=null) {

        APIClient apiClient = getApiClient()

        def payloadBuilder = PushPayload.newBuilder()
                                        .setAudience(Selectors.alias(alias))

        def notificationBuilder = Notification.newBuilder()
        notificationBuilder.alert = alert

        if (customFields) {
            notificationBuilder.addDeviceTypeOverride(DeviceType.ANDROID,
                                AndroidDevicePayload.newBuilder()
                                    .addAllExtraEntries(customFields).build())

            notificationBuilder.addDeviceTypeOverride(DeviceType.IOS,
                                IOSDevicePayload.newBuilder()
                                    .addAllExtraEntries(customFields).build())
        }

        if (additionalParams?.openLink != null) {
            def actionsBuilder = Actions.newBuilder()
            actionsBuilder.setOpen(new DeepLinkAction(additionalParams.openLink))
            notificationBuilder.actions = actionsBuilder.build()
        }

        if (additionalParams?.interactive != null) {
            def interactiveBuilder = Interactive.newBuilder()
            interactiveBuilder.type = additionalParams.interactive.type

            if (additionalParams.interactive?.links) {
                Map buttonActions = [:]
                additionalParams.interactive?.links.each { entry ->
                    buttonActions << [(entry.key): Actions.newBuilder()
                                                          .setOpen(new DeepLinkAction(entry.value))
                                                          .build()]
                }
                interactiveBuilder.buttonActions = buttonActions
            }
            notificationBuilder.interactive = interactiveBuilder.build()
        }

        payloadBuilder.notification = notificationBuilder.build()
        payloadBuilder.deviceTypes = DeviceTypeData.newBuilder()
                                                    .addDeviceType(DeviceType.IOS)
                                                    .addDeviceType(DeviceType.ANDROID)
                                                    .build()
        PushPayload payload = payloadBuilder.build()

        log.debug "PAYLOAD: ${payload.toJSON()}"

        try {
            APIClientResponse<APIPushResponse> response = apiClient.push(payload)
            log.debug("PUSH SUCCEEDED\nRESPONSE: $response")
        }
        catch (APIRequestException ex){
            log.error("APIRequestException $ex\nEXCEPTION $ex")

            APIError apiError = ex.error.get()
            log.error("Error $apiError.error")
            if (apiError.details.present)     {
                APIErrorDetails apiErrorDetails = apiError.details.get()
                log.error("Error details $apiErrorDetails.error")
            }

            throw ex
        }
        catch (IOException e){
            log.error("IOException in API request $e.message")

            throw e
        }
    }

    private APIClient getApiClient() {
        APIClient.newBuilder()
            .setKey(urbanAirshipConfig.appKey)
            .setSecret(urbanAirshipConfig.appMasterSecret)
            .build()
    }
}
