package at.sbaresearch.mqtt_backend;

public class API {
  public static final String INTENT_MQTT_RECEIVE =
      "at.sbaresearch.android.gcm.mqtt.intent.RECEIVE";

  public static final String INTENT_MQTT_CONNECT_HOST = "host";
  public static final String INTENT_MQTT_CONNECT_PORT = "port";
  public static final String INTENT_MQTT_CONNECT_TOPIC = "topic";
  public static final String INTENT_MQTT_CONNECT_CLIENT_KEY = "clientkey";
  public static final String INTENT_MQTT_CONNECT_CLIENT_CERT = "clientcert";

  public static final String SEND_PERM = "at.sbaresearch.android.gcm.mqtt.intent.SEND";

  public static String payload = "payload";
  public static String id = "id";
}
