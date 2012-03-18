/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2012 Kai Reinhard (k.reinhard@micromata.com)
//
// ProjectForge is dual-licensed.
//
// This community edition is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as published
// by the Free Software Foundation; version 3 of the License.
//
// This community edition is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, see http://www.gnu.org/licenses/.
//
/////////////////////////////////////////////////////////////////////////////

package org.projectforge.web.wicket.flowlayout;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.projectforge.web.wicket.WicketUtils;

/**
 * Represents an icon.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class IconButtonPanel extends Panel
{
  private static final long serialVersionUID = 3317775585548133768L;

  private final Button button;

  private WebMarkupContainer div;

  public IconButtonPanel(final String id, final IconType type)
  {
    this(id, type, (String) null);
  }

  @SuppressWarnings("serial")
  public IconButtonPanel(final String id, final IconType type, final String tooltip)
  {
    super(id);
    button = new Button("button") {
      /**
       * @see org.apache.wicket.markup.html.form.Button#onSubmit()
       */
      @Override
      public void onSubmit()
      {
        IconButtonPanel.this.onSubmit();
      }
    };
    add(button);
    init(type, tooltip);
  }

  @SuppressWarnings("serial")
  public IconButtonPanel(final String id, final IconType type, final Model<String> tooltip)
  {
    super(id);
    button = new Button("button") {
      /**
       * @see org.apache.wicket.markup.html.form.Button#onSubmit()
       */
      @Override
      public void onSubmit()
      {
        IconButtonPanel.this.onSubmit();
      }
    };
    add(button);
    init(type, null);
    if (tooltip != null) {
      WicketUtils.addTooltip(button, tooltip);
    }
  }

  public IconButtonPanel(final String id, final Button button, final IconType type, final String tooltip)
  {
    super(id);
    this.button = button;
    add(button);
    init(type, tooltip);
  }

  /**
   * Sets "light" as class attribute for having light grey colored buttons.
   * @return this for chaining.
   */
  public IconButtonPanel setLight()
  {
    button.add(AttributeModifier.append("class", "light"));
    return this;
  }

  /**
   * @param defaultFormProcessing
   * @return this for chaining.
   * @see Button#setDefaultFormProcessing(boolean)
   */
  public IconButtonPanel setDefaultFormProcessing(final boolean defaultFormProcessing)
  {
    button.setDefaultFormProcessing(defaultFormProcessing);
    return this;
  }

  /**
   * @param attributeName
   * @param value
   * @return this for chaining.
   * @see AttributeModifier#append(String, java.io.Serializable)
   */
  public IconButtonPanel oldAppendAttribute(final String attributeName, final Serializable value)
  {
    button.add(AttributeModifier.append(attributeName, value));
    return this;
  }

  /**
   * @see org.apache.wicket.markup.html.form.Button#onSubmit()
   */
  protected void onSubmit()
  {
  };

  private void init(final IconType type, final String tooltip)
  {
    button.add(AttributeModifier.append("class", "icon-only div-icon"));
    div = new WebMarkupContainer("div");
    button.add(div);
    div.add(AttributeModifier.append("class", type.getClassAttrValue()));
    if (tooltip != null) {
      WicketUtils.addTooltip(button, tooltip);
    }
  }
}