package api.category;

import model.category.UserCategoryDto;

import java.util.List;

public interface CategoryReadService {
    List<UserCategoryDto> getUserCategory();
}
