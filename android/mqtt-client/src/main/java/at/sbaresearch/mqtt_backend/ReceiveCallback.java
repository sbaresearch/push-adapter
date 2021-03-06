/*
 * Copyright (c) 2020 Harald Jagenteufel.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html.
 */

package at.sbaresearch.mqtt_backend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import lombok.Value;
import lombok.val;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

class ReceiveCallback implements MqttCallbackExtended {
  private static final String TAG = "ReceiveCallback";

  private final Context context;
  private TopicSubscriber subscriber;
  private final int id;

  public ReceiveCallback(Context ctx, int id,
      TopicSubscriber subscriber) {
    this.id = id;
    this.context = ctx;
    this.subscriber = subscriber;
  }

  @Override
  public void connectionLost(Throwable cause) {
    if (cause != null) {
      Log.e(TAG, id + " connection lost: " + cause.getMessage());
    } else {
      Log.w(TAG, id +  " connection lost: without cause (client disconnected?)");
    }
  }

  @Override
  public void connectComplete(boolean reconnect, String serverURI) {
    Log.i(TAG, "connection complete; is reconnect: " + reconnect);
    subscriber.subscribe();
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) {
    Log.i(TAG, id + " MQTT msg recv: " + message.toString());
    try {
      Message pay = parsePayload(message);
      Log.i(TAG, "MQTT msg parsed: " + pay.toString());
      sendIntent(pay);
    } catch (JSONException e) {
      Log.e(TAG, "MQTT msg parsing failed", e);
    }
  }

  private Message parsePayload(MqttMessage msg) throws JSONException {
    final JSONObject json = (JSONObject) new JSONTokener(new String(msg.getPayload())).nextValue();
    String app = json.getString("app");
    String sig = json.getString("signature");
    String name = json.getString("messageId");
    val data = json.getJSONObject("data").toString();
    long sentTime = json.getLong("sentTime");
    String senderId = json.getString("senderId");
    return new Message(app, sig, name, data, sentTime, senderId);
  }

  private void sendIntent(Message message) {
    // TODO register intent, well-defined intent constant
    Intent intent = new Intent(API.INTENT_MQTT_RECEIVE);
    intent.setClassName(API.ADAPTER_PKG, API.ADAPTER_MSG_RECEIVER);
    intent.putExtra(API.app, message.app);
    intent.putExtra(API.signature, message.signature);
    intent.putExtra(API.messageId, message.messageId);
    intent.putExtra(API.payload, message.dataAsJson);
    intent.putExtra(API.sentTime, message.sentTime);
    intent.putExtra(API.senderId, message.senderId);

    // TODO enforce some permission here? enforce package messageId here
    context.sendBroadcast(intent);
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    // nop
  }

  @Value
  private class Message {
    String app;
    String signature;
    String messageId;
    String dataAsJson;
    long sentTime;
    String senderId;
  }
}
