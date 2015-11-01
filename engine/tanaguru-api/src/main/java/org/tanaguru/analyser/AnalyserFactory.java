/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2015  Tanaguru.org
 *
 * This file is part of Tanaguru.
 *
 * Tanaguru is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: asqatasun AT asqatasun DOT org
 */
package org.tanaguru.analyser;

import java.util.List;
import org.tanaguru.entity.audit.Audit;
import org.tanaguru.entity.audit.ProcessResult;
import org.tanaguru.entity.subject.WebResource;

/**
 *
 * @author jkowalczyk
 */
public interface AnalyserFactory {

    /**
     *
     * @param netResultList
     * @return
     */
    Analyser create(List<ProcessResult> netResultList);

    /**
     * 
     * @param webResource
     * @param audit
     * @return
     */
    Analyser create(WebResource webResource, Audit audit);

}
