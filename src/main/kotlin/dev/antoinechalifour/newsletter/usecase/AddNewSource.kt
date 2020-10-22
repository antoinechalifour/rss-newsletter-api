package dev.antoinechalifour.newsletter.usecase

import dev.antoinechalifour.newsletter.domain.Source
import dev.antoinechalifour.newsletter.domain.SourcePort
import org.springframework.stereotype.Component

@Component
class AddNewSource(val sourcePort: SourcePort) {
    operator fun invoke(url: String): Source {
        val source = Source.of(url)

        sourcePort.save(source)

        return source
    }
}
