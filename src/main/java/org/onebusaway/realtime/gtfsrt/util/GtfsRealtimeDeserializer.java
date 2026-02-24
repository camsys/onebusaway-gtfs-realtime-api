package org.onebusaway.realtime.gtfsrt.util;

import com.google.protobuf.util.JsonFormat;
import com.google.transit.realtime.GtfsRealtime;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GtfsRealtimeDeserializer {

    public static GtfsRealtime.FeedMessage parseFeedMessage(byte[] message) throws IOException {
        if (isJson(message)) {
            return parseFeedMessageFromJson(message);
        } else {
            return parseFeedMessageFromProtobuf(message);
        }
    }

    private static boolean isJson(byte[] message) {
        if (message == null || message.length == 0) return false;
        // Skip any leading whitespace
        for (byte b : message) {
            if (b == ' ' || b == '\t' || b == '\r' || b == '\n') continue;
            return b == '{';
        }
        return false;
    }

    public static GtfsRealtime.FeedMessage parseFeedMessageFromJson(byte[] message) throws IOException {
        try {
            String jsonString = new String(message, StandardCharsets.UTF_8);
            GtfsRealtime.FeedMessage.Builder builder = GtfsRealtime.FeedMessage.newBuilder();
            JsonFormat.parser()
                    .ignoringUnknownFields()
                    .merge(jsonString, builder);

            return builder.build();
        } catch (Exception e) {
            throw new IOException("Failed to parse as JSON", e);
        }
    }

    public static GtfsRealtime.FeedMessage parseFeedMessageFromProtobuf(byte[] message) throws IOException {
        try {
            return GtfsRealtime.FeedMessage.parseFrom(message);
        } catch (Exception e) {
            throw new IOException("Failed to parse as protobuf", e);
        }
    }
}
