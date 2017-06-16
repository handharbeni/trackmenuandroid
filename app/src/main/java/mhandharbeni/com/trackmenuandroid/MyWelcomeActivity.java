package mhandharbeni.com.trackmenuandroid;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

/**
 * Created by root on 11/06/17.
 */

public class MyWelcomeActivity extends WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorPrimary)
                .page(new TitlePage(R.drawable.met_ic_clear,
                        "Title")
                )
                .page(new BasicPage(R.drawable.ic_home_black,
                        "Header",
                        "More text.")
                        .background(R.color.colorAccent)
                )
                .page(new BasicPage(R.drawable.ic_settings_black,
                        "Lorem ipsum",
                        "dolor sit amet.")
                )
                .swipeToDismiss(true)
                .build();
    }
}
