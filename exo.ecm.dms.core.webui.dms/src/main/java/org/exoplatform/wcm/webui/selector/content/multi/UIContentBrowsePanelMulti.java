/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.wcm.webui.selector.content.multi;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.ecm.webui.selector.UISelectable;
import org.exoplatform.wcm.webui.selector.UISourceGridUpdatable;
import org.exoplatform.wcm.webui.selector.content.UIContentBrowsePanel;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SAS.
 * 
 * @author : Hoa.Pham hoa.pham@exoplatform.com Jun 23, 2008
 */
@ComponentConfig(
  lifecycle = Lifecycle.class,
  template = "classpath:groovy/wcm/webui/selector/content/multi/UIContentBrowsePanel.gtmpl",
  events = {
    @EventConfig(listeners = UIContentBrowsePanel.ChangeContentTypeActionListener.class),
    @EventConfig(listeners = UIContentBrowsePanelMulti.SelectActionListener.class)
  }
)

public class UIContentBrowsePanelMulti extends UIContentBrowsePanel {

  private String itemPaths;
  
  public String getItemPaths() {
    return itemPaths;
  }
  
  public void setItemPaths(String itemPaths) {
    this.itemPaths = itemPaths;
  }

  public static class SelectActionListener extends EventListener<UIContentBrowsePanel> {
    public void execute(Event<UIContentBrowsePanel> event) throws Exception {
      UIContentBrowsePanel contentBrowsePanel = event.getSource();
      String returnFieldName = contentBrowsePanel.getReturnFieldName();
      String itemPaths = event.getRequestContext().getRequestParameter(OBJECTID);
      ((UISelectable)(contentBrowsePanel.getSourceComponent())).doSelect(returnFieldName, itemPaths);
      
      List<String> selectedItems = new ArrayList<String>();
      for (String selectedItem : itemPaths.split(";")) {
    	  selectedItems.add(selectedItem);
      }
      ((UISourceGridUpdatable)contentBrowsePanel.getSourceComponent()).doSave(selectedItems);
	}
  }
	
}
