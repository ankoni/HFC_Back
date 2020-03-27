package model.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCategoryDto implements Serializable {
    public String id;
    public String name;
    public boolean parent;
    public List<UserCategoryDto> children;
    public boolean editing;

    public UserCategoryDto(String id, String name) {
        setId(id);
        setName(name);
        setParent(false);
        setChildren(null);
    }
    public UserCategoryDto(String id, String name, List<UserCategoryDto> children) {
        setId(id);
        setName(name);
        setParent(true);
        setChildren(children);
    }
}
