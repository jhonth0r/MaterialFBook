package me.zeeroooo.materialfb.WebView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import com.facebook.login.LoginManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.zeeroooo.materialfb.Activities.MainActivity;
import me.zeeroooo.materialfb.R;

public class Helpers {
    public static final List<String> FB_PERMISSIONS = Arrays.asList("public_profile", "user_friends");

    // Method to retrieve a single cookie
    public static String getCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(MainActivity.FACEBOOK_URL_BASE);
        if (cookies != null) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains("c_user")) {
                    String[] temp1 = ar1.split("=");
                    return temp1[1];
                }
            }
        }
        // Return null as we found no cookie
        return null;
    }

    // Uncheck all items menu
    public static void uncheckRadioMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked()) {
                menu.getItem(i).setChecked(false);
                return;
            }
        }
    }

    static boolean isInteger(String str) {
        return (str.matches("^-?\\d+$"));
    }

    public static Bitmap Circle(Bitmap circle) {
        Bitmap bitmap = Bitmap.createBitmap(circle.getWidth(), circle.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, circle.getWidth(), circle.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(circle.getWidth() / 2, circle.getHeight() / 2, circle.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(circle, rect, rect, paint);
        return bitmap;
    }
    /**
     * Extracts URL from a given string
     * @param string Text which may contain an URL
     * @return Extracted URL or empty string if URL not found inside
     */
    public static String extractUrl(String string) {
        final Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = urlPattern.matcher(string);
        int matchStart = 0;
        int matchEnd = 0;
        while (matcher.find()) {
            matchStart = matcher.start(1);
            matchEnd = matcher.end();
        }
        return string.substring(matchStart, matchEnd);
    }

    /**
     * Download an image as Bitmap object (run always outside the Main Thread)
     */
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // "clean" and decode an url, all in one
    public static String cleanAndDecodeUrl(String url) {
        return decodeUrl(cleanUrl(url));
    }

    // "clean" an url and remove Facebook tracking redirection
    private static String cleanUrl(String url) {
        return url.replace("http://lm.facebook.com/l.php?u=", "")
                .replace("https://m.facebook.com/l.php?u=", "")
                .replace("http://0.facebook.com/l.php?u=", "")
                .replace("https://lm.facebook.com/l.php?u=", "")
                .replaceAll("&h=.*", "").replaceAll("\\?acontext=.*", "");
    }

    // url decoder, recreate all the special characters
    private static String decodeUrl(String url) {
        return url.replace("%3C", "<").replace("%3E", ">").replace("%23", "#").replace("%25", "%")
                .replace("%7B", "{").replace("%7D", "}").replace("%7C", "|").replace("%5C", "\\")
                .replace("%5E", "^").replace("%7E", "~").replace("%5B", "[").replace("%5D", "]")
                .replace("%60", "`").replace("%3B", ";").replace("%2F", "/").replace("%3F", "?")
                .replace("%3A", ":").replace("%40", "@").replace("%3D", "=").replace("%26", "&")
                .replace("%24", "$").replace("%2B", "+").replace("%22", "\"").replace("%2C", ",")
                .replace("%20", " ");
    }
}
