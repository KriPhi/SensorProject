package com.vu.lorasensorapp;

public class VigenereDecrypt {
        public static String sanitise(String text) {
            String out = "";
            int j;
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z')
                    out += text.charAt(i);
                else if (text.charAt(i) >= 'a' && text.charAt(i) <= 'z') {
                    j = (int)text.charAt(i) + (int)'A' - (int)'a';
                    out += (char)j;
                }
            }
            return out;
        }

        public String num_replace(String src) {
            String out = src;

            out = out.replace("1","B");
            out = out.replace("2","C");
            out = out.replace("3","D");
            out = out.replace("4","E");
            out = out.replace("5","F");
            out = out.replace("6","G");
            out = out.replace("7","H");
            out = out.replace("8","I");
            out = out.replace("9","J");
            out = out.replace("0","K");
            out = out.replace(".","Z");
            return out;
        }

        public static String chr_replace(String src) {
            String out = src;

            out = out.replace("B","1");
            out = out.replace("C","2");
            out = out.replace("D","3");
            out = out.replace("E","4");
            out = out.replace("F","5");
            out = out.replace("G","6");
            out = out.replace("H","7");
            out = out.replace("I","8");
            out = out.replace("J","9");
            out = out.replace("K","0");
            out = out.replace(".","Z");

            return out;
        }

        public static String decrypt(String text, String key) {
            String out = "", dec = text;
            int m;

            text = sanitise(dec);
            dec = "";

            for (int i = 0, j = 0; i < text.length(); ++i) {
                char c = text.charAt(i);
                m = ((int)c - (int)key.charAt(j) + 26) % 26 + (int)'A';
                out += (char)m;
                j = (j + 1) % key.length();
            }

            dec = chr_replace(out);
            return dec;
        }
    }