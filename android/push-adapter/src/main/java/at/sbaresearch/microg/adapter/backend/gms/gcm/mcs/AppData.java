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

// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: protos-repo/mcs.proto
package at.sbaresearch.microg.adapter.backend.gms.gcm.mcs;

public final class AppData {

  public static final String DEFAULT_KEY = "";
  public static final String DEFAULT_VALUE = "";

  public final String key;

  public final String value;

  public AppData(String key, String value) {
    this.key = key;
    this.value = value;
  }

  private AppData(Builder builder) {
    this(builder.key, builder.value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AppData appData = (AppData) o;

    if (key != null ? !key.equals(appData.key) : appData.key != null) return false;
    return value != null ? value.equals(appData.value) : appData.value == null;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  public static final class Builder {

    public String key;
    public String value;

    public Builder() {
    }

    public Builder(AppData message) {
      if (message == null) return;
      this.key = message.key;
      this.value = message.value;
    }

    public Builder key(String key) {
      this.key = key;
      return this;
    }

    public Builder value(String value) {
      this.value = value;
      return this;
    }

    public AppData build() {
      return new AppData(this);
    }
  }
}
