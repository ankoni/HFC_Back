package model.finance.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.IdNameObj;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateFinanceRecordDto implements Serializable {
    public Double amount;
    public IdNameObj category;
    public IdNameObj account;
    public String description;
    public Date recordDate;
    public boolean changeAccountBalance;
}
