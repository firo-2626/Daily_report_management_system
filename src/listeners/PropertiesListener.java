package listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */
@WebListener
public class PropertiesListener implements ServletContextListener {

    /**
     * Default constructor.
     */
    public PropertiesListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
         // TODO Auto-generated method stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */

    // 切り替え時の初期化
    public void contextInitialized(ServletContextEvent arg0)  {

        // ソルト文字設置のさいの、エラーを読み込む
        ServletContext context = arg0.getServletContext();

        String path = context.getRealPath("/META-INF/application.properties");
        try {
            // ソルト文字の読み込み
            InputStream is = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(is);
            is.close();

            // 状態リストの崇徳
            Iterator<String> pit = properties.stringPropertyNames().iterator();
            while(pit.hasNext()) {
                String pname = pit.next();
                context.setAttribute(pname, properties.getProperty(pname));
            }
        } catch(FileNotFoundException e) {
        } catch(IOException e) {}

    }

}
