package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.service.mapper;

import eu.europa.ec.fisheries.schema.vessel.Connector2BridgeRequest;
import eu.europa.ec.fisheries.schema.vessel.FLUXReportVesselInformation;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

@Stateless
public class FLUXReportVesselInformationMapper {

    public FLUXReportVesselInformation fromConnector2BridgeRequest(Connector2BridgeRequest request) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(FLUXReportVesselInformation.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (FLUXReportVesselInformation) unmarshaller.unmarshal(request.getAny());
    }

}
