package dev.antoinechalifour.newsletter.application

import dev.antoinechalifour.newsletter.usecase.CreateAccount
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/account")
class AccountController(val authenticationService: AuthenticationService, val createAccount: CreateAccount) {

    @PostMapping
    fun post() = createAccount(authenticationService.userDetails()).run {
        ResponseEntity.status(201).body(AccountResponse.of(this))
    }
}
