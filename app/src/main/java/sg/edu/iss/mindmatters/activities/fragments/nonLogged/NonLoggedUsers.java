package sg.edu.iss.mindmatters.activities.fragments.nonLogged;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sg.edu.iss.mindmatters.R;

public class NonLoggedUsers extends Fragment implements View.OnClickListener {

    View mView;
    INonLoggedUsersFragment iNonLoggedUsersFragment;

    public NonLoggedUsers() {
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

        View view = inflater.inflate(R.layout.fragment_non_logged_users, container, false);
        this.mView = view;

        mView.findViewById(R.id.resource_btn).setOnClickListener(this);
        mView.findViewById(R.id.test_button).setOnClickListener(this);
        mView.findViewById(R.id.gethelp_button).setOnClickListener(this);

        return view;
    }

    public interface INonLoggedUsersFragment{
        void itemClicked(int content);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.resource_btn)
            iNonLoggedUsersFragment.itemClicked(0);
        else if (view.getId() == R.id.test_button)
            iNonLoggedUsersFragment.itemClicked(1);
        else if (view.getId() == R.id.gethelp_button)
            iNonLoggedUsersFragment.itemClicked(2);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iNonLoggedUsersFragment = (INonLoggedUsersFragment) context;
    }

}