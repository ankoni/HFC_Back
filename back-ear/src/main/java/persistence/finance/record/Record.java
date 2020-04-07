package persistence.finance.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;
import persistence.EntityId;
import persistence.account.Account;
import persistence.category.Category;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RECORD_INFO")
public class Record extends EntityId {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    public Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    public Account account;

    @Column(name = "amount")
    public BigDecimal amount;

    @Column(name = "description")
    public String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "record_date")
    public Date recordDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date")
    public Date createDate;

    @Column(name = "ACCOUNT_INCLUDE")
    public boolean accountInclude;

    public Record(String id, Category category, Account account, BigDecimal amount, String description, Date recordDate, Date date, boolean include) {
        super(id);
        setCategory(category);
        setAccount(account);
        setAmount(amount);
        setDescription(description);
        setRecordDate(recordDate);
        setCreateDate(date);
        setAccountInclude(include);
    }

    public void editRecord(FinanceRecordTableRow editData, EntityManager em) {
        boolean changeAmount = !getAmount().equals(editData.getAmount());
        boolean changeAccount = !getAccount().getId().equals(editData.getAccount().getId());

        boolean changeCategory = !getCategory().getId().equals(editData.getCategory().getId());
        Category newCategory = null;
        if (changeCategory) {
            newCategory = em.find(Category.class, editData.getCategory().getId());
        }

        if (accountInclude && (changeAccount || changeAmount || changeCategory)) {
            getAccount().changeBalance(getCategory(), getAmount(), true);
            if (changeAccount) {
                Account newAccount = em.find(Account.class, editData.getAccount().getId());
                setAccount(newAccount);
            }
            getAccount().changeBalance(newCategory != null ? newCategory : getCategory(), editData.getAmount(), false);
        }

        setAmount(editData.getAmount());
        setDescription(editData.getDescription());
        if (!accountInclude && changeAccount) {
            setAccount(em.find(Account.class, editData.getAccount().getId()));
        }
        if (changeCategory) {
            setCategory(newCategory);
        }

    }

}
