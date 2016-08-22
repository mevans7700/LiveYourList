package com.evansappwriter.liveyourlist.core;

import com.evansappwriter.liveyourlist.util.Utils;

import org.json.JSONObject;

/**
 * Created by markevans on 8/3/16.
 */
public class APIParser {
    private static final String TAG = "APIPARSER";

    public static final int TYPE_PARSER_ERROR = -1;
    public static final int TYPE_PARSER_NONE = 0;
    public static final int TYPE_PARSER_ = 1;

    // this class cannot be instantiated
    private APIParser() {

    }

    public static void parseResponse(BundledData data) {
        int parserType = data.getParserType();

        Utils.printLogInfo(TAG, data.getHttpData());

        switch (parserType) {
            case TYPE_PARSER_ERROR:
                parseError(data);
                break;
            case TYPE_PARSER_:

                break;
            case TYPE_PARSER_NONE:
            default:
                // no parse needed
                break;
        }
    }

    private static void parseError(BundledData data) {
        if (getStringObject(data.getHttpData()) == null) {
            data.setAuxData("Bad Payload", data.getHttpData());
            return;
        }

        try {
            JSONObject json = new JSONObject(data.getHttpData());

            // ensure resources get cleaned up timely and properly
            data.setHttpData(null);



            data.setAuxData();
        } catch (Exception e) {
            Utils.printStackTrace(e);
            data.setAuxData("Server Error", data.getHttpData());
            data.setHttpData(null);
        }
    }

    // useful methods
    private static String getStringObject(String txt) {
        return txt == null ? null : txt.equalsIgnoreCase("null") ? null : txt;
    }
}
