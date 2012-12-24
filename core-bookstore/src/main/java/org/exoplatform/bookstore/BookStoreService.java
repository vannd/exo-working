package org.exoplatform.bookstore;
import java.util.List;

import org.exoplatform.bookstore.entity.Book;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Jun
 * 26, 2012
 */
public interface BookStoreService {

  /**
   * @param id
   * @return the book which has the id <code>id</code>
   */
  public Book getBook(String id);

  /**
   * add a new book
   * 
   * @param bookTitle the title of the book
   * @param price the price of the book
   */
  public void addBook(String bookTitle, long price);

  /**
   * delete a book which has the id <code>id</code>
   * @param id
   */
  public void deleteBook(String id);

  /**
   * replace the book content which has the <code>bookId</code> with the new book
   * @param bookId
   * @param title
   * @param price
   */
  public void editBook(String bookId, String title, long price);

  /**
   * @param bookTitle
   * @return a list of book with the title like <code>bookTitle</code>
   */
  public List<Book> searchTitle(String bookTitle);

  /**
   * 
   * @return a list of all books
   */
  public List<Book> getAll();

}