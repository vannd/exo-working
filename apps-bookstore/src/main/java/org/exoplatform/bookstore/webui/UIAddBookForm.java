package org.exoplatform.bookstore.webui;


import org.exoplatform.bookstore.BookStoreService;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;

@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "system:/groovy/webui/form/UIForm.gtmpl", 
    events = {
    @EventConfig(listeners = UIAddBookForm.CreateActionListener.class),
    @EventConfig(listeners = UIAddBookForm.CancelActionListener.class) })

public class UIAddBookForm extends UIForm {

  private static final Log   log                      = ExoLogger.getLogger(UIAddBookForm.class);

  public static final String TITLE_STRING_INPUT       = "UITitleStringInput";

  public static final String PRICE_STRING_INPUT       = "UIPriceStringInput";

  private BookStoreService   bookService;

  public UIAddBookForm() {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    log.info(" Exocontainer name "+container.getContext().getPortalContainerName());
    bookService = (BookStoreService) container.getComponentInstanceOfType(BookStoreService.class);
    if (bookService == null) {
      log.warn("cannot find bookService");
    }
    addChild(new UIFormStringInput(TITLE_STRING_INPUT,""));
    addChild(new UIFormStringInput(PRICE_STRING_INPUT,""));
  }

  public BookStoreService getBookService() {
    return bookService;
  }

  public static class CreateActionListener extends EventListener<UIAddBookForm> {
    public void execute(Event<UIAddBookForm> event) throws Exception {
      UIAddBookForm addForm = event.getSource();
      String title = addForm.getUIStringInput(TITLE_STRING_INPUT).getValue();
      long price = Long.valueOf(addForm.getUIStringInput(PRICE_STRING_INPUT).getValue());
      addForm.getBookService().addBook(title, price);
      
      //back to BookStoreView
      UIPopupWindow uiPopupWindow = addForm.getParent();
      uiPopupWindow.setShow(false);
      
      
    }
    
  }

  public static class CancelActionListener extends EventListener<UIAddBookForm> {
    public void execute(Event<UIAddBookForm> event) throws Exception {
      UIAddBookForm addForm = event.getSource();
      
      //back to BookStoreView
      UIPopupWindow uiPopupWindow = addForm.getParent();
      uiPopupWindow.setShow(false);
    }
  }
}