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

package org.projectforge.web.fibu;

import org.apache.log4j.Logger;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.projectforge.fibu.kost.Kost2ArtDO;
import org.projectforge.fibu.kost.Kost2ArtDao;
import org.projectforge.web.wicket.AbstractEditPage;
import org.projectforge.web.wicket.EditPage;


@EditPage(defaultReturnPage = Kost2ArtListPage.class)
public class Kost2ArtEditPage extends AbstractEditPage<Kost2ArtDO, Kost2ArtEditForm, Kost2ArtDao>
{
  private static final long serialVersionUID = -6455634137929376315L;

  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Kost2ArtEditPage.class);

  @SpringBean(name = "kost2ArtDao")
  private Kost2ArtDao kost2ArtDao;

  public Kost2ArtEditPage(final PageParameters parameters)
  {
    super(parameters, "fibu.kost2art");
    init();
  }

  @Override
  protected Kost2ArtDao getBaseDao()
  {
    return kost2ArtDao;
  }

  @Override
  protected Kost2ArtEditForm newEditForm(final AbstractEditPage< ? , ? , ? > parentPage, final Kost2ArtDO data)
  {
    return new Kost2ArtEditForm(this, data);
  }

  @Override
  protected Logger getLogger()
  {
    return log;
  }
}
