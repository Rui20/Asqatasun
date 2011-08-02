package org.opens.tanaguru.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opens.tanaguru.entity.audit.Audit;
import org.opens.tanaguru.entity.audit.EvidenceElement;
import org.opens.tanaguru.entity.audit.ProcessRemark;
import org.opens.tanaguru.entity.audit.ProcessResult;
import org.opens.tanaguru.entity.parameterization.Parameter;
import org.opens.tanaguru.entity.parameterization.ParameterElement;
import org.opens.tanaguru.entity.service.audit.AuditDataService;
import org.opens.tanaguru.entity.service.audit.ProcessRemarkDataService;
import org.opens.tanaguru.entity.service.audit.ProcessResultDataService;
import org.opens.tanaguru.entity.service.parameterization.ParameterDataService;
import org.opens.tanaguru.entity.service.parameterization.ParameterElementDataService;
import org.opens.tanaguru.entity.service.subject.WebResourceDataService;
import org.opens.tanaguru.entity.subject.Site;
import org.opens.tanaguru.entity.subject.WebResource;
import org.opens.tanaguru.service.AnalyserService;
import org.opens.tanaguru.service.AuditService;
import org.opens.tanaguru.service.AuditServiceListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * This class launches Tanaguru with urls passed as arguments by the user.
 *
 * @author jkowalczyk
 */
public class Tanaguru implements AuditServiceListener {

    private static final String APPLICATION_CONTEXT_FILE_PATH = "conf/context/application-context.xml";
    private AuditService auditService = null;
    private AnalyserService analyserService = null;
    private AuditDataService auditDataService = null;
    private WebResourceDataService webResourceDataService = null;
    private ProcessResultDataService processResultDataService = null;
    private ProcessRemarkDataService processRemarkDataService = null;
    private ParameterDataService parameterDataService = null;
    private ParameterElementDataService parameterElementDataService = null;
    
    public static void main(String[] args) {
        if (args != null && args.length == 2 && args[0] != null && args[1] != null) {
            new Tanaguru().run(args[0], args[1]);
        }
    }

    public Tanaguru() {
        super();
    }

    public void run(String urlTab, String tanaguruHome) {
        ApplicationContext springApplicationContext = new FileSystemXmlApplicationContext(tanaguruHome+"/"+APPLICATION_CONTEXT_FILE_PATH);
        BeanFactory springBeanFactory = springApplicationContext;
        auditService = (AuditService) springBeanFactory.getBean("auditService");
        auditDataService = (AuditDataService) springBeanFactory.getBean("auditDataService");
        webResourceDataService = (WebResourceDataService) springBeanFactory.getBean("webResourceDataService");
        analyserService = (AnalyserService) springBeanFactory.getBean("analyserService");
        processResultDataService = (ProcessResultDataService) springBeanFactory.getBean("processResultDataService");
        processRemarkDataService = (ProcessRemarkDataService) springBeanFactory.getBean("processRemarkDataService");
        parameterDataService = (ParameterDataService) springBeanFactory.getBean("parameterDataService");
        parameterElementDataService = (ParameterElementDataService) springBeanFactory.getBean("parameterElementDataService");
        auditService.add(this);
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(tanaguruHome+"/"+"tests/tests.properties"));
        } catch (IOException ex) {
            Logger.getLogger(Tanaguru.class.getName()).log(Level.SEVERE, null, ex);
        }
        Set<Parameter> paramSet = parameterDataService.getDefaultParameterSet();
        String[] pageUrlTab = urlTab.split(";");
        List<String> pageUrlList = new ArrayList<String>();
        pageUrlList.addAll(Arrays.asList(pageUrlTab));
        if (pageUrlList.size() > 1) {
            auditService.auditSite("site:" + pageUrlList.get(0), pageUrlList, paramSet);
        } else {
            auditService.auditPage(pageUrlList.get(0), paramSet);
        }
    }

    @Override
    public void auditCompleted(Audit audit) {
        audit = auditDataService.getAuditWithWebResource(audit.getId());
        List<ProcessResult> processResultList = (List<ProcessResult>) processResultDataService.getNetResultFromAudit(audit);
        System.out.println("Audit terminated with success at " + audit.getDateOfCreation());
        System.out.println("");
        System.out.println("Audit Mark : " + Float.valueOf(analyserService.analyse(processResultList)).intValue() + "%");
        if (audit.getSubject() instanceof Site) {
            int numberOfChildWebResource = webResourceDataService.getNumberOfChildWebResource(audit.getSubject()).intValue();
            for (int i = 0; i < numberOfChildWebResource; i++) {
                displayWebResourceResult(webResourceDataService.getWebResourceFromItsParent(audit.getSubject(), i, 1).iterator().next(),
                        processResultList);
            }
        } else {
            displayWebResourceResult(audit.getSubject(), processResultList);
        }
        System.out.println("");
        System.exit(0);
    }

    private void displayWebResourceResult(WebResource wr, List<ProcessResult> processResultList) {
        System.out.println("");
        System.out.println("Subject : " + wr.getURL());
        List<ProcessResult> prList = new ArrayList<ProcessResult>();
        for (ProcessResult netResult : processResultList) {
            if (netResult.getSubject().getURL().equalsIgnoreCase(wr.getURL())) {
                prList.add(netResult);
            }
        }
        System.out.println("Mark : " + Float.valueOf(analyserService.analyse(prList)).intValue() + "%");
        for (ProcessResult result : prList) {
            System.out.println(result.getTest().getCode() + ": " + result.getValue());
            Set<ProcessRemark> processRemarkList = (Set<ProcessRemark>) processRemarkDataService.findAllByProcessResult(result);
            for (ProcessRemark processRemark : processRemarkList) {
                System.out.println(" ->  " + processRemark.getIssue()
                        + " " + processRemark.getMessageCode());
                for (EvidenceElement el : processRemark.getElementList()) {
                    System.out.println("    -> " +el.getEvidence().getCode() + ":"+ el.getValue());
                }
            }
        }
    }

    @Override
    public void auditCrashed(Audit audit, Throwable exception) {
        exception.printStackTrace();
        System.out.println("crash (id+message): " + audit.getId() + " " + exception.fillInStackTrace());
    }

    /**
     * The default parameter set embeds a depth value that corresponds to the
     * site audit. We need here to replace this parameter by a parameter value
     * equals to 0.
     * @return
     */
    private Set<Parameter> getAuditPageParameterSet() {
         ParameterElement parameterElement = parameterElementDataService.getParameterElement("DEPTH");
         Parameter depthParameter = parameterDataService.getParameter(parameterElement, "0");
         Set<Parameter> auditPageParamSet = parameterDataService.updateParameter(
                 parameterDataService.getDefaultParameterSet(), depthParameter);
         return auditPageParamSet;
    }

}