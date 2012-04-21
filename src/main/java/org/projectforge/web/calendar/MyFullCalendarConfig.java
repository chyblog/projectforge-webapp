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

package org.projectforge.web.calendar;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.ftlines.wicket.fullcalendar.Config;

import org.apache.wicket.Component;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.projectforge.user.PFUserContext;

public class MyFullCalendarConfig extends Config
{
  private static final long serialVersionUID = 6825903475480593064L;

  private String weekMode = "liquid";

  private String allDayText, axisFormat;

  private String[] dayNames, dayNamesShort, monthNames, monthNamesShort;

  private boolean theme = true;

  private Integer year, month, dayOfMonth;

  @JsonProperty
  private final Map<ColumnFormat, String> titleFormat = new HashMap<Config.ColumnFormat, String>();

  private final Component parent;

  /**
   * @param parent Used for localization.
   * @see Component#getString(String)
   * @see Component#getLocale()
   */
  public MyFullCalendarConfig(final Component parent)
  {
    this.parent = parent;
    // setAspectRatio(1.5f);
    setSlotMinutes(15);
    setFirstHour(8);
    getHeader().setLeft("prev,next today");
    getHeader().setCenter("title");
    getHeader().setRight("month,agendaWeek,agendaDay");

    getButtonText().setToday(getString("calendar.today"));
    getButtonText().setWeek(getString("calendar.week"));
    getButtonText().setMonth(getString("calendar.month"));
    getButtonText().setDay(getString("calendar.day"));
    setAllDayText(getString("calendar.allday"));
    setAxisFormat(getString("calendar.format.axisFormat"));
    final DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(parent.getLocale());
    setDayNames(convert(dateFormatSymbols.getWeekdays()));
    setDayNamesShort(convert(dateFormatSymbols.getShortWeekdays()));
    setMonthNames(dateFormatSymbols.getMonths());
    setTitleFormatDay(getString("calendar.format.titleFormat.day"));
    setTitleFormatMonth(getString("calendar.format.titleFormat.month"));
    setTitleFormatWeek(getString("calendar.format.titleFormat.week"));
    setColumnFormatDay(getString("calendar.format.columnFormat.day"));
    setColumnFormatMonth(getString("calendar.format.columnFormat.month"));
    setColumnFormatWeek(getString("calendar.format.columnFormat.week"));
    setTimeFormat(getString("calendar.format.timeFormat"));
  }

  /**
   * DateFormatSymbols uses index {@link Calendar#SUNDAY}, {@link Calendar#MONDAY}, ... So the first entry is empty and there are 8 entries.
   * @param dateFormatSymbolsArray
   * @return The given array without the first element.
   */
  private String[] convert(final String[] dateFormatSymbolsArray)
  {
    return Arrays.copyOfRange(dateFormatSymbolsArray, 1, 8);
  }

  /**
   * @return the firstDay
   */
  public int getFirstDay()
  {
    return PFUserContext.getFirstDayOfWeek() - 1;
  }

  /**
   * @return the weekMode
   */
  public String getWeekMode()
  {
    return weekMode;
  }

  /**
   * @param weekMode the weekMode to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setWeekMode(final String weekMode)
  {
    this.weekMode = weekMode;
    return this;
  }

  /**
   * @return the theme
   */
  public boolean isTheme()
  {
    return theme;
  }

  /**
   * @param theme the theme to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setTheme(final boolean theme)
  {
    this.theme = theme;
    return this;
  }

  /**
   * @return the year
   */
  public Integer getYear()
  {
    return year;
  }

  /**
   * @param year the year to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setYear(final Integer year)
  {
    this.year = year;
    return this;
  }

  /**
   * @return the month
   */
  public Integer getMonth()
  {
    return month;
  }

  /**
   * @param month the month to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setMonth(final Integer month)
  {
    this.month = month;
    return this;
  }

  /**
   * @return the dayOfMonth
   */
  public Integer getDayOfMonth()
  {
    return dayOfMonth;
  }

  /**
   * @param dayOfMonth the dayOfMonth to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setDayOfMonth(final Integer dayOfMonth)
  {
    this.dayOfMonth = dayOfMonth;
    return this;
  }

  private String getString(final String key)
  {
    return parent.getString(key);
  }

  /**
   * @return the allDayText
   */
  public String getAllDayText()
  {
    return allDayText;
  }

  /**
   * @param allDayText the allDayText to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setAllDayText(final String allDayText)
  {
    this.allDayText = allDayText;
    return this;
  }

  /**
   * Determines the time-text that will be displayed on the vertical axis of the agenda views.
   * @return the axisFormat
   */
  public String getAxisFormat()
  {
    return axisFormat;
  }

  /**
   * @param axisFormat the axisFormat to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setAxisFormat(final String axisFormat)
  {
    this.axisFormat = axisFormat;
    return this;
  }

  /**
   * Full names of days-of-week.
   * @return the dayNames
   */
  public String[] getDayNames()
  {
    return dayNames;
  }

  /**
   * @param dayNames the dayNames to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setDayNames(final String[] dayNames)
  {
    this.dayNames = dayNames;
    return this;
  }

  /**
   * Abbreviated names of days-of-week.
   * @return the dayNamesShort
   */
  public String[] getDayNamesShort()
  {
    return dayNamesShort;
  }

  /**
   * @param dayNamesShort the dayNamesShort to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setDayNamesShort(final String[] dayNamesShort)
  {
    this.dayNamesShort = dayNamesShort;
    return this;
  }

  /**
   * Full names of months.
   * @return the monthNames
   */
  public String[] getMonthNames()
  {
    return monthNames;
  }

  /**
   * @param monthNames the monthNames to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setMonthNames(final String[] monthNames)
  {
    this.monthNames = monthNames;
    return this;
  }

  /**
   * Abbreviated names of months.
   * @return the monthNamesShort
   */
  public String[] getMonthNamesShort()
  {
    return monthNamesShort;
  }

  /**
   * @param monthNamesShort the monthNamesShort to set
   * @return this for chaining.
   */
  public MyFullCalendarConfig setMonthNamesShort(final String[] monthNamesShort)
  {
    this.monthNamesShort = monthNamesShort;
    return this;
  }

  @JsonIgnore
  public String getTitleFormatDay()
  {
    return titleFormat.get(ColumnFormat.day);
  }

  public void setTitleFormatDay(final String format)
  {
    titleFormat.put(ColumnFormat.day, format);
  }

  @JsonIgnore
  public String getTitleFormatWeek()
  {
    return titleFormat.get(ColumnFormat.week);
  }

  public void setTitleFormatWeek(final String format)
  {
    titleFormat.put(ColumnFormat.week, format);
  }

  @JsonIgnore
  public String getTitleFormatMonth()
  {
    return titleFormat.get(ColumnFormat.month);
  }

  public void setTitleFormatMonth(final String format)
  {
    titleFormat.put(ColumnFormat.month, format);
  }

}