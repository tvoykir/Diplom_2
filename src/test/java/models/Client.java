package models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Client {
    private String email;
    private String password;
    private String name;
}