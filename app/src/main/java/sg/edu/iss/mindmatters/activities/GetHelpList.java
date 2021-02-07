package sg.edu.iss.mindmatters.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import sg.edu.iss.mindmatters.R;

public class GetHelpList extends BaseActivity implements AdapterView.OnItemClickListener{

    private final int[] fnames = {
    R.string.info_1, R.string.info_2, R.string.info_3, R.string.info_4, R.string.info_5
    };
    private final int[] description = {
    R.string.description_1, R.string.description_2, R.string.description_3, R.string.description_4, R.string.description_5
    };

    public final String[] webURL = {"https://www.gov.sg/article/call-these-helplines-if-you-need-emotional-or-psychological-support",
            "https://www.imh.com.sg/contact-us/", "https://www.sos.org.sg/contact-us", "https://www.silverribbonsingapore.com/counselling.html",
            "https://www.touch.org.sg/about-touch/our-services/touch-cyber-wellness-homepage/contact-us"
    };
    public final String[] phone = {"tel:18002026868", "tel:63892222", "tel:18002214444", "tel:63853714", "tel:18003772252"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_help_list);

        GetHelpAdapter adapter = new GetHelpAdapter(this, 0);
        adapter.setData(fnames, description, phone, webURL);
        ListView listView = findViewById(R.id.listView);
        callCustomActionBar(GetHelpList.this,true);

        if (listView != null) {
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> av,
                            View v, int pos, long id) {

    }


}