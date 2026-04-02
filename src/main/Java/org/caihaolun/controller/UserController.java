package org.caihaolun.controller;

import org.caihaolun.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/3/9.
 * 简单的user控制层
 */
@Controller
public class UserController {
    private static final ExecutorService LOGIN_EXECUTOR = Executors.newCachedThreadPool();
    private static final int LOGIN_TIMEOUT_SECONDS = 3;
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/hrdeploysystem?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true&connectTimeout=3000&socketTimeout=3000";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "1234";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "login";
    }

    /**
     * 我现在还不知道怎么往视图传输数据然后跳转页面 就先这样吧
     *
     * @return
     */
    @RequestMapping(value = "welcome", method = RequestMethod.POST)
    public String loginValidate(@ModelAttribute("user") User user, ModelMap modelMap) {
        try {
            User user_ = findUserWithTimeout(user.getEmail());
            if (user_ == null) {
                //用户名输入错误或用户不存在
                return "error";
            } else if (user.getPassword().equals(user_.getPassword())) {
                //密码正确
                modelMap.addAttribute("user", user_);
                return "homePage/welcome";
            } else {
                //密码输入错误
                return "error";
            }
        } catch (TimeoutException ex) {
            return "errorDb";
        } catch (Exception ex) {
            return "errorDb";
        }
    }

    private User findUserWithTimeout(final String email) throws Exception {
        Future<User> f = LOGIN_EXECUTOR.submit(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return queryUser(email);
            }
        });
        try {
            return f.get(LOGIN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            f.cancel(true);
            throw ex;
        } catch (ExecutionException ex) {
            if (ex.getCause() instanceof Exception) {
                throw (Exception) ex.getCause();
            }
            throw new RuntimeException(ex.getCause());
        }
    }


//    @RequestMapping(value = "users")
//    public String getUsers(ModelMap modelMap) {
//        // 查询user表中所有记录
//        try {
//            // 将所有记录传递给要返回的jsp页面，放在userList当中
//            modelMap.addAttribute("userList", userDAO.findAll());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        // 返回users.jsp页面
//        return "users";
//    }

    //添加用户，用于注册的时候
    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    public String insertUser(@ModelAttribute("user") User user) {
        try {
            upsertUser(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "/login";
    }

//    // 删除用户
//    @RequestMapping(value = "users/delete/{email}", method = RequestMethod.GET)
//    public String deleteUser(@PathVariable("email") String email) {
//        try {
//            User user = userDAO.findByID(email);
//            if (user != null) {
//                userDAO.delete(user);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        // 立即刷新
//        return "redirect:users";
//    }

    @RequestMapping("/jsonSource")

//@RequestBody 将json对象转成java对象
//@ResponseBody 表示返回的是json对象

    public @ResponseBody User jsonSource(HttpServletRequest request) {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        System.out.println(user.getEmail() + " " + user.getUsername());
        return user;
    }

    private User queryUser(String email) throws Exception {
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
            User u = new User();
            u.setEmail(rs.getString("email"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            return u;
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

    private void upsertUser(User user) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            ps = conn.prepareStatement("INSERT INTO user_ (email, username, password) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE username = VALUES(username), password = VALUES(password)");
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        } finally {
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
}
