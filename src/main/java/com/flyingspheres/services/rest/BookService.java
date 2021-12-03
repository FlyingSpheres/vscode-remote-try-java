package com.flyingspheres.services.rest;

import com.flyingspheres.services.data.BookDao;
import com.flyingspheres.services.data.model.Book;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Path("/books")
@Api(description = "API For Book Objects")
public class BookService {
    private Logger log = Logger.getLogger(BookService.class.getName());

    @PersistenceContext(name = "primary")
    private EntityManager em;

    @Inject
    @Metric(name="BookService_Counter", displayName="Stats Hits", description="Number of hits on the /stats endpoint", absolute=true)
    Counter counter;

    @Inject
    @Metric(name="BookService_Timer", displayName="API Timer", description="Time to execute each end point", absolute=true)
    Timer timer;

    @Inject
    @Metric(name="BookService_Histogram", displayName="Histogram", description="Histogram", absolute=true)
    Histogram histogram;

    @Inject
    @Metric(name="BookService_Meter", displayName="Meter", description="Meter", absolute=true)
    Meter meter;

    @GET
    @Path("/{name}")
    @Consumes({ "text/plain" })
    @Produces({"application/json"})
    @ApiOperation(value = "", notes = "retrieve book from database by name", response = String.class, tags={ "Book",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book Retrieved", response = String.class) })
    public Response retrieveBookByName(@PathParam("name") String bookName) {
        log.log(Level.INFO, "Querying for book by name: " + bookName);
        Timer.Context context = timer.time();
        counter.inc();
        meter.mark();

        Response response = null;
        try {
            Book book = BookDao.retrieveBookByName(em, bookName);

            response = Response.ok().entity(book).build();
        } catch (Throwable t) {
            response = Response.serverError().entity(t.getMessage()).build();
        }

        return response;
    }

    @GET
    @Consumes({ "text/plain" })
    @Produces({"application/json"})
    @ApiOperation(value = "", notes = "retrieve book from database by name", response = String.class, tags={ "Book",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book Retrieved", response = String.class) })
    public Response retrieveAllBOoks() {
        log.log(Level.INFO, "Querying for all books");
        Timer.Context context = timer.time();
        counter.inc();
        meter.mark();

        Response response = null;
        try {
            List<Book> books = BookDao.retrieveAllBooks(em);

            response = Response.ok().entity(books).build();
        } catch (Throwable t) {
            response = Response.serverError().entity(t.getMessage()).build();
        }

        return response;
    }


    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "New Book", response = String.class, tags={ "Books",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book Saved", response = String.class) })
    public Response saveBook(Book book ) {
        log.log(Level.INFO, "entering web service Save Book");
        Response response = null;
        Timer.Context context = timer.time();
        counter.inc();
        meter.mark();

        Map<String, String> responseData = new HashMap<>();
        try {
            log.log(Level.INFO, "Saving booko: " + book.getName());
            BookDao.saveBook(em, book);
            responseData.put("message", "message received for " + book.getName());
            response = Response.ok(responseData, MediaType.APPLICATION_JSON).build();
        } catch (Throwable t) {
            responseData.put("message", "book save error for " + book.getName());
            responseData.put("details", t.getMessage());
            log.log(Level.SEVERE, "Error saving book: " + t.getMessage());
            t.printStackTrace();
            response = Response.serverError().build();
        }

        return response;
    }
}