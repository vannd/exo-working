package org.exoplatform.bookstore;


import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.exoplatform.bookstore.BookStoreService;
import org.exoplatform.bookstore.entity.Book;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
/**
 * Created by The eXo Platform SAS
 * author: VanND 
 *         Vannd@exoplatform.com
 * Otc 21 2014
 */
@Path("/bookstore")
public class BookStoreRestService implements ResourceContainer{
  private static final Log   log                            = ExoLogger.getLogger(BookStoreRestService.class);
  private static final CacheControl cc;
  static {
    cc = new CacheControl();
    cc.setNoCache(true);
    cc.setNoStore(true);
  }
  
  private BookStoreService bookStoreService;

  public BookStoreRestService() throws Exception {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    bookStoreService = (BookStoreService) container.getComponentInstanceOfType(BookStoreService.class);
    if (bookStoreService == null) {
      log.warn("cannot find bookService");
    }
  }
  public BookStoreService getBookStoreService() {
    return bookStoreService;
  }

  public void setBookStoreService(BookStoreService bookStoreService) {
    this.bookStoreService = bookStoreService;
  }
  
  @GET
  @Path("/getAll")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAll() {
    List<Book> books = bookStoreService.getAll();
  return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  @GET
  @Path("/getBook/{bookId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBook(@PathParam("bookId") String bookId) {
    Book book = bookStoreService.getBook(bookId);
  return Response.ok(book, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  @GET
  @Path("/search/{title}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response search(@PathParam("title") String title) {
    List<Book> books = bookStoreService.searchTitle(title);
  return Response.ok(books, MediaType.APPLICATION_JSON).cacheControl(cc).build();
  }
  
  @POST
  @Path("/add")
  public Response add(@FormParam("title") String title,
                      @FormParam("price") Long price){
    bookStoreService.addBook(title, price);
    return Response.status(200).entity("The book with title: "+ title + " and price: " + price + " just added").build();
  }
  
  @DELETE
  @Path("/delete/{bookId}")
  public Response deleteBook(@PathParam("bookId") String bookId) {
    if (bookStoreService.getBook(bookId) != null) {
      bookStoreService.deleteBook(bookId);
    } else {
      return Response.status(400).entity("The book with id: "+ bookId + " is not exist").build();
    }
  return Response.status(200).entity("The book with id: "+ bookId + " just deleted").build();
  }
  
  @POST
  @Path("/update")
  public Response update(@FormParam("id") String id,
                         @FormParam("title") String title,
                         @FormParam("price") Long price){
    if(bookStoreService.getBook(id) != null) {
    bookStoreService.editBook(id, title, price);
    } else {
      return Response.status(400).entity("Book Not Found").build();
    }
    return Response.status(200).entity("The book with Id: "+ id + " just updated").build();
  }
}
