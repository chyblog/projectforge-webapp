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

package org.projectforge.plugins.teamcal.event;

import org.apache.commons.lang.ObjectUtils;
import org.projectforge.plugins.teamcal.admin.TeamCalDO;
import org.projectforge.plugins.teamcal.admin.TeamCalRight;
import org.projectforge.user.PFUserDO;
import org.projectforge.user.UserRightAccessCheck;
import org.projectforge.user.UserRightCategory;
import org.projectforge.user.UserRightValue;
import org.projectforge.user.UserRights;

/**
 * Every user has access to own to-do's or to-do's he's assigned to. All other users have access if the to-do is assigned to a task and the
 * user has the task access.
 * @author Kai Reinhard (k.reinhard@me.de)
 * 
 */
public class TeamEventRight extends UserRightAccessCheck<TeamEventDO>
{
  private static final long serialVersionUID = 4076952301071024285L;

  private final TeamCalRight teamCalRight = new TeamCalRight();

  public TeamEventRight()
  {
    super(TeamEventDao.USER_RIGHT_ID, UserRightCategory.PLUGINS, UserRightValue.TRUE);
  }

  /**
   * General select access.
   * @return true
   * @see org.projectforge.user.UserRightAccessCheck#hasSelectAccess(org.projectforge.user.PFUserDO)
   */
  @Override
  public boolean hasSelectAccess(final PFUserDO user)
  {
    return true;
  }

  /**
   * @return true if user is assignee or reporter. If not, the task access is checked.
   * @see org.projectforge.user.UserRightAccessCheck#hasSelectAccess(org.projectforge.user.PFUserDO, java.lang.Object)
   */
  @Override
  public boolean hasSelectAccess(final PFUserDO user, final TeamEventDO obj)
  {
    final TeamCalDO calendar = obj.getCalendar();
    if (calendar == null) {
      return false;
    }
    if (ObjectUtils.equals(user.getId(), calendar.getOwnerId()) == true) {
      // User has full access to it's own calendars.
      return true;
    }
    final Integer userId = user.getId();
    if (teamCalRight.hasFullAccess(calendar, userId) == true || teamCalRight.hasReadonlyAccess(calendar, userId) == true) {
      return true;
    } else if (teamCalRight.hasMinimalAccess(calendar, userId) == true) {
      // Clear fields for users with minimal access.
      obj.clearFields();
      return true;
    }
    return false;
  }

  /**
   * General insert access.
   * @return true
   * @see org.projectforge.user.UserRightAccessCheck#hasInsertAccess(org.projectforge.user.PFUserDO)
   */
  @Override
  public boolean hasInsertAccess(final PFUserDO user)
  {
    return true;
  }

  /**
   * Same as {@link #hasUpdateAccess(PFUserDO, TeamEventDO, TeamEventDO)}
   * @see org.projectforge.user.UserRightAccessCheck#hasInsertAccess(org.projectforge.user.PFUserDO, java.lang.Object)
   */
  @Override
  public boolean hasInsertAccess(final PFUserDO user, final TeamEventDO obj)
  {
    return hasUpdateAccess(user, obj, null);
  }

  /**
   * Same as {@link #hasUpdateAccess(PFUserDO, TeamEventDO, TeamEventDO)}
   * @see org.projectforge.user.UserRightAccessCheck#hasDeleteAccess(org.projectforge.user.PFUserDO, java.lang.Object)
   */
  @Override
  public boolean hasDeleteAccess(final PFUserDO user, final TeamEventDO obj)
  {
    return hasUpdateAccess(user, obj, null);
  }

  /**
   * Owners of the given calendar and users with full access hav update access to the given calendar: obj.getCalendar().
   * @see org.projectforge.user.UserRightAccessCheck#hasUpdateAccess(org.projectforge.user.PFUserDO, java.lang.Object, java.lang.Object)
   */
  @Override
  public boolean hasUpdateAccess(final PFUserDO user, final TeamEventDO obj, final TeamEventDO oldObj)
  {
    if (obj == null) {
      return false;
    }
    final TeamCalDO calendar = obj.getCalendar();
    if (calendar == null) {
      return false;
    }
    return hasUpdateAccess(user, calendar);
  }

  public boolean hasUpdateAccess(final PFUserDO user, final TeamCalDO calendar) {
    if (ObjectUtils.equals(user.getId(), calendar.getOwnerId()) == true) {
      // User has full access to it's own calendars.
      return true;
    }
    final Integer userId = user.getId();
    if (teamCalRight.hasFullAccess(calendar, userId) == true || UserRights.getAccessChecker().isDemoUser() == true) {
      return true;
    }
    return false;
  }

  /**
   * Owners of the given calendar and users with full and read-only access have update access to the given calendar: obj.getCalendar().
   * @see org.projectforge.user.UserRightAccessCheck#hasHistoryAccess(org.projectforge.user.PFUserDO, java.lang.Object)
   */
  @Override
  public boolean hasHistoryAccess(final PFUserDO user, final TeamEventDO obj)
  {
    if (obj == null) {
      return true;
    }
    final TeamCalDO calendar = obj.getCalendar();
    if (calendar == null) {
      return false;
    }
    if (ObjectUtils.equals(user.getId(), calendar.getOwnerId()) == true) {
      // User has full access to it's own calendars.
      return true;
    }
    final Integer userId = user.getId();
    if (teamCalRight.hasFullAccess(calendar, userId) == true || teamCalRight.hasReadonlyAccess(calendar, userId) == true) {
      return true;
    }
    return false;
  }

  public boolean hasMinimalAccess(final TeamEventDO event, final Integer userId) {
    if (event.getCalendar() == null) {
      return true;
    }
    return teamCalRight.hasMinimalAccess(event.getCalendar(), userId);
  }
}
