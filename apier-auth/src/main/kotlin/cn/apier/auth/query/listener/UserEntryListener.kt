package cn.apier.auth.query.listener

import cn.apier.auth.domain.event.UserCreatedEvent
import cn.apier.auth.domain.event.UserDisabled
import cn.apier.auth.domain.event.UserEnabled
import cn.apier.auth.domain.event.UserPasswordUpdatedEvent
import cn.apier.auth.query.entry.UserEntry
import cn.apier.auth.query.repository.UserEntryRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserEntryListener {

    @Autowired
    private lateinit var userEntryRepository: UserEntryRepository

    @EventHandler
    fun onCreated(userCreatedEvent: UserCreatedEvent) {
        UserEntry(userCreatedEvent.uid, userCreatedEvent.mobile, userCreatedEvent.password, userCreatedEvent.enabled, userCreatedEvent.createdAt)
                .also { this.userEntryRepository.save(it) }
    }

    @EventHandler
    fun onPasswordUpdated(userPasswordUpdatedEvent: UserPasswordUpdatedEvent) {
        this.userEntryRepository.findOne(userPasswordUpdatedEvent.uid).also { it.password = userPasswordUpdatedEvent.password }
                .also { this.userEntryRepository.save(it) }
    }

    @EventHandler
    fun onEnabled(userEnabled: UserEnabled) {
        this.userEntryRepository.findOne(userEnabled.uid).also { it.enabled = true }.also { this.userEntryRepository.save(it) }
    }

    @EventHandler
    fun onDisabled(userDisabled: UserDisabled) {
        this.userEntryRepository.findOne(userDisabled.uid).also { it.enabled = false }.also { this.userEntryRepository.save(it) }

    }

}