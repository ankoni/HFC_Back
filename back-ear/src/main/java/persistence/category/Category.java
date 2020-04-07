package persistence.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.ConstData;
import persistence.EntityId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CATEGORY")
public class Category extends EntityId {
    @Column(name = "name")
    private String name;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "del_date")
    private Date delDate;

    public Category(String id, String name, String parentId) {
        super(id);
        setName(name);
        setParentId(parentId);
    }

    public boolean isIncome() {
        if (getParentId() == null) {
            return getId().equals(ConstData.INCOME_ID);
        } else {
            return getParentId().equals(ConstData.INCOME_ID);
        }
    }

    public boolean isConsumption() {
        if (getParentId() == null) {
            return getId().equals(ConstData.CONSUMPTION_ID);
        } else {
            return getParentId().equals(ConstData.CONSUMPTION_ID);
        }
    }
}
