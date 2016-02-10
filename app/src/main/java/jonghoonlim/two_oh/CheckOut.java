package jonghoonlim.two_oh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jong Hoon Lim on 2/7/2016.
 */
public class CheckOut extends Activity implements View.OnClickListener {

    private Button mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);

        // open the check-in page
        mainMenu = (Button) findViewById(R.id.main_menu);
        mainMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu :
                Intent mainMenuIntent = new Intent(getApplication(), Main.class);
                startActivity(mainMenuIntent);
                break;
            default :
                break;
        }
    }

}
