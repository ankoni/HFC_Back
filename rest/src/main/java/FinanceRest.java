import api.category.CategoryReadService;
import api.finance.record.FinanceRecordReadService;
import api.finance.record.FinanceRecordWriteService;
import model.finance.record.CreateFinanceRecordDto;
import model.finance.record.FinanceRecordTableRow;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.text.ParseException;
import java.util.List;

@Path("/finance")
public class FinanceRest {
    @EJB(lookup = "java:jboss/exported/back-ear/FinanceRecordService!api.finance.record.FinanceRecordReadService")
    FinanceRecordReadService financeRecordReadService;

    @EJB(lookup = "java:jboss/exported/back-ear/FinanceRecordWriteService!api.finance.record.FinanceRecordWriteService")
    FinanceRecordWriteService financeRecordWriteService;

    @GET
    @Path("record/user")
    public List<FinanceRecordTableRow> getAllFinanceUserRecords() {
        return financeRecordReadService.getUserRecords();
    }

    @PUT
    @Path("record/user/create")
    public List<FinanceRecordTableRow> createUserFinanceRecord(
            CreateFinanceRecordDto createData
    ) throws ParseException {
        return financeRecordWriteService.createUserFinanceRecord(createData);
    }

    @POST
    @Path("record/user/delete/{id}")
    public List<FinanceRecordTableRow> deleteUserFinanceRecord (
            @PathParam("id") String id
    ) {
        return financeRecordWriteService.deleteUserFinanceRecord(id);
    }

    @POST
    @Path("record/user/edit/{id}")
    public List<FinanceRecordTableRow> editUserFinanceRecord (
            @PathParam("id") String id,
            FinanceRecordTableRow editDto
    ) {
        return financeRecordWriteService.editUserFinanceRecord(id, editDto);
    }
}
