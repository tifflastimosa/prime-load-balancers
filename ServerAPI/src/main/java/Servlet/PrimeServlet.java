package Servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "PrimeServlet", value = "/prime/*")
public class PrimeServlet extends HttpServlet {

  private String[] urlArray;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // get the endpoint
    String urlPath = req.getPathInfo();
    resp.setStatus(HttpServletResponse.SC_OK);
    // get N
    // validate if N
//    if (urlPath == null || urlPath.isEmpty()) {
//      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//      resp.getWriter().write("Missing parameters");
//    }
//
//    if (isURLValid(urlPath)) {
//      // 2 conditions
//      // if N is valid prime - then send OK response
//      convertURLPath(urlPath);
//      if (isPrime(Integer.parseInt(this.urlArray[2]))) {
//        resp.setStatus(HttpServletResponse.SC_OK);
//      }
//      // else N - N not valid prime - send 401 response
//    } else {
//      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }

  private void convertURLPath(String urlPath) {
    this.urlArray = urlPath.split("/");
  }

  private boolean isURLValid(String urlPath) {
    String urlPattern = "/prime/\\d+";
    Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(urlPath);
    return matcher.matches();
  }



  // https://mkyong.com/java/how-to-determine-a-prime-number-in-java/
  private boolean isPrime(int n) {
    // split the path into an array
    // get the last N - assume N is valid input meaning between 1 - 10000
    if (n % 2 == 0) {
      return false;
    }
    for (int i = 3; i * i <= n; i +=2) {
      if (n % i == 0) {
        return false;
      }
    }
    return true;
  }

}
