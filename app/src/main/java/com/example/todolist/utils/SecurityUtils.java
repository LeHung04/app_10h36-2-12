package com.example.todolist.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Lớp tiện ích chứa các phương thức liên quan đến bảo mật.
 * Nhiệm vụ chính là mã hóa mật khẩu để lưu trữ an toàn.
 */
public class SecurityUtils {

    /**
     * Mã hóa một chuỗi mật khẩu bằng thuật toán SHA-256.
     * @param passwordToHash Mật khẩu gốc cần mã hóa.
     * @return Chuỗi mật khẩu đã được mã hóa thành dạng Hexadecimal, hoặc null nếu có lỗi.
     */
    public static String hashPassword(String passwordToHash) {
        try {
            // 1. Lấy một đối tượng MessageDigest với thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 2. "Băm" (hash) chuỗi mật khẩu thành một mảng các byte
            byte[] encodedhash = digest.digest(
                    passwordToHash.getBytes(StandardCharsets.UTF_8));

            // 3. Chuyển đổi mảng byte đó thành một chuỗi Hexa để dễ dàng lưu trữ
            return bytesToHex(encodedhash);

        } catch (NoSuchAlgorithmException e) {
            // Lỗi này hiếm khi xảy ra nếu tên thuật toán là "SHA-256"
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Phương thức trợ giúp để chuyển đổi một mảng byte thành chuỗi Hexadecimal.
     * Ví dụ: [10, -1] -> "0afe"
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
