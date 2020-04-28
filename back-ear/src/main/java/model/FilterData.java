package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.filter.FilterType;
import model.filter.ValueType;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilterData implements Serializable {
    public String columnName;
    public IdNameObj value;
    /**
     * Указывается тип сравнения =, >, <
     */
    public FilterType conditionType;

    /**
     * Тип значения
     */
    public ValueType valueType;
}
