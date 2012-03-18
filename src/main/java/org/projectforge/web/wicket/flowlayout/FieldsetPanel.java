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

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.projectforge.web.WebConfiguration;
import org.projectforge.web.wicket.WebConstants;
import org.projectforge.web.wicket.WicketUtils;
import org.projectforge.web.wicket.components.JiraIssuesPanel;

/**
 * Represents a entry of a group panel. This can be a label, text field or other form components.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class FieldsetPanel extends Panel
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FieldsetPanel.class);

  /**
   * Please use this only and only if you haven't multiple children. Please use {@link #newChildId()} instead.
   */
  private static final String FIELDS_ID = "fields";

  public static final String LABEL_SUFFIX_ID = "labelSuffix";

  public static final String DESCRIPTION_SUFFIX_ID = "descriptionSuffix";

  private static final long serialVersionUID = -6318707656650110365L;

  private final WebMarkupContainer fieldset, div;

  private WebMarkupContainer label;

  private boolean labelSide = true;

  private boolean rendered, labelFor, childAdded;

  private final boolean multipleChildren;

  private Component labelSuffix, descriptionSuffix;

  private String labelText;

  private RepeatingView repeater;

  private DivPanel fieldDiv;

  /**
   * Adds this FieldsetPanel to the parent panel.
   * @param parent
   * @param label
   */
  public FieldsetPanel(final DivPanel parent, final String label)
  {
    this(parent, label, null, false);
  }

  /**
   * Adds this FieldsetPanel to the parent panel.
   * @param parent
   * @param label
   * @param multipleChildren If true then multiple children are expected an organized in a RepeatingView. Please note, if you add help or
   *          additional icons multipleChilds has to be true.
   */
  public FieldsetPanel(final DivPanel parent, final String label, final boolean multipleChildren)
  {
    this(parent, label, null, multipleChildren);
  }

  /**
   * Adds this FieldsetPanel to the parent panel.
   * @param parent
   * @param label
   * @param description Description below or beside the label of the field-set.
   */
  public FieldsetPanel(final DivPanel parent, final String labelText, final String description)
  {
    this(parent, labelText, description, false);
  }

  /**
   * Adds this FieldsetPanel to the parent panel.
   * @param parent
   * @param label
   * @param description Description below or beside the label of the field-set.
   * @param multipleChildren If true then multiple children are expected an organized in a RepeatingView.
   */
  public FieldsetPanel(final DivPanel parent, final String labelText, final String description, final boolean multipleChildren)
  {
    this(parent.newChildId(), labelText, description, multipleChildren);
    parent.add(this);
  }

  /**
   */
  @SuppressWarnings("serial")
  private FieldsetPanel(final String id, final String labeltext, final String description, final boolean multipleChildren)
  {
    super(id);
    this.labelText = labeltext;
    this.multipleChildren = multipleChildren;
    fieldset = new WebMarkupContainer("fieldset");
    super.add(fieldset);
    fieldset.add((label = new WebMarkupContainer("label")));
    label.add(new Label("labeltext", new Model<String>() {
      @Override
      public String getObject()
      {
        return labelText;
      };
    }).setRenderBodyOnly(true));
    if (description != null) {
      label.add(new Label("labeldescription", description).setRenderBodyOnly(true));
    } else {
      label.add(WicketUtils.getInvisibleComponent("labeldescription"));
    }
    fieldset.add(div = new WebMarkupContainer("div"));
  }

  public FieldsetPanel setUnit(final String unit)
  {
    this.labelText = WicketUtils.getLabelWithUnit(labelText, unit);
    return this;
  }

  /**
   * @return the labelText
   */
  public String getLabel()
  {
    return labelText;
  }

  /**
   * 
   * @param labelSide
   * @return this for chaining.
   */
  public FieldsetPanel setLabelSide(final boolean labelSide)
  {
    this.labelSide = labelSide;
    return this;
  }

  /**
   * @param labelSuffix the labelSuffix to set
   * @return this for chaining.
   */
  public FieldsetPanel setLabelSuffix(final Component labelSuffix)
  {
    this.labelSuffix = labelSuffix;
    label.add(labelSuffix);
    return this;
  }

  /**
   * Sets the background color of this whole fieldset to red.
   * @return this for chaining.
   */
  public FieldsetPanel setWarninngBackground()
  {
    fieldset.add(AttributeModifier.replace("style", WebConstants.CSS_BACKGROUND_COLOR_RED));
    return this;
  }

  /**
   * @param descriptionSuffix the descriptionSuffix to set
   * @return this for chaining.
   */
  public FieldsetPanel setDescriptionSuffix(final Component descriptionSuffix)
  {
    this.descriptionSuffix = descriptionSuffix;
    label.add(descriptionSuffix);
    return this;
  }

  public FieldsetPanel setLabelFor(final Component component)
  {
    if (component instanceof ComponentWrapperPanel) {
      this.label.add(AttributeModifier.replace("for", ((ComponentWrapperPanel) component).getComponentOutputId()));
    } else {
      this.label.add(AttributeModifier.replace("for", component.getOutputMarkupId()));
    }
    labelFor = true;
    return this;
  }

  /**
   * Declares that there is no validation field which the label should set for. This has no other meaning and effect than not to display the
   * development warning "No label set for field...'.
   * @return
   */
  public FieldsetPanel setNoLabelFor()
  {
    labelFor = true;
    return this;
  }

  /**
   * No wrap of the multiple children.
   * @return this for chaining.
   */
  public FieldsetPanel setNowrap()
  {
    fieldDiv.add(AttributeModifier.append("style", "white-space: nowrap;"));
    return this;
  }

  /**
   * @see org.apache.wicket.MarkupContainer#add(org.apache.wicket.Component[])
   */
  @Override
  public MarkupContainer add(final Component... childs)
  {
    if (repeater == null) {
      if (childAdded == true) {
        throw new IllegalArgumentException("You can't add multiple children, please call constructor with multipleChildren=true.");
      }
      childAdded = true;
      checkLabelFor(childs);
      return div.add(childs);
    } else {
      childAdded = true;
      checkLabelFor(childs);
      for (final Component component : childs) {
        if (component instanceof InputPanel) {
          final InputPanel inputPanel = (InputPanel) component;
          if (inputPanel.getField() instanceof TextField) {
            inputPanel.getField().add(AttributeModifier.append("class", "text"));
          }
        }
      }
      return repeater.add(childs);
    }
  }

  /**
   * @param textField
   * @return The created InputPanel.
   * @see InputPanel#InputPanel(String, Component)
   */
  public InputPanel add(final TextField< ? > textField)
  {
    final InputPanel input = new InputPanel(newChildId(), textField);
    if (textField.getLabel() == null) {
      textField.setLabel(new Model<String>(labelText));
    }
    add(input);
    return input;
  }

  /**
   * @param passwordField
   * @return The created PasswordPanel.
   * @see PasswordPanel#PasswordPanel(String, Component)
   */
  public PasswordPanel add(final PasswordTextField passwordField)
  {
    final PasswordPanel passwordInput = new PasswordPanel(newChildId(), passwordField);
    if (passwordField.getLabel() == null) {
      passwordField.setLabel(new Model<String>(labelText));
    }
    add(passwordInput);
    return passwordInput;
  }

  /**
   * @return The Wicket id of the embedded text field of InputPanel
   */
  public final String getTextFieldId()
  {
    return InputPanel.WICKET_ID;
  }

  /**
   * @param textArea
   * @return The created InputPanel.
   * @see TextAreaPanel#TextAreaPanel(String, Component)
   */
  public TextAreaPanel add(final TextArea< ? > textArea)
  {
    final TextAreaPanel panel = new TextAreaPanel(newChildId(), textArea);
    if (textArea.getLabel() == null) {
      textArea.setLabel(new Model<String>(labelText));
    }
    add(panel);
    return panel;
  }

  /**
   * @return The Wicket id of the embedded text field of TextAreaPanel
   */
  public final String getTextAreaId()
  {
    return TextAreaPanel.WICKET_ID;
  }

  /**
   * @param id
   * @param label
   * @param model
   * @param values
   * @param renderer
   * @return The created DropDownChoicePanel.
   * @see DropDownChoicePanel#DropDownChoicePanel(String, String, IModel, List, IChoiceRenderer)
   */
  public <T> DropDownChoicePanel<T> addDropDownChoice(final IModel<T> model, final List< ? extends T> values,
      final IChoiceRenderer<T> renderer)
      {
    return addDropDownChoice(model, values, renderer, false);
      }

  /**
   * @param id
   * @param label
   * @param model
   * @param values
   * @param renderer
   * @param submitOnChange.
   * @return The created DropDownChoicePanel.
   * @see DropDownChoicePanel#DropDownChoicePanel(String, String, IModel, List, IChoiceRenderer, boolean))
   */
  public <T> DropDownChoicePanel<T> addDropDownChoice(final IModel<T> model, final List< ? extends T> values,
      final IChoiceRenderer<T> renderer, final boolean submitOnChange)
      {
    final DropDownChoicePanel<T> dropDownChoicePanel = new DropDownChoicePanel<T>(newChildId(), model, values, renderer, submitOnChange);
    dropDownChoicePanel.getDropDownChoice().setLabel(new Model<String>(getLabel()));
    add(dropDownChoicePanel);
    return dropDownChoicePanel;
      }

  /**
   * @param id
   * @param label
   * @param dropDownChoice
   * @return The created DropDownChoicePanel.
   * @see DropDownChoicePanel#DropDownChoicePanel(String, String, DropDownChoice)
   */
  public <T> DropDownChoicePanel<T> add(final DropDownChoice<T> dropDownChoice)
  {
    dropDownChoice.setLabel(new Model<String>(getLabel()));
    return add(dropDownChoice, false);
  }

  /**
   * @param id
   * @param label
   * @param dropDownChoice
   * @return The created DropDownChoicePanel.
   * @see DropDownChoicePanel#DropDownChoicePanel(String, String, DropDownChoice, boolean)
   */
  public <T> DropDownChoicePanel<T> add(final DropDownChoice<T> dropDownChoice, final boolean submitOnChange)
  {

    final DropDownChoicePanel<T> dropDownChoicePanel = new DropDownChoicePanel<T>(newChildId(), dropDownChoice, submitOnChange);
    dropDownChoicePanel.getDropDownChoice().setLabel(new Model<String>(getLabel()));
    add(dropDownChoicePanel);
    return dropDownChoicePanel;
  }

  /**
   * @return The Wicket id of the embedded text fiel of {@link DropDownChoicePanel}.
   */
  public String getDropDownChoiceId()
  {
    return DropDownChoicePanel.WICKET_ID;
  }

  /**
   * @param id
   * @param label
   * @param listChoice
   * @return The created ListMultipleChoicePanel.
   * @see ListMultipleChoicePanel#ListMultipleChoicePanel(String, String, ListMultipleChoice)
   */
  public <T> ListMultipleChoicePanel<T> add(final ListMultipleChoice<T> listChoice)
  {
    final ListMultipleChoicePanel<T> listChoicePanel = new ListMultipleChoicePanel<T>(newChildId(), listChoice);
    listChoicePanel.getListMultipleChoice().setLabel(new Model<String>(getLabel()));
    add(listChoicePanel);
    return listChoicePanel;
  }

  /**
   * @return The Wicket id of the embedded text fiel of {@link ListMultipleChoicePanel}.
   */
  public String getListChoiceId()
  {
    return ListMultipleChoicePanel.WICKET_ID;
  }

  /**
   * @param model
   * @param labelString
   * @return The created CheckBoxPanel.
   * @see CheckBoxPanel#CheckBoxPanel(String, IModel, String)
   */
  public CheckBoxPanel addCheckBox(final IModel<Boolean> model, final String labelString)
  {
    final CheckBoxPanel checkBoxPanel = new CheckBoxPanel(newChildId(), model, labelString);
    add(checkBoxPanel);
    return checkBoxPanel;
  }

  /**
   * Adds an alert icon at the top left corner of the field set label.
   * @param tooltip
   * @return this for chaining.
   */
  public FieldsetPanel addAlertIcon(final String tooltip)
  {
    final IconPanel icon = WicketUtils.getAlertTooltipIcon(this, tooltip);
    add(icon, FieldSetIconPosition.TOP_LEFT);
    return this;
  }

  /**
   * Adds a help icon at the top right corner of the field set.
   * @param tooltip
   * @return The created IconPanel.
   */
  public IconPanel addHelpIcon(final String tooltip)
  {
    return addHelpIcon(tooltip, FieldSetIconPosition.TOP_RIGHT);
  }

  /**
   * Adds a help icon at the top right corner of the field set.
   * @param tooltip
   * @return The created IconPanel.
   */
  public IconPanel addHelpIcon(final String tooltip, final FieldSetIconPosition iconPosition)
  {
    final IconPanel icon = new IconPanel(newChildId(), IconType.HELP, tooltip);
    add(icon, iconPosition);
    return icon;
  }

  /**
   * Adds a help icon at the top right corner of the field set.
   * @param tooltip
   * @return The created IconPanel.
   */
  public IconPanel addHelpIcon(final IModel<String> tooltip)
  {
    final IconPanel icon = new IconPanel(newChildId(), IconType.HELP, tooltip);
    add(icon, FieldSetIconPosition.TOP_RIGHT);
    return icon;
  }

  /**
   * Adds a keyboard icon at the bottom right corner of the field set.
   * @param tooltip
   * @return this for chaining.
   */
  public FieldsetPanel addKeyboardHelpIcon(final String tooltip)
  {
    return add(new IconPanel(newChildId(), IconType.KEYBOARD, tooltip), FieldSetIconPosition.BOTTOM_RIGHT);
  }

  /**
   * Adds a JIRA icon at the bottom right corner of the field set (only if JIRA is configured, otherwise this method does nothing). This
   * method is automatically called by {@link #addJIRAField()}.
   * @param tooltip
   * @return this for chaining.
   */
  public FieldsetPanel addJIRASupportHelpIcon()
  {
    if (WicketUtils.isJIRAConfigured() == true) {
      return add(WicketUtils.getJIRASupportTooltipIcon(this), FieldSetIconPosition.TOP_RIGHT);
    } else {
      // No JIRA configured.
      return this;
    }
  }

  /**
   * Adds a JIRA icon at the bottom right corner of the field set (only if JIRA is configured, otherwise this method does nothing).
   * @param tooltip
   * @return this for chaining.
   */
  public FieldsetPanel addJIRAField(final IModel<String> model)
  {
    if (WicketUtils.isJIRAConfigured() == true) {
      add(new JiraIssuesPanel(newChildId(), model));
      add(WicketUtils.getJIRASupportTooltipIcon(this), FieldSetIconPosition.TOP_RIGHT);
    }
    return this;
  }

  public FieldsetPanel add(final IconPanel icon, final FieldSetIconPosition iconPosition)
  {
    icon.getDiv().add(AttributeModifier.append("style", iconPosition.getStyleAttrValue()));
    add(icon);
    return this;
  }

  private void checkLabelFor(final Component... components)
  {
    if (labelFor == true) {
      return;
    }
    final Component component = components[0];
    if (component instanceof ComponentWrapperPanel) {
      this.label.add(AttributeModifier.replace("for", ((ComponentWrapperPanel) component).getComponentOutputId()));
      labelFor = true;
    }
    for (final Component comp : components) {
      if (comp instanceof FormComponent) {
        ((FormComponent< ? >) comp).setLabel(new Model<String>(getLabel()));
      }
    }
  }

  /**
   * Creates and add a new RepeatingView as div-child if not already exist.
   * @see RepeatingView#newChildId()
   */
  public String newChildId()
  {
    if (multipleChildren == true) {
      if (repeater == null) {
        fieldDiv = new DivPanel(FIELDS_ID, DivType.FIELD_DIV);
        div.add(fieldDiv);
        repeater = new RepeatingView(DivPanel.CHILD_ID);
        fieldDiv.add(repeater);
      }
      return repeater.newChildId();
    } else {
      return FIELDS_ID;
    }
  }

  public FieldsetPanel setDivStyle(final DivType divType)
  {
    this.div.add(AttributeModifier.append("class", divType.getClassAttrValue()));
    return this;
  }

  public DivPanel addNewCheckBoxDiv()
  {
    final DivPanel checkBoxDiv = new DivPanel(newChildId(), DivType.CHECKBOX);
    add(checkBoxDiv);
    return checkBoxDiv;
  }

  public DivPanel addNewRadioBoxDiv()
  {
    final DivPanel radioBoxDiv = new DivPanel(newChildId(), DivType.RADIOBOX);
    add(radioBoxDiv);
    return radioBoxDiv;
  }

  /**
   * @see org.apache.wicket.Component#onBeforeRender()
   */
  @Override
  protected void onBeforeRender()
  {
    if (rendered == false) {
      // The first time of rendering this component, so do the rest before:
      rendered = true;
      if (labelSide == true) {
        fieldset.add(AttributeModifier.append("class", "label_side"));
      }
      if (labelSuffix == null) {
        label.add(labelSuffix = WicketUtils.getInvisibleComponent("labelSuffix"));
      }
      if (descriptionSuffix == null) {
        label.add(descriptionSuffix = WicketUtils.getInvisibleComponent("descriptionSuffix"));
      }
      if (childAdded == false) {
        childAdded = true;
        div.add(WicketUtils.getInvisibleComponent(FIELDS_ID));
      }
    }
    if (labelFor == false && WebConfiguration.isDevelopmentMode() == true) {
      log.warn("No label set for field '" + labelText + "'. Please call setLabelFor(component) for this fieldset.");
    }
    super.onBeforeRender();
  }
}