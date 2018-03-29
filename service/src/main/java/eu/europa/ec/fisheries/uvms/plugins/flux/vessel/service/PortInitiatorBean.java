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
package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service;

import lombok.extern.slf4j.Slf4j;
import xeu.connector_bridge.wsdl.v1.BridgeConnectorPortType;
import xeu.connector_bridge.wsdl.v1.BridgeConnectorService;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.ws.BindingProvider;
import java.util.Map;

/**
 *
 */

/**
 * This class is intended to initiate the PortType for the intended WS-calls
 *
 */
@Singleton
@Startup
@Slf4j
public class PortInitiatorBean implements PortInitiator {

    private BridgeConnectorPortType port;

    @EJB
    StartupBean startupBean;

    public BridgeConnectorPortType getPort() {
        if (port == null) {
            port = setupPort();
        }
        return port;
    }

    public void updatePort() {
        port = setupPort();
    }

    private BridgeConnectorPortType setupPort() {
        BridgeConnectorService service = new BridgeConnectorService();
        BridgeConnectorPortType newPort = service.getBridgeConnectorSOAP11Port();
        BindingProvider bp = (BindingProvider) newPort;
        Map<String, Object> context = bp.getRequestContext();
        context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, startupBean.getSetting("FLUX_ENDPOINT"));
        log.info("Endpoint will be " + startupBean.getSetting("FLUX_ENDPOINT"));
        return newPort;
    }
}
