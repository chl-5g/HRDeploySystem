package org.caihaolun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;
import java.util.concurrent.*;

/**
 * 认证 API：数据库异常或超时时快速失败，避免前端长时间等待。
 */
@Controller
@RequestMapping("/auth")
public class AuthApiController {

    private static final ExecutorService LOGIN_EXECUTOR = Executors.newCachedThreadPool();
    private static final int LOGIN_TIMEOUT_SECONDS = 3;
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/hrdeploysystem?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true&connectTimeout=3000&socketTimeout=3000";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse login(@RequestBody LoginRequest req) {
        LoginResponse res = new LoginResponse();

        if (isBlank(req.getEmail()) || isBlank(req.getPassword())) {
            res.setSuccess(false);
            res.setCode("INVALID_REQUEST");
            res.setMessage("邮箱和密码不能为空");
            return res;
        }

        try {
            UserRow user = findUserWithTimeout(req.getEmail().trim());
            if (user == null || !req.getPassword().equals(user.password)) {
                res.setSuccess(false);
                res.setCode("INVALID_CREDENTIALS");
                res.setMessage("用户名或密码错误");
                return res;
            }

            res.setSuccess(true);
            res.setCode("OK");
            res.setMessage("登录成功");
            res.setEmail(user.email);
            res.setUsername(user.username);
            return res;
        } catch (TimeoutException ex) {
            res.setSuccess(false);
            res.setCode("DB_TIMEOUT");
            res.setMessage("登录超时，请稍后重试");
            return res;
        } catch (Exception ex) {
            res.setSuccess(false);
            res.setCode("DB_ERROR");
            res.setMessage("数据库异常，请稍后重试");
            return res;
        }
    }

    private UserRow findUserWithTimeout(final String email) throws Exception {
        Future<UserRow> f = LOGIN_EXECUTOR.submit(new Callable<UserRow>() {
            @Override
            public UserRow call() throws Exception {
                return queryUser(email);
            }
        });
        try {
            return f.get(LOGIN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            f.cancel(true);
            throw ex;
        }
    }

    private UserRow queryUser(String email) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            ps = conn.prepareStatement("SELECT email, username, password FROM user_ WHERE email = ? LIMIT 1");
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            UserRow row = new UserRow();
            row.email = rs.getString("email");
            row.username = rs.getString("username");
            row.password = rs.getString("password");
            return row;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ignore) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception ignore) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static class UserRow {
        String email;
        String username;
        String password;
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private boolean success;
        private String code;
        private String message;
        private String email;
        private String username;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
