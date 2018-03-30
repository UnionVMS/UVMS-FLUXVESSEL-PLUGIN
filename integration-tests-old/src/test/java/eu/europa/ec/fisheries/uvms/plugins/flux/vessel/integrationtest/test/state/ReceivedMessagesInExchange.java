package eu.europa.ec.fisheries.uvms.plugins.flux.vessel.integrationtest.test.state;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Exchange mocks can keep the received messages in this singleton,
 * so that the test can ask this singleton which messages were received
 */
@Singleton
public class ReceivedMessagesInExchange {

    private List<String> receivedMessages;

    @PostConstruct
    public void init() {
        receivedMessages = new ArrayList<>();
    }

    public void add(String message) {
        this.receivedMessages.add(message);
    }

    public void clear() {
        this.receivedMessages.clear();
    }

    public List<String> getAll() {
        return Collections.unmodifiableList(this.receivedMessages);
    }
}
