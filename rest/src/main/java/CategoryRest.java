import api.account.AccountReadService;
import api.account.AccountWriteService;
import api.category.CategoryReadService;
import api.category.CategoryWriteService;
import model.category.UserCategoryDto;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;

@Path("/category")
public class CategoryRest {
    @EJB(lookup = "java:jboss/exported/back-ear/CategoryReadService!api.category.CategoryReadService")
    CategoryReadService categoryReadService;
    @EJB(lookup = "java:jboss/exported/back-ear/CategoryWriteService!api.category.CategoryWriteService")
    CategoryWriteService categoryWriteService;

    @GET
    @Path("/user")
    public List<UserCategoryDto> getUserCategories() {
        return categoryReadService.getUserCategory();
    }

    @PUT
    @Path("/user/{parentCategoryId}/create")
    public UserCategoryDto createUserCategory(
            @PathParam("parentCategoryId") String parentId,
            UserCategoryDto newCategory
    ) {
        return categoryWriteService.createUserCategory(parentId, newCategory);
    }

    @POST
    @Path("/user/{categoryId}/edit")
    public UserCategoryDto editUserCategory(
            UserCategoryDto data
    ) {
        return categoryWriteService.editUserCategory(data);
    }

    @POST
    @Path("/user/{categoryId}/delete")
    public List<UserCategoryDto> deleteUserCategory(
            UserCategoryDto data
    ) {
        return categoryWriteService.deleteUserCategory(data);
    }
}
