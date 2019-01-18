package at.sbaresearch.mqtt4android.registration;

import at.sbaresearch.mqtt4android.relay.MqttBrokerConfig;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class PushService {

  JmsTemplate jmsTemplate;

  public void pushMessage(String msg) {
    log.info("pushing message: {}", msg);
    jmsTemplate.convertAndSend(MqttBrokerConfig.MQTT_MOCK_TOPIC, msg);
  }
}