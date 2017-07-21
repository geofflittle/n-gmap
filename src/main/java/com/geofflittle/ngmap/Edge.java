/* Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved. */

package com.geofflittle.ngmap;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Edge {
    @NonNull
    public final NGMap.Dart _1;
    @NonNull
    public final NGMap.Dart _2;
}
