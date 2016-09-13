package com.iquant.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yonggangli on 2016/9/1.
 */
public class Base64Helper {

    private static final Map<Integer, Character> INDEX_MAP = new HashMap<Integer, Character>();
    private static final char PADDING_CHAR = '_';

    static {
        int index = 0;
        for (int i = 0; i <= 25; i++) {
            INDEX_MAP.put(index, (char) ((int) 'A' + i));
            index++;
        }

        for (int j = 0; j <= 25; j++) {
            INDEX_MAP.put(index, (char) ((int) 'a' + j));
            index++;
        }

        for (int k = 0; k <= 9; k++) {
            INDEX_MAP.put(index, (char) ((int) '0' + k));
            index++;
        }

        INDEX_MAP.put(index, '+');
        index++;
        INDEX_MAP.put(index, '/');
    }

    private static final Map<Character, Integer> VALUE_MAP = new HashMap<Character, Integer>();

    static {
        int index = 0;
        for (char i = 'A'; i <= 'Z'; i++, index++) {
            VALUE_MAP.put(i, index);
        }

        for (char j = 'a'; j <= 'z'; j++, index++) {
            VALUE_MAP.put(j, index);
        }

        for (char k = '0'; k <= '9'; k++, index++) {
            VALUE_MAP.put(k, index);
        }

        VALUE_MAP.put('+', index);
        index++;
        VALUE_MAP.put('/', index);
    }

    public static String encode(byte[] bytes) throws Exception {
        /**
         * 1.转成二进制的字符串(长度为6的倍数) 2.获取转义后的字符串 3.不是4的位数，填充=号
         */
        String binaryString = convertByteArray2BinaryString(bytes);
        String escapeString = escapeBinaryString(binaryString);
        return paddingEscapeString(escapeString);
    }

    private static String convertByteArray2BinaryString(byte[] bytes) {

        StringBuilder binaryBuilder = new StringBuilder();
        for (byte b : bytes) {
            binaryBuilder.append(convertByte2BinaryString(b));
        }

        int paddingCount = binaryBuilder.length() % 6;
        int totalCount = paddingCount > 0 ? binaryBuilder.length() / 6 + 1
                : binaryBuilder.length() / 6;
        int actualLength = 6 * totalCount;

        // 百分号后面的-号表示长度不够规定长度时，右填充。否则左填充。
        return String.format("%-" + actualLength + "s",
                binaryBuilder.toString()).replace(' ', '0');
    }

    private static String escapeBinaryString(String binaryString)
            throws Exception {
        if (null == binaryString || binaryString.isEmpty()
                || binaryString.length() % 6 != 0) {
            System.out.println("error");
            throw new Exception("escape binary string error.");
        }

        StringBuilder escapeBuilder = new StringBuilder();
        for (int i = 0; i <= binaryString.length() - 1; i += 6) {
            String escapeString = binaryString.substring(i, i + 6);
            int index = Integer.parseInt(escapeString, 2);
            escapeBuilder.append(INDEX_MAP.get(index));
        }

        return escapeBuilder.toString();
    }

    private static String paddingEscapeString(String escapeString) {
        int paddingCount = escapeString.length() % 4;
        int totalCount = paddingCount > 0 ? escapeString.length() / 4 + 1
                : escapeString.length() / 4;
        int actualCount = 4 * totalCount;
        return String.format("%-" + actualCount + "s", escapeString).replace(
                ' ', PADDING_CHAR);
    }

    private static String convertByte2BinaryString(byte b) {

        /**
         * 对于非负数，直接使用Integer.toBinaryString方法把它打印出来
         */
        if (b >= 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(Integer.toBinaryString(b));
            return String.format("%08d", Integer.parseInt(builder.toString()));
        } else {
            /**
             * 对于负数，要记住内存保存的是补码。 不能直接使用Byte.parseByte()方法。
             * 因为这个方法最终调的是Integer.parseInt()方法，也就是说，负数如：10000001
             * 对Integer.parseInt()来说并不会认为是负数，符号位1被当作数值位，是129
             * 同时Byte.parseByte()方法里还对数值范围做了校验，符号位1，已超出范围，这样
             * 会抛出异常。而Byte又没有提供toBinaryString的方法 为了保存byte的二进制值，可利用按位与的方法
             * 例如有一个负数1000 1111，要把它以字符串保留出来，利用它与1111 1111的与操作， 再转成int类型。1000
             * 1111 & 1111 1111 在内存中保存的就是 00000000
             * 10001111，这时保存的是一个正整数。但我们不关心整数的正负， 因为我们的目的是要把这串字符串截取出来
             * 再利用Integer.toBinaryString()打印出来。
             * Integer.toBinaryString()对于正数，会将前面的零去掉，如上将打印出1000 1111，这就是我们要的结果。
             */
            int value = b & 0xFF;
            return Integer.toBinaryString(value);
        }
    }

    public static byte[] decode(String base64String) {

        if (null == base64String || base64String.isEmpty()) {
            return null;
        }
        /**
         * 1.去掉末尾拼凑的=符号 2.转成二进制 3.去掉末尾拼凑的0 4.截取每8位取数
         */
        base64String = removePaddingChar(base64String);
        String binaryString = getBinaryString(base64String);
        binaryString = removePaddingNumber(binaryString);

        return convertBinaryString2Bytes(binaryString);

    }

    /**
     * 删除末尾拼凑的=符号
     *
     * @param base64String
     * @return
     */
    private static String removePaddingChar(String base64String) {
        int firstPaddingIndex = base64String.indexOf(PADDING_CHAR);
        return firstPaddingIndex >= 0 ? base64String.substring(0,
                firstPaddingIndex) : base64String;
    }

    /**
     * 将base64字符串转成二进制字符串
     *
     * @param base64String
     * @return
     */
    private static String getBinaryString(String base64String) {
        StringBuilder binaryBuilder = new StringBuilder();
        for (char c : base64String.toCharArray()) {
            int value = VALUE_MAP.get(c);
            binaryBuilder.append(String.format("%6s",
                    Integer.toBinaryString(value)).replace(" ", "0"));
        }

        return binaryBuilder.toString();
    }

    /**
     * 二进制字符串中的末尾有一些0是因为不足6的倍数而填充的，需要删除
     *
     * @param binaryString
     * @return
     */
    private static String removePaddingNumber(String binaryString) {
        int remainder = binaryString.length() % 8;

        binaryString = binaryString.substring(0, binaryString.length()
                - remainder);

        return binaryString;
    }

    private static byte[] convertBinaryString2Bytes(String binaryString) {
        if (null == binaryString || binaryString.length() % 8 != 0) {
            System.out.println("binary string not well formatted.");
            return null;
        }
        int size = binaryString.length() / 8;
        byte[] bytes = new byte[size];
        int arrayIndex = 0;
        for (int i = 0; i <= binaryString.length() - 1; i += 8, arrayIndex++) {
            String byteString = binaryString.substring(i, i + 8);
            /**
             * 非负数，直接使用Byte.parseByte()方法
             */
            if (byteString.startsWith("0")) {
                bytes[arrayIndex] = Byte.parseByte(byteString, 2);
            } else {
                /**
                 * 10000000为-128是规定而来的。 -128并没有原码和反码表示。对-128的补码表示[10000000]补
                 * 算出来的原码是[0000 0000]原, 这是不正确的)
                 */
                if (byteString.equals("1000000")) {
                    bytes[arrayIndex] = (byte) -128;
                    continue;
                }
                /**
                 * 其他的负数，就要按照补码的规则来计算 即，原码取反+1=补码 那么，补码-1取反=原码
                 * 注意这都是真值部分的计算，符号位不能变
                 */
                // 补码
                String twosComplement = byteString.substring(1);
                byte twoComplementValue = Byte.parseByte(twosComplement, 2);

                // 反码
                byte oneComplementValue = (byte) (twoComplementValue - 1);

                /**
                 * 这里用到的是0x7F而不是0xFF。因为oneComplementValue是0开头
                 * 若与1开头的异或，结果为1，而在int中该位不是符号位，会当成数值计算，造成数值错误。
                 * 因此，必须结果是0，即，两个0的异或。
                 */
                // 真值 8位的计算
                int trueValue = oneComplementValue ^ 0x7F;

                bytes[arrayIndex] = (byte) (trueValue * (-1));

            }
        }

        return bytes;
    }

    public static void main(String[] args) {
        try {
            System.out.println(Base64Helper.encode("yonggangli@sohu-inc.com".getBytes()));
            System.out.println(new String(Base64Helper.decode("eW9uZ2dhbmdsaUBzb2h1LWluYy5jb20_")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
