package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.usecase.SynchronizeAccount
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/account")
class AccountController(val authenticationService: AuthenticationService, val synchronizeAccount: SynchronizeAccount) {

    @PostMapping
    fun post() = synchronizeAccount(authenticationService.userDetails()).run {
        ResponseEntity.status(201).body(AccountResponse.of(this))
    }
}
