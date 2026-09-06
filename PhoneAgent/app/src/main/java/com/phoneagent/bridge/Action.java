package com.phoneagent.bridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Immutable representation of a single agent action, as produced by the
 * local language model and consumed by {@link AgentAccessibilityService}.
 *
 * Only one Action is ever produced per model call. Coordinates and duration
 * default to -1 / 0 when absent so {@link ActionValidator} can detect
 * missing required fields.
 */
public final class Action {

    public static final String TAP = "tap";
    public static final String TYPE = "type";
    public static final String SWIPE = "swipe";
    public static final String LONG_PRESS = "long_press";
    public static final String LAUNCH = "launch";
    public static final String BACK = "back";
    public static final String HOME = "home";
    public static final String WAIT = "wait";
    public static final String TAP_AT = "tap_at";
    public static final String DONE = "done";
    public static final String FAIL = "fail";

    public final String type;
    public final String value;
    public final int x;
    public final int y;
    public final int x2;
    public final int y2;
    public final int duration;

    public Action(String type, String value, int x, int y, int x2, int y2, int duration) {
        this.type = type;
        this.value = value;
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        this.duration = duration;
    }

    /**
     * Parses an Action from a JSON object. Throws JSONException on any
     * structural problem; callers must treat parse failures as a reason to
     * stop the agent loop, never as a reason to guess.
     */
    public static Action fromJson(JSONObject obj) throws JSONException {
        if (obj == null) {
            throw new JSONException("null action object");
        }
        if (!obj.has("action")) {
            throw new JSONException("missing 'action' field");
        }

        String type = obj.optString("action", "").trim().toLowerCase();
        if (type.isEmpty()) {
            throw new JSONException("empty action type");
        }

        String value = obj.has("value") ? obj.optString("value", "") : "";
        int x = obj.has("x") ? obj.optInt("x", -1) : -1;
        int y = obj.has("y") ? obj.optInt("y", -1) : -1;
        int x2 = obj.has("x2") ? obj.optInt("x2", -1) : -1;
        int y2 = obj.has("y2") ? obj.optInt("y2", -1) : -1;
        int duration = obj.has("duration") ? obj.optInt("duration", 0) : 0;

        return new Action(type, value, x, y, x2, y2, duration);
    }

    /**
     * Scans free-form model output and extracts the first balanced JSON
     * object found in it, tolerating leading/trailing prose. Returns null
     * if no balanced object is found. This does not validate the object's
     * contents -- that happens in {@link #fromJson} and
     * {@link ActionValidator}.
     */
    public static String extractJson(String text) {
        if (text == null) {
            return null;
        }
        int start = text.indexOf('{');
        if (start < 0) {
            return null;
        }

        int depth = 0;
        boolean inString = false;
        boolean escape = false;

        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);
            if (inString) {
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }
            if (c == '"') {
                inString = true;
            } else if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return text.substring(start, i + 1);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Action{type=" + type + ", value=" + value + ", x=" + x + ", y=" + y
                + ", x2=" + x2 + ", y2=" + y2 + ", duration=" + duration + "}";
    }
}
