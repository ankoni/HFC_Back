package model.account;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class AccountBalanceRow implements Serializable {
    private String name;

    private Double balance;
}

