package sg.edu.iss.mindmatters.activities.fragments.getHelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import sg.edu.iss.mindmatters.R;

public class getHelpFragment extends Fragment implements  AdapterView.OnItemClickListener {

    private final int[] fnames = {
            R.string.info_1, R.string.info_2, R.string.info_3, R.string.info_4, R.string.info_5, R.string.info_6
    };
    private final int[] description = {
            R.string.description_1, R.string.description_2, R.string.description_3, R.string.description_4, R.string.description_5, R.string.description_6
    };

    public final String[] webURL = {"https://www.gov.sg/article/call-these-helplines-if-you-need-emotional-or-psychological-support",
            "https://www.imh.com.sg/contact-us/", "https://www.sos.org.sg/contact-us", "https://www.giving.sg/web/silver-ribbon-singapore-",
            "https://www.touch.org.sg/about-touch/our-services/touch-cyber-wellness-homepage/contact-us", "#"
    };
    public final String[] phone = {"tel:18002026868", "tel:63892222", "tel:18002214444", "tel:63853714", "tel:18003772252", "tel:98276591"};

    View mView;

    public getHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_get_help, container, false);
        this.mView = view;

        GetHelpAdapter adapter = new GetHelpAdapter(getActivity(), 0);
        adapter.setData(fnames, description, phone, webURL);
        ListView listView = mView.findViewById(R.id.listView);

        if (listView != null) {
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        }


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> av,
                            View v, int pos, long id) {

    }
}