/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.bookstore;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.bookstore.entity.Book;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.util.IdGenerator;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jun 26, 2012  
 */
public class JCRDataStorage {
  private static final Log   log                 = ExoLogger.getLogger(JCRDataStorage.class);

  public static final String DEFAULT_PARENT_PATH = "/bookStore";

  public static final String EXO_BOOK_STORE      = "exo:eXoBookStore";

  public static final String EXO_BOOK            = "exo:book";

  public static final String EXP_BOOK_TITLE      = "exo:booktitle";

  public static final String EXP_BOOK_PRICE      = "exo:bookprice";

  private RepositoryService  repoService;

  public JCRDataStorage(RepositoryService repoService) {
    this.repoService = repoService;
    init();
  }

  /**
   * @param sprovider
   * @return the JCRSession with default Workspace and current Repository
   * @throws Exception
   */
  private Session getSession(SessionProvider sprovider) throws Exception {
    ManageableRepository currentRepo = repoService.getCurrentRepository();
    return sprovider.getSession(currentRepo.getConfiguration().getDefaultWorkspaceName(),
                                currentRepo);
  }

  /**
   * @param nodePath
   * @param sessionProvider
   * @return the Node at path <code>nodePath</code>
   * @throws Exception
   */
  private Node getNodeByPath(String nodePath, SessionProvider sessionProvider) throws Exception {
    return (Node) getSession(sessionProvider).getItem(nodePath);
  }

  /**
   * initialize the JCR Repository. Create the bookStore node of nodetype
   * exo:eXoBookStore if not existed
   */
  private void init() {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    Node node = null;
    log.info("initialize BookStoreServiceImpl");
    try {
      node = getNodeByPath(DEFAULT_PARENT_PATH, sProvider);
    } catch (PathNotFoundException e) {
      // If the path not exist then create new path
      try {
        node = getNodeByPath("/", sProvider);
        node.addNode("bookStore", EXO_BOOK_STORE);
        node.getSession().save();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } catch (Exception e) {
      log.error("Failed to init BookStore jcr node's path", e);
    } finally {
      sProvider.close();
    }
  }

  /**
   * 
   * @param bookNode
   * @return a book object from a node
   * @throws Exception
   */
  private Book createBookByNode(Node bookNode) throws Exception {
    if (bookNode == null) {
      return null;
    }

    Book bookNew = new Book();
    bookNew.setId(bookNode.getName());

    bookNew.setTitle(bookNode.getProperty(EXP_BOOK_TITLE).getString());
    bookNew.setPrice(bookNode.getProperty(EXP_BOOK_PRICE).getLong());
    return bookNew;
  }

  public Book getBook(String bookid) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + "/" + bookid, sProvider);
      return createBookByNode(node);
    } catch (PathNotFoundException e) {
      return null;
    } catch (Exception e) {
      log.error("Failed to get book by id", e);
      return null;
    } finally {
      sProvider.close();
    }
  }

  public void addBook(String bookTitle, long price) {
    Book book = new Book(bookTitle, price);
    SessionProvider sProvider = SessionProvider.createSystemProvider();

    String nodeId = IdGenerator.generate();
    book.setId(nodeId);

    try {
      Node parentNode = getNodeByPath(DEFAULT_PARENT_PATH, sProvider);
      Node bookNode = parentNode.addNode(nodeId, EXO_BOOK);
      bookNode.setProperty(EXP_BOOK_TITLE, book.getTitle());
      bookNode.setProperty(EXP_BOOK_PRICE, book.getPrice());

      parentNode.getSession().save();
    } catch (PathNotFoundException e) {
    } catch (Exception e) {
      log.error("Failed to add book", e);
    } finally {
      sProvider.close();
    }
  }

  public void deleteBook(String id) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + "/" + id, sProvider);
      node.remove();
      node.getSession().save();
    } catch (PathNotFoundException e) {
      log.error("PathNotFound");
    } catch (Exception e) {
      log.error("Failed to delete book by id", e);
    } finally {
      sProvider.close();
    }
  }

  public void editBook(String bookId, String bookTitle, long bookPrice) {
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      Node node = getNodeByPath(DEFAULT_PARENT_PATH + "/" + bookId, sProvider);
      node.setProperty(EXP_BOOK_TITLE, bookTitle);
      node.setProperty(EXP_BOOK_PRICE, bookPrice);
      node.getSession().save();
    } catch (Exception e) {
      log.error("Failed to delete all book", e);
    } finally {
      sProvider.close();
    }
  }

  public List<Book> searchTitle(String key) {
    key = key.replaceAll("\"", "\\\"").replaceAll("-", StringUtils.EMPTY);

    StringBuffer queryString = new StringBuffer("select * from " + EXO_BOOK);
    queryString.append(" where " + EXP_BOOK_TITLE + " like '%" + key + "%'");

    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(queryString.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator iterator = result.getNodes();

      List<Book> books = new ArrayList<Book>();
      while (iterator.hasNext()) {
        Node node = iterator.nextNode();
        Book book = createBookByNode(node);
        books.add(book);
      }
      return books;
    } catch (Exception e) {
      log.error("Failed to search book by name", e);
      return null;
    } finally {
      sProvider.close();
    }
  }

  public List<Book> getAll() {
    StringBuffer queryString = new StringBuffer("select * from " + EXO_BOOK);
    SessionProvider sProvider = SessionProvider.createSystemProvider();
    try {
      QueryManager queryManager = getSession(sProvider).getWorkspace().getQueryManager();
      Query query = queryManager.createQuery(queryString.toString(), Query.SQL);
      QueryResult result = query.execute();
      NodeIterator iterator = result.getNodes();

      List<Book> books = new ArrayList<Book>();
      while (iterator.hasNext()) {
        Node node = iterator.nextNode();
        Book book = createBookByNode(node);
        books.add(book);
      }
      return books;
    } catch (Exception e) {
      log.error("Failed to get all book", e);
      return null;
    } finally {
      sProvider.close();
    }
  }
}