package com.flyingspheres.services.rest;

import com.flyingspheres.services.data.BookDao;
import com.flyingspheres.services.data.ChapterDao;
import com.flyingspheres.services.data.ChapterWordsDao;
import com.flyingspheres.services.data.WordDao;
import com.flyingspheres.services.data.model.Book;
import com.flyingspheres.services.data.model.Chapter;
import com.flyingspheres.services.data.model.ChapterWords;
import com.flyingspheres.services.data.model.Word;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Path("/chapters")
@Api(description = "API For Chapter Objects")
public class ChapterService {
    private Logger log = Logger.getLogger(ChapterService.class.getName());


    @PersistenceContext(name = "primary")
    private EntityManager em;

    @Inject
    @Metric(name="ChapterService_Counter", displayName="Stats Hits", description="Number of hits on the /stats endpoint", absolute=true)
    Counter counter;

    @Inject
    @Metric(name="ChapterService_Timer", displayName="API Timer", description="Time to execute each end point", absolute=true)
    Timer timer;

    @Inject
    @Metric(name="ChapterService_Histogram", displayName="Histogram", description="Histogram", absolute=true)
    Histogram histogram;

    @Inject
    @Metric(name="ChapterService_Meter", displayName="Meter", description="Meter", absolute=true)
    Meter meter;

    @GET
    @Path("/retrieveChaptersForBook/{bookId}")
    @Consumes({ "text/plain" })
    @Produces({"application/json"})
    @ApiOperation(value = "", notes = "retrieve Chapter from database by name", response = String.class, tags={ "Chapter",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chapter Retrieved", response = String.class) })
    public Response retrieveChaptersByBook(@PathParam("bookId") int bookId) {
        Response response = null;
        List<Chapter> chapters = ChapterDao.retrieveChaptersByBook(em, bookId);
        response = Response.ok(chapters, MediaType.APPLICATION_JSON).build();
        return response;
    }

    @POST
    @Path("/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "New Book", response = String.class, tags={ "Books",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book Saved", response = String.class) })
    public Response saveChapter(Chapter chapter) {
        log.log(Level.INFO, "entering web service save Chapter");
        Response response = null;
        Timer.Context context = timer.time();
        counter.inc();
        meter.mark();

        Map<String, String> responseData = new HashMap<>();
        try {
            log.log(Level.INFO, "Saving Chpater: " + chapter.getChapterName());
            Book linkedBook = BookDao.retrieveBookByName(em, chapter.getBook().getName());
            chapter.setBook(linkedBook);
            ChapterDao.saveChapter(em, chapter);
//            BookDao.addChapterToBook(em, chapter.getBook(), chapter);
            responseData.put("message", "message received for " + chapter.getBook().getName());
            response = Response.ok(responseData, MediaType.APPLICATION_JSON).build();
        } catch (Throwable t) {
            responseData.put("message", "book save error for " + chapter.getBook().getName());
            responseData.put("details", t.getMessage());
            log.log(Level.SEVERE, "Error saving book: " + t.getMessage());
            t.printStackTrace();
            response = Response.serverError().build();
        }

        return response;
    }

    @GET
    @Path("/analyze/{chapterId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "", notes = "Analyze Chapter", response = String.class, tags={ "Message",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Analyze Chapter", response = String.class) })
    public Response saveChapter(@PathParam("chapterId") int chapterId) {
        Chapter selectedChapter = ChapterDao.retrieveChapterById(em, chapterId);
        String chapterText = selectedChapter.getText();
        String[] sentences = chapterText.split("\n");
        List<String> words = new ArrayList<>();
        Map<String, Integer> uniqueWords = new HashMap<>();
        for (String sentence : sentences){
            String[] wordsInSentence = sentence.split(" ");
            words.addAll(Arrays.asList(wordsInSentence));
            for (String aWord : wordsInSentence) {
                uniqueWords.put(aWord, aWord.length());
            }
        }

        Map <String, Word> savedWordMap = new HashMap<>();
        for (String aWord : words){
            String cleanWord = cleanWord(aWord);
            if (cleanWord.length() > 0 && !cleanWord.equals(" ")) {
                Word w = WordDao.retrieveWordByValue(em, cleanWord);
                if (w == null) {
                    w = new Word();
                    w.setWord(cleanWord.toLowerCase().trim());
                    WordDao.saveWord(em, w);
                }
                savedWordMap.put(aWord, w);

                ChapterWords xref = new ChapterWords();
                xref.setChapterId(selectedChapter.getId());
                xref.setWordId(w.getId());

                ChapterWordsDao.saveXref(em, xref);
            }
        }

        StringBuffer buffer = new StringBuffer("Word Count for Book: ").append(selectedChapter.getBook().getName());
        buffer.append(" Chapter: ").append(selectedChapter.getChapterName()).append(" - ").append(selectedChapter.getChapterNumber()).append("\n");
        buffer.append("Word Count: ").append(words.size()).append("\n");
        buffer.append("Unique Word Count: ").append(uniqueWords.size()).append("\n");
        buffer.append("Saved Word Count: " ).append(savedWordMap.size()).append("\n");
        return Response.ok(buffer.toString(), MediaType.TEXT_PLAIN_TYPE).build();
    }

    private String cleanWord(String aWord) {
        String cleanWord = aWord.replace("?", "");
        cleanWord = cleanWord.replace("¿", "");
        cleanWord = cleanWord.replace("!","");
        cleanWord = cleanWord.replace("¡","");
        cleanWord = cleanWord.replace("—","");
        cleanWord = cleanWord.replace(".", "");
        cleanWord = cleanWord.replace(",", "");
        cleanWord = cleanWord.replace("«", "");
        cleanWord = cleanWord.replace("»", "");
        cleanWord = cleanWord.replace(";", "");
        cleanWord = cleanWord.replace("…", "");
        return cleanWord.trim();
    }

}
