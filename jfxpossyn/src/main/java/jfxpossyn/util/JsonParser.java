package jfxpossyn.util;

import java.util.*;

public class JsonParser {
    public static Object parse(String json) {
        if (json == null) return null;
        json = json.trim();
        if (json.isEmpty()) return null;
        return new Parser(json).parse();
    }

    private static class Parser {
        private final String src;
        private int pos = 0;

        Parser(String src) {
            this.src = src;
        }

        Object parse() {
            skipWhiteSpace();
            if (pos >= src.length()) return null;
            char c = src.charAt(pos);
            if (c == '{') {
                return parseObject();
            } else if (c == '[') {
                return parseArray();
            } else if (c == '"') {
                return parseString();
            } else if (c == '-' || Character.isDigit(c)) {
                return parseNumber();
            } else if (c == 't' || c == 'f') {
                return parseBoolean();
            } else if (c == 'n') {
                return parseNull();
            }
            throw new RuntimeException("Unexpected character: " + c + " at position " + pos);
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new LinkedHashMap<>();
            consume('{');
            skipWhiteSpace();
            if (src.charAt(pos) == '}') {
                consume('}');
                return map;
            }
            while (true) {
                skipWhiteSpace();
                String key = parseString();
                skipWhiteSpace();
                consume(':');
                skipWhiteSpace();
                Object value = parse();
                map.put(key, value);
                skipWhiteSpace();
                if (pos >= src.length()) {
                    throw new RuntimeException("Expected ',' or '}' but reached EOF");
                }
                char c = src.charAt(pos);
                if (c == '}') {
                    consume('}');
                    break;
                } else if (c == ',') {
                    consume(',');
                } else {
                    throw new RuntimeException("Expected ',' or '}' at " + pos);
                }
            }
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            consume('[');
            skipWhiteSpace();
            if (src.charAt(pos) == ']') {
                consume(']');
                return list;
            }
            while (true) {
                Object value = parse();
                list.add(value);
                skipWhiteSpace();
                if (pos >= src.length()) {
                    throw new RuntimeException("Expected ',' or ']' but reached EOF");
                }
                char c = src.charAt(pos);
                if (c == ']') {
                    consume(']');
                    break;
                } else if (c == ',') {
                    consume(',');
                } else {
                    throw new RuntimeException("Expected ',' or ']' at " + pos);
                }
            }
            return list;
        }

        private String parseString() {
            consume('"');
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') {
                    return sb.toString();
                } else if (c == '\\') {
                    if (pos >= src.length()) throw new RuntimeException("Unterminated escape sequence");
                    char next = src.charAt(pos++);
                    switch (next) {
                        case '"': sb.append('"'); break;
                        case '\\': sb.append('\\'); break;
                        case '/': sb.append('/'); break;
                        case 'b': sb.append('\b'); break;
                        case 'f': sb.append('\f'); break;
                        case 'n': sb.append('\n'); break;
                        case 'r': sb.append('\r'); break;
                        case 't': sb.append('\t'); break;
                        case 'u':
                            if (pos + 4 > src.length()) throw new RuntimeException("Incomplete unicode escape");
                            String hex = src.substring(pos, pos + 4);
                            sb.append((char) Integer.parseInt(hex, 16));
                            pos += 4;
                            break;
                        default: sb.append(next);
                    }
                } else {
                    sb.append(c);
                }
            }
            throw new RuntimeException("Unterminated string");
        }

        private Number parseNumber() {
            int start = pos;
            if (src.charAt(pos) == '-') pos++;
            while (pos < src.length() && (Character.isDigit(src.charAt(pos)) || src.charAt(pos) == '.' || src.charAt(pos) == 'e' || src.charAt(pos) == 'E' || src.charAt(pos) == '+' || src.charAt(pos) == '-')) {
                pos++;
            }
            String numStr = src.substring(start, pos);
            if (numStr.contains(".") || numStr.contains("e") || numStr.contains("E")) {
                return Double.parseDouble(numStr);
            } else {
                return Long.parseLong(numStr);
            }
        }

        private Boolean parseBoolean() {
            if (src.startsWith("true", pos)) {
                pos += 4;
                return true;
            } else if (src.startsWith("false", pos)) {
                pos += 5;
                return false;
            }
            throw new RuntimeException("Expected boolean at " + pos);
        }

        private Object parseNull() {
            if (src.startsWith("null", pos)) {
                pos += 4;
                return null;
            }
            throw new RuntimeException("Expected null at " + pos);
        }

        private void skipWhiteSpace() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) {
                pos++;
            }
        }

        private void consume(char expected) {
            skipWhiteSpace();
            if (pos >= src.length() || src.charAt(pos) != expected) {
                throw new RuntimeException("Expected '" + expected + "' but got " + (pos >= src.length() ? "EOF" : "'" + src.charAt(pos) + "'") + " at " + pos);
            }
            pos++;
        }
    }
}
