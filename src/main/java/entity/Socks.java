package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Socks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String color; //цвет носков, строка (например, black, red, yellow)
    private Byte cottonPart; //процентное содержание хлопка в составе носков, целое число от 0 до 100 (например, 30, 18, 42)
    private Integer quantity; //количество пар носков, целое число больше 0.
}