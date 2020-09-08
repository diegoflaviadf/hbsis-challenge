package br.com.hbsis.challenge.weather.weather.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Weather")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", updatable = false, unique = true, nullable = false)
    private UUID id;

    @Column(name = "DT_CREATED_AT", nullable = false)
    @CreationTimestamp
    private LocalDateTime dtCreatedAt;

    @Type(type = "jsonb")
    @Column(name = "JS_ATTRIBUTES", columnDefinition = "jsonb", updatable = false, nullable = false)
    private Attributes attributes;

    public Weather(Attributes attributes) {
        this.attributes = attributes;
    }

    public static class Attributes extends LinkedHashMap<String, Object> {

    }
}
