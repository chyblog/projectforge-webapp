/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2013 Kai Reinhard (k.reinhard@micromata.de)
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

package org.projectforge.web.core;

import java.math.BigDecimal;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.IConverter;
import org.projectforge.common.NumberHelper;
import org.projectforge.core.ConfigurationDO;
import org.projectforge.core.ConfigurationType;
import org.projectforge.task.TaskDO;
import org.projectforge.task.TaskDao;
import org.projectforge.web.task.TaskSelectPanel;
import org.projectforge.web.wicket.AbstractEditForm;
import org.projectforge.web.wicket.components.MaxLengthTextArea;
import org.projectforge.web.wicket.components.MaxLengthTextField;
import org.projectforge.web.wicket.components.MinMaxNumberField;
import org.projectforge.web.wicket.components.TimeZonePanel;
import org.projectforge.web.wicket.converter.BigDecimalPercentConverter;
import org.projectforge.web.wicket.flowlayout.DivTextPanel;
import org.projectforge.web.wicket.flowlayout.FieldsetPanel;
import org.projectforge.web.wicket.flowlayout.InputPanel;
import org.projectforge.web.wicket.flowlayout.TextAreaPanel;

public class ConfigurationEditForm extends AbstractEditForm<ConfigurationDO, ConfigurationEditPage>
{
  public ConfigurationEditForm(final ConfigurationEditPage parentPage, final ConfigurationDO data)
  {
    super(parentPage, data);
  }

  private static final long serialVersionUID = 6156899763199729949L;

  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ConfigurationEditForm.class);

  private TaskDO task;

  @SpringBean(name = "taskDao")
  private TaskDao taskDao;

  @Override
  @SuppressWarnings("serial")
  protected void init()
  {
    super.init();
    gridBuilder.newGridPanel();
    {
      // Parameter name
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.configuration.parameter")).supressLabelForWarning();
      fs.add(new DivTextPanel(fs.newChildId(), getString(data.getI18nKey())));
    }
    {
      // Parameter value
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.configuration.value"));
      if (data.getConfigurationType() == ConfigurationType.INTEGER) {
        fs.add(new TextField<Integer>(InputPanel.WICKET_ID, new PropertyModel<Integer>(data, "intValue")));
      } else if (data.getConfigurationType() == ConfigurationType.PERCENT) {
        fs.add(new MinMaxNumberField<BigDecimal>(InputPanel.WICKET_ID, new PropertyModel<BigDecimal>(data, "floatValue"), BigDecimal.ZERO,
            NumberHelper.HUNDRED) {
          /**
           * @see org.projectforge.web.wicket.components.MinMaxNumberField#getConverter(java.lang.Class)
           */
          @SuppressWarnings({ "rawtypes", "unchecked"})
          @Override
          public IConverter getConverter(final Class type)
          {
            return new BigDecimalPercentConverter(true);
          };
        });
      } else if (data.getConfigurationType() == ConfigurationType.STRING) {
        fs.add(new MaxLengthTextField(InputPanel.WICKET_ID, new PropertyModel<String>(data, "stringValue")));
      } else if (data.getConfigurationType() == ConfigurationType.TEXT) {
        fs.add(new MaxLengthTextArea(TextAreaPanel.WICKET_ID, new PropertyModel<String>(data, "stringValue")));
      } else if (data.getConfigurationType() == ConfigurationType.BOOLEAN) {
        fs.addCheckBox(new PropertyModel<Boolean>(data, "booleanValue"), null);
      } else if (data.getConfigurationType() == ConfigurationType.TIME_ZONE) {
        fs.add(new TimeZonePanel(fs.newChildId(), new PropertyModel<TimeZone>(data, "timeZone")));
      } else if (data.getConfigurationType() == ConfigurationType.TASK) {
        if (data.getTaskId() != null) {
          this.task = taskDao.getById(data.getTaskId());
        }
        final TaskSelectPanel taskSelectPanel = new TaskSelectPanel(fs.newChildId(), new PropertyModel<TaskDO>(this, "task"), parentPage,
            "taskId");
        fs.add(taskSelectPanel);
        taskSelectPanel.init();
      } else {
        throw new UnsupportedOperationException("Parameter of type '" + data.getConfigurationType() + "' not supported.");
      }
    }
    {
      // Description
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("description")).supressLabelForWarning();
      fs.add(new DivTextPanel(fs.newChildId(), getString("administration.configuration.param." + data.getParameter() + ".description")));
    }
  }

  public TaskDO getTask()
  {
    return task;
  }

  public void setTask(final TaskDO task)
  {
    this.task = task;
    if (task != null) {
      data.setTaskId(task.getId());
    } else {
      data.setTaskId(null);
    }
  }

  public void setTask(final Integer taskId)
  {
    if (taskId != null) {
      setTask(taskDao.getById(taskId));
    } else {
      setTask((TaskDO) null);
    }
  }

  @Override
  protected Logger getLogger()
  {
    return log;
  }
}
