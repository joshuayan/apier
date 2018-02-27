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
        this.userEntryRepository.findById(userPasswordUpdatedEvent.uid).ifPresent { it.password = userPasswordUpdatedEvent.password; this.userEntryRepository.save(it) }
    }

    @EventHandler
    fun onEnabled(userEnabled: UserEnabled) {
        this.userEntryRepository.findById(userEnabled.uid).ifPresent { it.enabled = true; this.userEntryRepository.save(it) }
    }

    @EventHandler
    fun onDisabled(userDisabled: UserDisabled) {
        this.userEntryRepository.findById(userDisabled.uid).ifPresent { it.enabled = false; this.userEntryRepository.save(it) }

    }

}