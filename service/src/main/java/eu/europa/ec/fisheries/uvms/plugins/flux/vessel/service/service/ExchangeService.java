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
package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.service;

import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.exception.PluginException;
import eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.producer.ExchangeEventMessageProducerBean;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@LocalBean
@Stateless
@Slf4j
public class ExchangeService {

    @EJB
    private ExchangeEventMessageProducerBean producer;

    //TODO
    public void sendVesselInformation() throws PluginException {
        try {
            producer.sendModuleMessage("TODO", null);
        } catch (Exception e) {
            throw new PluginException("Unable to send Vessel information to the Exchange queue. Reason: " + e.getMessage(), e);
        }
    }
}
