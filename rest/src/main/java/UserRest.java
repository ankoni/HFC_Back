import api.UserReadService;
import api.UserWriteService;
import model.auth.AuthData;
import model.user.UserCreateDto;
import model.user.UserDto;
import model.user.UserEditDto;

import javax.ejb.EJB;
import javax.ws.rs.*;

@Path("/user")
public class UserRest {

    @EJB(lookup = "java:jboss/exported/rest/UserReadService!api.UserReadService")
    UserReadService userReadService;

    @EJB(lookup = "java:jboss/exported/back-ear/UserWriteService!api.UserWriteService")
    UserWriteService userWriteService;

    @GET
    @Path("/")
    public UserDto getUser() {
        return userReadService.getUser();
    }

    @PUT
    @Path("/create")
    public AuthData createUser(
            UserCreateDto userCreateDto
    ) throws Exception {
        try {
            AuthData data = userWriteService.createUser(userCreateDto);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @POST
    @Path("/{id}")
    public UserDto editUser(
            @PathParam("id") String id,
            UserEditDto userEdit
    ) throws Exception {
        return userWriteService.editUser(id, userEdit);
    }
}
