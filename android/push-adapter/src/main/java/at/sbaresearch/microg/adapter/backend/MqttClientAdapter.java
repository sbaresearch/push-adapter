/*
 * Copyright (C) 2013-2017, 2020 microG Project Team, Harald Jagenteufel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.sbaresearch.microg.adapter.backend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import at.sbaresearch.microg.adapter.backend.gms.gcm.GcmPrefs;
import at.sbaresearch.microg.adapter.backend.gms.gcm.GcmPrefs.MqttSettings;
import lombok.val;

import static at.sbaresearch.microg.adapter.backend.MQTT_API.*;
import static at.sbaresearch.microg.adapter.backend.gms.gcm.GcmConstants.PERMISSION_CONNECT;

public class MqttClientAdapter {

  private static final String TAG = MqttClientAdapter.class.getSimpleName();

  public static void ensureBackendConnection(Context ctx) {
    Log.d(TAG, "ensureBackendConnection");
    val prefs = GcmPrefs.get(ctx);
    val mqttSettings = prefs.getMqttSettings();

    val connectIntent = new Intent(MQTT_API.INTENT_MQTT_CONNECT);
    connectIntent.putExtras(fromSettings(mqttSettings));
    connectIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
    connectIntent.setClassName("at.sbaresearch.mqtt_backend", "at.sbaresearch.mqtt_backend.MqttConnectReceiver");

    ctx.sendBroadcast(connectIntent, PERMISSION_CONNECT);
  }

  private static Bundle fromSettings(MqttSettings s) {
    val bundle = new Bundle(5);
    bundle.putString(INTENT_MQTT_CONNECT_HOST, s.getHost());
    bundle.putInt(INTENT_MQTT_CONNECT_PORT, s.getPort());
    bundle.putString(INTENT_MQTT_CONNECT_TOPIC, s.getTopic());
    bundle.putByteArray(INTENT_MQTT_CONNECT_CLIENT_KEY, s.getPrivKey());
    bundle.putByteArray(INTENT_MQTT_CONNECT_CLIENT_CERT, s.getCert());
    return bundle;
  }
}
