import api.finance.record.FinanceRecordReadService;
import api.finance.record.FinanceRecordWriteService;
import model.FilterData;
import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;

import javax.annotation.Nullable;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.util.List;

@Path("/finance")
@Produces(MediaType.APPLICATION_JSON)
public class FinanceRest {
    @EJB(lookup = "java:jboss/exported/back-ear/FinanceRecordService!api.finance.record.FinanceRecordReadService")
    FinanceRecordReadService financeRecordReadService;

    @EJB(lookup = "java:jboss/exported/back-ear/FinanceRecordWriteService!api.finance.record.FinanceRecordWriteService")
    FinanceRecordWriteService financeRecordWriteService;

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @POST
    @Path("/record/user")
    public List<FinanceRecordTableRow> getAllFinanceUserRecords(
            @Nullable List<FilterData> filter
    ) {
        request.getUserPrincipal();
        return financeRecordReadService.getUserRecords(filter);
    }

    @GET
    @Path("record/{id}/user")
    public FinanceRecordTableRow getUserFinanceRecordById(
            @PathParam("id") String id
    ) {
        return financeRecordReadService.getUserRecordById(id);
    }

    @PUT
    @Path("record/user/create")
    public FinanceRecordTableRow createUserFinanceRecord(
            CreateFinanceRecordDto createData
    ) throws ParseException {
        return financeRecordWriteService.createUserFinanceRecord(createData);
    }

    @POST
    @Path("record/user/delete/{id}")
    public FinanceRecordTableRow deleteUserFinanceRecord (
            @PathParam("id") String id
    ) throws ParseException {
        return financeRecordWriteService.deleteUserFinanceRecord(id);
    }

    @POST
    @Path("record/user/edit/{id}")
    public FinanceRecordTableRow editUserFinanceRecord (
            @PathParam("id") String id,
            FinanceRecordTableRow editDto
    ) throws ParseException {
        return financeRecordWriteService.editUserFinanceRecord(id, editDto);
    }
}
