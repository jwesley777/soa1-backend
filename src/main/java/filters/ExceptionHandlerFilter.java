package filters;

import dto.ExceptionDTO;
import exceptions.EntityIsNotValidException;
import exceptions.XmlParseException;
import org.hibernate.exception.ConstraintViolationException;
import xml.JavaToXmlConverter;

import javax.persistence.NoResultException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "ExceptionHandlerFilter", urlPatterns = {"/tickets/*", "/persons/*",
        "/coordinates/*", "/locations/*", "/additional"})
public class ExceptionHandlerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ConstraintViolationException | EntityIsNotValidException | XmlParseException | NumberFormatException e) {
            JavaToXmlConverter javaToXml = new JavaToXmlConverter();
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(400);
            ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
            response.getWriter().write(javaToXml.exceprionToXML(exceptionDTO));
        } catch (NoResultException e) {
            JavaToXmlConverter javaToXml = new JavaToXmlConverter();
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(404);
            ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
            response.getWriter().write(javaToXml.exceprionToXML(exceptionDTO));
        } catch (Exception e) {
            JavaToXmlConverter javaToXml = new JavaToXmlConverter();
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(500);
            ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
            e.printStackTrace();
            response.getWriter().write(javaToXml.exceprionToXML(exceptionDTO));
        }
    }
}
