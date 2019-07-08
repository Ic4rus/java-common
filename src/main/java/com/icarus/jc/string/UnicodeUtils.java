package com.icarus.jc.string;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UnicodeUtils {

    /**
     * Convert to Vietnamese without accent.
     *
     * @param str
     * @return
     */
    public static String convertToUnsign(String str) {

        char[] originCharArray = str.toCharArray();
        char[] unsignCharArray = new char[originCharArray.length];
        for (int i = 0; i < originCharArray.length; i++) {
            switch (originCharArray[i]) {
                case '\u00E1':// á
                case '\u00E0':// à
                case '\u1EA3':// ả
                case '\u00E3':// ã
                case '\u1EA1':// ạ
                case '\u0103':// ă
                case '\u1EAF':// ắ
                case '\u1EB1':// ằ
                case '\u1EB3':// ẳ
                case '\u1EB5':// ẵ
                case '\u1EB7':// ặ
                case '\u00E2':// â
                case '\u1EA5':// ấ
                case '\u1EA7':// ầ
                case '\u1EA9':// ẩ
                case '\u1EAB':// ẫ
                case '\u1EAD':// ậ
                case '\u0203':// â
                case '\u01CE':// ă
                    unsignCharArray[i] = 'a';
                    break;

                case '\u00E9':// é
                case '\u00E8':// è
                case '\u1EBB':// ẻ
                case '\u1EBD':// ẽ
                case '\u1EB9':// ẹ
                case '\u00EA':// ê
                case '\u1EBF':// ế
                case '\u1EC1':// ề
                case '\u1EC3':// ể
                case '\u1EC5':// ễ
                case '\u1EC7':// ệ
                case '\u0207':// ê
                    unsignCharArray[i] = 'e';
                    break;

                case '\u00ED':// í
                case '\u00EC':// ì
                case '\u1EC9':// ỉ
                case '\u0129':// ĩ
                case '\u1ECB':// ị
                    unsignCharArray[i] = 'i';
                    break;

                case '\u00F3':// ó
                case '\u00F2':// ò
                case '\u1ECF':// ỏ
                case '\u00F5':// õ
                case '\u1ECD':// ọ
                case '\u00F4':// ô
                case '\u1ED1':// ố
                case '\u1ED3':// ồ
                case '\u1ED5':// ổ
                case '\u1ED7':// ỗ
                case '\u1ED9':// ộ
                case '\u01A1':// ơ
                case '\u1EDB':// ớ
                case '\u1EDD':// ờ
                case '\u1EDF':// ở
                case '\u1EE1':// ỡ
                case '\u1EE3':// ợ
                case '\u020F':// ô
                    unsignCharArray[i] = 'o';
                    break;

                case '\u00FA':// ú
                case '\u00F9':// ù
                case '\u1EE7':// ủ
                case '\u0169':// ũ
                case '\u1EE5':// ụ
                case '\u01B0':// ư
                case '\u1EE9':// ứ
                case '\u1EEB':// ừ
                case '\u1EED':// ử
                case '\u1EEF':// ữ
                case '\u1EF1':// ự
                    unsignCharArray[i] = 'u';
                    break;

                case '\u00FD':// ý
                case '\u1EF3':// ỳ
                case '\u1EF7':// ỷ
                case '\u1EF9':// ỹ
                case '\u1EF5':// ỵ
                    unsignCharArray[i] = 'y';
                    break;

                case '\u0111':// đ
                    unsignCharArray[i] = 'd';
                    break;

                case '\u00C1':// Á
                case '\u00C0':// À
                case '\u1EA2':// Ả
                case '\u00C3':// Ã
                case '\u1EA0':// Ạ
                case '\u0102':// Ă
                case '\u1EAE':// Ắ
                case '\u1EB0':// Ằ
                case '\u1EB2':// Ẳ
                case '\u1EB4':// Ẵ
                case '\u1EB6':// Ặ
                case '\u00C2':// Â
                case '\u1EA4':// Ấ
                case '\u1EA6':// Ầ
                case '\u1EA8':// Ẩ
                case '\u1EAA':// Ẫ
                case '\u1EAC':// Ậ
                case '\u0202':// Â
                case '\u01CD':// Ă
                    unsignCharArray[i] = 'A';
                    break;

                case '\u00C9':// É
                case '\u00C8':// È
                case '\u1EBA':// Ẻ
                case '\u1EBC':// Ẽ
                case '\u1EB8':// Ẹ
                case '\u00CA':// Ê
                case '\u1EBE':// Ế
                case '\u1EC0':// Ề
                case '\u1EC2':// Ể
                case '\u1EC4':// Ễ
                case '\u1EC6':// Ệ
                case '\u0206':// Ê
                    unsignCharArray[i] = 'E';
                    break;

                case '\u00CD':// Í
                case '\u00CC':// Ì
                case '\u1EC8':// Ỉ
                case '\u0128':// Ĩ
                case '\u1ECA':// Ị
                    unsignCharArray[i] = 'I';
                    break;

                case '\u00D3':// Ó
                case '\u00D2':// Ò
                case '\u1ECE':// Ỏ
                case '\u00D5':// Õ
                case '\u1ECC':// Ọ
                case '\u00D4':// Ô
                case '\u1ED0':// Ố
                case '\u1ED2':// Ồ
                case '\u1ED4':// Ổ
                case '\u1ED6':// Ỗ
                case '\u1ED8':// Ộ
                case '\u01A0':// Ơ
                case '\u1EDA':// Ớ
                case '\u1EDC':// Ờ
                case '\u1EDE':// Ở
                case '\u1EE0':// Ỡ
                case '\u1EE2':// Ợ
                case '\u020E':// Ô
                    unsignCharArray[i] = 'O';
                    break;

                case '\u00DA':// Ú
                case '\u00D9':// Ù
                case '\u1EE6':// Ủ
                case '\u0168':// Ũ
                case '\u1EE4':// Ụ
                case '\u01AF':// Ư
                case '\u1EE8':// Ứ
                case '\u1EEA':// Ừ
                case '\u1EEC':// Ử
                case '\u1EEE':// Ữ
                case '\u1EF0':// Ự
                    unsignCharArray[i] = 'U';
                    break;

                case '\u00DD':// Ý
                case '\u1EF2':// Ỳ
                case '\u1EF6':// Ỷ
                case '\u1EF8':// Ỹ
                case '\u1EF4':// Ỵ
                    unsignCharArray[i] = 'Y';
                    break;

                case '\u0110':// Đ
                case '\u00D0':// Ð
                case '\u0089':
                    unsignCharArray[i] = 'D';
                    break;
                default:
                    unsignCharArray[i] = originCharArray[i];
            }
        }
        return new String(unsignCharArray);
    }

    /**
     * Convert a string to standard ASCII for unicode.
     *
     * @param str
     * @return
     */
    private static String convertASCIIString(String str) {

        // encode for GL
        str = str.replaceAll("%C3%A%E1%BA%A2", "%E1%BB%83");// åAẢ -> ể
        str = str.replaceAll("%C4%82%CC%89", "%C4%82");// Ẳ -> Ă
        str = str.replaceAll("%C4%83%CC%89", "%C4%83");// ẳ -> ă
        str = str.replaceAll("%C3%82%CC%89", "%C3%82");// Ẩ -> Â
        str = str.replaceAll("%C3%A2%CC%89", "%C3%A2");// ẩ -> â
        str = str.replaceAll("%C4%82%CC%80", "%E1%BA%B0");// Ằ -> Ằ
        str = str.replaceAll("%C4%83%CC%80", "%E1%BA%B1");// ằ -> ằ
        str = str.replaceAll("%C3%82%CC%80", "%E1%BA%A6");// Ầ -> Ầ
        str = str.replaceAll("%C3%A2%CC%80", "%E1%BA%A7");// ầ -> ầ
        str = str.replaceAll("%C3%8A%CC%80", "%E1%BB%80");// Ề -> Ề
        str = str.replaceAll("%C3%AA%CC%80", "%E1%BB%81");// ề -> ề
        str = str.replaceAll("%C3%94%CC%80", "%E1%BB%92");// Ồ -> Ồ
        str = str.replaceAll("%C3%B4%CC%80", "%E1%BB%93");// ồ -> ồ
        str = str.replaceAll("%C6%A0%CC%80", "%E1%BB%9C");// Ờ -> Ờ
        str = str.replaceAll("%C6%A1%CC%80", "%E1%BB%9D");// ờ -> ờ
        str = str.replaceAll("%C6%AF%CC%80", "%E1%BB%AA");// Ừ -> Ừ
        str = str.replaceAll("%C6%B0%CC%80", "%E1%BB%AB");// ừ -> ừ

        str = str.replaceAll("%C4%82%CC%89", "%E1%BA%B2");// Ẳ -> Ẳ
        str = str.replaceAll("%C4%83%CC%89", "%E1%BA%B3");// ẳ -> ẳ
        str = str.replaceAll("%C3%82%CC%89", "%E1%BA%A8");// Ẩ -> Ẩ
        str = str.replaceAll("%C3%A2%CC%89", "%E1%BA%A9");// ẩ -> ẩ
        str = str.replaceAll("%C3%8A%CC%89", "%E1%BB%82");// Ể -> Ể
        str = str.replaceAll("%C3%AA%CC%89", "%E1%BB%83");// ể -> ể
        str = str.replaceAll("%C3%94%CC%89", "%E1%BB%94");// Ổ -> Ổ
        str = str.replaceAll("%C3%B4%CC%89", "%E1%BB%95");// ổ -> ổ
        str = str.replaceAll("%C6%A0%CC%89", "%E1%BB%9E");// Ở -> Ở
        str = str.replaceAll("%C6%A1%CC%89", "%E1%BB%9F");// ở -> ở
        str = str.replaceAll("%C6%AF%CC%89", "%E1%BB%AC");// Ử -> Ử
        str = str.replaceAll("%C6%B0%CC%89", "%E1%BB%AD");// ử -> ử

        str = str.replaceAll("%C4%82%CC%83", "%E1%BA%B4");// Ẵ -> Ẵ
        str = str.replaceAll("%C4%83%CC%83", "%E1%BA%B5");// ẵ -> ẵ
        str = str.replaceAll("%C3%82%CC%83", "%E1%BA%AA");// Ẫ -> Ẫ
        str = str.replaceAll("%C3%A2%CC%83", "%E1%BA%AB");// ẫ -> ẫ
        str = str.replaceAll("%C3%8A%CC%83", "%E1%BB%84");// Ễ -> Ễ
        str = str.replaceAll("%C3%AA%CC%83", "%E1%BB%85");// ễ -> ễ
        str = str.replaceAll("%C3%94%CC%83", "%E1%BB%96");// Ỗ -> Ỗ
        str = str.replaceAll("%C3%B4%CC%83", "%E1%BB%97");// ỗ -> ỗ
        str = str.replaceAll("%C6%A0%CC%83", "%E1%BB%A0");// Ỡ -> Ỡ
        str = str.replaceAll("%C6%A1%CC%83", "%E1%BB%A1");// ỡ -> ỡ
        str = str.replaceAll("%C6%AF%CC%83", "%E1%BB%AE");// Ữ -> Ữ
        str = str.replaceAll("%C6%B0%CC%83", "%E1%BB%AF");// ữ -> ữ

        str = str.replaceAll("%C4%82%CC%81", "%E1%BA%AE");// Ắ -> Ắ
        str = str.replaceAll("%C4%83%CC%81", "%E1%BA%AF");// ắ -> ắ
        str = str.replaceAll("%C3%82%CC%81", "%E1%BA%A4");// Ấ -> Ấ
        str = str.replaceAll("%C3%A2%CC%81", "%E1%BA%A5");// ấ -> ấ
        str = str.replaceAll("%C3%8A%CC%81", "%E1%BA%BE");// Ế -> Ế
        str = str.replaceAll("%C3%AA%CC%81", "%E1%BA%BF");// ế -> ế
        str = str.replaceAll("%C3%94%CC%81", "%E1%BB%90");// Ố -> Ố
        str = str.replaceAll("%C3%B4%CC%81", "%E1%BB%91");// ố -> ố
        str = str.replaceAll("%C6%A0%CC%81", "%E1%BB%9A");// Ớ -> Ớ
        str = str.replaceAll("%C6%A1%CC%81", "%E1%BB%9B");// ớ -> ớ
        str = str.replaceAll("%C6%AF%CC%81", "%E1%BB%A8");// Ứ -> Ứ
        str = str.replaceAll("%C6%B0%CC%81", "%E1%BB%A9");// ứ -> ứ

        str = str.replaceAll("%C4%82%CC%A3", "%E1%BA%B6");// Ặ -> Ặ
        str = str.replaceAll("%C4%83%CC%A3", "%E1%BA%B7");// ặ -> ặ
        str = str.replaceAll("%C3%8A%CC%A3", "%E1%BB%86");// Ệ -> Ệ
        str = str.replaceAll("%C3%AA%CC%A3", "%E1%BB%87");// ệ -> ệ
        str = str.replaceAll("%C3%94%CC%A3", "%E1%BB%98");// Ộ -> Ộ
        str = str.replaceAll("%C3%B4%CC%A3", "%E1%BB%99");// ộ -> ộ
        str = str.replaceAll("%C6%A0%CC%A3", "%E1%BB%A2");// Ợ -> Ợ
        str = str.replaceAll("%C6%A1%CC%A3", "%E1%BB%A3");// ợ -> ợ
        str = str.replaceAll("%C6%AF%CC%A3", "%E1%BB%B0");// Ự -> Ự
        str = str.replaceAll("%C6%B0%CC%A3", "%E1%BB%B1");// ự -> ự

        // character ` (%CC%80)
        str = str.replaceAll("A%CC%80", "%C3%80");// À -> À
        str = str.replaceAll("a%CC%80", "%C3%A0");// à -> à
        str = str.replaceAll("E%CC%80", "%C3%88");// È -> È
        str = str.replaceAll("e%CC%80", "%C3%A8");// è -> è
        str = str.replaceAll("I%CC%80", "%C3%8C");// Ì -> Ì
        str = str.replaceAll("i%CC%80", "%C3%AC");// ì -> ì
        str = str.replaceAll("O%CC%80", "%C3%92");// Ò -> Ò
        str = str.replaceAll("o%CC%80", "%C3%B2");// ò -> ò
        str = str.replaceAll("U%CC%80", "%C3%99");// Ù -> Ù
        str = str.replaceAll("u%CC%80", "%C3%B9");// ù -> ù
        str = str.replaceAll("Y%CC%80", "%E1%BB%B2");// Ỳ -> Ỳ
        str = str.replaceAll("y%CC%80", "%E1%BB%B3");// ỳ -> ỳ

        // character ? (%CC%89)
        str = str.replaceAll("A%CC%89", "%E1%BA%A2");// Ả -> Ả
        str = str.replaceAll("a%CC%89", "%E1%BA%A3");// ả -> ả
        str = str.replaceAll("E%CC%89", "%E1%BA%BA");// Ẻ -> Ẻ
        str = str.replaceAll("e%CC%89", "%E1%BA%BB");// ẻ -> ẻ
        str = str.replaceAll("I%CC%89", "%E1%BB%88");// Ỉ -> Ỉ
        str = str.replaceAll("i%CC%89", "%E1%BB%89");// ỉ -> ỉ
        str = str.replaceAll("O%CC%89", "%E1%BB%8E");// Ỏ -> Ỏ
        str = str.replaceAll("o%CC%89", "%E1%BB%8F");// ỏ -> ỏ
        str = str.replaceAll("U%CC%89", "%E1%BB%A6");// Ủ -> Ủ
        str = str.replaceAll("u%CC%89", "%E1%BB%A7");// ủ -> ủ
        str = str.replaceAll("Y%CC%89", "%E1%BB%B6");// Ỷ -> Ỷ
        str = str.replaceAll("y%CC%89", "%E1%BB%B7");// ỷ -> ỷ

        // character ~ (%CC%83)
        str = str.replaceAll("A%CC%83", "%C3%83");// Ã -> Ã
        str = str.replaceAll("a%CC%83", "%C3%A3");// ã -> ã
        str = str.replaceAll("E%CC%83", "%E1%BA%BC");// Ẽ -> Ẽ
        str = str.replaceAll("e%CC%83", "%E1%BA%BD");// ẽ -> ẽ
        str = str.replaceAll("I%CC%83", "%C4%A8");// Ĩ -> Ĩ
        str = str.replaceAll("i%CC%83", "%C4%A9");// ĩ -> ĩ
        str = str.replaceAll("O%CC%83", "%C3%95");// Õ -> Õ
        str = str.replaceAll("o%CC%83", "%C3%B5");// õ -> õ
        str = str.replaceAll("U%CC%83", "%C5%A8");// Ũ -> Ũ
        str = str.replaceAll("u%CC%83", "%C5%A9");// ũ -> ũ
        str = str.replaceAll("Y%CC%83", "%E1%BB%B8");// Ỹ -> Ỹ
        str = str.replaceAll("y%CC%83", "%E1%BB%B9");// ỹ -> ỹ

        // character ' (%CC%81)
        str = str.replaceAll("A%CC%81", "%C3%81");// Á -> Á
        str = str.replaceAll("a%CC%81", "%C3%A1");// á -> á
        str = str.replaceAll("E%CC%81", "%C3%89");// É -> É
        str = str.replaceAll("e%CC%81", "%C3%A9");// é -> é
        str = str.replaceAll("I%CC%81", "%C3%8D");// Í -> Í
        str = str.replaceAll("i%CC%81", "%C3%AD");// í -> í
        str = str.replaceAll("O%CC%81", "%C3%93");// Ó -> Ó
        str = str.replaceAll("o%CC%81", "%C3%B3");// ó -> ó
        str = str.replaceAll("U%CC%81", "%C3%9A");// Ú -> Ú
        str = str.replaceAll("u%CC%81", "%C3%BA");// ú -> ú
        str = str.replaceAll("Y%CC%81", "%C3%9D");// Ý -> Ý
        str = str.replaceAll("y%CC%81", "%C3%BD");// ý -> ý

        // character . (%CC%A3)
        str = str.replaceAll("A%CC%A3", "%E1%BA%A0");// Ạ -> Ạ
        str = str.replaceAll("a%CC%A3", "%E1%BA%A1");// ạ -> ạ
        str = str.replaceAll("E%CC%A3", "%E1%BA%B8");// Ẹ -> Ẹ
        str = str.replaceAll("e%CC%A3", "%E1%BA%B9");// ẹ -> ẹ
        str = str.replaceAll("I%CC%A3", "%E1%BB%8A");// Ị -> Ị
        str = str.replaceAll("i%CC%A3", "%E1%BB%8B");// ị -> ị
        str = str.replaceAll("O%CC%A3", "%E1%BB%8C");// Ọ -> Ọ
        str = str.replaceAll("o%CC%A3", "%E1%BB%8D");// ọ -> ọ
        str = str.replaceAll("U%CC%A3", "%E1%BB%A4");// Ụ -> Ụ
        str = str.replaceAll("u%CC%A3", "%E1%BB%A5");// ụ -> ụ
        str = str.replaceAll("Y%CC%A3", "%E1%BB%B4");// Ỵ -> Ỵ
        str = str.replaceAll("y%CC%A3", "%E1%BB%B5");// ỵ -> ỵ

        return str;
    }

    /**
     * Convert a string to standard unicode.
     *
     * @param str
     * @return
     */
    public static String convertUnicodeString(String str) {

        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String clone = str;
        try {
            clone = URLEncoder.encode(clone, "UTF-8");
            clone = convertASCIIString(clone);
            clone = URLDecoder.decode(clone);
            str = clone;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
