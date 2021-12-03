package com.flyingspheres.services.rest;

import com.flyingspheres.services.rest.domain.ListContainer;
import com.flyingspheres.services.rest.domain.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Histogram;
import org.eclipse.microprofile.metrics.Meter;
import org.eclipse.microprofile.metrics.Timer;
import org.eclipse.microprofile.metrics.annotation.Metric;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Path("/messages")
@Api(description = "the Messages API")
public class MessagesRestService {
    private Logger log = Logger.getLogger(MessagesRestService.class.getName());

//    @Inject
//    EventsDao eventsDao;


    @Inject
    @Metric(name="MessagesService_Counter", displayName="Stats Hits", description="Number of hits on the /stats endpoint", absolute=true)
    Counter counter;

    @Inject
    @Metric(name="MessagesService_Timer", displayName="API Timer", description="Time to execute each end point", absolute=true)
    Timer timer;

    @Inject
    @Metric(name="MessagesService_Histogram", displayName="Histogram", description="Histogram", absolute=true)
    Histogram histogram;

    @Inject
    @Metric(name="MessagesService_Meter", displayName="Meter", description="Meter", absolute=true)
    Meter meter;

    @GET
    @Path("/status")
    //In development you can see this here: http://localhost:9090/general/rest/messages/status
    public Response retrieveStatus(){
        return Response.ok("Sever Running").build();
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "New Message", response = String.class, tags={ "Messages",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message Saved", response = String.class) })
    public Response sendMessage(Message message ) {
        log.log(Level.INFO, "entering web service send message");
        Response response = null;
        Timer.Context context = timer.time();
        counter.inc();
        meter.mark();

        Map<String, String> responseData = new HashMap<>();
        try {
            System.out.println("Sending message to: " + message.getToUserId());
            log.log(Level.INFO, "Sending message to: " + message.getToUserId());
            responseData.put("message", "message received for " + message.getToUserId());
            response = Response.ok(responseData, MediaType.APPLICATION_JSON).build();
        } catch (Throwable t) {
            responseData.put("message", "message received error for " + message.getToUserId());
            responseData.put("details", t.getMessage());
            log.log(Level.SEVERE, "Error saving event: " + t.getMessage());
            t.printStackTrace();
            response = Response.serverError().build();
        }

        return response;
    }

    @GET
    @Path("/{userId}/")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Get Messages for user", response = String.class, tags={ "Messages",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Messages retrieved", response = String.class) })
    public Response sendMessage(@PathParam("userId") String userId ) {

        List<Message> messageList = new ArrayList<>();

        messageList.add(Message.build("aobrien", "pedro", "Hola, ¿como estás?", false));
        messageList.add(Message.build("aobrien", "pedro", "¿Estás bien?", false));
        messageList.add(Message.build("aobrien", "mon", "¿Como se va?", false));

        ListContainer listContainer = new ListContainer();
        listContainer.setMessageList(messageList);

        return Response.ok(listContainer, MediaType.APPLICATION_JSON).build();

    }
}