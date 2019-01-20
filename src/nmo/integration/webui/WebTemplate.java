package nmo.integration.webui;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class WebTemplate
{
	public static final Configuration PARSER = new Configuration(Configuration.VERSION_2_3_23);
	static
	{
		PARSER.setClassForTemplateLoading(WebTemplate.class, "/resources/ftl");
		PARSER.setDefaultEncoding("UTF-8");
		PARSER.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		PARSER.setLogTemplateExceptions(false);
		PARSER.setDateFormat("dd MMM yyyy");
		PARSER.setTimeFormat("hh:mm a");
		PARSER.setDateTimeFormat("dd MMM yyyy, hh:mm a");
		PARSER.setLocale(Locale.UK);
	}

	public static final void renderTemplate(String template, HttpServletResponse response, Object model) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException
	{
		StringWriter writer = new StringWriter();
		PARSER.getTemplate(template).process(model, writer);
		response.getWriter().append(writer.toString());
	}

	public static final void renderTemplate(String template, HttpServletResponse response, Object model, Writer writer) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException
	{
		StringWriter writer_ = new StringWriter();
		PARSER.getTemplate(template).process(model, writer_);
		writer.append(writer_.toString());
	}

	public static final String renderBody(String template, Object model) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException
	{
		StringWriter writer = new StringWriter();
		PARSER.getTemplate(template).process(model, writer);
		return writer.toString();
	}
}
