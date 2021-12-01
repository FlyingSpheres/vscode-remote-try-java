package com.flyingspheres.services;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/authentication", name="authenticationServlet")
public class AuthenticationServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println(System.currentTimeMillis() + "ms Received a GET request in " + this.getClass().getName());
            response.getWriter().print(System.currentTimeMillis() + "ms Received a GET request in " + this.getClass().getName());
        } catch (Throwable t){
            try {
                response.getWriter().print(System.currentTimeMillis() + "Ay no! Ocurrió un error");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String destination = "http://localhost:8888/messages.html";
        String userId   = request.getParameter("userId");
        String password = request.getParameter("password");
        System.out.println("Received UserID: "   + userId);
        System.out.println("Received Password: " + password);
        Cookie c = new Cookie("userId", userId);
        c.setMaxAge(1800);
        response.addCookie(c);
        System.out.println(System.currentTimeMillis() + "ms Received a POST request in " + this.getClass().getName());
        System.out.println("Sending user to: " + destination);
        try {
           //For this to work we are dependent on a proxy running so that we are
           //  staying within the same domain
            response.sendRedirect(destination);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}