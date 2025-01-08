/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.jsonrpc4j.v1_6;

import com.googlecode.jsonrpc4j.AnnotationsErrorResolver;
import com.googlecode.jsonrpc4j.DefaultErrorResolver;
import com.googlecode.jsonrpc4j.ErrorResolver;
import com.googlecode.jsonrpc4j.MultipleErrorResolver;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.api.instrumenter.AttributesExtractor;
import javax.annotation.Nullable;

// Check https://opentelemetry.io/docs/specs/semconv/rpc/json-rpc/
final class JsonRpcServerAttributesExtractor
    implements AttributesExtractor<JsonRpcRequest, JsonRpcResponse> {

  private static final AttributeKey<Long> RPC_JSONRPC_ERROR_CODE =
      AttributeKey.longKey("rpc.jsonrpc.error_code");

  private static final AttributeKey<String> RPC_JSONRPC_ERROR_MESSAGE =
      AttributeKey.stringKey("rpc.jsonrpc.error_message");

  @Override
  public void onStart(
      AttributesBuilder attributes, Context parentContext, JsonRpcRequest jsonRpcRequest) {
    attributes.put("rpc.jsonrpc.version", "2.0");
  }

  @Override
  public void onEnd(
      AttributesBuilder attributes,
      Context context,
      JsonRpcRequest jsonRpcRequest,
      @Nullable JsonRpcResponse jsonRpcResponse,
      @Nullable Throwable error) {
    // use the DEFAULT_ERROR_RESOLVER to extract error code and message
    if (error != null) {
      ErrorResolver errorResolver =
          new MultipleErrorResolver(
              AnnotationsErrorResolver.INSTANCE, DefaultErrorResolver.INSTANCE);
      ErrorResolver.JsonError jsonError =
          errorResolver.resolveError(
              error, jsonRpcRequest.getMethod(), jsonRpcRequest.getArguments());
      attributes.put(RPC_JSONRPC_ERROR_CODE, jsonError.code);
      attributes.put(RPC_JSONRPC_ERROR_MESSAGE, jsonError.message);
    } else {
      attributes.put(RPC_JSONRPC_ERROR_CODE, ErrorResolver.JsonError.OK.code);
    }
  }
}
