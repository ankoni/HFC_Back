package impl.category;

import api.UserReadService;
import api.category.CategoryReadService;
import impl.UserReadServiceImpl;
import model.category.UserCategoryDto;
import model.user.UserDto;
import persistence.ConstData;
import persistence.category.Category;
import persistence.category.UserCategory;
import persistence.user.User;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Remote(CategoryReadService.class)
@LocalBean
@Stateless(name = "CategoryReadService")
public class CategoryReadServiceImpl implements CategoryReadService {
    @Inject
    UserReadServiceImpl userReadService;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UserCategoryDto> getUserCategory() {
        UserDto user = userReadService.getUser();
        if (user != null) {
            List<UserCategory> userCategories = entityManager.createQuery("select main from UserCategory main \n" +
                    "where main.user.id = :user \n" +
                    "and main.category.delDate is null", UserCategory.class)
                    .setParameter("user", user.getId())
                    .getResultList();

            List<Category> categories = entityManager.createQuery("select main from Category main " +
                    "where main.id in (:incomeId, :consumptionId)", Category.class)
                    .setParameter("incomeId", ConstData.INCOME_ID)
                    .setParameter("consumptionId", ConstData.CONSUMPTION_ID)
                    .getResultList();

            List<UserCategoryDto> categoryDto = new ArrayList<>();

            categories.forEach(mainCategory -> {
                categoryDto.add(new UserCategoryDto(
                        mainCategory.getId(),
                        mainCategory.getName(),
                        userCategories.stream()
                                .filter(child -> child.getCategory().getParentId().equals(mainCategory.getId()))
                                .map(child -> new UserCategoryDto(child.getId(), child.getCategory().getName()))
                                .collect(Collectors.toList())
                ));
            });

            return categoryDto;
        } else {
            return null;
        }

    }
}
