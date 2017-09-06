package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import generated.tables.records.ReceiptsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class TagController {
    final ReceiptDao receipts;

    public TagController(ReceiptDao receipts) {
        this.receipts = receipts;
    }
    @GET
    @Path("/{tag}")
    public List<ReceiptResponse> getTag(@PathParam("tag") String tagName){
        List<ReceiptsRecord> receiptRecords = receipts.getTag(tagName);
        if(receiptRecords==null)
            return null;
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
    @PUT
    @Path("/{tag}")
    public void changeTag(@PathParam("tag") String tagName,@Valid @NotNull int receipt_id) {
        receipts.changeTag(receipt_id,tagName);
    }
}
