/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.jsonrpc4j.v1_6;

import io.opentelemetry.instrumentation.api.incubator.semconv.rpc.RpcAttributesGetter;

// Check
// https://github.com/open-telemetry/semantic-conventions/blob/main/docs/rpc/rpc-metrics.md#attributes
// Check https://opentelemetry.io/docs/specs/semconv/rpc/json-rpc/
public enum JsonRpcClientAttributesGetter implements RpcAttributesGetter<SimpleJsonRpcRequest> {
  INSTANCE;

  @Override
  public String getSystem(SimpleJsonRpcRequest request) {
    return "jsonrpc";
  }

  @Override
  public String getService(SimpleJsonRpcRequest request) {
    // TODO
    if (request.getMethod() != null) {
      return request.getMethod().getDeclaringClass().getName();
    }
    return "NOT_AVAILABLE";
  }

  @Override
  public String getMethod(SimpleJsonRpcRequest request) {
    return request.getMethodName();
  }
}
