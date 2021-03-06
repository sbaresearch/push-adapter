/*
 * Copyright (c) 2020 Harald Jagenteufel.
 *
 * This file is part of push-relay.
 *
 *     push-relay is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     push-relay is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with push-relay.  If not, see <https://www.gnu.org/licenses/>.
 */

package at.sbaresearch.mqtt4android.registration.crypto;

import at.sbaresearch.mqtt4android.testdata.Clients.EncodedKeys;
import at.sbaresearch.mqtt4android.pinning.ClientKeyCert;
import at.sbaresearch.mqtt4android.pinning.PinningSslFactory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

@Profile("it")
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CryptoTestHelper {

  Certificate serverCert;

  public CryptoTestHelper(@Qualifier("serverCert") Certificate serverCert) {
    this.serverCert = serverCert;
  }

  public PinningSslFactory createPinningFactory(
      final byte[] privateKey, final byte[] cert) throws Exception {
    val keys = new ClientKeyCert(privateKey, cert);
    val in = toInputStream(serverCert);
    return new PinningSslFactory(keys, in);
  }

  private InputStream toInputStream(Certificate cert) throws CertificateEncodingException {
    return new ByteArrayInputStream(cert.getEncoded());
  }

  public RestTemplateBuilder addClientKeys(RestTemplateBuilder restBuilder, EncodedKeys keys)
      throws Exception {
    val sslContext =
        createPinningFactory(keys.getPrivateKey(), keys.getCert()).getSslContext();
    val csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
    val httpClient = HttpClients.custom()
        .setSSLSocketFactory(csf)
        .build();
    val requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);

    return restBuilder.requestFactory(() -> requestFactory);
  }
}
