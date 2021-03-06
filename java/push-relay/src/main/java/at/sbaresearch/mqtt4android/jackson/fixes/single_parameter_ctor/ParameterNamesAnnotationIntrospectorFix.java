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

package at.sbaresearch.mqtt4android.jackson.fixes.single_parameter_ctor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.*;

import java.lang.reflect.Executable;
import java.lang.reflect.MalformedParametersException;
import java.lang.reflect.Parameter;

/** implements single ctor args fix: https://github.com/FasterXML/jackson-modules-java8/issues/50
 */
public class ParameterNamesAnnotationIntrospectorFix extends NopAnnotationIntrospector {
  private static final long serialVersionUID = 1L;

  private final JsonCreator.Mode creatorBinding;
  private final ParameterExtractor parameterExtractor;

  public ParameterNamesAnnotationIntrospectorFix(JsonCreator.Mode creatorBinding, ParameterExtractor parameterExtractor)
  {
    this.creatorBinding = creatorBinding;
    this.parameterExtractor = parameterExtractor;
  }

  @Override
  public String findImplicitPropertyName(AnnotatedMember m) {
    if (m instanceof AnnotatedParameter) {
      return findParameterName((AnnotatedParameter) m);
    }
    return null;
  }

  private String findParameterName(AnnotatedParameter annotatedParameter) {

    Parameter[] params;
    try {
      params = getParameters(annotatedParameter.getOwner());
    } catch (MalformedParametersException e) {
      return null;
    }

    Parameter p = params[annotatedParameter.getIndex()];
    return p.isNamePresent() ? p.getName() : null;
  }

  private Parameter[] getParameters(AnnotatedWithParams owner) {
    if (owner instanceof AnnotatedConstructor) {
      return parameterExtractor.getParameters(((AnnotatedConstructor) owner).getAnnotated());
    }
    if (owner instanceof AnnotatedMethod) {
      return parameterExtractor.getParameters(((AnnotatedMethod) owner).getAnnotated());
    }

    return null;
  }

    /*
    /**********************************************************
    /* Creator information handling
    /**********************************************************
     */

  @Override
  public JsonCreator.Mode findCreatorAnnotation(MapperConfig<?> config, Annotated a) {
    JsonCreator ann = _findAnnotation(a, JsonCreator.class);
    if (ann != null) {
      JsonCreator.Mode mode = ann.mode();
      // but keep in mind that there may be explicit default for this module
      if ((creatorBinding != null)
          && (mode == JsonCreator.Mode.DEFAULT)) {
        mode = creatorBinding;
      }
      return mode;
    }
    return creatorBinding;
  }

  @Override
  @Deprecated // remove AFTER 2.9
  public JsonCreator.Mode findCreatorBinding(Annotated a) {
    JsonCreator ann = _findAnnotation(a, JsonCreator.class);
    if (ann != null) {
      JsonCreator.Mode mode = ann.mode();
      if ((creatorBinding != null)
          && (mode == JsonCreator.Mode.DEFAULT)) {
        mode = creatorBinding;
      }
      return mode;
    }
    return creatorBinding;
  }

  @Override
  @Deprecated // since 2.9
  public boolean hasCreatorAnnotation(Annotated a)
  {
    // 02-Mar-2017, tatu: Copied from base AnnotationIntrospector
    JsonCreator ann = _findAnnotation(a, JsonCreator.class);
    if (ann != null) {
      return (ann.mode() != JsonCreator.Mode.DISABLED);
    }
    return false;
  }






  public static class ParameterExtractor {

    public Parameter[] getParameters(Executable executable) {
      return executable.getParameters();
    }
  }

}
