/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2014  Open-S Company
 *
 * This program is free software: you can redistribute it and/or modify
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
 * Contact us by mail: open-s AT open-s DOT com
 */
package org.opens.tanaguru.rules.rgaa30;

import org.opens.tanaguru.entity.audit.TestSolution;
import org.opens.tanaguru.rules.rgaa30.test.Rgaa30RuleImplementationTestCase;

/**
 * Unit test class for the implementation of the rule 3-2-3 of the referential Rgaa30Name.
 *
 * @author
 */
public class Rgaa30Rule030203Test extends Rgaa30RuleImplementationTestCase {

    /**
     * Default constructor
     */
    public Rgaa30Rule030203Test (String testName){
        super(testName);
    }

    @Override
    protected void setUpRuleImplementationClassName() {
        setRuleImplementationClassName(
                "org.opens.tanaguru.rules.rgaa30.Rgaa30Rule030203");
    }

    @Override
    protected void setUpWebResourceMap() {
//        getWebResourceMap().put("Rgaa30.Test.3-2-3-1Passed-01",
//              getWebResourceFactory().createPage(
//              getTestcasesFilePath() + "rgaa30/Rgaa30Rule030203/Rgaa30.Test.3.2.3-1Passed-01.html"));
//        getWebResourceMap().put("Rgaa30.Test.3-2-3-2Failed-01",
//              getWebResourceFactory().createPage(
//              getTestcasesFilePath() + "rgaa30/Rgaa30Rule030203/Rgaa30.Test.3.2.3-2Failed-01.html"));
        getWebResourceMap().put("Rgaa30.Test.3-2-3-3NMI-01",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa30/Rgaa30Rule030203/Rgaa30.Test.3.2.3-3NMI-01.html"));
//        getWebResourceMap().put("Rgaa30.Test.3-2-3-4NA-01",
//              getWebResourceFactory().createPage(
//              getTestcasesFilePath() + "rgaa30/Rgaa30Rule030203/Rgaa30.Test.3.2.3-4NA-01.html"));
    }

    @Override
    protected void setProcess() {
//        assertEquals(TestSolution.PASSED,
//                processPageTest("Rgaa30.Test.3-2-3-1Passed-01").getValue());
//        assertEquals(TestSolution.FAILED,
//                processPageTest("Rgaa30.Test.3-2-3-2Failed-01").getValue());
        assertEquals(TestSolution.NOT_TESTED,
                processPageTest("Rgaa30.Test.3-2-3-3NMI-01").getValue());
//        assertEquals(TestSolution.NOT_APPLICABLE,
//                processPageTest("Rgaa30.Test.3-2-3-4NA-01").getValue());
    }

    @Override
    protected void setConsolidate() {
//        assertEquals(TestSolution.PASSED,
//                consolidate("Rgaa30.Test.3-2-3-1Passed-01").getValue());
//        assertEquals(TestSolution.FAILED,
//                consolidate("Rgaa30.Test.3-2-3-2Failed-01").getValue());
        assertEquals(TestSolution.NOT_TESTED,
                consolidate("Rgaa30.Test.3-2-3-3NMI-01").getValue());
//        assertEquals(TestSolution.NOT_APPLICABLE,
//                consolidate("Rgaa30.Test.3-2-3-4NA-01").getValue());
    }

}