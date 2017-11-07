package com.iobeam.portal.ui.web.taglib.billing;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.iobeam.portal.model.billing.BillingPeriod;

/**
 * this tag renders the current billing period based
 * on the system date and time.
 * @property - either startdate or enddate, which field in billing
 * period to display.
 */
public class CurrentBillingPeriodTag extends TagSupport {

	private static final String START_DATE = "startdate";
	private static final String END_DATE = "enddate";

	private String mProperty;
	private static final SimpleDateFormat cFormat = 
		new SimpleDateFormat("MM/dd/yyyy");

	
	/**
	 * sets the property to display.
	 * either 'startdate' or 'enddate'
	 * default is 'enddate'
	 */
	public void setProperty(String property) {
		mProperty = property;
	}

	public String getProperty() {
		return mProperty;
	}

	/**
	 * renders current billing period based on system date.
	 */
	public int doStartTag() throws JspException {
		Date currentDate = new Date();
		BillingPeriod period = null;

		StringBuffer results = new StringBuffer(0);

		try {
			period = BillingPeriod.getInstanceFor(currentDate);
			if (START_DATE.equals(mProperty)) {
				results.append(cFormat.format(period.getStartDate()));
			}
			else {
				results.append(cFormat.format(period.getEndDate()));
			}
			write(pageContext, results.toString());
		}
		catch (Exception e) {
			throw new JspException(e);
		}
					
		return EVAL_BODY_INCLUDE;
	}

	private void write(PageContext context, String buffer) 
			throws Exception {

		JspWriter writer = context.getOut();
		writer.print(buffer);

	}

}



