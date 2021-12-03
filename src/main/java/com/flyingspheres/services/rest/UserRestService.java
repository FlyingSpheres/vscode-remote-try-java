package com.flyingspheres.services.rest;

import com.flyingspheres.services.rest.domain.User;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
@Path("/user")
@Api(description = "User Services API")
public class UserRestService {
    private Logger log = Logger.getLogger(UserRestService.class.getName());

    @Inject
    @Metric(name="UserService_Counter", displayName="Stats Hits", description="Number of hits on the /stats endpoint", absolute=true)
    Counter counter;

    @Inject
    @Metric(name="UserService_Timer", displayName="API Timer", description="Time to execute each end point", absolute=true)
    Timer timer;

    @Inject
    @Metric(name="UserService_Histogram", displayName="Histogram", description="Histogram", absolute=true)
    Histogram histogram;

    @Inject
    @Metric(name="UserService_Meter", displayName="Meter", description="Meter", absolute=true)
    Meter meter;

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Validate User", response = String.class, tags={ "User",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User Validated", response = String.class) })
    public Response validateUser(User user){
        boolean valid = false;
        Map<String, String> responseData = new HashMap<>();

        if ("aobrien".equalsIgnoreCase(user.getUserId()) &&
            "password".equals(user.getPassword())){
            valid = true;
        }

        if (valid){
            responseData.put("validated", "true");
            responseData.put("userId", user.getUserId());
            responseData.put("firstName", "Aaron");
            responseData.put("lastName", "OBrien");
        } else {
            responseData.put("validated", "false");
            responseData.put("userId", user.getUserId());
        }

        return Response.ok(responseData, MediaType.APPLICATION_JSON).build();
    }

}