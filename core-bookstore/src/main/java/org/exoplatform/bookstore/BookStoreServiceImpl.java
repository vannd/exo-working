/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the ho
pe that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.bookstore;

import java.util.List;

import org.exoplatform.bookstore.entity.Book;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.jcr.RepositoryService;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Jun
 * 26, 2012
 */
public class BookStoreServiceImpl implements BookStoreService {

  private JCRDataStorage dataStorage;

  public BookStoreServiceImpl() {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    RepositoryService repoService = (RepositoryService) container.getComponentInstanceOfType(RepositoryService.class);
    dataStorage = new JCRDataStorage(repoService);
  }


  public Book getBook(String bookid) {
    return dataStorage.getBook(bookid);
  }

  public void addBook(String bookTitle, long price) {
    dataStorage.addBook(bookTitle, price);
  }

  public void deleteBook(String id) {
    dataStorage.deleteBook(id);
  }

  public void editBook(String bookId, String bookTitle, long bookPrice) {
    dataStorage.editBook(bookId, bookTitle, bookPrice);
  }

  public List<Book> searchTitle(String key) {
    return dataStorage.searchTitle(key);
  }

  public List<Book> getAll() {
    return dataStorage.getAll();
  }

}