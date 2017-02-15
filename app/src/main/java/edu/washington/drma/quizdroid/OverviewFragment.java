package edu.washington.drma.quizdroid;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TOPIC_INDEX = "drma.topicIndex";

    private int topicIndex;

    private OnFragmentInteractionListener mListener;

    // Data
//    String[][] quizOverviewData = new String[][]{
//            {"Math", "5"},
//            {"Physics", "2"},
//            {"Marvel", "2"}
//    };

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param topicIndex Parameter 1.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(int topicIndex) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TOPIC_INDEX, topicIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicIndex = getArguments().getInt(ARG_TOPIC_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        QuizApp app = (QuizApp)getActivity().getApplication();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //String[] currentQuiz = quizOverviewData[topicIndex];

        TextView textTitle = (TextView)view.findViewById(R.id.textTitle);
        TextView textDescr = (TextView)view.findViewById(R.id.textDescr);
        TextView textQuestions = (TextView)view.findViewById(R.id.textQuestions);

        textTitle.setText(app.getRepository().getTopicTitle(topicIndex));
        textDescr.setText(app.getRepository().getTopicDescShort(topicIndex));
        textQuestions.setText(app.getRepository().getNumOfQuestions(topicIndex) + " questions");

        Button btnBegin = (Button)view.findViewById(R.id.btnBegin);
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onBeginPressed(topicIndex);
                }
            }
        });

        return view;
    }

    // onAttach used activities rather than contexts prior to api 23
    // http://stackoverflow.com/questions/32604552/onattach-not-called-in-fragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBeginPressed(int quizIndex);
    }
}