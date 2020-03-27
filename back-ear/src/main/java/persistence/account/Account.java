package persistence.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import persistence.user.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT")
public class Account {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private BigDecimal balance;

    @Temporal(TemporalType.DATE)
    @Column(name = "CHANGE_DATE")
    private Date changeDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "ADD_DATE")
    private Date addDate;
}
