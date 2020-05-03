package model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public enum Permission {
    AdminPanel("Административная панель");

    private String permissionName;

    Permission(String s) {
        this.permissionName = s;
    }
}
