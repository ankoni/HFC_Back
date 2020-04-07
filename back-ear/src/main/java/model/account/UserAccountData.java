package model.account;

import lombok.*;

import javax.sql.DataSource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountData implements Serializable {
    public String id;
    public String name;
    public BigDecimal balance;
    /**
     * Дата изменения счета
     */
    public Date update;
    /**
     * Дата создания
     */
    public Date create;
    public boolean newRow;

    public UserAccountData(String id, String name, BigDecimal balance, Date update, Date create) {
        setId(id);
        setName(name);
        setBalance(balance);
        setUpdate(update);
        setCreate(create);
        setNewRow(false);
    }
}
