package impl.category;

import api.category.CategoryWriteService;
import impl.UserReadServiceImpl;
import model.category.UserCategoryDto;
import persistence.category.Category;
import persistence.category.UserCategory;
import persistence.user.User;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Remote(CategoryWriteService.class)
@LocalBean
@Stateless(name = "CategoryWriteService")
public class CategoryWriteServiceImpl implements CategoryWriteService {
    @Inject
    UserReadServiceImpl userReadService;

    @Inject
    CategoryReadServiceImpl categoryReadService;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public UserCategoryDto createUserCategory(String parentId, @NotNull UserCategoryDto data) {
        User user = userReadService.getUserPersist();
        Category category = new Category(
                UUID.randomUUID().toString(),
                data.getName(),
                parentId
        );
        entityManager.persist(category);

        UserCategory userCategory = new UserCategory(
                category.getId(),
                user,
                category
        );
        entityManager.persist(userCategory);
        data.setId(userCategory.getId());
        return data;
    }

    @Override
    public UserCategoryDto editUserCategory(UserCategoryDto data) {
        Category userCategory = entityManager.find(Category.class, data.getId());
        userCategory.setName(data.getName());
        entityManager.flush();
        return data;
    }

    @Override
    public List<UserCategoryDto> deleteUserCategory(UserCategoryDto data) {
        //// TODO: 27.03.2020 добавить проверку на связь с Записями (если связи нет - удалять из базы)
        UserCategory userCategory = entityManager.find(UserCategory.class, data.getId());
        userCategory.getCategory().setDelDate(new Date());
        entityManager.flush();
        return categoryReadService.getUserCategory();
    }
}
