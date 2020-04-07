package model.finance.record;

import lombok.*;
import model.IdNameObj;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinanceRecordTableRow implements Serializable {
    public String id;
    public IdNameObj account;
    public BigDecimal amount;
    public IdNameObj category;
    public String description;
    public Date date;
    public Date loadDate;
}
