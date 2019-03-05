package at.sbaresearch.mqtt4android.registration.web;

import at.sbaresearch.mqtt4android.registration.RegistrationService;
import at.sbaresearch.mqtt4android.registration.RegistrationService.AppRegistration;
import at.sbaresearch.mqtt4android.registration.RegistrationService.DeviceId;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@AllArgsConstructor
public class RegistrationResource {

  RegistrationService registrationService;

  // TODO are parameters needed at all?
  @PostMapping(path = "/device", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public DeviceRegistrationResponse registerDevice(@RequestBody DeviceReqistrationRequest req) {
    // TODO create clientCert
    //  save clientCert ID / hash / whatever to associate with device
    //  create mqtt topic and authorize clientCert for this topic
    //  get connection settings of mqtt-relay
    //  return clientCert + connection settings + topic
    return null;
  }

  // TODO get deviceId from client TLS cert. how to do this with spring?
  // TODO actually, this is registerApp; rename endpoint to "app" or so
  @PostMapping(path = "/new", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public AppRegistrationResponse registerApp(@RequestBody AppRegistrationRequest req) {
    log.info("register app: {}", req);

    // TODO deviceID should be extracted from clientCert the adapter-backend got on device registration
    val deviceId = new DeviceId("1234foobar");
    val registrationData = mapFromRequest(req);
    val token = registrationService.registerApp(deviceId, registrationData);

    return new AppRegistrationResponse(token, null);
  }

  private AppRegistration mapFromRequest(AppRegistrationRequest req) {
    return new AppRegistration(req.app, req.cert);
  }

  @RequestMapping(path = "/list", method = RequestMethod.GET)
  public String listAll() {
    log.info("listing");
    // TODO this must not be exposed
    // TODO mocked for now
    return "testRegisterId....";
  }

  @Value
  public static class AppRegistrationRequest {
    String app;
    String cert;
    Integer appVer;
    String appVerName;
    String info;
    //    Boolean delete;
    //    int osv;
    //    int gmsv;
    //    String scope;
    //    TODO appID will be most likely needed
    //    String appid;
    //    String gmpAppId;
  }

  // TODO this should be a static class
  @Value
  private class AppRegistrationResponse {
    @NonNull
    String token;
    String deleted;
  }

  @Value
  public static class DeviceReqistrationRequest {

  }

  @Value
  public static class DeviceRegistrationResponse {
    // client cert
    // mqtt-topic
    // mqtt connection settings
  }
}
