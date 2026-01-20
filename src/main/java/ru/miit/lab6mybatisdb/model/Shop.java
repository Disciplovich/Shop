package ru.miit.lab6mybatisdb.model;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private LocalDate openingDate;
    private Boolean active;
    private Integer employeeCount;
    private Double area; // площадь в кв.м.
    private String category; // "продукты", "одежда", "электроника"
    private Double revenue; // выручка
}
