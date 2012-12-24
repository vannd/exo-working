package org.exoplatform.bookstore.portlet;

import org.exoplatform.bookstore.webui.UIBookStoreMainForm;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

@ComponentConfig(lifecycle = UIApplicationLifecycle.class)
public class UIBookStorePortlet extends UIPortletApplication {

  public UIBookStorePortlet() throws Exception {
    super();
    addChild(UIBookStoreMainForm.class, null, null);
  }

}