package api.category;

import model.category.UserCategoryDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CategoryWriteService {
    UserCategoryDto createUserCategory(String parentId, @NotNull UserCategoryDto data);

    UserCategoryDto editUserCategory(UserCategoryDto data);

    List<UserCategoryDto> deleteUserCategory(UserCategoryDto data);
}
