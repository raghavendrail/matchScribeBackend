package com.matchscribe.matchscribe_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "common")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Common {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sl")
    private Long sl;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "type1", length = 200)
    private String type1;

    @Column(name = "type2", length = 200)
    private String type2;

    @Column(name = "code", length = 200)
    private String code;
}
