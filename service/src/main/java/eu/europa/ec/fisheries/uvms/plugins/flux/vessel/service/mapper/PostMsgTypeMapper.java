/*
 ﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 © European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.POSTMSG;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.exception.PluginException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

@LocalBean
@Stateless
@Slf4j
public class PostMsgTypeMapper {

    public POSTMSG wrapInPostMsgType(@NonNull String message, @NonNull String df, @NonNull String ad) {
        POSTMSG postmsg = new POSTMSG();
        postmsg.setBUSINESSUUID(UUID.randomUUID().toString());
        postmsg.setAD(ad);
        postmsg.setDF(df);
        postmsg.setDT(DateTime.now());
        postmsg.setAny(marshalToDOM(message));

        return postmsg;
    }

    private Element marshalToDOM(String message) {
        try {
            InputStream xmlAsInputStream = new ByteArrayInputStream(message.getBytes("UTF-8"));
            javax.xml.parsers.DocumentBuilderFactory b = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            b.setNamespaceAware(false);
            DocumentBuilder db = b.newDocumentBuilder();
            Document document = db.parse(xmlAsInputStream);
            return document.getDocumentElement();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new PluginException("Could not wrap object " + message + " in post msg", e);
        }
    }

    public void addHeaderValueToRequest(Object port, final Map<String, String> values) {
        BindingProvider bp = (BindingProvider) port;
        Map<String, Object> context = bp.getRequestContext();

        Map<String, List<String>> headers = new HashMap<>();
        for (Entry entry : values.entrySet()) {
            headers.put(entry.getKey().toString(), Collections.singletonList(entry.getValue().toString()));
        }
        context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }
}
