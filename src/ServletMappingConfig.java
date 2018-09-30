import java.util.ArrayList;
import java.util.List;

public class ServletMappingConfig {
    public static List<ServletMapping> servletMappingList = new ArrayList<>();

    static {
        servletMappingList.add(new ServletMapping("findGirl","/girl","FindGirlServlet"));
        servletMappingList.add(new ServletMapping("helloWorld","/world","HelloWorldServlet"));

        servletMappingList.add(new ServletMapping("findGirl","/favicon.ico","FindGirlServlet"));
    }
}
