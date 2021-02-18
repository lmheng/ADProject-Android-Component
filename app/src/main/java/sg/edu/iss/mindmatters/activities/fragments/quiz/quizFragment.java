package sg.edu.iss.mindmatters.activities.fragments.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import sg.edu.iss.mindmatters.R;
import sg.edu.iss.mindmatters.webview.QuizActivity;


public class quizFragment extends Fragment implements View.OnClickListener {

    private View mView;


    public quizFragment() {
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

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        this.mView = view;

        LinearLayout layout = mView.findViewById(R.id.monthly_quiz);
        layout.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.monthly_quiz){
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            startActivity(intent);
        }
    }
}