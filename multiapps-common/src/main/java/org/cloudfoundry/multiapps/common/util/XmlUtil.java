package org.cloudfoundry.multiapps.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.SchemaFactory;

import org.cloudfoundry.multiapps.common.Messages;
import org.cloudfoundry.multiapps.common.ParsingException;
import org.cloudfoundry.multiapps.common.SLException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlUtil {

    private static final String EXTERNAL_GENERAL_ENTITIES_FEATURE = "http://xml.org/sax/features/external-general-entities";
    private static final String EXTERNAL_PARAMETER_ENTITIES_FEATURE = "http://xml.org/sax/features/external-parameter-entities";
    private static final String DISALLOW_DOCTYPE_DECLARATION_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

    private XmlUtil() {
    }

    public static <T> String toXml(T object) throws SLException {
        return toXml(object, false);
    }

    public static <T> String toXml(T object, Map<String, Object> properties) throws SLException {
        return toXml(object, true, properties);
    }

    public static <T> String toXml(T object, boolean formattedOutput) throws SLException {
        try {
            Writer buffer = new StringWriter();
            getMarshaller(getContext(object.getClass()), formattedOutput).marshal(object, buffer);

            return buffer.toString();
        } catch (JAXBException e) {
            handleMarshallingException(e);
            return null;
        }
    }

    public static <T> String toXml(T object, boolean formattedOutput, Map<String, Object> properties) throws SLException {
        try {
            Writer buffer = new StringWriter();
            getMarshaller(getContext(object.getClass()), properties).marshal(object, buffer);
            return buffer.toString();
        } catch (JAXBException e) {
            handleMarshallingException(e);
            return null;
        }
    }

    public static <T> T fromXml(InputStream xml, Class<T> clazz, URL schemaLocation) throws ParsingException {
        return fromXml(getAsDocument(xml), clazz, schemaLocation);
    }

    public static <T> T fromXml(InputStream xml, Class<T> clazz) throws ParsingException {
        return fromXml(xml, clazz, null);
    }

    public static <T> T fromXml(String xml, Class<T> clazz, URL schemaLocation) throws ParsingException {
        return fromXml(getAsDocument(xml), clazz, schemaLocation);
    }

    public static <T> T fromXml(String xml, Class<T> clazz) throws ParsingException {
        return fromXml(xml, clazz, null);
    }

    public static <T> T fromXml(Document xml, Class<T> clazz, URL schemaLocation) throws ParsingException {
        try {
            return getUnmarshaller(getContext(clazz), schemaLocation).unmarshal(xml, clazz)
                                                                     .getValue();
        } catch (SAXException | JAXBException e) {
            handleUnmarshallingException(e, schemaLocation);
            return null;
        }
    }

    private static <T> JAXBContext getContext(Class<T> clazz) throws JAXBException {
        return JAXBContext.newInstance(clazz);
    }

    private static Marshaller getMarshaller(JAXBContext context, boolean formattedOutput) {
        try {
            return formattedOutput ? getFormattingMarshaller(context) : getRegularMarshaller(context);
        } catch (JAXBException e) {
            throw new SLException(Messages.COULD_NOT_CREATE_JAXB_MARSHALLER, e);
        }
    }

    private static Marshaller getMarshaller(JAXBContext context, Map<String, Object> properties) {
        try {
            return properties.isEmpty() ? getRegularMarshaller(context) : getPropertyMarshaller(context, properties);
        } catch (JAXBException e) {
            throw new SLException(Messages.COULD_NOT_CREATE_JAXB_MARSHALLER, e);
        }
    }

    private static Marshaller getPropertyMarshaller(JAXBContext context, Map<String, Object> properties) throws JAXBException {
        Marshaller marshaller = getRegularMarshaller(context);
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            marshaller.setProperty(property.getKey(), property.getValue());
        }
        return marshaller;
    }

    private static Marshaller getFormattingMarshaller(JAXBContext context) throws JAXBException {
        Marshaller marshaller = getRegularMarshaller(context);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        return marshaller;
    }

    private static Marshaller getRegularMarshaller(JAXBContext context) throws JAXBException {
        return context.createMarshaller();
    }

    private static Document getAsDocument(InputStream xml) {
        return getAsDocument(new InputSource(xml));
    }

    private static Document getAsDocument(String xml) {
        return getAsDocument(new InputSource(new StringReader(xml)));
    }

    private static Document getAsDocument(InputSource xml) {
        try {
            return getDocumentBuilder().parse(xml);
        } catch (SAXException | IOException e) {
            throw new ParsingException(e, Messages.CANNOT_PARSE_XML_STREAM);
        }
    }

    private static DocumentBuilder getDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            factory.setFeature(DISALLOW_DOCTYPE_DECLARATION_FEATURE, true);
            factory.setFeature(EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
            factory.setFeature(EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ParsingException(Messages.COULD_NOT_CREATE_DOCUMENT_BUILDER_FACTORY, e);
        }
    }

    private static Unmarshaller getValidatingUnmarshaller(JAXBContext context, URL schemaLocation) throws SAXException, JAXBException {
        Unmarshaller unmarshaller = getRegularUnmarshaller(context);
        unmarshaller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                                            .newSchema(schemaLocation));
        return unmarshaller;
    }

    private static Unmarshaller getUnmarshaller(JAXBContext context, URL schemaLocation) throws SAXException {
        try {
            return (schemaLocation != null) ? getValidatingUnmarshaller(context, schemaLocation) : getRegularUnmarshaller(context);
        } catch (JAXBException e) {
            throw new ParsingException(Messages.COULD_NOT_CREATE_JAXB_UNMARSHALLER, e);
        }
    }

    private static void handleUnmarshallingException(Exception e, URL schemaLocation) {
        if (e instanceof SAXException) {
            throw new ParsingException(MessageFormat.format(Messages.UNABLE_TO_PARSE_SCHEMA, schemaLocation), e);
        } else if (e instanceof JAXBException && e.getCause() instanceof SAXParseException) {
            Throwable cause = e.getCause();
            throw new ParsingException(cause.getMessage(), cause);
        } else if (e instanceof JAXBException) {
            throw new ParsingException(Messages.UNABLE_TO_UNMARSHAL_OBJECT, e);
        }
    }

    private static Unmarshaller getRegularUnmarshaller(JAXBContext context) throws JAXBException {
        return context.createUnmarshaller();
    }

    private static void handleMarshallingException(Exception e) {
        throw new SLException(e, Messages.UNABLE_TO_MARSHAL_OBJECT);
    }

}
