package com.ohforbidden.bugreport.infrastructure.message.slack

import com.ohforbidden.bugreport.global.util.toMap
import com.ohforbidden.bugreport.infrastructure.message.slack.dto.SignUpParam
import com.slack.api.Slack
import com.slack.api.model.kotlin_extension.block.dsl.LayoutBlockDsl
import com.slack.api.model.kotlin_extension.block.withBlocks
import com.slack.api.webhook.Payload
import com.slack.api.webhook.WebhookResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SlackTransmitter(
    private val slack: Slack,
    @Value("\${spring.profiles.active}") val activeProfile: String,
    @Value("\${slack.webhook.url.sign-up}") val signUpMsgUrl: String
) {
    private val stringBuilder = StringBuilder()
    private val logger = KotlinLogging.logger{}

    fun sendSignUpApplication(dto: SignUpParam) = sendMessage(signUpMsgUrl, "회원가입 신청", toMap(dto))

    private fun sendMessage(webhookUrl: String, title: String, fields: Map<String, Any?>): WebhookResponse? {
        return try {
            send(webhookUrl) {
                header {
                    text("$title - $activeProfile")
                }
                divider()
                section {
                    fields.forEach { (key, value) ->
                        stringBuilder.appendLine("*$key* : $value")
                        markdownText(stringBuilder.toString())
                    }
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Slack 메시지 전송 요청 실패" }
            null
        }
    }

    private fun send(webhookUrl: String, builder: LayoutBlockDsl.() -> Unit): WebhookResponse {
        return slack.send(webhookUrl, Payload.builder().blocks(withBlocks(builder)).build())
    }
}