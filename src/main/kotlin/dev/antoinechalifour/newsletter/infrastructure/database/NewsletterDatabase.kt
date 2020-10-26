package dev.antoinechalifour.newsletter.infrastructure.database

import com.fasterxml.jackson.databind.ObjectMapper
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.json.JsonStringType
import dev.antoinechalifour.newsletter.domain.Newsletter
import dev.antoinechalifour.newsletter.domain.Recipient
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "newsletter")
@TypeDefs(
    TypeDef(name = "json", typeClass = JsonStringType::class),
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
)
open class NewsletterDatabase(
    @Id
    open var id: UUID? = null,

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    open var payload: String? = null
) {
    companion object {
        val mapper = ObjectMapper()

        fun of(newsletter: Newsletter) = NewsletterDatabase(
            UUID.randomUUID(),
            mapper.writeValueAsString(newsletter)
        )
    }

    fun toNewslettter(): Newsletter {
        return Newsletter(Recipient("", ""), emptyList())
    }

}
