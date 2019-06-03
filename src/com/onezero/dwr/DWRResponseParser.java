package com.onezero.dwr;

import com.onezero.dwr.exception.InvalidDWRResponseException;

/**
 * Utility for dwr tests
 */
public class DWRResponseParser {
    private static final String CALLBACK_PREFIX = "dwr.engine._remoteHandleCallback('";

    private static final String EXCEPTION_PREFIX = "dwr.engine._remoteHandleException('";

    private static final String REPLY_PREFIX = "//#DWR-REPLY";

    private static final String PREFIX_2 = "','0',";

    private static final String SUFFIX = ");";

    private DWRResponseParser() {
    }

    public static Object parseResponse(String dwrResponse, int batchID) throws RuntimeException {
        if (dwrResponse == null) {
            return null;
        }

        String exceptionPrefix = EXCEPTION_PREFIX + String.valueOf(batchID) + PREFIX_2;
        if (dwrResponse.contains(exceptionPrefix)) {
            String exceptionBody = dwrResponse
                .substring(dwrResponse.indexOf(exceptionPrefix) + exceptionPrefix.length(),
                    dwrResponse.lastIndexOf(SUFFIX));
//            JSONObject jsonObject = new JSONObject(exceptionBody);
//            throw new DWRException(getString(jsonObject, "errorType"), getString(jsonObject, "fingerprint"),
//                getString(jsonObject, "errorId"), getString(jsonObject, "message"), getString(jsonObject, "errorId"),
//                getString(jsonObject, "timestamp"));
            return null;
        }

        String callbackPrefix = CALLBACK_PREFIX + String.valueOf(batchID) + PREFIX_2;
        if (dwrResponse.contains(callbackPrefix)) {
            int bodyStart = dwrResponse.indexOf(callbackPrefix);
            String body = dwrResponse.substring(bodyStart + callbackPrefix.length(), dwrResponse.lastIndexOf(SUFFIX));
            String reply = dwrResponse.substring(dwrResponse.indexOf(REPLY_PREFIX) + REPLY_PREFIX.length(), bodyStart)
                .trim();
            return DWRResponseDecoder.getInstance().decode(reply, body);
        }
        return new InvalidDWRResponseException(dwrResponse);
    }

//    public static String getString(JSONObject jsonObject, String field) {
//        try {
//            if (jsonObject.has(field)) {
//                return jsonObject.getString(field);
//            }
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    public static JSONObject getJSONObject(JSONObject jsonObject, String field) {
//        try {
//            if (jsonObject.has(field)) {
//                return jsonObject.getJSONObject(field);
//            }
//        } catch (JSONException e) {
//        }
//        return null;
//    }
}
