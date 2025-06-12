package com.example.demo.admin.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "cancel_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
